# 선물하기 프로젝트

- 다른 회원이나 나에게 상품을 선물하며 서로 마음을 확인할수 있는 웹어플리케이션

## Entitiy diagram
![domain-diagram(give me a gift)](https://user-images.githubusercontent.com/67785334/129677738-03e7936b-6c75-48ce-bc77-3383f04f7679.png)

## Domain diagram
![erd(give me a gift)](https://user-images.githubusercontent.com/67785334/129677807-5b8cc79d-5b48-410d-8c35-b35d54c92477.png)

## Database
- h2
- mySQL

### h2
<application.yml>
```yml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: (url)
    username: (username)
    password: (password)
```

### mySql
<application.yml>
```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:(포트번호)/(DB이름)?serverTimezone=Asia/Seoul
    username: (username)
    password: (password)
```
