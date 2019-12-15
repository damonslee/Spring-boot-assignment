# 주택금융 공급현황 분석 서비스
[![Coverage Status](https://coveralls.io/repos/github/hanaset/Spring-boot-assignment/badge.svg?branch=master)](https://coveralls.io/github/hanaset/Spring-boot-assignment?branch=master)
<br/>

### 요구사항

- csv 파일을 받아 각 레코드를 데이터베이스에 저장하는 API
- 주택금융 공급 금융기간(은행) 목록을 제공해주는 API
- 년도별 각 금융기간의 지원금액 합계를 제공해주는 API
- 각 년도별 각 기간의 전체 지원금액 중 가장 큰 금액의 기관명을 제공해주는 API
- 전체 년도에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 제공해주는 API
- 특정 은행의 특정 달에 대해서 2018년도 해당 달에 금융지원 금액을 예측하여 제공해주는 API (선택)

- jpa 사용 / 주택금융 공급기관에 대해서 독립 엔티티로 디자인 ("institute_name", "institute_code"}

<br/>

<추가사항>
- jwt를 사용하여 Token 인증 및 회원가입, 로그인 구현
- 회원가입 시 DB에 저장 ( 패스워드는 인코딩, 토큰은 특정 secret으로 서명하여 생성 )

- 유닛테스트 코드 개발 / 모든 입출력은 JSON형식 / README.md파일에는 개발 프레임워크, 문제해결 전략, 빌드 및 실행 방법 작성

<br/>

## 개발환경
* Java 8 + Spring boot 2.1.12
* Mysql
* Gradle 5.4.1
* travis
* coveralls

<br/>

## 문제해결 전략

- **csv 파일을 받아 각 레코드를 데이터 베이스에 저장하는 API**
    - 문제점 : 은행에 대한 데이터를 처리할 방법이 필요
        - csv파일의 첫번쨰 row데이터를 통해 은행을 체크하고 있을 경우 테이블에서 기존의 코드를, 없을 경우 새로운 코드를 발행하여 테이블에 저장하였습니다. 

<br>

- **2018년도 해당 달에 대한 지원 금액 예측 API**
    - 문제점 : 예측 알고리즘 구현
        1. 예측 방법으로는 2005~2017년도까지의 상승액에 대한 평균을 가져옵니다. (선형 그래프의 기울기를 계산)
        2. 계산 된 선형 그래프와의 2005~2017년도 데이터가 매달 몇 프로의 오차가 있는지 계산합니다. (선형 그래프와의 오차율 계산)
        3. 2018년 2월 달의 데이터를 얻고 싶다면 매년 2월들의 오차에 임의의 임계치를 넣어 매년 2월들의 오차율을 평균냅니다 (해당 달에 대한 오차율 계산)
        4. 선형그래프에 계산되어 나온 오차율을 곱함으로써 예측 데이터를 뽑아냅니다.
    
<br>

- **패스워드 관리**
    - 문제점 : 데이터의 암호화
        - 패스워드는 사용자에게 제공해줄 필요가 없는 데이터이기 sha256 방식을 사용하여 단방향 암호화를 하였습니다.
        - password 체크 시 입력한 데이터를 암호화여 테이블 존재하는 이미 암호화되어 저장되어 있는 데이터와 비교하여 로그인을 구현하였습니다.

<br>

- **유닛테스트 코드 작성**
    - 문제점 : 단위별로 테스트를 할 수 있도록 테스트 코드 작성
        - Unit Test를 Class 단위로 작성
        
<br>

## 구현 API 
- **회원가입 : ```POST /auth/sign_up```**
    - Request : 
    ```
    Content-Type : Application/json
    {
        "id" : {id} // 회원 가입할 아이디 (숫자, 영문자만 / 최대 16자리까지 입력 가능)
        "password" : {password} // 비밀번호 (숫자, 영문자, !@#$%^&* / 10~50자리까지 입력 가능)
    }
    ```
    - Response : 
    ```
    {
        "code": "0",
        "data": {
            "token": "eyJ0eXAiOiJKV1QiLCJyZWdEYXRlIjoxNTc2NDA2MTg0OTQ2LCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NzY0MDk3ODQsInN1YiI6InRva2VuIiwidXNlcl9pZCI6Imtha2FvcGF5MSJ9.GtOnzJuYDp8kh-JvBcYfXWNh0Jtsgy4KapBEc1TpkAY",
            "expired_time": 1576409784.939,
            "expired_string": "2019-12-15T20:36:24.939+09:00[Asia/Seoul]"
        }
    }
    ```
- **로그인 : ```POST /auth/sign_in```**
    - Request : 
    ```
    Content-Type : Application/json
    {
        "id" : {id} // 로그인할 아이디
        "password" : {password} // 비밀번호 
    }
    ```
    - Response : 
    ```
    {
        "code": "0",
        "data": {
            "token": "eyJ0eXAiOiJKV1QiLCJyZWdEYXRlIjoxNTc2NDA2MTg0OTQ2LCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NzY0MDk3ODQsInN1YiI6InRva2VuIiwidXNlcl9pZCI6Imtha2FvcGF5MSJ9.GtOnzJuYDp8kh-JvBcYfXWNh0Jtsgy4KapBEc1TpkAY",
            "expired_time": 1576409784.939,
            "expired_string": "2019-12-15T20:36:24.939+09:00[Asia/Seoul]"
        }
    }
    ```
- **토큰 재발급 : ```POST /auth/refresh```**
    - Request :
    ```
    Content-Type: Application/json
    Authorization: Bearer {token}
    ```
    - Response : 
    ```
    {
        "code": "0",
        "data": {
            "token": "eyJ0eXAiOiJKV1QiLCJyZWdEYXRlIjoxNTc2NDA2MTg0OTQ2LCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NzY0MDk3ODQsInN1YiI6InRva2VuIiwidXNlcl9pZCI6Imtha2FvcGF5MSJ9.GtOnzJuYDp8kh-JvBcYfXWNh0Jtsgy4KapBEc1TpkAY",
            "expired_time": 1576409784.939,
            "expired_string": "2019-12-15T20:36:24.939+09:00[Asia/Seoul]"
        }
    }   
    ```
- **파일업로드 : ```POST /file```**
    - Request :
    ```
    Content-Type: multipart/form-data
    file: {file} 업로드할 파일
    ```
    - Response : 
    ``` 
    {
        "code":"0",
        "data": "{file} 을 정상적으로 업로드 하였습니다."
    }
    ```
- **은행 목록 조회 : ```GET /info/bank_list```**
    - Request :
    ```
    Content-Type: application/json
    Authorization: {token} 
    ```
    - Response :
    ```
    {
        "code": "0",
        "data": [
            {
                "code": "bnk1",
                "name": "주택시기금억원"
            },
            {
                "code": "bnk2",
                "name": "국민은행"
            },
            {
                "code": "bnk3",
                "name": "우리은행"
            },
            {
                "code": "bnk4",
                "name": "신한은행"
            }, ...
        ]
    } 
    ```
- **년도별 각 금융기관의 지원금액 합계 조회** : ```GET /info/every_year```
    - Request :
    ```
    Content-Type: application/json
    Authorization: {token} 
    ```
    - Response :
    ```
    {
        "name": "주택금융 공급현황",
        "support_amount": [
            {
                "year": 2005,
                "total_amount": 48016,
                "detail_amount": {
                    "농협은행/수협은행": 1486,
                    "하나은행": 3122,
                    "주택시기금억원": 22247,
                    "우리은행": 2303,
                    "국민은행": 13231,
                    "신한은행": 1815,
                    "외환은행": 1732,
                    "기타은행": 1376,
                    "한국시티은행": 704
                }
            }, ...
        ]
    }
    ```
- **년도별 각 기관의 전체 지원 금액 중 가장 큰 금액 기관 조회 : ```GET /info/maximum_amount_year```**
    - Request :
    ```
    Content-Type: application/json
    Authorization: {token} 
    ```
    - Response :
    ```
    [
        {
            "year": 2005,
            "bank": "주택시기금억원"
        }, ...
    ]
    ```
- **외환은행 최대 지원금액 연도와 최소 지원금액 연도 조회 : ```GET /info/history_max_min```**
    - Request :
    ```
    Content-Type: application/json
    Authorization: {token} 
    ```
    - Response :
    ```
    {
        "bank": "외환은행",
        "support_amount": [
            {
                "year": 2008,
                "amount": 78
            },
            {
                "year": 2015,
                "amount": 1702
            }
        ]
    }
    ```
- **2018년도 지원금액 예측 조회 : ```POST /info/prediction ```**
    - Request :
    ``` 
    Content-Type: application/json
    Authorization: {token} 
    {
        "bank": "국민은행",
        "month": 2
    }
    ```
    - Response :
    ```
    {
        "bank": "bnk2",
        "year": 2018,
        "month": 2,
        "amount": 4958
    } 
    ```
    
<br>

## Error Code
**Error Code**|**Description**
:------------:|:--------------:
__FILE_UPLOAD_FAILED__| 파일 업로드 실패
__BANK_NOT_FOUND__| 조회가 불가능한 은행
__SIGN_UP_FAILED__| 회원가입 실패
__TOKEN_CREATE_FAILED__| 토큰 생성 실패
__SIGN_IN_FAILED__| 로그인 실패
__AUTH_TOKEN_FAILED__| 올바르지 않은 토큰
__REFRESH_TOKEN_ERROR__| 토큰 갱신 실패 
__EXPIRED_TOKEN_ERROR__| 토큰 기간 만료
__REQUEST_ERROR__| 요청 형태에 맞지 않

<br>

## 빌드 및 실행 방법
```
$git clone https://github.com/hanaset/Spring-boot-assignment.git

# clone 후 프로젝트 내에 application-development.yml에 DB 설정

$cd Spring-boot-assignment
$gradle wrap
$./gradlew build
$java -Dspring.profiles.active=development -Dserver.port=5000 -jar build/libs/luke-0.0.1-SNAPSHOT.jar
```