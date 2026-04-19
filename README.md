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
# 프로젝트 정보
진행 단체 : 영남직업기술개발원
<br>
프로젝트 목적 : 시승신청 예약관리 사이트 개발
<br>
개발 기간 : 2023-11-22 ~ 2023-12-21
<br>
# 팀 소개
<br>
<img src="https://github.com/mydkonline/CarWebProject/assets/67779682/bd5ab885-0daf-4a54-8ee3-c611210ce4b5" alt="프로필 이미지" width="100" height="100">
<br>
박승현

# 프로젝트 소개
## 서비스 목적
<br>
우리는 사용자들의 편의를 위해 자동차 시승신청 시 한곳에서 
편하게 보고 결정할 수 있도록 통합시승신청을 지향하면서 사이트를 만들었습니다.
사이트 이름은 모션볼트(MotionVolt)로 보다 친환경이고 미래로 갈것이기에 이렇게 지었고
다른 브랜드들의 신차출시에 어떤 유형을 보고 비교하고 앞으로의 자동차 모양의 유행이나
디자인이 앞으로 어떻게 변화할지도 알 수 있기에 이 점이 더욱 장점으로써 기대해 볼 수 있겠습니다.

## 주요기능
### 메인페이지
![스크린샷 2023-12-22 103457](https://github.com/mydkonline/CarWebProject/assets/67779682/93b2537b-4b9f-49bd-9ab8-e93edfa01603)
### 시승신청 시연 과정
![스크린샷 2023-12-22 101921](https://github.com/mydkonline/CarWebProject/assets/67779682/659c016a-4033-4341-88f7-4246c4c78394)
### 관리자 페이지
![스크린샷 2023-12-22 102347](https://github.com/mydkonline/CarWebProject/assets/67779682/0f91ded7-6021-4ec4-a615-fa8f113de746)


| 주요 기능 | 메인 페이지 | 시승신청 시연 과정 | 관리자 페이지 |
|------------|--------------|--------------------|----------------|
|            | ![메인 페이지](https://github.com/mydkonline/CarWebProject/assets/67779682/93b2537b-4b9f-49bd-9ab8-e93edfa01603) | ![시승신청 시연 과정](https://github.com/mydkonline/CarWebProject/assets/67779682/659c016a-4033-4341-88f7-4246c4c78394) | ![관리자 페이지](https://github.com/mydkonline/CarWebProject/assets/67779682/0f91ded7-6021-4ec4-a615-fa8f113de746) |
