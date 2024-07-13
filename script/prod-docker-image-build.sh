#!/bin/bash

echo "1. 🚀 Start Prod Docker Image Build!\n"
cd ..

echo "⚡ bootJar"
./gradlew clean bootJar

JAR_FILE=$(find build/libs/ -type f -name "*.jar" | head -n 1)
echo "\n2. 🎯 Target JAR: $JAR_FILE"

if [ -z "$1" ]; then
  read -p "Enter Harbor Hostname: " HARBOR_HOSTNAME
else
  HARBOR_HOSTNAME=$1
fi

IMAGE_NAME=$(basename "$JAR_FILE" | cut -d '-' -f 1)
IMAGE_TAG=$(basename "$JAR_FILE" | cut -d '-' -f 2 | cut -d '.' -f 1-3)
echo "\n3. 🐬 Docker Image Build, Version: wypl/$IMAGE_NAME:$IMAGE_TAG"
docker build -t "$HARBOR_HOSTNAME/wypl/$IMAGE_NAME":"$IMAGE_TAG" .
docker image tag "$HARBOR_HOSTNAME/wypl/$IMAGE_NAME":"$IMAGE_TAG" "$HARBOR_HOSTNAME/wypl/$IMAGE_NAME:latest"