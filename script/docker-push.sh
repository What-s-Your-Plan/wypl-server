#!/bin/bash

echo "1. 🚀 Start Docker Image Push!\n"

JAR_FILE=$(find build/libs/ -type f -name "*.jar" | head -n 1)
IMAGE_NAME=$(basename "$JAR_FILE" | cut -d '-' -f 1)
IMAGE_TAG=$(basename "$JAR_FILE" | cut -d '-' -f 2 | cut -d '.' -f 1-3)

if [ -z "$1" ]; then
  read -p HARBOR_HOSTNAME
else
  HARBOR_HOSTNAME=$1
fi

if [ -z "$2" ]; then
  read -p HARBOR_USERNAME
else
  HARBOR_USERNAME=$2
fi

if [ -z "$3" ]; then
  read -p HARBOR_PASSWORD
else
  HARBOR_PASSWORD=$3
fi

echo "\n2. 🐬 Docker Image Push, Version: $HARBOR_HOSTNAME/wypl/$IMAGE_NAME:$IMAGE_TAG"

echo $HARBOR_PASSWORD | docker login $HARBOR_HOSTNAME -u $HARBOR_USERNAME --password-stdin
docker push $HARBOR_HOSTNAME/wypl/$IMAGE_NAME:$IMAGE_TAG
docker push $HARBOR_HOSTNAME/wypl/$IMAGE_NAME:latest