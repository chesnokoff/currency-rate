#!/usr/bin/env bash
set -euo pipefail

RELEASE_ID="${1:?Usage: scripts/run-release.sh <release-id> <rate-provider|rate-printer> [args...]}"
APP="${2:?Usage: scripts/run-release.sh <release-id> <rate-provider|rate-printer> [args...]}"
shift 2

MAJOR="$(java -version 2>&1 | awk -F '"' '/version/ {split($2,a,"."); print a[1]; exit}')"
if [ -z "${MAJOR}" ] || [ "${MAJOR}" -lt 25 ]; then
  echo "java 25+ required on PATH, found '${MAJOR:-none}'" >&2
  exit 1
fi

JAR="$(ls build/releases/${RELEASE_ID}/${APP}-*.jar | head -n 1)"
exec java -jar "${JAR}" "$@"
