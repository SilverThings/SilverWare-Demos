The idea
========
Sensors are regularly posting their data through a REST interface to the sample service. 
The data are filtered for just changes in the values to be stored in the database. 
If a change is detected, the value is written in the time series in InfluxDB.

How to build
============
> mvn clean package

How to run
==========
> java -jar target/openalt-2015-*.jar "-Dinfluxdb.url=http://localhost:8086" "-Dinfluxdb.user=root" "-Dinfluxdb.password=root"

You might have different configuration parameters to connect to your InfluxDB.

How to test
===========
Make sure you have sensors database created in your InfluxDB.

Insert temperature data ('sensor1' is sensor name and 25 is the temperature in Celsius):
curl -i -X POST http://localhost:8081/rest/RegisterTemperature/temperature -d '[ "sensor1", 25 ]'

Insert humidity data ('sensor1' is sensor name and 75 is the relative humidity):
curl -i -X POST http://localhost:8081/rest/RegisterTemperature/humidity -d '[ "sensor1", 75 ]'
