#! /bin/sh
set -e
cd ~/arcano
git pull
mvn wildfly:start deploy
