# Name Generator
본 프로젝트는 Spring Boot 기반으로 이름 생성과 운세 조회 기능을 제공하는 애플리케이션입니다.

## 주요 기능
- **이름 생성**: 데이터베이스의 이름 테이블을 이용해 새로운 이름을 조합하여 생성합니다.
- **GPT 이름 생성**: OpenAI API를 호출해 조건에 맞는 이름 목록을 받아옵니다.
- **이름 저장 및 조회**: 선택한 이름을 쿠키에 저장하고 다시 불러올 수 있습니다.
- **이름 통계**: 이름별 연도별 순위와 등록 수를 조회하고 한자 풀이도 제공합니다.
- **운세 생성**: 입력한 단어와 생년월일을 기반으로 운세 문장을 제공합니다.

## 빌드 및 실행
Gradle로 빌드하며 JDK 17 이상이 필요합니다.

## 패키지 구조
- `api` : REST API 컨트롤러
- `name` : 이름 생성 로직과 관련 DTO, 서비스, 리포지토리
- `fortune` : 운세 생성 및 쿠키 저장 기능
- `stats` : 이름 통계 조회 서비스
- `chatgpt` : ChatGPT 연동을 통한 이름 생성
- `common` : 공통 유틸리티 및 예외 처리

## 의존성
주요 의존성은 다음과 같습니다.
- Spring Boot 3.4.4
- Spring Data JPA, Validation, Web, Thymeleaf
- MyBatis, QueryDSL
- H2, PostgreSQL, MySQL 드라이버
- Lombok, ModelMapper

## 기타
`src/main/resources/translate/hanja.txt` 파일에는 이름 한자 풀이를 위한 데이터가 포함되어 있습니다.