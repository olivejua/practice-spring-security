# spring security
학습 출처: https://docs.spring.io/spring-security/site/docs/current/reference/html5/


## how to start security
1. 의존성 설정 (스프링부트 환경)

#### gradle인 경우

<b>build.gradle</b>
```
dependencies {
    compile "org.springframework.boot:spring-boot-starter-security"
}
```

#### maven인 경우

<b>pom.xml</b>
```
<dependencies>
    <!-- ... other dependency elements ... -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```
 
## Spring Boot Auto Configuration
- Spring Security의 기본 설정: `springsecurityFilterChain`이라는 빈 이름을 가진 servlet `Filter`를 만든다. 이 빈은 애플리케이션 내의 URL들을 보호하고, username과 password를 인증하고, 로그인폼으로 리다이렉트하는 등의 역할을 한다.
- `UserDetailsService` 빈 생성:  `user`라는 username과 콘솔에 기록되는 랜덤하게 생성되는 암호를 가지고 있다.
- 모든 request마다 Servlet container의 springSecurityFilterChain이라는 이름을 가진 빈의 Filter를 등록한다.


Spring Boot는 많은 설정이 되어 있지는 않지만, 많은 일을 수행하고 있다. 
### 기능 요약은 다음과 같다.
- 애플리케이션 내의 상호작용을 위해서 인증된 유저를 요구한다.
- 기본 로그인 폼을 제공한다.
- `user`라는 username과 콘솔에 기록되는 패스워드로 로그인해라.
- BCrypt를 이용해 패스워드 저장소를 보호한다. (BCrypt: 비밀번호를 해시함수로 Niels Provos와 David Mazieres에 의해 만들어진 Blowfish라는 암호에 기반하였다.)
- 유저를 로그아웃 시켜준다.
- [CSRF attack](https://en.wikipedia.org/wiki/Cross-site_request_forgery) 예방
- [Session Fixation](https://en.wikipedia.org/wiki/Session_fixation) 보호
- Security header integration
    - 보안요청을 위한 [HTTP Strict Transport Security](https://en.wikipedia.org/wiki/HTTP_Strict_Transport_Security)
    - X-Content-Type-Options integration
    - Cache Control
    - X-XSS-Protection integration
    - X-Frame-Options integration 
- 다음 Servlet API Methods와 통합
    - [HttpServletRequest#getRemoteUser()](https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html#getRemoteUser())
    - [HttpServletRequest.html#getUserPrincipal()](https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html#getUserPrincipal())
    - [HttpServletRequest.html#isUserInRole(java.lang.String)](https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html#isUserInRole(java.lang.String))
    - [HttpServletRequest.html#login(java.lang.String, java.lang.String)](https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html#login(java.lang.String,%20java.lang.String))
    - [HttpServletRequest.html#logout()](https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html#logout())

## A Review of `Filter`s

![filterChaine](/docs/images/filterchain.png)

클라이언트가 애플리케이션에 요청을 보내면, 컨테이너는 `FilterChain`을 만들어준다. (`FilterChain`에는 요청 URI에 따른 `HttpServletRequest`를 처리할 `Filter`들과 `Servlet`을 포함하고 있다. )
MVC 애플리케이션에서는 Servlet은 `DispatcherServlet`의 인스턴스다. 하나의 Servlet은 각 하나의 `HttpServletRequest`와 `HttpServletResponse`를 다룰 수 있다. 그러나 Filter는 하나 이상이 다음과 같은 용도로 사용할 수 있다.
- 또다른 `Filter`들과 `Servlet`을 주입받는 것으로부터 막아준다. 이 경우에 `Filter`는 HttpServletResponse라고 쓰인다.
- Filter들과 Servlet에 의해 사용되는 `HttpServletRequest` 또는 `HttpServletResponse`를 수정한다.


## DelegatingFilterProxy

Spring은 `DelegatingFilterProxy`라는 이름의 `Filter` 구현체를 제공한다. `DelegatingFilterProxy`는 서블릿 컨테이너의 lifecycle와 스프링의 `ApplicationContext` 사이를 연결시켜준다.
서블릿 컨테이너는 자체 표준을 사용하여 `Filter`를 등록할 수 있지만 스프링에 정의된 Beans은 인식하지 못한다. 하지만 DeletegatingFilterProxy는 표준 서블릿 컨테이너 매커니즘을 통해 등록될 수 있다. 그리고 이름대로 모든 작업은 `Filter`를 구현하는 스프링 빈에 위임한다.

![delegatingfilterproxy](/docs/images/delegatingfilterproxy.png)

`DelegatingFilterProxy`의 또다른 장점은 Filter bean 인스턴스들을 찾는 것을 지연시킬 수 있다. 이게 중요한 이점이 되는 이유는 컨테이너는 실행되기 전에 모든 `Filter` 인스턴스들을 등록해야한다.
그런데 Spring은 보통 스프링 빈을 로드시키는데 ContextLoaderListener를 사용하는데 `Filter` 인스턴스이 등록되어야할 때까지 수행되지 않기 때문이다.

## FilterChainProxy
Spring Security 의 서블릿 지원은 `FilterChainProxy`가 포함된다. `FilterChainProxy`는 Spring Security가 제공하는 특별한 `Filter`이다. `FilterChainProxy`는 `SecurityFilterChain`을 통해 많은 Filter 인스턴스들에게 위임해줄 수 있다.
`FilterChainProxy`는 빈이기 때문에 `DelegatingFilterProxy`에 감싸져있다.

![filterchainproxy](/docs/images/filterchainproxy.png) 

## SecurityFilterChain
`SpringFilterChain`은 `FilterChainProxy`에 의해 사용되어진다. 
`SpringFilterChain`는 request에 따라 주입되어야 하는 Spring Security `Filter`를 결정하는데 사용된다. 

![securityfilterchain](/docs/images/securityfilterchain.png)

`SecurityFilterChain`에 `Security Filters`는 대부분 Bean이다. 그러나 이 빈들은 `DelegatingFilterProxy`대신에 `FilterChainProxy`에 등록되어진다. 
`FilterChainProxy`를 서블릿 컨테이너나 DelegatingFilterProxy에 직접 등록할 때 수많은 이점들이 있다. 먼저, 모든 Spring Security Servlet support의 시작포인트를 제공한다.
그 이유인 즉슨, 당신이 Spring Security's Servlet support에 문제해결 시도를 했을 때 `FilterChainProxy` 안에 디버그 포인트를 추가하기에 아주 좋은 지점이다. 왜냐하면 FilterChainProxy가 Springg Security Servlet support의 시작점이기 때문이다.

두번째, `FilterChainProxy`는 Spring Security 사용의 중심이 되기 때문에, 선택적으로 보여지지 않는 태스크들을 수행할 수 있다. 
예를 들어, Security context를 메모리 누출을 피하기 위해 청소한다. ㅓ또한 Spring Securityd의 HttpFirewall을 특정 타입의 공격을 방어하기 위해 지원한다.

게다가, `SecurityFilterChain`이 주입받아야하는 시점을 더욱 유연하게 결정할 수 있다. Servlet container에서는 `Filter`가 URL만으로 판단되어 주입을 받는다. 하지만 `FilterChainProxy`는 `HTTPServletRequest`를 고려하여 주입을 판단할 수 있다.

사실, `FilterChainProxy`는 어떤 `SecurityFilterChain`을 사용할지 결정하는데 사용한다. 우리의 애플리케이션의 다른 부분에 맞게 환경설정을 할 수 있다.




#Authentication

### ArchitectureComponent

## SecurityContextHolder
`SecurityContextHolder`는 Spring Security가 인증된 사용자의 상세정보들을 저장하는 곳이다. 