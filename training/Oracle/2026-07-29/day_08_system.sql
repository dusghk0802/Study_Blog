//SYSTEM사용자 소유의 project 테이블에 my_project 전용 동의어를 생성
create table project
(project_id NUMBER(5) constraint pro_id_pk primary key,
 project_name varchar2(100), studno number(5), profno number(5));
 
insert into project values(12345, 'portfolio', 10101, 9901);

SELECT * FROM project;

//동의어 생성 방법
grant SELECT ON project to scott;

CONNECT scott/tiger;

SELECT * FROM project;

SELECT * FROM system.project;

grant CREATE SYNONYM to scott;

CREATE SYNONYM my_project for system.project;

SELECT * FROM my_project;

//공용 동의어 생성
CREATE PUBLIC SYNONYM pub_project FOR project;

SELECT * FROM pub_project;

//동의어 삭제
DROP PUBLIC SYNONYM pub_project;

//kosa라는 테이블 스페이스를 만드세요.
//sql plus에서 실행
//CREATE USER kosa IDENTIFIED BY kosa
//default tablespace users
//temporary tablespace temp;
//사용자가 생성되었습니다.

//kosa라는 유저를 만들고 디폴트 테이블스페이스는 kosa를 임시테이블스페이스를 temp를 사용
//connect system/oracle;
//연결되었습니다.
//ALTER SESSION SET "_oracle_script"=TRUE;
//세션이 변경되었습니다.
//grant connect, resource to kosa;
//권한이 부여되었습니다.

//system이 소유의 EMPLOYEE 테이블을 생성하고, 데이터를 하나 입력
--Name Null Type
-------------- ------------- --------------------------
--ID NUMBER(7)
--LAST_NAME VARCHAR2(25)
--FIRST_NAME VARCHAR2(25)
--DEPT_ID NUMBER(7)

CREATE table EMPLOYEE
(id NUMBER(7) constraint employee_id_pk primary key,
 last_name varchar2(25), first_name varchar2(25), dept_id number(7));
 
INSERT INTO employee VALUES(12345, '구마', '고', 9999);

SELECT * FROM EMPLOYEE;

//system의 employee테이블에 대해 pub_employee라는 공용 동의어를 생성
CREATE PUBLIC SYNONYM pub_employee FOR employee;

//위에서 생성한 공용 동의어에 의해 system소유의 employee 테이블을 kosa 유저가 조회
grant select on employee TO kosa;

//sql plus에서 실행
//CONNECT kosa/kosa;
//연결되었습니다.
//SELECT *FROM pub_employee;

--        ID LAST_NAME
---------- --------------------------------------------------
--FIRST_NAME                                            DEPT_ID
-------------------------------------------------- ----------
--     12345 구마
--고                                                       9999

//공용동의어 pub_employee를 삭제
DROP PUBLIC SYNONYM pub_employee;





