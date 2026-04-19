FROM eclipse-temurin:25-jre

ARG RELEASE_ID

RUN test -n "${RELEASE_ID}" || { echo "RELEASE_ID build arg is required (point it at a dir under build/releases)" >&2; exit 1; }

WORKDIR /app
COPY build/releases/${RELEASE_ID}/rate-printer-*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
