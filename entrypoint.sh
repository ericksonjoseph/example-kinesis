export CLASSPATH=~/.m2/repository:/global/target/classes
mvn clean install

# Main
mvn exec:java -Dexec.mainClass="com.ericksonjoseph.examplekinesis.Main" -Dexec.args=""
# Component
mvn exec:java -Dexec.mainClass="com.ericksonjoseph.examplekinesis.component.Component" -Dexec.args="verify"

#echo "**** STARTING PROVIDER ****\n";
#mvn exec:java -Dexec.mainClass="com.ericksonjoseph.examplekinesis.writer.EventWriter" -Dexec.args="examplekinesis us-east-1" & > ./logs/producer.log

#echo "**** STARTING CONSUMER ****\n";
#mvn exec:java -Dexec.mainClass="com.ericksonjoseph.examplekinesis.processor.EventProcessor" -Dexec.args="examplekinesis examplekinesis us-east-1" > ./logs/consumer.log
