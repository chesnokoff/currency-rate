#!/usr/bin/env bash

set -e

PROVIDER_INSTANCES="${PROVIDER_INSTANCES:-2}"
PRINTER_INSTANCES="${PRINTER_INSTANCES:-2}"

mkdir -p .run/logs

docker compose -f service-registry/docker-compose.yaml up -d
docker compose -f pact-broker/docker-compose.yaml up -d
docker compose -f monitoring/docker-compose.yaml up -d

./gradlew :rate-provider:bootJar :rate-printer:bootJar

PROVIDER_JAR="$(find rate-provider/build/libs -maxdepth 1 -name '*.jar' ! -name '*-plain.jar' | head -n 1)"
PRINTER_JAR="$(find rate-printer/build/libs -maxdepth 1 -name '*.jar' ! -name '*-plain.jar' | head -n 1)"
JAVA_25_HOME="$(/usr/libexec/java_home -v 25 2>/dev/null || true)"
JAVA_CMD="${JAVA_25_HOME:+$JAVA_25_HOME/bin/java}"

if [ -z "$JAVA_CMD" ]; then
  JAVA_CMD="java"
fi

for i in $(seq 1 "$PROVIDER_INSTANCES"); do
  nohup "$JAVA_CMD" -jar "$PROVIDER_JAR" --server.port=0 > ".run/logs/rate-provider-$i.log" 2>&1 &
done

for i in $(seq 1 "$PRINTER_INSTANCES"); do
  nohup "$JAVA_CMD" -jar "$PRINTER_JAR" --server.port=0 > ".run/logs/rate-printer-$i.log" 2>&1 &
done

echo "Consul: http://localhost:8500"
echo "Pact Broker: http://localhost:9292"
echo "Prometheus: http://localhost:9090"
echo "Grafana: http://localhost:3000"
echo "rate-provider instances: $PROVIDER_INSTANCES"
echo "rate-printer instances: $PRINTER_INSTANCES"
echo "Logs: .run/logs/"

sleep 8

if command -v jq >/dev/null 2>&1; then
  echo
  echo "rate-provider URLs:"
  curl -s http://localhost:8500/v1/catalog/service/rate-provider | jq -r '.[] | "http://\(.ServiceAddress):\(.ServicePort) | http://\(.ServiceAddress):\(.ServicePort)/actuator/prometheus"'

  echo
  echo "rate-printer URLs:"
  curl -s http://localhost:8500/v1/catalog/service/rate-printer | jq -r '.[] | "http://\(.ServiceAddress):\(.ServicePort) | http://\(.ServiceAddress):\(.ServicePort)/actuator/prometheus"'
fi
