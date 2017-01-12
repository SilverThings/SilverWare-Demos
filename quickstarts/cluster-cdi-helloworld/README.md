Shows clustering integration:
Compile and start with:

mvn clean package exec:exec

then start in other shell:

mvn exec:exec 

Two instances should be started and you can see results by using:
 
curl http://localhost:8081/rest/RestEndpoint/cdiHello