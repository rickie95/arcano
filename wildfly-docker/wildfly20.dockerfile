FROM jboss/wildfly:20.0.1.Final
EXPOSE 8080 8443

# Installs MySql driver
COPY wildfly-docker/mysql-connector-java-8.0.26.jar /opt/jboss/wildfly/modules/com/mysql/main/
COPY wildfly-docker/mysql-connector-module.xml /opt/jboss/wildfly/modules/com/mysql/main/module.xml
COPY wildfly-docker/TestConnection.java /opt/jboss/wildfly/TestConnection.java

# Copies the configuration file
ADD wildfly-docker/standalone-https-support.xml /opt/jboss/wildfly/standalone/configuration/standalone-custom.xml
ADD wildfly-docker/server.keystore /opt/jboss/wildfly/standalone/configuration/server.keystore

# Adds admin account
RUN /opt/jboss/wildfly/bin/add-user.sh admin arcano --silent

# Add startup script
COPY wildfly-docker/startup-script.sh /opt/jboss/wildfly/startup-script.sh

CMD ["/opt/jboss/wildfly/startup-script.sh", "/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-custom.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

# Deploys the artifact
ADD target/arcano.war /opt/jboss/wildfly/standalone/deployments/
