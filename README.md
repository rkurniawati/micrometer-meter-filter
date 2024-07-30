# Example Spring Boot app with Custom Micrometer MeterFilters

This is an example Spring Boot application that demonstrates how to create custom Micrometer MeterFilters to filter out specific metrics. More information can be found in this [article](https://medium.com/@ruth.kurniawati/using-custom-micrometer-meter-filters-in-a-spring-boot-application-a58aff7dbe4d).

## Requirements

To run this sample, you will need to have the following software installed on your machine:
- Java (17 or later)
- Maven (3.8.3 or later)

## Running the Application

To run the application without any custom filters, use the following command:
```bash
mvn spring-boot:run
```
To run the application with custom filters defined as Spring beans, use the following command:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=beans
```

To run the application with custom filters defined in a `MeterRegistryCustomizer` bean, use the following command:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=beans
```

To run the application with all custom filters, use the following command:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=beans,customizer
```

## Querying Metrics

To query the metrics exposed by the application along with their values, you can use the following command:

```bash
curl http://localhost:8080/actuator/prometheus
```

Alternatively, you can use the following command to query the metric names in JSON format:
    
```bash
curl http://localhost:8080/actuator/metrics
```

If you use the `/actuator/metrics` endpoint, you can get the metric value by appending the metric name to the URL. For example, to get the value of the `jvm.gc.live.data.size` metric, you can use the following command:

```bash
curl http://localhost:8080/actuator/metrics/jvm.gc.live.data.size
```

## Running the Unit Tests

To run the unit tests, run:

```bash
mvn test
```

Note that the `OneMeterFilterBeanTest` will fail until you declare the `MeterFilter` bean with a priority
higher than Spring Boot's `PropertiesMeterFilter` bean, see the comment in the source code for more details.