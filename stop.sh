#!/usr/bin/env bash

set -e

pkill -f 'java -jar .*rate-provider.*\.jar' || true
pkill -f 'java -jar .*rate-printer.*\.jar' || true

docker compose -f monitoring/docker-compose.yaml down
docker compose -f pact-broker/docker-compose.yaml down
docker compose -f service-registry/docker-compose.yaml down

echo "Stopped"
