# DevConf 2016 Demo: IoT Gateway for Intelligent Home

# Building and running
```sh
$ mvn clean package
$ java -jar target/devconf-2016-intelligent-home-*.jar
```

## Public REST API

### GET [/reset]()
Resets all the LED drivers. All the LEDs are turned off.
---

### GET [/led/setrgb]()
Sets the brightness of R G B channels of the specified LED to give light with a particular color.
* URL query parameters are:
    * `led` The LED's number (0-14)
    * `r`   The value of R channel (0-100)%
    * `g`   The value of G channel (0-100)%
    * `b`   The value of B channel (0-100)%

#### Examples:
* To turn the LED #1 completely off

> GET [/led/setrgb?led=1&r=0&g=0&b=0]()

* To turn the LED #1 on with full brightness of all channels

> GET [/led/setrgb?led=1&r=100&g=100&b=100]()

* To set the LED #1's brightness to R=0%, G=25% and B=80%

> GET [/led/setrgb?led=1&r=0&g=25&b=80]()
---

### GET [/led/setrgb/all]()
Sets the brightness of R G B channels of all LEDs to give light with a particular color.
* URL query parameters are:
    * `r`   The value of R channel (0-100)%
    * `g`   The value of G channel (0-100)%
    * `b`   The value of B channel (0-100)%

#### Examples:
* To turn all the LEDs completely off

> GET [/led/setrgb/all?r=0&g=0&b=0]()

* To turn all the LEDs on with full brightness of all channels

> GET [/led/setrgb?led=1&r=100&g=100&b=100]()

* To set all the LEDs brightness to R=0%, G=25% and B=80%

> GET [/led/setrgb?led=1&r=0&g=25&b=80]()
---

### POST [/led/batch]()
Allows to set the color and brightness of multiple LEDs. The batch is send in the payload of the HTTP request.

The batch is a set of instructions in the format of `<led>;<channel>;<value>` each on separate line where
* `led` is the number of LED,
* `channel` is the LED's channel to set (one of `r`, `g` or `b`) and
* `value` is the percentage value of brightness to which the particular channel is set.

#### Examples:
* To turn the LED #1 and LED #5 completely off

> POST [/led/batch]()
```
1;r;0
1;g;0
1;b;0
5;r;0
5;g;0
5;b;0
```

* To turn the LED #4 and LED #2 completely on

> POST [/led/batch]()
```
4;r;100
4;g;100
4;b;100
2;r;100
2;g;100
2;b;100
```

* To set the LED #1's R channel to 5%, LED #9's R and B channels to 20% and to set the color of the LED #5, #0 and #10 to full pure green

> POST [/led/batch]()
```
1;r;5
9;r;20
9;b;20
5;r;0
5;g;100
5;b;0
0;r;0
0;g;100
0;b;0
10;r;0
10;g;100
10;b;0
```
---

### GET [/sensorData]()
Return the current temperature [°C] and humidity [%] measured by the [DHT11](https://www.adafruit.com/product/386) exposed over I<sup>2</sup>C bus in JSON format.

#### Examples:
* To get the current sensor data

> GET [/sensorData]()

The returned JSON object with temperature of 24 °C and humidity of 33 % looks like:
```json
{
  "sensorName" : "LivingRoom",
  "temperature" : 24,
  "humidity" : 33,
  "timestamp" : "2016-02-03 14:36:19"
}
```
---

### GET [/ac/on]()
Turn the air condition fan on.
---

### GET [/ac/off]()
Turn the air condition fan off.
---

### GET [/fireplace/on]()
"Starts a fire" in the fireplace.
---

### GET [/fireplace/off]()
"Extinguish the fire" in the fireplace.
---

### GET [/tv/reset]()
Plays `/root/reset.wav` from the TV's speaker.
---

### GET [/tv/news]()
Plays `/root/news.wav` from the TV's speaker.
---

### GET [/tv/romantic]()
Plays `/root/romantic.wav` from the TV's speaker.

## Internal REST API
### POST [/pca9685/batch]()
Similar to (and used by) [/led/batch]() to set the PWM value (0-4095) of one or more [PCA9685](https://www.adafruit.com/product/815) LED drivers to a specified value.

The batch is send in the payload of the HTTP request.

The batch is a set of instructions in the format of `<I2C address>;<PWM output>;<PWM value>` each on separate line where
* `<I2C address>` is the I2C address of PCA9685 driver the instruction is meant for,
* `<PWM output>` is the # of PWM output (0-15)
* `<PWM value>` is the PWM value set to the given PWM output.

#### Examples:
* To set PWM output #5 of PCA9685 driver with I2C address of 0x42 to a value of 1024 and PWM output #7 of PCA9685 driver with I2C address of 0x40 to a value of 0

> POST [/led/batch]()
```
0x42;5;1024
0x40;7;0
```
---

### GET [/servo/set]()
Sets one the two [MG90S](http://www.electronicoscaldas.com/datasheet/MG90S_Tower-Pro.pdf) servos connected to PCA9685 driver to a specified angle (0-180)°.
* URL query parameters are:
    * `servo`   The servo number (0-1)
    * `value`   The angle to set the servo to (0-180)°

#### Examples:
* To set the servo #1 to 90°
> GET [/servo/set?servo=1&value=90]()