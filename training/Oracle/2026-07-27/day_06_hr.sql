//정보미디어학부(부서번호:100)에 소속된 모든 학생의 학번, 이름, 학과번호를 출력
SELECT name, grade, deptno
FROM student
WHERE deptno IN (SELECT deptno
                 FROM department
                 WHERE college = 100);
                 
//모든 학생중에서 4학년학생중에서 키가 제일 작은 학생보다 키가 큰 학생의 학번, 이름, 키를 출력
SELECT studno, name, height
FROM student
WHERE height > ANY (SELECT height
                    FROM student
                    WHERE grade = '4');
                    
//모든 학생중에서 4학년 학생중에서 키가 가장큰학생보다 키가 큰 학생의 학번, 이름, 키를 출력
SELECT studno, name, height
FROM student
WHERE height > ALL (SELECT height
                    FROM student
                    WHERE grade = '4');
                    
//보직수당을 받는 교수가 한명이라도 있으면 모든 교수의 교수번호, 이름, 보직수당, 
//급여와 보직수당의 합을 출력
SELECT profno, name, sal, comm, sal+nvl(comm, 0)
FROM professor
WHERE EXISTS (SELECT profno
              FROM professor
              WHERE comm IS NOT NULL);
                    
//학생중에서 ‘goodstudent’이라는 사용자아이디가 없으면 1을 출력
SELECT 1 userid_exist
FROM dual
WHERE NOT EXISTS (SELECT userid
                  FROM student
                  WHERE userid = 'goodstudent');
                  
//학과별 평균 몸무게가 최대인 학과번호와 최대 몸무게를 출력
SELECT deptno 학과번호 , avg(weight) 최대몸무게
FROM student
GROUP BY deptno
HAVING avg(weight) = (SELECT MAX(avg(weight))
                      FROM student
                      GROUP BY deptno);

//학과별 평균 몸무게가 최대인 학과번호, 학과명, 최대 몸무게를 출력
SELECT s.deptno 학과번호 , d.dname 학과명, avg(S.weight) 최대몸무게
FROM student s, department d
WHERE s.deptno=d.deptno
GROUP BY s.deptno, d.dname
HAVING avg(s.weight) = (SELECT MAX(avg(weight))
                        FROM student
                        GROUP BY deptno);
                      
SELECT s.deptno 학과번호 , d.dname 학과명, avg(S.weight) 최대몸무게
FROM student s INNER JOIN department d
ON s.deptno=d.deptno
GROUP BY s.deptno, d.dname
HAVING avg(s.weight) = (SELECT MAX(avg(weight))
                        FROM student
                        GROUP BY deptno);
//결과값 위와 같음

//PAIRWISE 비교방법에 의해 학년별로 몸무게가 최소인 학생의 이름, 학년, 몸무게를 출력
SELECT name, grade, weight
FROM student
WHERE (grade, weight) IN (SELECT grade, MIN(weight)
                          FROM student
                          GROUP BY grade);

//UNPAIRWISE 비교방법에 의해 학년별로 몸무게가 최소인 학생의 이름, 학년, 몸무게를 출력
SELECT name, grade, weight
FROM student
WHERE grade IN (SELECT grade
                FROM student
                GROUP BY grade)
AND weight IN (SELECT MIN(weight)
               FROM student
               GROUP BY grade);
          
//단일 행 입력     
INSERT INTO student
VALUES(10110, '홍길동', 'hong', '1', '8501011143098', '85/01/01', '041)630-3114', 
       170, 70, 101, 9903);
       
SELECT studno, name
FROM student
WHERE studno = 10110;

//단일 행 입력
INSERT INTO department(deptno, dname)
VALUES (300, '생명공학부');

COMMIT;

SELECT *
FROM department
WHERE deptno = 300;

//명시적으로 NULL을 입력
INSERT INTO department
VALUES (301, '환경보건학과', '','');

SELECT *
FROM department
WHERE deptno = 301;

//교수테이블에서 입사일을 2006년1월1일로 입력
INSERT INTO professor(profno, name, position, hiredate, deptno)
VALUES (9920, '최윤식', '조교수', 
TO_DATE('2006/01/01', 'yyyy/mm/dd'), 102);

COMMIT;

SELECT *
FROM professor
WHERE profno = 9920;

//교수테이블에서 새로운 행을 입력할때 입사일을 현재날짜로 입력
INSERT INTO professor
VALUES (9910, '백미선', 'white', '전임강사', 200, sysdate, 10, 101);

COMMIT;

SELECT * FROM professor WHERE profno = 9910;

ROLLBACK;

//단일테이블에 다중행 입력
CREATE TABLE t_student
AS SELECT * FROM student
WHERE 1=0;

INSERT INTO t_student
SELECT * FROM student;

COMMIT;

DESC t_student;

//교수 테이블과 동일한 구조를 갖는 imsi_professor 테이블을 생성하고 동일한 데이터를 입력
CREATE TABLE imsi_professor
AS SELECT * FROM professor;

INSERT INTO imsi_professor
SELECT * FROM professor;

//다중행입력을 위한 height_info, weight_info 예제 테이블생성
CREATE TABLE height_info(studno number(5), name varchar2(10), height number(5,2));

CREATE TABLE weight_info(studno number(5), name varchar2(10), weight number(5,2));

//학생테이블에서 2학년이상의 학생을 검색하여 height_info 테이블에는 학번, 이름, 키, 
//weight_info 테이블에는 학번, 이름, 몸무게를 각각 입력
INSERT ALL
INTO height_info VALUES (studno, name, height)
INTO weight_info VALUES (studno, name, weight)
SELECT studno, name, height, weight
FROM student
WHERE grade >= '2';

COMMIT;

SELECT * 
FROM height_info;

SELECT * 
FROM weight_info;

//weight_info, height_info 테이블 데이터 모두 삭제
DELETE FROM height_info;

DELETE FROM weight_info;

COMMIT;

SELECT * 
FROM height_info;

SELECT * 
FROM weight_info;

//학생테이블에서 2학년이상의 학생을 검색하여 height_info 테이블에는 키가 170보다 
//큰 학생의 학번, 이름, 키를 입력하고 weight_info 테이블에는 몸무게가 70보다 큰학생의 
//학번, 이름, 몸무게를 각각 입력
INSERT ALL
WHEN height > 170 THEN
     INTO height_info VALUES (studno, name, height)
WHEN weight > 70 THEN
     INTO weight_info VALUES (studno, name, weight)
SELECT studno, name, height, weight
FROM student
WHERE grade >= '2';

SELECT * 
FROM height_info;

SELECT * 
FROM weight_info;

//교수 테이블에서 교수번호, 교수이름으로 구성된 테이블 PROF1, PROF2를 생성
CREATE TABLE PROF1
AS SELECT profno, name 
FROM professor
WHERE 1=2;

CREATE TABLE PROF2
AS SELECT profno, name 
FROM professor
WHERE 1=2;

//교수 테이블에서 교수번호가 9901~9905까지인 교수의 교수번호와 이름은 prof1 테이블에 입력
//교수번호가 9906~9920까지인 교수번호와 이름은 prof2테이블에 입력
SELECT * FROM professor;
INSERT ALL
WHEN profno BETWEEN 9901 and 9905 THEN
     INTO prof1 VALUES (profno, name)
WHEN profno BETWEEN 9906 and 9920 THEN
     INTO prof2 VALUES (profno, name)
SELECT profno, name
FROM professor;

SELECT * FROM prof1;

SELECT * FROM prof2;

//학생테이블에서 2학년이상의 학생을 검색하여 height_info 테이블에는 키가170보다 
//큰 학생의 학번, 이름, 키를입력하고 weight_info 테이블에는 몸무게가 70보다 큰 학생의 학번, 
//이름, 몸무게를 각각 입력
//단, 키가 170보다 작고, 몸무게가 70보다 큰 학생은 weight_info 테이블만 입력
INSERT FIRST
WHEN height > 170 THEN
     INTO height_info VALUES (studno, name, height)
WHEN weight > 70 THEN
     INTO weight_info VALUES (studno, name, weight)
SELECT studno, name, height, weight
FROM student
WHERE grade >= '2';

SELECT * FROM weight_info;

SELECT * FROM height_info;

//교수번호가 9903인 교수의 현재직급을 ‘부교수’로 수정
SELECT * FROM professor;

UPDATE professor
SET position = '부교수';

ROLLBACK;

UPDATE professor
SET position = '부교수'
WHERE profno = 9903;

COMMIT;

//서브쿼리를 이용하여 학번이 10201인 학생의 학년과 학과번호를 10103학번 학생의 학년과 
//학과번호와 동일하게 수정
UPDATE student
SET (grade, deptno) = (SELECT grade, deptno
                       FROM student
                       WHERE studno = 10103)
WHERE studno = 10201;

COMMIT;

SELECT studno, grade, deptno FROM student
WHERE studno = 10201;

SELECT studno, grade, deptno FROM student
WHERE studno = 10103;

//남은혁 교수의 uerid를 black으로 변경
SELECT * FROM professor;
UPDATE professor
SET userid = 'black'
WHERE name = '남은혁';

SELECT name, userid 
FROM professor
where name = '남은혁';

//학생테이블에서 학번이 20103인 학생의 데이터를 삭제
DELETE
FROM student
WHERE studno = 20103;

COMMIT;

SELECT *
FROM student
WHERE studno = 20103;

//학생테이블에서 컴퓨터공학과에 소속된 학생을 모두 삭제
DELETE
FROM student
WHERE deptno = (SELECT deptno
                FROM department
                WHERE dname = '컴퓨터공학과');
                
SELECT *
FROM student
WHERE deptno = (SELECT deptno
                FROM department
                WHERE dname = '컴퓨터공학과'); 
                
//사원테이블에서 DALLAS에 근무하는 사원들을 모두 삭제(EMP, DEPT)
DELETE
FROM emp
WHERE deptno = (SELECT deptno
                FROM dept
                WHERE loc = 'DALLAS');

//학생테이블에서 20000에서 25000번에 해당하는 학생들 삭제
DELETE
FROM student
WHERE studno between 20000 and 25000;

SELECT *
FROM student
WHERE studno between 20000 and 25000;

//professor 테이블과 professor_temp 테이블을 비교하여 professor 테이블에 있는 기존데이터는
//professor_temp 테이블의 데이터에의해 수정, professor 테이블에 없는 데이터는 신규로 입력
CREATE TABLE professor_temp AS
SELECT *
FROM professor
WHERE position = '교수';

UPDATE professor_temp
SET position = '명예교수'
WHERE position = '교수';

INSERT INTO professor_temp
VALUES(9999, '김도경', 'arom21', '전임강사', 200, sysdate, 10, 101);

SELECT * FROM professor;

SELECT * FROM professor_temp;

//MERGE 사용
MERGE INTO professor p
USING professor_temp f
on (p.profno = f.profno)
WHEN matched THEN
UPDATE SET p.position = f.position
WHEN not MATCHED THEN
INSERT VALUES(f.profno, f.name, f.userid, f.position, f.sal, f.hiredate, 
              f.comm, f.deptno);
ROLLBACK;

SELECT * FROM professor;

//교수 테이블에서 성연희 교수의 직급과 동일 직급을 가진 교수들 중 현재 급여가 410이 안되는 
//교수들의 급여를 12% 인상
select name, position, sal, sal*1.12 from professor
where position = (select position from professor where name = '성연희');

//STUDNO, NAME, USERID, GRADE 그리고 DEPTNO 열만을 포함하는
//STUDENT 테이블의 구조를 기초로 STUDENT2 테이블을 생성
CREATE TABLE STUDENT2 
AS SELECT studno, name, userid, grade, deptno
FROM student
WHERE 1=5;

//위에 조건에서 STUDENT2 테이블에
//1명 입력: 55555, '테스트', 'test', '2', '101'
insert into student2 values(55555, '테스트', 'test', '2', '101');

//STUDENT 테이블과 STUDENT2테이블을 MERGE 시키세요.(결과 테이블:student)
MERGE INTO student s
USING student2 a
on (s.studno=a.studno)
WHEN matched THEN
UPDATE SET s.name=a.name
WHEN not MATCHED THEN
INSERT(studno, name, userid, grade, deptno) 
VALUES(a.studno, a.name, a.userid, a.grade, a.deptno);

//교수 테이블에 교수 3명을 입력하세요. ( 직급: '시간강사')
INSERT INTO professor
VALUES(8888,'고구마','pto','시간강사','','','','');
INSERT INTO professor
VALUES(8889,'사이다','soda','시간강사','','','','');
INSERT INTO professor
VALUES(8890,'필요해','need','시간강사','','','','');

SELECT * FROM professor;

//입력한 교수 중에서 2명을 삭제
DELETE FROM professor
WHERE name = '고구마' or name ='필요해';

//입력한 교수 중 1명은 직급을 '전문가'로 수정
UPDATE professor
SET position = '전문가'
WHERE name = '사이다';

//각 학과별로 입사일이 가장 오래된 교수의 교수번호와 이름, 입사일, 학과명을 출력
//(입사일 순으로 정렬하세요.)

--교수 NO. 교수명 입사일 학과
--===== ===== ===== ===========
--9901 김도훈 82/06/24 컴퓨터공학과
--9905 권혁일 86/01/08 멀티미디어학과
--9908 남은혁 90/11/18 기계공학과
--9902 이재우 95/04/12 전자공학과

SELECT p.profno "교수NO.", p.name "교수명", p.hiredate "입사일", d.dname "학과명"
FROM professor p, department d
WHERE p.deptno=d.deptno
GROUP BY p.deptno, p.profno, p.name, p.hiredate, d.dname
HAVING p.hiredate IN (SELECT MIN(hiredate)
                        FROM professor
                        GROUP BY deptno)
ORDER BY hiredate;
