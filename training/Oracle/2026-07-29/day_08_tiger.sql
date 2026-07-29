//Tiger 사용자에게 scott 소유의 테이블을 SELECT 할수있는 권한을 부여
//ALTER SESSION SET "_oracle_script"=TRUE;
//세션이 변경되었습니다.
//CREATE USER tiger IDENTIFIED BY tiger123
//default tablespace users
//temporary tablespace temp;
//사용자가 생성되었습니다.
//grant connect, resource to tiger;
//권한이 부여되었습니다.
//connect scott/tiger
//연결되었습니다.
//grant select on scott.student to tiger;
//권한이 부여되었습니다.
//connect tiger/tiger123
//연결되었습니다.

select * from scott.student;

//Tiger 사용자에게 scott가 소유한 student 테이블의 키와 몸무게를 수정할 수 있는 권한을 부여
//sql plus에서 실행
//CONNECT scott/tiger
//연결되었습니다.
//GRANT UPDATE(height,weight) ON student TO tiger;
//권한이 부여되었습니다.
UPDATE scott.student
set height = 180, weight = 80
WHERE deptno = 101;

//tiger 사용자에게 부여된 사용자객체, 칼럼에 부여된 객체권한을 조회
SELECT * from user_tab_privs_made;

SELECT * FROM user_tab_privs_recd;

//scott에 의해tiger에게 부여된 student 테이블에 대한 SELECT,UPDATE 권한을 철회
//sql plus에서 실행
//connect scott/tiger;
//연결되었습니다.
//REVOKE UPDATE ON student FROM tiger;
//권한이 취소되었습니다.
//REVOKE SELECT ON student FROM tiger;
//권한이 취소되었습니다.
//CONNECT tiger/tiger123
//연결되었습니다.

SELECT * FROM scott.student;
//ORA-00942: 테이블 또는 뷰가 존재하지 않습니다

//롤에 부여한 시스템 권한 조회
select * from role_sys_privs;

select * from user_role_privs;



