SELECT * FROM dept;
--select * from dept;
SELECT dname dpt_name, deptno as dn
FROM department;

--학과명, 학과번호로 변경
SELECT dname 학과명, deptno 학과번호
FROM department;

--교수테이블에서 교수명, 학과번호, 급여 출력
SELECT * FROM professor;
SELECT name 교수명, deptno as 학과번호, sal 급여 
FROM professor;

--칼럼 별명 부여
SELECT * FROM department;
SELECT dname "Deaprtment Name", deptno "부서 번호#"
FROM department;

--교수 이름과 급여 출력
--열의 레이블 professor와 monthly salary
SELECT * FROM professor;
SELECT name "Professor", sal "monthly salary"
FROM professor;

--합성 연산자 사용
SELECT * FROM student;
SELECT studno ｜｜ ' ' ｜｜ name "student"
FROM student;

--산술 연산자 사용
SELECT * FROM student;
SELECT name, weight, weight*2.2 as weight_pound
FROM student;

CREATE table ex_type
(c char(10), v varchar2(10));

desc ex_type;

INSERT INTO ex_type
values('spl', 'spl');

SELECT * 
FROM ex_type
where c='spl';
      
SELECT sysdate FROM dual;

SELECT sysdate-1 YESTERDAY, sysdate TODAY, sysdate+1 TOMOROW 
FROM dual;

SELECT rowid studno, name 
FROM student;
SELECT rowid, name, position 
FROM professor;
desc student;

SELECT 23*25 FROM dual;

--더미 사용
desc sys.dual;
SELECT dummy FROM dual;

--where절 사용
SELECT studno,name, deptno
FROM student
where grade='1';

--교수 테이블에서 직급이 교수인 분들만 이름, 직급, 급여, 학과번호 출력
SELECT * FROM professor;
SELECT name, position, sal, deptno
FROM professor
where position = '교수';

--비교 연산자 사용
SELECT studno, name, grade, deptno, weight
FROM student
where weight <=70;

--학생 테이블 1학년, 몸무게 70키로 이상인 항생만 검색하여 이름, 학년, 몸무게, 학과번호
SELECT studno, name, weight, deptno
FROM student
where grade='1'
and weight >=70;

--교수 테이블에서 직급이 조교수이면서 급여가 350이상인 교수이름, 급여, 직급, 학과번호 출력
SELECT * FROM professor;
SELECT name, sal, position, deptno
FROM professor
where position='조교수'
and sal >=350;

--학생 테이블에서 1학년이거나 몸무개가 70kg이상인 학생만 검색하여 이름, 학년, 몸무게, 학과번호 출력
SELECT studno, name, weight, deptno
FROM student
where (grade ='1' or weight >=70);

--학생테이블에서 학과번호가‘101’이 아닌학생의 학번과 이름과 학과번호를 출력
SELECT studno, name, weight, deptno
FROM student
where not deptno = 101;

--BETWEEN 연산자를 사용하여 몸무게가 50kg에서 70kg 사이인 학생의 학번, 이름, 몸무게를 출력
SELECT studno, name, weight
FROM student
where weight between 50 and 70;

--교수 테이블에서 급여가 350에서 400사이고 102번 학번과 아닌 교수들의 이름, 급여, 학과번호 출력
SELECT name, sal, deptno
FROM professor
where sal between 350 and 400
and not deptno=102;

--학생테이블에서 81년에서 83년도에 태어난 학생의 이름과 생년 월일을 출력하고 그 중에서 1학년이거나 3학년인 학생
--이름, 학년, 생일
SELECT name, grade, birthdate
FROM student
where birthdate between '81/01/01' and '83/12/31'
and (grade= '1' or grade= '3');

--IN 연산자를 사용하여 102번학과와 201번학과 학생의 이름, 학년, 학과번호를 출력
SELECT name, grade, deptno
FROM student
where deptno in (102,201);

--교수 테이블에서 급여가 400~500이고, 학과번호가 101이나 201에 속하는 교수들의 교수번호, 이름, 급여, 학과 번호를 출력
SELECT * FROM professor;
SELECT profno "교수no" , name "교수명", sal "급여", deptno "학과번호#"
FROM professor
where sal between 400 and 500
and deptno in (101, 201);

--학생테이블에서 성이‘김’씨인 학생의 이름, 학년, 학과번호를 출력
SELECT name, grade, deptno
FROM student
where name like '김%';

--학생 테이블에서 이름이 '훈'으로 끝나는 학생의 이름, 학년, 학과번호 출력
SELECT name, grade, deptno
FROM student
where name like '%훈';

--학생테이블에서 이름이 3글자, 성은‘김’씨고 마지막글자가 ‘영’으로 끝나는학생의 이름, 학년, 학과번호를 출력
SELECT name, grade, deptno
FROM student
where name like '김_영';

--학생 테이블에서 남학생의 이름, 학년, 주민등록번호, 학과번호 출력
SELECT * FROM student;
SELECT studno, name, grade, idnum, deptno
FROM student
where idnum like '______1%';

--학생 테이블에서 남학생이면서 101학과 79년생의 이름, 학년, 주민등록번호, 학과번호 출력
SELECT * FROM student;
SELECT studno, name, grade, idnum, deptno
FROM student
where idnum like '______1%'
and deptno='101'
and birthdate like '79%';

insert into student (studno, name)
values (33333, '황보_정호');
SELECT * FROM student;

select name from student
where name like '황보\_%' escape '\';

--null값
--사번, 이름, 급여, 커미션, 급여+커미션 출력
SELECT empno, ename, sal, comm, sal + comm
FROM emp;

--교수테이블에서 보직수당이없는 교수의 이름, 직급, 보직수당을 출력
SELECT name, position, comm
FROM professor
where comm=Null;

SELECT name, position, comm
FROM professor
where comm is Null;

--학생테이블에서 이름이 '진'으로 끝나고 지도교수를 배정받지 못한 101번 학과 학생의 아이디,
--이름, 학년, 학과 번호, 지도교수를 출력
SELECT userid, name, grade, deptno, profno
FROM student
where name like '%진'
and profno is Null
and deptno in '101';

--학생 테이블에서 지도 교수를 배정받은 학생들의 학생들의 아이디, 이름, 학년, 학과번호, 지도교수 출력
SELECT userid, name, grade, deptno, profno
FROM student
where profno is not null;

--102번학과의 학생중에서 1학년 또는 4학년 학생의 이름, 학년, 학과번호를 출력
SELECT name, grade, deptno
FROM student
where deptno=102
and (grade= '1' or grade= '4');

--102번학과이면서 1학년 또는 4학년에 해당하는 학생중에서 이름, 학년, 학과번호를 출력
SELECT name, grade, deptno
FROM student
where deptno=102
and grade= '1' 
or grade= '4';

--1학년이면서 몸무게가70kg 이상인 학생의 집합(stud_heavy)과 1학년이면서 101번학과에 소속된
--학생(stud_101)으로 구성된 두개의 테이블생성
CREATE TABLE stud_heavy
as select *
from student
where weight >=70 and grade='1';

CREATE TABLE stud_101
as select *
from student
where deptno = 101 and grade='1';

--stud_heavy와 stud_101테이블에 대해 UNION, UNION ALL 연산을 각각수행한결과를 출력
SELECT studno, name
FROM stud_heavy
union
SELECT studno, name
FROM stud_101;

SELECT studno, name
FROM stud_heavy
union all
SELECT studno, name
FROM stud_101;

--학생과 교수 전체의 명단에서 이름, 학과번호, 급여를 출력(급여가 없다면 0으로)
SELECT name, deptno, 0 as sal
FROM student
union
SELECT name, deptno, sal
FROM professor;

--INTERSECT 연산 사용
select name from  stud_heavy
intersect
SELECT name FROM stud_101;

--MINUS 연산 사용
SELECT studno 학번, name 이름
FROM stud_heavy
MINUS
SELECT studno, name
FROM stud_101;

SELECT studno 학번, name 이름
FROM stud_101
MINUS
SELECT studno, name
FROM stud_heavy;

--교수 테이블에서 전체 교수의 급여를 인상하기 위한 직원 명단을 출력
--단, 직급이 전임강사인 사람들은 명단에서 제외
SELECT name, position
FROM professor
minus
SELECT name, position
FROM professor
where position='전임강사';
