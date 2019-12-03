FROM openjdk:8u212-jre-alpine3.9
# workspace
WORKDIR usr/pavel
# add .jar under target from host
# into this image
ADD target/wheels-up.jar         wheels-up.jar
ADD target/wheels-up-tests.jar   wheels-up-tests.jar
ADD target/libs 				 libs
ADD target/drivers               drivers

ENTRYPOINT java -cp wheels-up.jar:wheels-up-test.jar:libs/* -DBrowser=$BROWSER -Dexecute.mode=$MODE -Dhub.host=$HUB_HOST org.junit.runner.JUnitCore com.selenium.DemoTest 