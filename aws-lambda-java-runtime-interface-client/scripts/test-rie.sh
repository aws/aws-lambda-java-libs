#!/bin/bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
SERIALIZATION_ROOT="$(dirname "$PROJECT_ROOT")/aws-lambda-java-serialization"

if ! ls "$PROJECT_ROOT"/target/aws-lambda-java-runtime-interface-client-*.jar >/dev/null 2>&1; then
  echo "RIC jar not found. Please build the project first with 'mvn package'."
  exit 1
fi

IMAGE_TAG="java-ric-rie-test"

HANDLER="${1:-EchoHandler::handleRequest}"

echo "Starting RIE test setup for Java..."

# Build local dependencies if not present
CORE_ROOT="$(dirname "$PROJECT_ROOT")/aws-lambda-java-core"
if ! ls "$PROJECT_ROOT"/target/aws-lambda-java-core-*.jar >/dev/null 2>&1; then
  echo "Building local aws-lambda-java-core..."
  (cd "$CORE_ROOT" && mvn package -DskipTests)
  cp "$CORE_ROOT"/target/aws-lambda-java-core-*.jar "$PROJECT_ROOT/target/"
fi

if ! ls "$PROJECT_ROOT"/target/aws-lambda-java-serialization-*.jar >/dev/null 2>&1; then
  echo "Building local aws-lambda-java-serialization..."
  (cd "$SERIALIZATION_ROOT" && mvn package -DskipTests)
  cp "$SERIALIZATION_ROOT"/target/aws-lambda-java-serialization-*.jar "$PROJECT_ROOT/target/"
fi

echo "Compiling EchoHandler..."
javac -source 11 -target 11 -cp "$(ls "$PROJECT_ROOT"/target/aws-lambda-java-runtime-interface-client-*.jar):$(ls "$PROJECT_ROOT"/target/aws-lambda-java-core-*.jar):$(ls "$PROJECT_ROOT"/target/aws-lambda-java-serialization-*.jar)" \
  -d "$PROJECT_ROOT/test-handlers/" "$PROJECT_ROOT/test-handlers/EchoHandler.java"

echo "Building test Docker image..."
docker build -t "$IMAGE_TAG" -f "$PROJECT_ROOT/Dockerfile.rie" "$PROJECT_ROOT"

echo "Starting test container on port 9000..."
echo ""
echo "In another terminal, invoke with:"
echo "curl -s -X POST -H 'Content-Type: application/json' \"http://localhost:9000/2015-03-31/functions/function/invocations\" -d '{\"message\":\"test\"}'"
echo ""

exec docker run -it -p 9000:8080 -e _HANDLER="$HANDLER" "$IMAGE_TAG"