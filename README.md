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
* file : [build.gradle](build.gradle)
```groovy
implementation 'org.springdoc:springdoc-openapi-ui:1.6.6'
```

#### Config
* file : [SwaggerConfig.java](src/main/java/com/sik/template/config/SwaggerConfig.java)
* file : [IndexController.java](src/main/java/com/sik/template/biz/IndexController.java)


=============
## H2 Database
* H2 Embedded Database 연동.
* 서비스 실행 후 http://localhost:8080/h2-console

#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
runtimeOnly 'com.h2database:h2'
```

#### Config
* file : [application.properties](src/main/resources/application.properties)
```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.datasource.url=jdbc:h2:~/test;
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```


## Spring Data JPA

#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
```

#### Config
* file : [application.properties](src/main/resources/application.properties)
```properties
# JPA
# DDL-AUTO Config
spring.jpa.hibernate.ddl-auto=create-drop
```
* DDL-AUTO Config 설정하여 Entity 로부터 테이블을 자동 생성/변경할 수 있다. 

### Entity
* package : [model/entity](src/main/java/com/sik/template/model/entity)
* ID 열에 @GeneratedValue(strategy = GenerationType.IDENTITY) 설정하여 Auto Increment 설정.
* Entity 에는 Setter 메소드를 정의하지 않는다. 
* LOB 타입 열에 @Basic(fetch = FetchType.LAZY) 설정하여 기본 지연로딩을 설정.

### Audit 
* file : [BaseAuditTimeEntity.java](src/main/java/com/sik/template/model/base/BaseAuditTimeEntity.java) 
* 추상 클래스를 선언하여 상속받는 하위 Entity 들은 생성/변경이력 컬럼 선언을 자동화.


