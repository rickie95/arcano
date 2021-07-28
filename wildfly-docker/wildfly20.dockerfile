FROM maven:3.8.1-openjdk-11-slim
EXPOSE 8080 8443
VOLUME $HOME/.m2:/root/.m2

RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*

# Clone repo
RUN git clone https://www.github.com/rickie95/arcano

CMD ["mvn", "-f", "arcano/pom.xml", "wildfly:run"]