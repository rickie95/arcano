# Arcano
A simple backend for Congrega App.

[![Java CI with Maven](https://github.com/rickie95/arcano/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/rickie95/arcano/actions/workflows/maven.yml)

## Composition
- Standard JEE technologies: JAX-RS for REST APIs, CDI and JPA. Compatible with every JEE complaiant application server and ORM+Database.
- GraphQL live and working endpoint, realized with [Smallrye's GraphQL implementation](https://github.com/smallrye/smallrye-graphql).
- Authentication & Authorization granted by JWT.
- Ready to go Docker Compose image with Wildfly 20 and MySQL.

## Requirements
- Java 11 + Maven 3.6.3, neeeded to build the artifact.
- Docker + Docker Compose to start the server and the DB.

## Usage
1) Clone the repository: `git clone https://github.com/rickie95/arcano`
2) Move in root directory and build the `.war`: `cd arcano && mvn clean package`
3) Download the MySQL connector (`mysql-connector-java-8.0.21.jar`) from MySQL website, and place in `../wildfly-docker` folder.
4) Generate a self-signed certificate with `keytool -genkeypair -alias localhost -keyalg RSA -keysize 2048 -validity 365 -keystore server.keystore -dname "cn=Arcano,o=Arcano,c=IT" -keypass secret -storepass secret`
5) Build the image and fire up the containers: `docker-compose build && docker-compose up`
