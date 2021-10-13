#! /bin/sh

mvn clean package -DskipTest=true
if [[ $? -ne 0 ]]; then
    echo "Maven build failed"
    exit 1
fi

docker-compose build
if [[ $? -ne 0 ]]; then
    echo "Docker build failed"
    exit 2
fi

docker-compose up
