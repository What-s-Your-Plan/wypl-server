#!/bin/bash

echo -e "🚀 Start Dev Docker Image Build!\n"
cd ..

echo "⚡ bootJar"
./gradlew clean bootJar

JAR_FILE=$(find build/libs/ -type f -name "*.jar" | head -n 1)
echo -e "\n🎯 Target JAR: $JAR_FILE"

IMAGE_NAME=$(basename "$JAR_FILE" | cut -d '-' -f 1)
IMAGE_TAG=$(basename "$JAR_FILE" | cut -d '-' -f 2 | cut -d '.' -f 1-3)
echo -e "\n🐬 Docker Image Build, Version: wypl/dev-$IMAGE_NAME:$IMAGE_TAG"
docker build -t "wypl/dev-$IMAGE_NAME":"$IMAGE_TAG" .
docker build -t "wypl/dev-$IMAGE_NAME":latest .