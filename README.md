## 🏡 저기어때 : 대규모 트래픽 처리 숙박 예약 서비스

![Modern App Portfolio Mockup Presentation (3) (1)](https://github.com/user-attachments/assets/61b3d03e-f550-451f-9dab-c6416631ee1c)

## 👋 서비스/프로젝트 소개

<aside>

- 대규모 트래픽 처리 E-Commerce 숙박 예약 서비스
    - 사용자가 다양한 숙소를 검색하고 예약하며 결제를 완료할 수 있도록 지원하는 플랫폼
    - MSA(마이크로서비스 아키텍처)를 통해 각 기능을 독립적으로 관리하며 확장성, 유연성, 그리고 장애 복구 능력을 높임
</aside>

## 👑 서비스/프로젝트 목표

<aside>

- **대규모 트래픽 대응**
    - MSA 아키텍처 도입으로 각 기능 독립 관리, 확장성 및 장애 복구 능력 강화
    - Redis와 Kafka를 활용하여 대규모 트래픽에 안정적으로 대응
    - 주요 API 처리량 200.0/sec 이상 달성
    - 동시성 문제 해결로 안전한 서비스 제공


- **모니터링 시스템 구축**
    - Prometheus, Grafana로 실시간 메트릭 모니터링으로 안정성 있는 프로젝트 구축
    - Loki로 알람 상황에 맞춰 설정 → 빠른 파악
      <details>
        <summary>알람</summary>
            <image src="https://github.com/user-attachments/assets/2aa97fbc-df36-4572-9164-ce2379301c7a" width="900"></image>
            <image src="https://github.com/user-attachments/assets/02c21190-a6df-41ff-97e0-e05d168ff3ca" width="900" height="700"></image>
        </details>
      
      <details>
      <summary>Dashboard 설정 변경</summary>
            <image src="https://github.com/user-attachments/assets/dc5dfedd-a383-44a1-b61e-4b574ef1dce1" width="900"></image>
    </details>

 - **데이터 일관성 및 트랜잭션 관리**
   - TransactionSynchronizationManager로 데이터 일관성 관리
   - 중복 및 데이터 손실을 방지하는 Kafka Batch Listener 구현

- **운영 및 배포 효율화**
   - Docker와 Github Actions를 이용한 CI/CD 파이프라인 구축으로 배포 자동화
        
</aside>

## 🤝 역할 분담 및 협업 방식

### **🧑‍💻 CONTRIBUTORS**

| 이름      | 포지션   | 담당                                                                                                      | Github 링크                  |
|-----------|----------|-----------------------------------------------------------------------------------------------------------------------------|------------------------------|
| 김성훈     | `리더`     |▶ Coupon 도메인 서비스 개발 <br>▶ Gateway 통합 swagger 설정 완료<br>▶ 모니터링(Promethues, Grafana, Loki)설정, Kafka, Redis<br>▶Docker-compose 파일 설정| https://github.com/kimsung3113 |
| 강찬욱 | `팀원`     | ▶ Auth/User 서비스 개발<br>▶ Gateway 개발<br>▶ 깃 서브모듈 구현<br>▶ 인프라 및 CI/CD 구축<br>                                               | https://github.com/chanwookK     |
| 김지수 | `팀원`     | ▶ Payment 서비스 개발<br>▶ 포트원 API 연동을 통해 실제 결제 환경 제공<br>▶ Point 서비스 개발<br>| https://github.com/jelliclesu    |
| 문시원 | `팀원`     | ▶ 숙소 도메인 서비스 개발<br>▶ S3 이미지 업로드 구현<br>▶ 협업을 위한 컨벤션 설정<br>| https://github.com/muncool39    |
| 조우석 | `팀원`   | ▶ 예약 관련 서비스 개발<br>▶ 리뷰 관련 서비스 개발<br>▶ S3 이미지 업로드 구현                          | https://github.com/wooseok50    |

## 🖌 인프라 설계도
<details>
<summary>인프라 설계도</summary>
    <image src="https://github.com/user-attachments/assets/540b4e69-ff5a-4cf1-917a-96d365c9983e" width="900"></image>
</details>

## 🖼 ERD
<details>
<summary>ERD</summary>
    <image src="https://github.com/user-attachments/assets/9d3f9f53-3881-4c07-bf40-6b2d1f0cb220" width="900"></image>
</details>

## 📄 주요 기능

### 예약-결제 Flow
<details>
<summary>예약-결제 Flow</summary>
    <image src="https://github.com/user-attachments/assets/1dbcfbbc-5454-43c3-9da4-8d0c474e3cf9" width="900"></image>
    <li> 예약에 쿠폰, 포인트가 모두 사용된 경우를 나타내며, 사용되지 않았을 경우 유효성 검사 및 상태 변경 진행되지 않음</li>
    <li> 현재 테스트 환경에서는 결제 URL 을 직접 브라우저에 입력해야 하지만, 최종적인 목표는 MVC 패턴</li>
    <li> 모든 요청의 실패에 대한 롤백 로직이 작성되어 있으며, 다이어그램 간소화를 위해 생략함 </li>
</details>

### 쿠폰 발급 Flow

<details>
<summary>쿠폰 발급 Flow</summary>
    <image src="https://github.com/user-attachments/assets/059c7716-6669-4c3b-854a-5464d080f135" width="900"></image>
</details>

<aside>  
    
🏠 **숙소 예약 서비스**:

- 숙소 정보 검색 및 숙박 날짜 예약
- 예약 시 쿠폰 및 포인트 적용
</aside>

<aside>

💰 **결제 서비스**:

- [📖 결제 요청/취소 과정](https://github.com/How-about-over-there/server/wiki/%5BDocs%5D-%EA%B2%B0%EC%A0%9C-%EC%9A%94%EC%B2%AD-&-%EA%B2%B0%EC%A0%9C-%EC%B7%A8%EC%86%8C-%EA%B3%BC%EC%A0%95)

- 포트원 API 연동으로 결제 처리
- 결제 성공, 취소 상태 관리
</aside>

<aside>

💵 **포인트 서비스**:

- [📖 포인트 적립/사용 과정](https://github.com/How-about-over-there/server/wiki/%5BDocs%5D-%ED%8F%AC%EC%9D%B8%ED%8A%B8-%EC%A0%81%EB%A6%BD-&-%ED%8F%AC%EC%9D%B8%ED%8A%B8-%EC%82%AC%EC%9A%A9-%EA%B3%BC%EC%A0%95)

- 포인트 적립, 사용 및 자동 만료 처리
- Redis Cache 로 포인트 사용/적립/만료 내역 관리
</aside>

<aside>

🎫 **쿠폰 서비스**: **Redis 및 Kafka를 통한 비동기 쿠폰 발급, 동시성 처리**

## 🚀 기술 스택

<details>
<summary>🖥️ <strong>언어 및 프레임워크</strong></summary>

- **Java 17**: 최신 기능과 안정성을 제공하는 언어.
- **Spring Boot 3.3.7**: 빠르고 유연한 애플리케이션 개발.
- **Spring Cloud Gateway**: API 게이트웨이 및 로드 밸런싱.
- **Spring Cloud Netflix Eureka**: 서비스 디스커버리 및 로드 밸런싱.
- **Spring Cloud OpenFeign**: 마이크로서비스 간 통신을 위한 HTTP 클라이언트.
- **JPQL & QueryDSL**: 복잡한 데이터 조회와 효율적인 SQL 작성.
- **Mapstruct**: DTO 변환 자동화.

</details>

<details>
<summary>📩 <strong>메시징</strong></summary>

- **Apache Kafka**: 고성능, 실시간 스트리밍 데이터 플랫폼.

</details>

<details>
<summary>🛠 <strong>툴 및 소스 관리</strong></summary>

- **Git & Github**: 소스 버전 관리.
- **Postman**: API 테스트 및 디버깅.
- **IntelliJ IDEA**: 강력한 개발 환경.
- **Slack**: 팀 협업 및 커뮤니케이션.
- **Gradle**: 프로젝트 빌드 및 의존성 관리.

</details>

<details>
<summary>📊 <strong>로그 관리 및 모니터링</strong></summary>

- **Prometheus**: 모니터링 및 경보 시스템.
- **Grafana**: 데이터 시각화 및 대시보드.
- **Zipkin**: 분산 트레이싱.
- **Loki**: 로그 관리.
- **Actuator**: 애플리케이션 상태 모니터링.

</details>

<details>
<summary>🔐 <strong>인증 및 보안</strong></summary>

- **JWT**: 안전하고 가벼운 인증 토큰.
- **Argon2**: 강력한 비밀번호 해시 알고리즘.

</details>

<details>
<summary>🌍 <strong>외부 API</strong></summary>

- **PortOne API**: 결제 처리 및 통합.

</details>

<details>
<summary>🗄 <strong>ORM</strong></summary>

- **Spring Data JPA**: 객체-관계 매핑 및 데이터 액세스.

</details>

<details>
<summary>💾 <strong>데이터베이스</strong></summary>

- **PostgreSQL**: 고성능 오픈소스 RDBMS.

</details>

<details>
<summary>⚡ <strong>성능 최적화 및 캐싱</strong></summary>

- **Redis & Redisson**: 고속 데이터 캐싱 및 분산 잠금.
- **LuaScript**: Redis 스크립팅 확장.

</details>

<details>
<summary>☁️ <strong>인프라 및 배포</strong></summary>

- **AWS EC2, RDS, S3, ECS, ECR, Elastic Cache**: 클라우드 인프라 및 관리.
- **Docker & Docker Compose**: 컨테이너 관리 및 오케스트레이션.
- **Github Actions (CI/CD)**: 지속적 통합 및 배포.

</details>

<details>
<summary>✅ <strong>테스트</strong></summary>

- **Jacoco**: 코드 커버리지 분석.
- **SonarQube**: 코드 품질 관리.
- **JUnit5 & Mockito**: 단위 테스트 및 목(Mock) 테스트.

</details>

<details>
<summary>📈 <strong>성능 테스트</strong></summary>

- **Apache JMeter**: 부하 및 성능 테스트.

</details>

<details>
<summary>📚 <strong>문서화</strong></summary>

- **Notion**: 프로젝트 관리 및 문서화.
- **Swagger**: API 문서화 및 테스트.

</details>

<aside>

## 🗣️ 기술적 의사 결정
[💡 ELK Stack Vs Prometheus + Grafana + Loki](https://github.com/How-about-over-there/server/wiki/%5BConcern%5D-ELK-Stack-Vs-Prometheus---Grafana---Loki)

[💡 팩토리 메서드 생성 vs Mapstruct](https://github.com/How-about-over-there/server/wiki/%5BConcern%5D-%ED%8E%99%ED%86%A0%EB%A6%AC-%EB%A9%94%EC%84%9C%EB%93%9C-%EC%83%9D%EC%84%B1-vs-Mapstruct)

[💡 Kafka, RabbitMQ 중 Kafka 선택](https://github.com/How-about-over-there/server/wiki/%5BConcern%5D-Kafka,-RabbitMQ-%EC%A4%91-Kafka-%EC%84%A0%ED%83%9D)

[💡 Redis 자료구조 SortedSet vs Set](https://github.com/How-about-over-there/server/wiki/%5BConcern%5D-Redis-%EC%9E%90%EB%A3%8C%EA%B5%AC%EC%A1%B0-SortedSet-vs-Set)

[💡 숙소 날짜 예약 시의 동시성 문제 해결 방법 결정](https://github.com/How-about-over-there/server/wiki/%5BConcern%5D-%EB%8F%99%EC%8B%9C%EC%84%B1)

[💡 보안 전략에 관한 기술적 의사 결정](https://github.com/How-about-over-there/server/wiki/%5BConcern%5D-%EB%B3%B4%EC%95%88-%EC%A0%84%EB%9E%B5%EC%97%90-%EA%B4%80%ED%95%9C-%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC-%EA%B2%B0%EC%A0%95)

[💡 특정 포인트 내역 조회에 Redis 캐싱 적용](https://github.com/How-about-over-there/server/wiki/%5BConcern%5D-%ED%8A%B9%EC%A0%95-%ED%8F%AC%EC%9D%B8%ED%8A%B8-%EB%82%B4%EC%97%AD-%EC%A1%B0%ED%9A%8C%EC%97%90-Redis-%EC%BA%90%EC%8B%B1-%EC%A0%81%EC%9A%A9)

[💡 포인트 만료 처리 설계: 분산 배치 처리](https://github.com/How-about-over-there/server/wiki/%5BConcern%5D-%ED%8F%AC%EC%9D%B8%ED%8A%B8-%EB%A7%8C%EB%A3%8C-%EC%B2%98%EB%A6%AC-%EC%84%A4%EA%B3%84:-%EB%B6%84%EC%82%B0-%EB%B0%B0%EC%B9%98-%EC%B2%98%EB%A6%AC)

## 🔥 트러블 슈팅

[🦄 서브 모듈 분리](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-%EC%84%9C%EB%B8%8C-%EB%AA%A8%EB%93%88-%EB%B6%84%EB%A6%AC)

[🌸 관리자 API와 유저 API 분리](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-%EA%B4%80%EB%A6%AC%EC%9E%90-API-%EC%99%80-%EC%9C%A0%EC%A0%80-API-%EB%B6%84%EB%A6%AC)

[🐢 쿠폰 발급 동시 요청으로 인한 Consumer Lag 문제, Jmeter 부하테스트](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-%EC%BF%A0%ED%8F%B0-%EB%B0%9C%EA%B8%89-%EB%8F%99%EC%8B%9C-%EC%9A%94%EC%B2%AD%EC%9C%BC%EB%A1%9C-%EC%9D%B8%ED%95%9C-Consumer-Lag-%EB%AC%B8%EC%A0%9C,-Jmeter-%EB%B6%80%ED%95%98%ED%85%8C%EC%8A%A4%ED%8A%B8)

[🐯 쿠폰 발급 동시성 문제 해결 및 기술 선택, Redis ‐ 분산락 vs LuaScript](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-%EC%BF%A0%ED%8F%B0-%EB%B0%9C%EA%B8%89-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0-%EB%B0%8F-%EA%B8%B0%EC%88%A0-%EC%84%A0%ED%83%9D,-Redis-%E2%80%90-%EB%B6%84%EC%82%B0%EB%9D%BD-vs-LuaScript)

[🐥 front가 있다는 가정: 요청 제한을 위해  front에 데이터 전송 = 로그 출력 시점 문제 해결](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-%5Bfront%EA%B0%80-%EC%9E%88%EB%8B%A4%EB%8A%94-%EA%B0%80%EC%A0%95%5D-%EC%9A%94%EC%B2%AD-%EC%A0%9C%ED%95%9C%EC%9D%84-%EC%9C%84%ED%95%B4--front%EC%97%90-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%84%EC%86%A1-=-%EB%A1%9C%EA%B7%B8-%EC%B6%9C%EB%A0%A5-%EC%8B%9C%EC%A0%90-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)

[🐻 Coupon, Event N+1 문제](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-Coupon,-Event-N-1-%EB%AC%B8%EC%A0%9C) 

[🦋 Kafka Exporter 자동 종료 문제 해결](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-Kafka-Exporter-%EC%9E%90%EB%8F%99-%EC%A2%85%EB%A3%8C-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)

[🐰 Kafka Consumer 역직렬화 문제](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-Kafka-Consumer-%EC%97%AD%EC%A7%81%EB%A0%AC%ED%99%94-%EB%AC%B8%EC%A0%9C)

[🍀 Redis 역/직렬화 문제 해결 과정](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-Redis-%EC%97%AD-%EC%A7%81%EB%A0%AC%ED%99%94-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0-%EA%B3%BC%EC%A0%95)

[🎀 Feign Client 통신 문제](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-Feign-Client-%ED%86%B5%EC%8B%A0-%EB%AC%B8%EC%A0%9C)

[🐸 Feign Client 예외 처리 문제](https://github.com/How-about-over-there/server/wiki/%5BTrouble-Shooting%5D-Feign-Client-%EC%98%88%EC%99%B8-%EC%B2%98%EB%A6%AC-%EB%AC%B8%EC%A0%9C)

## 🌟 문서 및 발표 자료
[💬 상태코드 명세서](https://github.com/How-about-over-there/server/wiki/%5BDocs%5D-Error-%EC%83%81%ED%83%9C-%EC%BD%94%EB%93%9C%ED%91%9C)

[📃 최종발표자료](https://github.com/How-about-over-there/server/wiki/%5BDocs%5D-%EC%B5%9C%EC%A2%85-%EB%B0%9C%ED%91%9C-%EC%9E%90%EB%A3%8C)
