# Hystrix hello world quickstart

1. Run `mvn clean install -DskipTests` in the root directory of [SilverWare](https://github.com/SilverThings/SilverWare) project.
2. Run `mvn clean package cargo:start exec:exec` in **hystrix-helloworld** directory of this project.
3. Access [Hystrix Dashboard](https://github.com/Netflix/Hystrix/wiki/Dashboard) on [http://localhost:8081/hystrix-dashboard](http://localhost:8081/hystrix-dashboard).
4. Add a new stream with address [http://localhost:8080/hystrix.stream](http://localhost:8080/hystrix.stream) and start monitoring it.
