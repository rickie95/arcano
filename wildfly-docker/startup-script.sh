#! /bin/sh
set -e

cmd="$@"

MYSQL_DRIVER=/opt/jboss/wildfly/modules/com/mysql/main/mysql-connector-java-8.0.26.jar
TEST_CONNECTION=/opt/jboss/wildfly/TestConnection.java
# Wait for mysql to be ready for connection

until java -cp $MYSQL_DRIVER $TEST_CONNECTION; do
    >&2 echo "Waiting for MySQl"
    sleep 2
done

# Then start Wildfly
>&2 echo "MySQL is ready to accept connections, starting Wildfly."
exec $cmd
