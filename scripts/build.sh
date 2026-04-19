#!/usr/bin/env bash
set -euo pipefail

VERSION="${1:?Usage: scripts/build.sh <version>}"

ARTIFACT_DIR="build/artifacts/${VERSION}"
if [ -e "${ARTIFACT_DIR}" ]; then
  echo "artifact ${ARTIFACT_DIR} already exists (builds are immutable)" >&2
  exit 1
fi

./gradlew -PreleaseVersion="${VERSION}" :rate-provider:bootJar :rate-printer:bootJar

mkdir -p "${ARTIFACT_DIR}"
cp "rate-provider/build/libs/rate-provider-${VERSION}.jar" "${ARTIFACT_DIR}/"
cp "rate-printer/build/libs/rate-printer-${VERSION}.jar" "${ARTIFACT_DIR}/"

echo "Built ${ARTIFACT_DIR}"
