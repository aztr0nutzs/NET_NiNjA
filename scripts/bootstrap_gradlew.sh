#!/usr/bin/env bash
set -euo pipefail

GRADLE_VERSION="${GRADLE_VERSION:-8.7}"
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TMP_DIR="${ROOT_DIR}/.tmp_gradle_bootstrap"
DIST_URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"

mkdir -p "${TMP_DIR}"
cd "${TMP_DIR}"

echo "Downloading Gradle ${GRADLE_VERSION}..."
if command -v curl >/dev/null 2>&1; then
  curl -L -o gradle.zip "${DIST_URL}"
elif command -v wget >/dev/null 2>&1; then
  wget -O gradle.zip "${DIST_URL}"
else
  echo "Need curl or wget."
  exit 1
fi

echo "Unzipping..."
unzip -q -o gradle.zip
GRADLE_BIN="${TMP_DIR}/gradle-${GRADLE_VERSION}/bin/gradle"

echo "Generating wrapper..."
cd "${ROOT_DIR}"
"${GRADLE_BIN}" wrapper --gradle-version "${GRADLE_VERSION}"

echo "Cleaning up..."
rm -rf "${TMP_DIR}"
echo "Done: ./gradlew is ready."
