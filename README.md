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
# Contents
- [Spring MVC Request Lifecycle](#spring-mvc-request-lifecycle)
  - [Filter](#filter)
  - [DispatcherServlet](#dispatcherservlet)
  - [Common Services](#common-services)
  - [Handler Mappings](#handler-mappings)
  - [Handler Interceptor](#handler-interceptor)
  - [Handler Exception Resolver](#handler-exception-resolver)
  - [View Resolver](#view-resolver)
- [Logging](#logging)
- [Swagger](#swagger)
  - [Dependencies](#dependencies)
  - [Config](#config)
  + [Swagger with Authentication](#swagger-with-authentication)
- [Database](#database)
  - [Dependencies](#dependencies-1)
  - [Config](#config-1)
- [i18n](#i18n)
- [Profile Management](#profile-management)
- [Spring Data JPA](#spring-data-jpa)
  - [Dependencies](#dependencies-2)
  - [Config](#config-2)
  + [Entity](#entity)
    - [Entity Auditing](#entity-auditing)
  + [Repository](#repository)
  * [Querydsl](#querydsl)
    - [Dependencies](#dependencies-3)
    - [Config](#config-3)
    + [Querydsl CustomRepository](#querydsl-customrepository)
- [Faker](#faker)
  - [Dependencies](#dependencies-4)
  + [Data Initialize](#data-initialize)
- [Rest API](#rest-api)
  + [Response](#response)
  + [Controller](#controller)
  + [DTO](#dto)
  + [VO](#vo)
  + [Service](#service)
  * [ModelMapper](#modelmapper)
    - [Dependencies](#dependencies-5)
    + [ModelMapper.map](#modelmappermap)
- [Exception Handling](#exception-handling)
- [Spring Security](#spring-security)
  * [UserDetails](#userdetails)
    - [Custom User Details](#custom-user-details)
  * [JWT Token](#jwt-token)
    - [Dependencies](#dependencies-6)
    - [Config](#config-4)
    + [Publish Token](#publish-token)
  * [Spring boot Security](#spring-boot-security)
    - [Dependencies](#dependencies-7)
    - [Config](#config-5)
    + [SecurityFilterChain](#securityfilterchain)
    + [JWT Token Authentication Filter](#jwt-token-authentication-filter)

<small><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></small>

-------------------------
# Spring MVC Request Lifecycle
#### Filter
* J2EE 표준 스펙 기능으로 DispatcherServlet 에 요청이 전달되기 전/후에 URL 패턴에 맞는 요청에 대해 부가작업을 수행.
* 최전방의 DispatcherServlet보다 앞서 수행되므로, 필터는 Spring 범위 밖에서 처리된다.
  * Spring Container 가 아닌 Web Container 에의해 관리(Bean 등록은 됨)
* javax.servlet 의 Filter 인터페이스를 구현.
  * init() : Filter 객체 초기화, Web Container에 의해 1회 수행. 
  * doFilter() : url-pattern 에 맞는 요청에 대한 처리 수행. 전달인자 FilterChain chain 에 의해 다음 대상으로 요청을 전달. 
  * destroy() : Filter 객체를 서비스에서 제거. Web Container에 의해 1회 수행.
```text
Filter는 대체로 Spring과 무관한 전역적으로 처리하는 작업에 대한 처리를 구현한다.
보안 검사 등으로 스프링 컨테이너 도달 전에 차단하는 등 안정성을 확보할 수 있다. 
ServletRequest, ServletResponse 를 직접 조작할 수 있다는 점에서 Interceptor 보다 강력한 기술.

Interceptor 는 클라이언트 요청에 대한 전역적 처리를 구현. 컨트롤러로 넘겨주기 위한 정보를 가공하기에 용이함. 
```

#### DispatcherServlet
* HTTP 프로토콜로 진입하는 모든 요청을 먼저 받아 적절한 컨트롤러로 요청을 전달. 
* DispatcherServlet 은 HandlerAdapter 어댑터 인터페이스를 통해 요청을 위임. 
  * 어댑터 패턴을 적용하여 컨트롤러의 다양한 구현 방식에 무관하게 요청을 위임할 수 있다. 
* 정적 자원에 대한 처리
  * 특정 패턴의 요청은 정적 자원에 대한 요청으로 처리함을 명시하는 방법.
  * 대응하는 컨트롤러 매핑이 없는 경우 정적 자원에 대한 요청으로 처리. 

#### Common Services
* 모든 요청에 대해 제공되는 서비스 레이어. 
* i18n, Theme, File upload 등의 기능을 지원. 
* DispatcherServlet 의 WebApplicationContext 에 정의. 

#### Handler Mappings
* RequestMappingHandlerMapping Class.
* 요청 매핑 정보를 관리하고, 요청이 왔을 때 이를 처리하는 대상(Handler)를 찾는 클래스.
* 실제 매핑 정보는 상속하는 부모 클래스 AbstractHandlerMethodMapping 의 MappingRegistry 에 기록된다. 
  * MappingRegistry<RequestMappingInfo, MappingRegistration>
  * RequestMappingInfo : Http Method 와 URI 를 포함한 헤더, 파라미터 등의 조건
  * MappingRegistration : HandlerMethod. 매핑되는 컨트롤러의 메소드와 컨트롤러 빈 정보.

#### Handler Interceptor
* Dispatcher Servlet 이 컨트롤러 호출 전후 요청과 응답을 참조/가공할 수 있는 기능을 제공. 
  * preHandle() : 컨트롤러 메소드 호출 전 수행. 반환 타입(boolean)의 값으로 다음 절차의 수행을 제어할 수 있다. 
  * postHandle() : 컨트롤러 메소드 호출 후 수행. Exception 발생 시 수행되지 않는다. 
  * afterCompletion() : 뷰 수준의 처리까지 모두 완료된 후 수행. 요청 처리 중 사용한 리소스를 반환할 때 사용하기 적합. 
* AOP Advice 로 대체할 수 있으나, Request 객체를 얻을 수 없고 컨트롤러 메소드 구현방식이 다양하여 모두 대응하기 어렵다. 

#### Handler Exception Resolver
* 핸들러의 요청 처리 중 발생하는 예상하지 못한 예외를 처리하기 위해 설계. 
* 에러 처리가 시작되는 곳은 DispatcherServlet. doDispatch 는 Exception 과 Throwable 을 catch 한다. 
* 컨트롤러에 정의한 ExceptionHandler 로 처리가 불가한 경우, 모든 ControllerAdvice Bean 을 검사하여 예외처리한다. 

#### View Resolver
* Controller 는 출력할 View 와 View 에 전달할 객체를 담은 ModelAndView 객체를 반환. 
* 반환된 View 의 논리적인 이름을 기반으로 ViewResolver 가 해석하여 연결. 
* ViewResolver 의 주요 구현 클래스
  * ContentNegotiationViewResolver : 요청 URL 확장자(MediaType)를 이용해 AcceptHandler 를 사용한 탐색. 
  * FreeMarkerViewResolver : FreeMarker 기반의 템플릿을 탐색.  
  * VelocityViewResolver : Velocity 기반의 뷰를 탐색. 
  * JasperReportsViewResolver : Jasper 리포트 파일로 정의된 뷰를 탐색. 







-------------------------
# Logging
* Springboot 기본 Logging Framework 는 Logback.
* MultiThread 환경에서 비동기 로깅성능이 중시되는 경우, Log4j2 교체한다.
  * 방법 : spring-boot-starter-logging 모듈에 대하여 spring-boot-starter-log4j2 으로 교체를 명시. 





-------------------------
# Swagger
* Spring-Fox : Spring Boot 2.6 이하
* Spring-Doc : Spring boot 2.6 이상 

#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
implementation "org.springdoc:springdoc-openapi-ui:1.6.6"
```

#### Config
* file : [SwaggerConfig.java](src/main/java/com/sik/template/config/SwaggerConfig.java)
* file : [IndexController.java](src/main/java/com/sik/template/biz/IndexController.java)


### Swagger with Authentication
* 스키마 정의를 통해 JWT Token 을 Header 에 삽입하도록 구현할 수 있다.
* file : [SwaggerConfig.java](src/main/java/com/sik/template/config/SwaggerConfig.java)
```java
@SecuritySchemes({
      @SecurityScheme(
              name = "X-AUTH-TOKEN",
              type = SecuritySchemeType.APIKEY,
              description = "JWT Token",
              in = SecuritySchemeIn.HEADER,
              paramName = "X-AUTH-TOKEN"
      )
})
```




-------------------------
# Database
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
spring.h2.console.settings.web-allow-others=true
spring.h2.console.path=/h2-console

spring.datasource.url=jdbc:h2:~/test;
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```



-------------------------
# i18n
* 메시지에 대한 다국어 처리를 지원한다.
* MessageSource 구현체를 Bean으로 등록하며 BaseNames에 지정한 접두어 리소스들을 메시지 처리한다.
* messages_언어코드_국가코드.properties 형태로 타 언어를 지원할 수 있다.
* 일반적으로 HTTP accept-language 헤더 값 또는 locale 정보를 기반으로 국제화 파일을 선택한다.
* file : [MessageSourceConfig.java](src/main/java/com/sik/template/config/MessageSourceConfig.java)
* file : [messages.properties](src/main/resources/messages.properties)



-------------------------
# Profile Management
* Profile 을 지정하여 어플리케이션의 구성을 Profile 별로 달리 할수 있다. 
* application.properties 를 application-[PROFILE_NAME].properties 으로 구성한다. 
  * [application-local.properties](src/main/resources/application-local.properties)
  * [application-test.properties](src/main/resources/application-test.properties)
  * [application-prod.properties](src/main/resources/application-prod.properties)

* Application 실행 시, 아래 프로퍼티를 지정하면 해당 프로파일이 적용된다.
```text
--spring.profiles.active=[PROFILE_NAME]
```
* 또는 특정 Bean 이 지정된 Profile 에서만 등록되도록 설정할 수 있다.
```java
@Component
@Profile("test") // 이 클래스는 test Profile 에서만 빈으로 등록된다.
public class TestProfileOnlyBean {
  ...
}
```

-------------------------
# Spring Data JPA

#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
implementation "org.springframework.boot:spring-boot-starter-data-jpa"
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
* 추상 클래스를 선언하여 상속받는 하위 Entity 들은 생성/변경이력 컬럼 선언을 자동화.
* 부모 클래스를 선언하여 Entity 에 일괄 적용할 수 있다. 
  * file : [BaseAuditTimeEntity.java](src/main/java/com/sik/template/domain/base/BaseAuditTimeEntity.java) 
* 로그인 세션 또는 JWT 토큰으로부터 생성자/수정자 PK를 획득할 수 있다.
  * file : [LoginUserAuditorAware.java](src/main/java/com/sik/template/common/security/LoginUserAuditorAware.java)
  * file : [AuditingConfig.java](src/main/java/com/sik/template/config/AuditingConfig.java)

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
* 비즈니스 로직 패키지의 Querydsl 이 적용된 Custom Repository를 사용해 페이징 및 구체적인 조인, 조건문을 구현한다.
* Q엔티티에 접근하여 다양한 쿼리를 구현할 수 있다. 
* file : [BoardCustomRepository.java](src/main/java/com/sik/template/domain/repository/BoardCustomRepository.java)
* file : [BoardCustomRepositoryImpl.java](src/main/java/com/sik/template/domain/repository/impl/BoardCustomRepositoryImpl.java)

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
# Faker
* Java-faker 는 테스트 데이터를 만들어주는 패키지.

#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
implementation "org.yaml:snakeyaml:1.33"
implementation "com.github.javafaker:javafaker:1.0.2"
```

### Data Initialize
* 서비스 실행 시 샘플 데이터를 삽입하도록 구현할 수 있다. 
* 프로파일 local 및 test 에서만 동작하도록 구현한다. 
  * [DatabaseInitializer.java](src/main/java/com/sik/template/domain/DatabaseInitializer.java)




-------------------------
# Rest API

### Response
* API가 항상 일정한 포맷으로 반환하기 위해 공용 Response 를 정의한다. 
* 단순 객체를 반환하는 경우, 운영 중 스펙 변경이 매우 어려워 Wrapping 을 수행. 
* Generic Type 으로 VO 및 DTO를 정의한다.
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
implementation "org.modelmapper:modelmapper:3.1.0"
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






-------------------------
# Exception Handling
* API 호출에 있어 Exception 발생 시 메시지를 반환한다. 
* 오류 코드를 정의하고, @RestControllerAdvice 어드바이저를 구현하여 exceptionHandler 로 예외처리를 한곳에서 처리한다.
* file : [ExceptionCode.java](src/main/java/com/sik/template/common/exception/ExceptionCode.java)
* file : [ApiBizException.java](src/main/java/com/sik/template/common/exception/ApiBizException.java)
* file : [ApiExceptionAdvice.java](src/main/java/com/sik/template/common/advice/ApiExceptionAdvice.java)
* 오류 반환 시 국제화 기능을 이용해 오류 메시지를 처리할 수 있다.
```java
throw new ApiBizException(ExceptionCode.RUNTIME_EXCEPTION, messageSource.getMessage("msg_test", null, Locale.KOREA));
```
* 반환 예시
```json
{
  "errorCode": "E0001",
  "errorMessage": "사용자 정의 예외처리 테스트"
}
```



-------------------------
# Spring Security
* 허가되지 않은 사용자에 대한 서비스 접근/수행을 제한. 


## UserDetails
* 사용자 정보를 담는 인터페이스이다.
* Spring Security 에서 사용자의 정보를 불러오기 위해 구현해야 하는 인터페이스. 
* 이 때, getUserName() 이 고유한 값을 의미하는데 중복되지 않는 DB 컬럼(PK) 설정을 권장. 

#### Custom User Details
* Account(=User) 및 Role Entity 를 정의한다. 두 엔티티의 관계는 ManyToMany 이다.  
* file : [Account.java](src/main/java/com/sik/template/domain/entity/Account.java)
* file : [Role.java](src/main/java/com/sik/template/domain/entity/Role.java)

* UserDetailsService 인터페이스를 상속하는 Service 와 구현체를 구현한다. 
* 앞서 정의한 엔티티를 이용해 JPA 를 이용한 조회를 구현.
* file : [AccountServiceImpl.java](src/main/java/com/sik/template/biz/api/account/service/impl/AccountServiceImpl.java)
```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  Account account = accountRepository.findByUsername(username).orElseThrow(
          () -> new UsernameNotFoundException("Username : " + username + " not found")
        );
  return new User(account.getUsername(), account.getPassword(), getAuthorities(account));
}
```


## JWT Token
* 토큰 기반 인증. 클라이언트가 서버로부터 인증 수행 후 토큰을 획득하여 매 요청 시 헤더에 전달.

#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
implementation "io.jsonwebtoken:jjwt:0.9.1"
```

#### Config
* file : [JwtTokenProvider.java](src/main/java/com/sik/template/common/security/JwtTokenProvider.java)
* 토큰의 생성과 검증을 수행하는 클래스를 정의한다.
* 앞서 구축한 UserDetails 를 이용해 사용자 정보를 획득한다.
```java
public Authentication getAuthentication(String token) {
  UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(token));
  return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
}
```

### Publish Token
* 로그인 로직을 수행한 후, JwtTokenProvider 의 createToken 을 호출하여 토큰을 발행한다. 
* file : [AccountController.java](src/main/java/com/sik/template/biz/api/account/controller/AccountController.java)
```java
  AccountDTO loginUser = (AccountDTO) accountService.loadUserByUsername(accountDTO.getUsername());
  SignDTO signDTO = new SignDTO();

  /*
  * Password 검증 수행...
  * */

  List<String> roleList = loginUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
  String token = jwtTokenProvider.createToken(loginUser.getUsername(), roleList);
```
* 이후 토큰은 HTTP Header 에 포함하여 요청한다. 
```text
X-AUTH-TOKEN : [JWT_TOKEN]
```


## Spring boot Security
#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
implementation "org.springframework.boot:spring-boot-starter-security"
```

#### Config
* WebSecurityConfigurerAdapter 는 Deprecated 되었다. 
* SecurityFilterChain 을 Bean으로 등록하여 사용하여야 한다.
* file : [SecurityConfig](src/main/java/com/sik/template/config/SecurityConfig.java)
```java
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public WebSecurityCustomizer configure() {
    ...
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    ...
  }
}
```

### SecurityFilterChain
* 다른 필터보다 먼저 springSecurityFilterChain을 사용하도록 DelegatingFilterProxy 를 등록.
* 특정 요청에 대해 권한 검사를 수행, 적절한 Response 를 반환할 수 있다.
* Swagger 및 h2-console 등 자격 검사 예외 처리할 수 있다. 
* file : [SecurityConfig.java](src/main/java/com/sik/template/config/SecurityConfig.java)
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  return http
  .antMatchers("/api/**")
  .hasAuthority("API_CALL")
        ...
  .and()
  .build();

}
```
* 권한 설정 메소드
  * antMatchers(String ... resources) : 권한을 설정할 리소스(URI)를 지정.
    * permitAll() : 인증절차 없이 허용.
    * hasAnyRole(String roleName) : 특정 권한을 가진 사용자만 접근을 허용. 
  * anyRequests().authenticated() : antMatchers 에 지정한 리소스 외의 모든 요청은 인증절차 필요. 
* 로그인 처리 메소드
  * formLogin() : 로그인 페이지의 로그인 처리 및 성공 실패 처리 사용여부.
  * loginPage(String uri) : 특정 로그인 페이지를 사용하고자 할 때 사용. 
  * loginProcessingUrl(String uri) : 로그인 인증처리를 수행하는 URI 설정. 해당 리소스 호출 시 인증처리 필터 호출됨.
  * defaultSuccessUrl(String uri) : 인증 성공 시 이동하는 페이지 정의. 
  * successHandler() : 인증 성공 후 특정 핸들러가 실행되어야 하는 경우 정의. 
  * failureUrl(String uri) : 인증 실패 시 이용하는 페이지 설정. 
  * failureHandler() : 인증 실패 시 특정 핸들러가 실행되어야 하는 경우 정의. 
* 커스텀 필터
  * addFilterBefore(CUSTOM_FILTER, TARGET_FILTER) : TARGET_FILTER 앞에 CUSTOM_FILTER를 추가. 
  * addFilterAfter(CUSTOM_FILTER, TARGET_FILTER) : TARGET_FILTER 뒤에 CUSTOM_FILTER를 추가.


### JWT Token Authentication Filter
* JWT 인증을 수행하기 위한 Filter 를 정의한다. 
* file : [JwtAuthenticationFilter](src/main/java/com/sik/template/common/security/JwtAuthenticationFilter.java)

* SecurityFilterChain 에 filter를 등록한다.
* file : [SecurityConfig.java](src/main/java/com/sik/template/config/SecurityConfig.java)
```java
.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
```