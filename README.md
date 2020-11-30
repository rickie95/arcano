# Arcano
[![Build Status](https://travis-ci.org/rickie95/arcano.svg?branch=master)](https://travis-ci.org/rickie95/arcano)

A simple backend for Congrega App.

## Composition
- Standard JEE technologies: JAX-RS for REST APIs, CDI and JPA. Compatible with every JEE complaiant application server and ORM+Database.
- GraphQL live and working endpoint, realized with [Smallrye's GraphQL implementation](https://github.com/smallrye/smallrye-graphql).
- Authentication & Authorization granted by JWT.
- Ready to go Docker Compose image.

## Requirements
- Java 11 + Maven 3.6.3, neeeded to build the artifact.
- Docker + Docker Compose to start the server and the DB.

## Usage
- Clone the repository: `git clone https://github.com/rickie95/arcano`
- Move in root directory and build the `.war`: `cd arcano && mvn clean package`
- Build the image and fire up the containers: `docker-compose build && docker-compose up`
