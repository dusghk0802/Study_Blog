//부서테이블에서 name 칼럼을 고유인덱스로 생성
//단, 고유 인덱스의 이름을 idx_dept_name으로 정의
CREATE UNIQUE INDEX idx_dept_name 
ON department(dname);

//학생 테이블의 birthdate 칼럼을 비고유 인덱스로 생성
//비고유 인덱스의 이름은 idx_stud_birthdate로 정의
CREATE INDEX idx_stud_birthdate
ON student(birthdate);

//학생 테이블의 deptno, grade 칼럼을 결합인덱스로 생성
//결합 인덱스의 이름은 dx_stud_dno_grade로 정의
CREATE INDEX idx_stud_dno_grade
ON student(deptno, grade);

//학생 테이블의 deptno와 name 칼럼으로 결합인덱스를 생성
//단, deptno 칼럼을 내림차순으로 name 칼럼은 오름차순으로 생성
CREATE INDEX fix_stud_no_name
ON student(deptno DESC, name ASC);

//함수기반 인덱스 사용
CREATE INDEX uppercase_idx on emp (upper(ename));

SELECT * FROM emp
WHERE upper(ename) = 'KING';

//학생테이블에서 학생들의 비만도 측정을 위해 표준체중에 대한 함수기반 인덱스를 생성
//표준체중을 구하는 공식은 ‘신장-100(cm)*0.9’
CREATE INDEX idx_standard_weight 
on student((height-100)*0.9);

//학과 테이블에서 학과이름이 ‘정보미디어학부’인 학과번호를 검색한 결과에 대한 실행경로를 분석
//dname 컬럼에 고유 인덱스가 생성
SELECT deptno, dname
FROM department
WHERE dname = '정보미디어학부';
//sqlplus에서 실행시 set autot on/off/trace하고 위에 입력후 실행
//여기에서 실행시 F10

DROP INDEX idx_dept_name;

SELECT name, birthdate
FROM student
WHERE birthdate = '79/04/02';

DROP INDEX idx_stud_birthdate;

//학생 테이블에 생성된 인덱스를 조회
SELECT index_name, uniqueness
FROM user_indexes
WHERE table_name = 'STUDENT';

SELECT index_name, column_name
FROM user_ind_columns
WHERE table_name = 'STUDENT';

//학생 테이블에 생성된 stud_no_pk 인덱스를 재구성
ALTER INDEX stud_no_pk REBUILD;

//학생 테이블에서 101번학과 학생들의 학번, 이름, 학과번호로 정의되는 단순뷰를 생성
//sqlplus에서 실행
//conn /as sysdba
//연결되었습니다.
//grant create view to hr;
//권한이 부여되었습니다.

CREATE VIEW v_stud_dept101 as
SELECT studno, name, deptno
FROM student
WHERE deptno=101;

SELECT * FROM v_stud_dept101;

//학생테이블과부서테이블을조인하여102번학과학생들의학번, 이름, 학년, 학과 이름으로 정의되는 복합뷰를생성
CREATE view v_stud_dept102
AS SELECT s.studno, s.name, s.grade, d.dname
FROM student s, department d
WHERE s.deptno = d.deptno and s.deptno=102;

SELECT * FROM v_stud_dept102;

//추가로 데이터 입력하여 뷰 생성
CREATE OR REPLACE VIEW v_stud_dept102
AS SELECT s.studno, s.name, s.grade, d.dname, d.deptno
FROM student s, department d
WHERE s.deptno = d.deptno and s.deptno=102;

SELECT * FROM v_stud_dept102;

//교수테이블에서 학과별 평균급여와 총계로 정의되는 뷰를 생성
CREATE view v_prof_avg_sal
AS SELECT deptno, sum(sal) sam_sal, avg(sal) avg_sal
FROM professor
GROUP BY deptno;

//인라인 뷰를 사용하여 학과별로 학생들의 평균 키와 평균 몸무게, 학과이름을 출력
//@C:\Users\kosa\Documents\오라클\table.sql
//테이블 다시 불러오기
SELECT dname, avg_height, avg_weight
FROM (SELECT deptno, avg(height) avg_height, avg(weight) avg_weight
      FROM student
      GROUP BY deptno) s, department d
WHERE s.deptno = d.deptno;

SELECT * FROM professor;
SELECT * FROM student;
