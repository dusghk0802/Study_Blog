//인라인 뷰를 사용하여 학과별로 학생들의 평균 키와 평균 몸무게, 학과이름을 출력
//@C:\Users\kosa\Documents\오라클\table.sql
//hr 제약조건으로 인해 scott에서 테이블 다시 불러오기
SELECT dname, avg_height, avg_weight
FROM (SELECT deptno, avg(height) avg_height, avg(weight) avg_weight
      FROM student
      GROUP BY deptno) s, department d
WHERE s.deptno = d.deptno;

//각 학년의 평균 키를 구하고 평균 키보다 큰 학생의 학년, 이름, 키, 각 학년의 평균 키를 출력
SELECT s.grade, s.name, s.height, a.avg_height
FROM student s,(SELECT grade, round(avg(height)) avg_height
                FROM student
                GROUP BY grade) a
WHERE s.grade = a.grade
AND s.height > a.avg_height
ORDER BY 1;

//emp, dept 테이블을 조회하여 부서 번호와 부서별 최대 급여, 부서명을 출력
SELECT e.deptno, d.dname, e.sal
from (SELECT deptno, MAX(sal) sal 
      FROM emp
      GROUP BY deptno) e, dept d
WHERE e.deptno = d.deptno
order by deptno;

//뷰 생성
CREATE VIEW v_stud_dept101
AS SELECT studno, name, deptno, grade
FROM student
WHERE deptno = 101;

SELECT * FROM v_stud_dept101
WHERE name = '김영균';

//사용자가 생성한 모든 뷰에 대한 정의를 저장
SELECT VIEW_name, text
FROM user_views;

//기존 V_STUD_DEPT101뷰에 idnum을 추가하여 재정의
CREATE OR REPLACE VIEW v_stud_dept101
AS SELECT studno, name, deptno, grade, idnum
FROM student
WHERE deptno = 101;

//뷰의 삭제
DROP VIEW v_stud_dept101;

//query rewrite 시스템권한을 scott사용자와 모든사용자에게 부여
//query rewrite 권한은 함수기반 인덱스를 생성하기 위해 필요한 권한
//sql plus에서 실행
//conn /as sysdba
//연결되었습니다.
//grant create view to scott;
//권한이 부여되었습니다.
//grant query rewrite to public;
//권한이 부여되었습니다.

SELECT * FROM user_sys_privs;

//현재세션에서 사용자와 롤에 부여된 시스템 권한을 조회 가능
SELECT * FROM session_privs;
