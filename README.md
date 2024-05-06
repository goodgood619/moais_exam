## TODO List 서비스 개발

* Language : Kotlin
* Framework : Spring Boot
* DataBase
  * test : h2
  * runtime : mysql
* 실제 서버를 구성하진 않았고, 
  * 해당 서버를 실행 시에는
  * docker와 docker-compose를 깔아야 합니다


### 구현 방향 (회원 가입, 로그인, 로그아웃)

---

* 회원 가입 시, 핸드폰번호와 닉네임 그리고, 비밀번호가 있다
    * 핸드폰번호를 userId 값으로 사용하려고 한다
    * 그러나 핸드폰번호는 개인정보기에 저장시에는 비밀번호와 똑같이 단방향 암호화로 저장한다
* 회원 가입과 로그인을 할 경우는, permit을 해서 SecurityContextHoler를 가지고 있지 않아도 되게 설정
* 로그인을 하면, DB 해당 record에 발급한 JWT token로 업데이트
* 반대로, 로그아웃을 하면 DB 해당 record에 token을 빈공백으로 update

### 구현 방향 (TODO)

---
* 로그인한 회원만 TODO List를 작성하거나, 조회 할수 있습니다.
  * 로그인하지 않으면, TODO와 관련된 API를 이용할수 없습니다.
* 회원은 여러 TODO List를 작성할 수 있으므로, 회원과 TODO는 1 : n 관계로 구성
* TODO List의 id는 UUID로 설정 -> 사용자가 많아지면서, 동시에 많은 TODO를 작성 할 수 있는 것을 고려

### 문제 해결

---

* Mac M1 Docker Setting
    * https://velog.io/@sujeongim/%EC%98%A4%EB%A5%98-%EC%B2%9C%EA%B5%AD-Docker%ED%8E%B8-Mac-M1-no-matching-manifest-for-linuxarm64v8
* mysql UTF8 설정
    * https://m.blog.naver.com/PostView.naver?blogId=playhoos&logNo=221509276474&proxyReferer=

### DDL 작성

---

```mysql
    create table users (
      created_at datetime(6),
      updated_at datetime(6),
      id varchar(255) not null,
      nickname varchar(255),
      token varchar(255),
      user_password varchar(255),
      primary key (id)
     ) engine=InnoDB;

    create table todo (
      created_at datetime(6),
      due_date datetime(6),
      updated_at datetime(6),
      id binary(16) not null,
      description varchar(255),
      title varchar(255),
      user_id varchar(255),
      priority_status enum ('LOW','NORMAL','HIGH','CRITICAL'),
      status enum ('TODO','IN_PROGRESS','DONE','PENDING'),
      primary key (id)
    ) engine=InnoDB;

    alter table todo
      add constraint FKdcopxq1yu1u8ijb7rjexhsr6v
      foreign key (user_id)
      references users (id);
```

### 실행 방법

---

* 사전 세팅 사항 : docker-compose를 깔아 주세요
* Dockerfile이 있는 디렉토리에서 다음의 명령어를 실행해 주세요
```shell
docker-compose up -d
```

### 구현 API

---

```text


## 공통 응답 코드 (Common Response Code)

{
   "code": 200, 
   "message": "ok", 
   "data":{
       ...
	}
}

- 비고 : data가 null일 경우 전달하지 않음
- 아래에서 Response 라고 서술 되어 있는 내용은 data 의 상세 예시 형태를 의미 합니다.

## 회원 등록

- POST /user/signup
    
    RequestBody : {
        "userId" : "010-1234-1234",
        "nickName": "goodgood",
        "password" : "password"
    }

## 로그인

- POST /user/login
    RequestBody : {
        "userId" : "010-1234-1234",
        "password" : "password"
    }
    
    Response : {
        "id": "9Gb7kXR5HEnaFaMVklCitQ==", // 암호화된 user.id
        "token": "eyJhbGciOi...DwD-KleUjA" // 로그인 후 발행 한 JWT Token
    }

## 로그 아웃

- GET /user/logout
    Headers : {
        "Authorization" : "로그인 후 발행 받은 JWT Token"
    }
    
## 회원 탈퇴

- DELETE /user/delete
    Headers : {
        "Authorization" : "로그인 후 발행 받은 JWT Token"
    }

## TODO 등록

- POST /todo

    Headers : {
        "Authorization" : "로그인 후 발행 받은 JWT Token"
    }
    
    RequestBody : {
        "userId" : "9Gb7kXR5HEnaFaMVklCitQ==",
        "title": "테스트2",
        "description": "상세 설명 내용 테스트2",
        "dueDate" : "2024-05-05" // 마감 날짜
    }

## TODO 목록 조회 (단 특정 회원에만 속함)

- GET /todo

    Headers : {
        "Authorization" : "로그인 후 발행 받은 JWT Token"
    }
    
    RequestParam : {"userId": "9Gb7kXR5HEnaFaMVklCitQ=="}

    Response : {
        "id": "bab8609c-1ec3-4f04-805f-b02fcfe499a1",
        "title": "테스트 제목",
        "description": "상세 내용 2",
        "dueDate": "2024-05-06 00:00:00",
        "todoStatus": "TODO",
        "priorityStatus": "NORMAL",
    }
    
## TODO LAST 조회 (단 특정 회원에만 속함)

- GET /todo/last

    Headers : {
        "Authorization" : "로그인 후 발행 받은 JWT Token"
    }
    
    RequestParam : {"userId": "9Gb7kXR5HEnaFaMVklCitQ=="}
 
    Response : {
        "id": "bab8609c-1ec3-4f04-805f-b02fcfe499a1",
        "title": "테스트 제목",
        "description": "상세 내용 2",
        "dueDate": "2024-05-06 00:00:00",
        "todoStatus": "TODO",
        "priorityStatus": "NORMAL",
    }


## TODO List 상태 변경

- PUT /todo/{id}/status

    - id : TODO의 id (UUID)
    
    Headers : {
        "Authorization" : "로그인 후 발행 받은 JWT Token"
    }
    
    RequestBody : {
      "todoStatus": "IN_PROGRESS"
    }
    
    Response : {
        "id": "bab8609c-1ec3-4f04-805f-b02fcfe499a1",
        "todoStatus": "IN_PROGRESS",
    }

```