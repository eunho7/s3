# S3 File CRUD API (Spring Boot + MyBatis + MySQL)

AWS S3에는 파일 바이너리를 저장하고, MySQL의 `tb_file_attachment` 테이블에는 파일 메타데이터를 저장하는 백엔드 전용 REST API 프로젝트입니다.

## 1) 기술 스택
- Java 17
- Spring Boot 3.x
- MyBatis (XML Mapper)
- MySQL
- AWS SDK v2 (S3, S3 Presigner)
- springdoc-openapi (Swagger UI)

## 2) 프로젝트 구조
```text
src/main/java/com/example/s3files
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── mapper
└── service
    └── impl
```

계층은 `Controller / Service / Mapper` 구조로 분리했습니다.

## 3) DB 스키마
`docs/schema.sql`을 사용해 테이블을 생성합니다.

```bash
mysql -u root -p s3_file_db < docs/schema.sql
```

## 4) 설정
`src/main/resources/application.yml` 기본값을 수정하거나,
`application-example.yml`을 복사해 환경별 profile 설정에 사용하세요.

### 주요 설정
- `spring.datasource.*`: MySQL 접속 정보
- `aws.s3.bucket`: S3 버킷명
- `aws.s3.region`: 리전
- `aws.s3.presigned-url-duration-minutes`: 다운로드 URL 만료 시간(분)

## 5) 실행 방법

### 5-1. 빌드
```bash
mvn clean package
```

### 5-2. 실행
```bash
mvn spring-boot:run
```

서버 기본 포트: `8080`

## 6) Swagger/OpenAPI
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 7) API 명세

### 7-1. 파일 업로드
- **POST** `/api/files`
- Content-Type: `multipart/form-data`
- Form-data
  - `file` (MultipartFile)
  - `createUser` (String)

### 7-2. 파일 상세 조회
- **GET** `/api/files/{fileSeq}`

### 7-3. 파일 목록 조회
- **GET** `/api/files?useYn=Y&keyword=report`

### 7-4. 파일 메타데이터 수정
- **PUT** `/api/files/{fileSeq}`
- Body(JSON)
```json
{
  "updateUser": "admin",
  "useYn": "Y"
}
```

### 7-5. 파일 삭제 (소프트 삭제)
- **DELETE** `/api/files/{fileSeq}?updateUser=admin`
- S3 오브젝트를 삭제하고, DB는 `use_yn='N'`으로 업데이트합니다.

### 7-6. 다운로드 Presigned URL 조회
- **GET** `/api/files/{fileSeq}/download-url`

## 8) 구현 포인트
1. 업로드 시 UUID 기반 `stored_file_name` 생성
2. 파일 바이너리는 S3, 메타데이터는 MySQL 저장
3. MyBatis XML Mapper 방식 사용
4. 공통 예외 처리를 위한 `@RestControllerAdvice` 적용
5. Request/Response DTO 분리

## 9) 참고
AWS 인증은 기본 자격증명 체인(DefaultCredentialsProvider)을 사용합니다.
로컬에서는 다음 중 하나가 필요합니다.
- `~/.aws/credentials`
- 환경변수 (`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`)
- EC2/ECS IAM Role
