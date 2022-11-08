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
* package : [entity](src/main/java/com/sik/template/domain/entity)
* ID 열에 @GeneratedValue(strategy = GenerationType.IDENTITY) 설정하여 Auto Increment 설정.
* Entity 에는 Setter 메소드를 정의하지 않는다. 
* LOB 타입 열에 @Basic(fetch = FetchType.LAZY) 설정하여 기본 지연로딩을 설정.

#### Entity Auditing 
* file : [BaseAuditTimeEntity.java](src/main/java/com/sik/template/domain/base/BaseAuditTimeEntity.java) 
* 추상 클래스를 선언하여 상속받는 하위 Entity 들은 생성/변경이력 컬럼 선언을 자동화.

### Repository
* package : [repository](src/main/java/com/sik/template/domain/repository)
* JpaRepository 를 상속하여 기본 CRUD 및 페이징 기능 추상화.



## Querydsl
* JpaRepository, CrudRepository 가 기본 제공하는 함수로는 다양한 쿼리에 대응하기 어려움. 
* 컴파일 단계에서 오류를 확인할 수 있는 장점. 

#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
// Querydsl
buildscript {
    ext {
        queryDslVersion = "5.0.0"
    }
}
...(중략)...

plugins {
    ...
    id 'com.ewerk.gradle.plugins.querydsl' version "1.0.10"
}
...(중략)...

dependencies {
    ...
    implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
    implementation "com.querydsl:querydsl-apt:${queryDslVersion}"
    ...
}
...(중략)...

// Querydsl
def querydslDir = "$buildDir/generated/querydsl"

querydsl {
    jpa = true
    querydslSourcesDir = querydslDir
}
sourceSets {
    main.java.srcDir querydslDir
}
compileQuerydsl{
    options.annotationProcessorPath = configurations.querydsl
}
configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
    querydsl.extendsFrom compileClasspath
}
```
* Gradle > Tasks > other > compileQuerydsl 우클릭 > run ... 실행.
* /build/generated/querydsl 위치에 QEntityName 으로 생성됨을 확인.

#### Config
* file : [QuerydslConfig.gradle](src/main/java/com/sik/template/config/QuerydslConfig.java)


