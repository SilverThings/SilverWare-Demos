package io.silverware.demos.devconf2016.intelligent_home.model;

public class SensorData {

   private String sensorName = "DHT11";

   private int temperature = -1;

   private int humidity = -1;

   private String timestamp = "-1";

   // Bean methods

   public String getSensorName() {
      return this.sensorName;
   }

   public void setSensorName(String sensorName) {
      this.sensorName = sensorName;
   }

   public int getTemperature() {
      return this.temperature;
   }

   public void setTemperature(int temperature) {
      this.temperature = temperature;
   }

   public int getHumidity() {
      return this.humidity;
   }

   public void setHumidity(int humidity) {
      this.humidity = humidity;
   }

   public String getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
   }

   // Fluent API

   public SensorData sensorName(String sensorName) {
      setSensorName(sensorName);
      return this;
   }

   public String sensorName() {
      return getSensorName();
   }

   public SensorData temperature(int temperature) {
      setTemperature(temperature);
      return this;
   }

   public int temperature() {
      return getTemperature();
   }

   public SensorData humidity(int humidity) {
      setHumidity(humidity);
      return this;
   }

   public int humidity() {
      return getHumidity();
   }

   public SensorData timestamp(String timestamp) {
      setTimestamp(timestamp);
      return this;
   }

   public String timestamp() {
      return getTimestamp();
   }
}
