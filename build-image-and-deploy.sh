#!/bin/sh

############ Variable ############
PROFILE="$1"
PORT="$2"
IMAGE_NAME="$3" # image name
TAG="latest"
VOLUME_PROFILE="$4" # volume directory name of profile
HOST_VOLUME_PATH="/home/ubuntu/$VOLUME_PROFILE/logs"
DOCKER_VOLUME_PATH="/logs"

########### Build Docker Image ##########
echo "Building Docker image..."
docker build -t "$IMAGE_NAME":"$TAG" .

# date tag
# DATE_TAG=$(date +%y%m%d%H%M)
# docker build -t "$REPOSITORY_NAME":"$DATE_TAG" .

########### Make Log Directory ##########
if [ ! -d "/home/ubuntu/$VOLUME_PATH/logs" ]; then
    echo "Making log directory..."
    mkdir -p /home/ubuntu/$VOLUME_PATH/logs
fi

########## Deploy ##########
CONTAINER_IDS=$(docker ps -aqf "name=$IMAGE_NAME") #$(docker ps -aq --filter ancestor=$IMAGE_NAME)
echo "Find CONTAINER_IDS ...  >>  $CONTAINER_IDS"

if [ -n "$CONTAINER_IDS" ]; then
    echo 'Stopping and removing Docker container...'
    docker stop $CONTAINER_IDS
    docker rm $CONTAINER_IDS
fi
echo "Deploy Spring Boot!!"
docker run -d --name $IMAGE_NAME -e PROFILE=$PROFILE -p $PORT:8080 -v $HOST_VOLUME_PATH:$DOCKER_VOLUME_PATH $IMAGE_NAME:$TAG