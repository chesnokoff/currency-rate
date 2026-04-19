#!/usr/bin/env bash
set -euo pipefail

VERSION="${1:?Usage: scripts/release.sh <version> [release-prefix]}"
PREFIX="${2:-$(date -u +%Y%m%dT%H%M%SZ)}"

RELEASE_ID="${PREFIX}-$(git rev-parse --short=12 HEAD)"
RELEASE_DIR="build/releases/${RELEASE_ID}"

if [ -e "${RELEASE_DIR}" ]; then
  echo "release ${RELEASE_DIR} already exists (releases are immutable)" >&2
  exit 1
fi

mkdir -p "${RELEASE_DIR}"
cp "build/artifacts/${VERSION}"/*.jar "${RELEASE_DIR}/"

echo "RELEASE_ID=${RELEASE_ID}"
