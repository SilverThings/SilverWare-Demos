This quickstart shows integration with Apache DeltaSpike

$ mvn clean package
$ mvn exec:exec

To fully utilize the DeltaSpike configuration mechanism try running the resulting jar file manually with various options.
Note: The logging property is there to bridge JUL to Log4j2 to get you a nicer output.

$ java -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager -Ddb.port.mysql=111 -Ddb.port.postgresql=112 -Ddb.vendor=postgresql -jar deltaspike-config-*.jar 

$ java -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager -Ddb.port.mysql=4406 -Ddb.port.postgresql=6543 -Ddb.vendor=mysql -jar deltaspike-config-*.jar 

See how the configuration property 'dbPort' differs in the output.