# template-springboot-application

=============

### 참고문헌
* 전문가를 위한 Spring5 개정판, 길벗출판사
* 스프링 부트와 AWS로 혼자 구현하는 웹 서비스

### Environments
* Framework : Spring Boot 2.7.5
* SDK : Azul zulu version 17.0.4
* Gradle : 7.5.1


=============
## Swagger
* Spring-Fox : Spring Boot 2.6 이하
* Spring-Doc : Spring boot 2.6 이상 

#### Dependencies
* [build.gradle](build.gradle)
```groovy
implementation 'org.springdoc:springdoc-openapi-ui:1.6.6'
```

#### Config
* [SwaggerConfig.java](src/main/java/com/sik/template/config/SwaggerConfig.java)
* [IndexController.java](src/main/java/com/sik/template/biz/IndexController.java)


=============
## H2 Database
* H2 Embedded Database 연동.
* 서비스 실행 후 http://localhost:8080/h2-console

#### Dependencies
* [build.gradle](build.gradle)
```groovy
runtimeOnly 'com.h2database:h2'
```

#### Config
* [application.properties](src/main/resources/application.properties)
```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.datasource.url=jdbc:h2:~/test;
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```



