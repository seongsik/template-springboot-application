# template-springboot-application

-------------------------

### 참고문헌
* 전문가를 위한 Spring5 개정판, 길벗출판사
* 스프링 부트와 AWS로 혼자 구현하는 웹 서비스

### Environments
* Framework : Spring Boot 2.7.5
* SDK : Azul zulu version 17.0.4
* Gradle : 7.5.1


-------------------------
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


-------------------------
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


-------------------------
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
* 페이징이나 복잡한 조인 등의 작업은 아래의 querydsl 을 이용한 CustomRepository를 이용한다. 


-------------------------
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
* /build/generated/querydsl 위치에 Q엔티티가 생성됨을 확인.

#### Config
* file : [QuerydslConfig.gradle](src/main/java/com/sik/template/config/QuerydslConfig.java)

### Querydsl CustomRepository
* Gradle build를 통해 생성된 Q엔티티를 이용해 쿼리를 수행할 수 있다. 
* 기본 Repository 패키지가 아닌, 비즈니스로직 구현 패키지에 위치한다. 
* JpaRepository를 상속받는 인터페이스와 구현체 클래스를 구현한다. 
* file : [BoardCustomRepository.java](src/main/java/com/sik/template/biz/api/board/repository/BoardCustomRepository.java)
* file : [BoardCustomRepositoryImpl.java](src/main/java/com/sik/template/biz/api/board/repository/BoardCustomRepositoryImpl.java)
* Q엔티티에 접근하여 다양한 쿼리를 구현할 수 있다. 
```java
import static com.sik.template.domain.entity.QBoard.board;
...(중략)...

@Repository
@AllArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> findAllBoard(Pageable pageable) {
        return jpaQueryFactory.selectFrom(board)
                .leftJoin(board.boardComments)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
```


-------------------------
## Rest API

### Response
* API가 항상 일정한 포맷으로 반환하기 위해 공용 Response 를 정의한다. 
* Generic Type 으로 VO 및 DTO를 정의한다.
* file : [BaseDTO.java](src/main/java/com/sik/template/biz/api/base/dto/BaseDTO.java)
* file : [RestApiResponse.java](src/main/java/com/sik/template/biz/api/base/response/RestApiResponse.java)

### Controller
* 일반적인 RestAPI Controller.
* 페이징이 필요한 경우 Pageable 객체를 전달받는다. 
* file : [BoardApiController.java](src/main/java/com/sik/template/biz/api/board/controller/BoardApiController.java)

### DTO
* API는 절대로 Entity를 직접 반환하지 않고, DTO로 매핑하여 반환한다.
  * Entity 직접 반환 시, Entity 의 변경으로 API 스펙이 전부 영향을 받는다. 
* BaseDTO 를 상속받아 기본 Audit 정보를 멤버로 갖는다.
* ModelMapper 를 이용한 Entity Convert 메서드를 구현해주었다. 
* file : [BaseDTO.java](src/main/java/com/sik/template/biz/api/base/dto/BaseDTO.java)
* file : [BoardDTO.java](src/main/java/com/sik/template/biz/api/board/dto/BoardDTO.java)

### VO
* Controller 에 ModelAttribute로 전달하는 검색조건 객체. 
* BaseVO를 상속받아 기본 검색 정보를 멤버로 갖는다.
* file : [BaseVO.java](src/main/java/com/sik/template/biz/api/base/vo/BaseVO.java)
* file : [BoardVO.java](src/main/java/com/sik/template/biz/api/board/vo/BoardVO.java)

### Repository
* 기본 Repository 와 Custom Repository로 구분하여 사용한다. 
* 기본 기능은 domain 패키지의 기본 Repository를 사용한다.
* 비즈니스 로직 패키지의 Querydsl 이 적용된 Custom Repository를 사용해 페이징 및 구체적인 조인, 조건문을 구현한다.
* file : [BoardCustomRepository.java](src/main/java/com/sik/template/biz/api/board/repository/BoardCustomRepository.java)
* file : [BoardCustomRepositoryImpl.java](src/main/java/com/sik/template/biz/api/board/repository/BoardCustomRepositoryImpl.java)

### Service
* Controller의 메서드 호출을 통해 필요한 데이터를 Repository로부터 반환한다. 
* Entity로 반환되는 데이터를 DTO로 변환하여 반환한다.
* file : [BoardApiService.java](src/main/java/com/sik/template/biz/api/board/service/BoardApiService.java)
* file : [BoardApiServiceImpl.java](src/main/java/com/sik/template/biz/api/board/service/impl/BoardApiServiceImpl.java)




-------------------------
## ModelMapper
* Entity <-> DTO 간의 변환을 위해 사용. 

#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
implementation 'org.modelmapper:modelmapper:3.1.0'
```

### ModelMapper.map
* DTO 에 정적 메서드로 구현함을 고려해볼 것.
* 필드명이 같은 경우, map() 메서드를 이용해 매핑할 수 있다. 
* 컬렉션의 경우, stream 을 이용해 아래와 같이 일괄 변환할 수 있다. 
```java
List<Board> entities = boardCustomRepository.findAllBoard(pageable);
return entities.stream().map(o -> modelMapper.map(o, BoardDTO.class)).collect(Collectors.toList());
```
* 필드명이 다른 경우, PropertyMap 을 구현체에 map().setter() 를 이용해 매핑할 수 있다. 
* PropertyMap 을 이용해 skip 할 수 있다. 
  * 모든 필드를 조회하면서 관계 엔티티의 Lazy Loading N+1 이 발생하는 경우.
  * 특정 필드를 비공개 하고자 하는 경우.
```java
mapper.addMappings(new PropertyMap<Board, BoardDTO>() {
    @Override
    protected void configure() {
        skip(destination.getBoardComments());
    }
});
```