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
- Security 헤더 통합



![filterChaine](/docs/images/filterchain.png)

클라이언트가 애플리케이션에 요청을 보내면, 컨테이너는 `FilterChain`을 만들어준다. (`FilterChain`에는 요청 URI에 따른 `HttpServletRequest`를 처리할 `Filter`들과 `Servlet`을 포함하고 있다. )

MVC 애플리케이션에서는 Servlet은 `DispatcherServlet`의 인스턴스다. 하나의 Servlet은 각 하나의 `HttpServletRequest`와 `HttpServletResponse`를 다룰 수 있다. 그러나 Filter는 하나 이상이 다음과 같은 용도로 사용할 수 있다.
- 또다른 `Filter`들과 `Servlet`을 주입받는 것으로부터 막아준다. 이 경우에 `Filter`는 HttpServletResponse라고 쓰인다.
- Filter들과 Servlet에 의해 사용되는 `HttpServletRequest` 또는 `HttpServletResponse`를 수정한다.

