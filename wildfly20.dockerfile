FROM jboss/wildfly:20.0.1.Final
EXPOSE 8080

# Installs MySql driver
COPY wildfly-resources/mysql-connector-java-8.0.21.jar /opt/jboss/wildlfy/modules/com/mysql/main/
COPY wildfly-resources/mysql-connector-module.xml /opt/jboss/wildlfy/modules/com/mysql/main/module.xml

# Copies the configuration file
ADD wildfly-resources/standalone-custom.xml /opt/jboss/wildfly/standalone/configuration/

# Deploys the artifact and the datasource file
ADD target/arcano-0.0.1-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
ADD datasource/arcano-ds.xml /opt/jboss/wildfly/standalone/deployments/

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-custom.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
