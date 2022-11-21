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
## Spring MVC Request Lifecycle
#### Filter
* 모든 Request 에 적용되어
#### DispatcherServlet
* Rqeust 를 분석하고 처리하기 위해 적절한 Controller 에 요청을 전달. 
#### Common Services
* 모든 요청에 대해 제공되는 서비스 레이어. 
* i18n, Theme, File upload 등의 기능을 지원. 
* DispatcherServlet 의 WebApplicationContext 에 정의. 
#### Handler Mappings
* 들어오는 Request를 Controller 클래스 내의 Handler Method 에 매핑.
#### Handler Interceptor
* 공통 로직 수행 용도의 Handler Interceptor를 등록.
#### Handler Exception Resolver
* 핸들러의 요청 처리 중 발생하는 예상하지 못한 예외를 처리하기 위해 설계. 
#### View Resolver
* Controller가 반환한 논리적인 이름을 기반으로 View를 해석하여 연결. 
* ContentNegotiationViewResolverm FreeMarkerViewResolver, VelocityViewResolver, JasperReportsViewResolver 등.







-------------------------
## Logging
* Springboot 기본 Logging Framework 는 Logback.
* MultiThread 환경에서 비동기 로깅성능이 중시되는 경우, Log4j2 교체한다.
  * 방법 : spring-boot-starter-logging 모듈에 대하여 spring-boot-starter-log4j2 으로 교체를 명시. 





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
spring.h2.console.settings.web-allow-others=true
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
## Rest API

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






-------------------------
## Exception Handler
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
## 국제화
* 메시지에 대한 다국어 처리를 지원한다.
* MessageSource 구현체를 Bean으로 등록하며 BaseNames에 지정한 접두어 리소스들을 메시지 처리한다.  
* messages_언어코드_국가코드.properties 형태로 타 언어를 지원할 수 있다. 
* 일반적으로 HTTP accept-language 헤더 값 또는 locale 정보를 기반으로 국제화 파일을 선택.
* file : [MessageSourceConfig.java](src/main/java/com/sik/template/config/MessageSourceConfig.java)
* file : [messages.properties](src/main/resources/messages.properties)







-------------------------
## Spring Security
* 허가되지 않은 사용자에 대한 서비스 접근/수행을 제한. 


### UserDetails
* 사용자 정보를 담는 인터페이스이다.
* Spring Security 에서 사용자의 정보를 불러오기 위해 구현해야 하는 인터페이스. 
* 이 때, getUserName() 이 고유한 값을 의미하는데 중복되지 않는 DB 컬럼(PK) 설정을 권장. 

#### Custom User Details 구현
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


### JWT 
* 토큰 기반 인증. 클라이언트가 서버로부터 인증 수행 후 토큰을 획득하여 매 요청 시 헤더에 전달.

#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
implementation 'io.jsonwebtoken:jjwt:0.9.1'
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

#### 토큰의 발급
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


### Spring boot Security
#### Dependencies
* file : [build.gradle](build.gradle)
```groovy
implementation 'org.springframework.boot:spring-boot-starter-security'
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


### Filter 에 JWT Token 적용
* JWT 인증을 수행하기 위한 Filter 를 정의한다. 
* file : [JwtAuthenticationFilter](src/main/java/com/sik/template/common/security/JwtAuthenticationFilter.java)

* SecurityFilterChain 에 filter를 등록한다.
* file : [SecurityConfig.java](src/main/java/com/sik/template/config/SecurityConfig.java)
```java
.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
```