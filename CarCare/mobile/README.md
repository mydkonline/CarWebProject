# MotionVolt React Native Frontend

Spring Boot JPA API 백엔드와 연결되는 Expo 기반 React Native 앱입니다.
화면과 이미지는 React Native가 담당하고, Spring Boot는 JSON API만 제공합니다.

## 실행 순서

1. Spring Boot 서버 실행

```bash
cd /Users/desktop/CarWebProject/CarCare
mvn spring-boot:run
```

2. 모바일 앱 실행

```bash
cd /Users/desktop/CarWebProject/CarCare/mobile
npm install
npm start
```

## 접속 위치

- 고객 시승 예약: Expo 앱 상단의 `시승 예약`
- 관리자 대시보드: Expo 앱 상단의 `관리자`
- 관리자 샘플 계정: `admin / admin123`
- Spring Boot 상태 확인: `http://localhost:18081/`
- 관리자 API 확인: `http://localhost:18081/api/admin/summary`

## API 주소 변경

`App.js`의 `API_BASE_URL` 값을 실행 환경에 맞게 바꿔 주세요.

- iOS simulator: `http://localhost:18081`
- Android emulator: `http://10.0.2.2:18081`
- Real device: `http://<Mac LAN IP>:18081`

## 연결된 API

- `GET /api/cars`
- `GET /api/cars/{carId}/options`
- `GET /api/options/{optionId}/centers`
- `POST /api/reservations`

## 이미지 리소스

기존 Spring Boot 정적 이미지들은 `mobile/assets/images`로 이동했습니다.
React Native는 `require('./assets/images/...')` 형식의 로컬 이미지를 사용하므로 백엔드 이미지 URL에 의존하지 않습니다.

현재 예약 API는 로컬 데모를 위해 `data.sql`의 샘플 카카오 사용자 `9000000001`을 사용합니다. 실제 출시 단계에서는 React Native Kakao SDK 로그인 결과의 사용자 ID를 `kakaoUserId`로 넘기면 됩니다.

## PostgreSQL 실행

기본 백엔드는 PostgreSQL을 바라봅니다.

```bash
createdb carcare
cd /Users/desktop/CarWebProject/CarCare
DB_USERNAME=carcare DB_PASSWORD=carcare SQL_INIT_MODE=always mvn spring-boot:run
```

`SQL_INIT_MODE=always`는 `schema.sql`, `data.sql`을 다시 적용하므로 샘플 초기화가 필요할 때만 사용하세요. 로컬 메모리 DB로 빠르게 확인하려면 다음처럼 실행할 수 있습니다.

```bash
cd /Users/desktop/CarWebProject/CarCare
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Docker Compose 실행

Docker Compose는 PostgreSQL, Spring Boot API, Expo Web을 함께 실행합니다.

```bash
cd /Users/desktop/CarWebProject/CarCare
cp .env.example .env
docker compose up --build
```

접속 위치:

- React Native Web: `http://localhost:19006`
- Spring Boot API: `http://localhost:18081`
- PostgreSQL: `localhost:5432`

기본 Compose 설정은 샘플 데이터를 바로 볼 수 있도록 `SQL_INIT_MODE=always`입니다. DB 데이터를 유지하려면 `.env`에서 아래처럼 바꾼 뒤 API 컨테이너를 다시 실행하세요.

```bash
SQL_INIT_MODE=never
```

DB 볼륨까지 완전히 초기화하려면 다음 명령을 사용합니다.

```bash
docker compose down -v
```
