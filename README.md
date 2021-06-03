# spring security
https://docs.spring.io/spring-security/site/docs/current/reference/html5/


## how to start security
1. 의존성 설정 (스프링부트 환경)

- gradle인 경우

[build.gradle]
```
dependencies {
    compile "org.springframework.boot:spring-boot-starter-security"
}
```
  
하지만 spring boot를 이용하는 경우 spring-boot-start-security 에 위 두가지 의존성이 포함되어 있어서 하나만 추가해줘도 된다.
- maven인 경우

[pom.xml]
```
<dependencies>
    <!-- ... other dependency elements ... -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```
 