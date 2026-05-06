#!/bin/bash
set -e

# Load .env defaults if present; existing env vars take priority (set -a exports all)
if [ -f ".env" ]; then
  set -a
  # shellcheck source=.env
  source .env
  set +a
fi

echo "======================================"
echo " Selenium POM Test Suite"
echo "======================================"
echo " Environment : ${ENV:-dev}"
echo " Browser     : ${BROWSER:-chrome}"
echo " Headless    : ${HEADLESS:-true}"
echo " Base URL    : ${BASE_URL:-(from environments.json)}"
echo "======================================"

mvn clean test \
  -DENV="${ENV:-dev}" \
  -DBROWSER="${BROWSER:-chrome}" \
  -DHEADLESS="${HEADLESS:-true}" \
  -DBASE_URL="${BASE_URL}" \
  -Dsurefire.suiteXmlFiles=testng.xml

EXIT_CODE=$?

echo "======================================"
if [ $EXIT_CODE -eq 0 ]; then
  echo " Tests PASSED"
else
  echo " Tests FAILED (exit code: $EXIT_CODE)"
fi
echo "======================================"

exit $EXIT_CODE
