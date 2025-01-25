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
    - MSA를 통해 각 기능을 독립적으로 관리하며 확장성, 유연성, 그리고 장애 복구 능력을 높임
</aside>

<aside>

- **모니터링 시스템 구축**
    - Prometheus, Grafana로 실시간 메트릭 모니터링으로 안정성 있는 프로젝트 구축
    - Loki로 알람 상황에 맞춰 설정 → 빠른 파악
    - 알람
        
        ![스크린샷 2025-01-22 182843.png](attachment:d58871cc-23e4-4837-aa3d-aa478597fa36:스크린샷_2025-01-22_182843.png)
        
        ![스크린샷 2025-01-22 182152.png](attachment:f4417e35-8449-41cb-b06a-247e0fee4766:스크린샷_2025-01-22_182152.png)
        
    - Dashboard 설정 변경
        
        ![스크린샷 2025-01-25 003209.png](attachment:1995fe8e-6ad6-467b-b9c0-d5c11eb06432:스크린샷_2025-01-25_003209.png)
        
</aside>

<details>
<summary>## 🖌 인프라 설계도</summary>
![제목 없는 다이어그램 drawio](https://github.com/user-attachments/assets/fc4cbd06-5619-484b-839d-bc6734ab42f7)
</details>

## 📄 주요 기능

### 예약-결제 Flow

<img width="1075" alt="image" src="https://github.com/user-attachments/assets/1dbcfbbc-5454-43c3-9da4-8d0c474e3cf9" />


- 예약에 쿠폰, 포인트가 모두 사용된 경우를 나타내며, 사용되지 않았을 경우 유효성 검사 및 상태 변경 진행되지 않음
- 현재 테스트 환경에서는 결제 URL 을 직접 브라우저에 입력해야 하지만, 최종적인 목표는 MVC 패턴
- 모든 요청의 실패에 대한 롤백 로직이 작성되어 있으며, 다이어그램 간소화를 위해 생략함

### 쿠폰 발급 Flow

![image (1)](https://github.com/user-attachments/assets/059c7716-6669-4c3b-854a-5464d080f135)

<aside>
🏠**숙소 예약 서비스**

- 숙소 정보 검색 및 숙박 날짜 예약
- 예약 시 쿠폰 및 포인트 적용
</aside>

<aside>
💰

**결제 서비스**:

[결제 요청/취소 과정](https://www.notion.so/7ecd7621e166448fb5dc606708f38c55?pvs=21)

- 포트원 API 연동으로 결제 처리
- 결제 성공, 취소 상태 관리
</aside>

<aside>
💵

**포인트 서비스**:

[포인트 적립/사용 과정](https://www.notion.so/1842dc3ef514812e81b8cdd71a7e2231?pvs=21)

- 포인트 적립, 사용 및 자동 만료 처리
- Redis Cache 로 포인트 사용/적립/만료 내역 관리
</aside>

<aside>
🎫

**쿠폰 서비스**: **Redis 및 Kafka를 통한 비동기 쿠폰 발급, 동시성 처리**

- 대용량 트래픽을 수용하기 위한 비동기 쿠폰 발급
- Redis의 Lua Script로 동시성 제어 및 쿠폰 발급 상태 관리
- Kafka 를 활용한 실시간 이벤트 & 병렬 처리 (시간 및 수량 제한)
</aside>

<aside>
💻

**리뷰 서비스**

- 숙소 리뷰 관리 서비스
- S3 이미지 업로드
</aside>

## 🗣️ 기술적 의사 결정
💡 ELK Stack Vs Prometheus + Grafana + Loki

💡 팩토리 메서드 생성 vs Mapstruct

💡 Kafka, RabbitMQ 중 Kafka 선택

💡 Redis 자료구조 SortedSet vs Set

[💡 숙소 날짜 예약 시의 동시성 문제 해결 방법 결정](https://github.com/How-about-over-there/server/wiki/%5BConcern%5D-%EB%8F%99%EC%8B%9C%EC%84%B1)

💡 보안 전략에 관한 기술적 의사 결정

💡 특정 포인트 내역 조회에 Redis 캐싱 적용

💡 포인트 만료 처리 설계: 분산 배치 처리
