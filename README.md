# 요구사항
현재 `CarCare`는 Spring Boot MVC 기반으로 리팩토링되었습니다.

- Java 8
- Spring Boot 2.7.x
- Maven
- Thymeleaf
- H2 sample DB by default, MySQL optional
- Hexagonal Architecture

## 실행

```bash
cd CarCare
mvn spring-boot:run
```

기본 실행 주소는 `http://localhost:18081/main`입니다. 다른 포트를 쓰려면 `SERVER_PORT` 환경변수를 지정하세요.

기본 실행은 내장 H2 샘플 DB를 사용합니다. 별도 MySQL 서버 없이 카탈로그, 센터, 관리자 예약/상품 조회 화면을 바로 확인할 수 있습니다.

- H2 Console: `http://localhost:18081/h2-console`
- JDBC URL: `jdbc:h2:mem:carcare`
- User: `sa`
- Password: empty
- Admin Login: `admin / admin123`

실제 MySQL로 전환할 때만 DB 접속 정보를 환경변수로 설정하세요. 이때 샘플 초기화를 끄려면 `SQL_INIT_MODE=never`를 같이 지정합니다.

```bash
DB_URL=jdbc:mysql://localhost:3306/carcare?serverTimezone=Asia/Seoul\&characterEncoding=UTF-8
DB_USERNAME=root
DB_PASSWORD=your-password
DB_DRIVER=com.mysql.cj.jdbc.Driver
SQL_INIT_MODE=never
KAKAO_CLIENT_ID=your-kakao-client-id
KAKAO_REDIRECT_URI=http://localhost:18081/kakao
```

## Spring Boot 구조

```bash
src/main/java/com/motionvolt/carcare
├── adapter
│   ├── in/web
│   └── out/persistence, oauth
├── application
│   ├── port/in
│   ├── port/out
│   └── service
└── domain/model
```

기존 JSP 화면은 Thymeleaf 템플릿으로 전환되었고, 이미지/CSS/JS는 Spring Boot 정적 리소스 위치로 이관되었습니다.

```bash
src/main/resources/templates
src/main/resources/static
```

# 프로젝트 파일 디렉터리 구조
```bash
├── CarWebProject
│   ├── CarCare
│   │   ├── pom.xml
│   │   ├── docs
│   │   └── src/main
│   │       ├── java/com/motionvolt/carcare
│   │       └── resources
│   │           ├── static
│   │           ├── templates
│   │           ├── schema.sql
│   │           └── data.sql
```



