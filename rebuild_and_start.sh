#! /bin/sh

mvn clean package -DskipTest=true
docker-compose build
docker-compose up
