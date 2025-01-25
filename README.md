## 🏡 저기어때 : 대규모 트래픽 처리 숙박 예약 서비스

![Modern App Portfolio Mockup Presentation (3) (1).png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/09ebf1b7-b8bf-4f81-81f9-493edc63f59a/Modern_App_Portfolio_Mockup_Presentation_(3)_(1).png)

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

## 🖌 인프라 설계도

![제목 없는 다이어그램.drawio.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/34226674-3b13-404a-aa48-8278a1bcad05/%EC%A0%9C%EB%AA%A9_%EC%97%86%EB%8A%94_%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.drawio.png)

## 📄 주요 기능

### 예약-결제 Flow

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/2323b609-17b9-4419-b04b-8201e01fca58/image.png)

- 예약에 쿠폰, 포인트가 모두 사용된 경우를 나타내며, 사용되지 않았을 경우 유효성 검사 및 상태 변경 진행되지 않음
- 현재 테스트 환경에서는 결제 URL 을 직접 브라우저에 입력해야 하지만, 최종적인 목표는 MVC 패턴
- 모든 요청의 실패에 대한 롤백 로직이 작성되어 있으며, 다이어그램 간소화를 위해 생략함

### 쿠폰 발급 Flow

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/7ee96953-5919-4085-818b-58d91648eab9/image.png)

<aside>
🏠

**숙소 예약 서비스**

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
