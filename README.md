#### ERD

## Lecture 테이블
- id(PK): 식별 ID
- registered_number: 강의 수강 신청을 한 인원 수
- date: 강의 날짜

## User 테이블
- id(PK): 식별 ID

## LectureRegistration 테이블
- user_id(Key) 복합키 User 테이블의 ID 값을 외래키로 설정
- lecture_id(Key) 복합키 Lecture 테이블의 ID 값을 외래키로 설정

## 추가 설명
- 강의 목록/ 신청을 위한 최소한의 값들로만 세팅
- 일반적으로 기능에 있는 name, created_at, role 전부 제거
- Lecture 테이블에 영속성을 위한 수강 신청 수, 강의 날짜만 저장하도록 설정
- User에서는 사용자 식별값만 가져가는 형태로 세팅
- LectureRegistration 테이블에서 수강 신청 내역을 저장하도록 함
- 1대다나 다대다 연관관계를 피하기 위해서 LectureRegistration을 위와 같은 방식으로 설계
- 강의 날짜는 범위가 아닌 특정 날짜에 하루 하는 것으로 파악하여서 start_date, end_date가 아닌 date로 표현