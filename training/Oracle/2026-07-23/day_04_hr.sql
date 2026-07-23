//201번학과 교수의 이름, 직급, 급여, 보직수당, 급여와 보직수당의 합계를 출력
//단, 보직수당이 NULL인 경우에는 보직수당을 0으로 계산
SELECT name, position, sal, comm, sal+comm, sal+nvl(comm,0) sl, nvl(sal+comm,sal) s2
FROM professor
where deptno=201;

//위에 조건에서 모든 학과 교수를 출력할 경우
SELECT name, position, sal, comm, sal+comm, sal+nvl(comm,0) sl, nvl(sal+comm,sal) s2
FROM professor;

//102번학과 교수중에서 보직수당을 받는사람은 급여와 보직수당을 더한값을 급여총액으로 출력
SELECT name, position, sal, comm, nvl2(comm, sal+comm, sal) total
FROM professor
where deptno=102;

//교수테이블에서 이름의 바이트수와 사용자아이디의 바이트수를 비교
//같으면 NULL을 반환하고 같지않으면 이름의 바이트수를 반환
SELECT name, userid, lengthb(name)-3 "lengthb(name)", lengthb(userid), 
       nullif(lengthb(name)-3, lengthb(userid)) nullif_result
FROM professor;

//오라클에서 캐릭터 셋이 AL32UTF8 인경우 한글을 한자당 3바이트씩 인식한다
//#현재 캐릭터셋 확인
//SELECT *
//FROM nls_database_parameters
//WHERE parameter = 'NLS_CHARACTERSET';
//KO16KSC5601 :한글 완성형 2,350자지원 글자당 2Byte
//KO16MSWIN949: KO16KSC5601+확장 8822자 지원, 글자당 2Bytes
//UTF8: 한글 11,172자 글자당 3바이트

//교수테이블에서 보직수당이 NULL이 아니면 보직수당을 출력
//보직수당이 NULL이 고급여가 NULL이 아니면 급여를 출력, 보직수당과 급여가 NULL이면 0을 출력
SELECT name, comm, coalesce(comm, sal, 0) co_result
from professor;

//교수테이블에서 교수의 소속학과번호를 학과이름으로 변환하여 출력
//학과번호가 101이면‘컴퓨터공학과’, 102이면 ‘멀티미디어학과’, 201이면‘전자공학과’, 
//나머지 학과번호는 ‘기계공학과’(default)로 변환
SELECT name, deptno, 
decode(deptno, 101, '컴퓨터공학과', 102, '멀티미디어학과', 201, '전자공학과', '기계공학과') dname
from professor;

//학생 테이블에서 101학과 학생들은 'Commputer Science'로 나머지는 'ETC'로 출력
//학과번호, 이름, 학과명
SELECT deptno, name, decode(deptno, 101, 'Commputer Science', 'ETC') "학과명"
FROM student;

//교수테이블에서 소속학과에 따라 보너스를 다르게 계산하여 출력
//학과번호별로 보너스는 다음과 같이 계산
//학과번호가 101이면 보너스는 급여의 10%, 102이면 20%, 201이면 30%, 나머지학과는 0%
SELECT name, deptno, sal, 
       CASE WHEN deptno=101 THEN sal*0.1
       WHEN deptno=102 THEN sal*0.2
       WHEN deptno=201 THEN sal*0.3
       ELSE 0
       END bobus
FROM professor;

//학생 테이블에서 학생들이 태어난 월과 몇 사분기에 태어났는지 출력(이름, 태어난 월, 분기)
SELECT name, to_char(birthdate, 'mm') birthdate, 
       CASE when to_char(birthdate, 'mm') in (1,2,3) THEN '1/4'
       when to_char(birthdate,'mm') in (4,5,6) then '2/4'
       when to_char(birthdate,'mm') in (7,8,9) then '3/4'
       when to_char(birthdate,'mm') in (10,11,12) then '4/4' 
       end "Quarter"
FROM student;
       
//101번 학과교수중에서 보직수당을 받는 교수의 수를 출력
SELECT COUNT(COMM)
FROM professor
WHERE deptno=101;
       
SELECT COUNT(*)
FROM professor
WHERE deptno=101 AND comm IS NOT NULL;
       
//101번학과 학생들의 몸무게 평균과 합계를 출력
SELECT AVG(weight), SUM(weight)
FROM student
WHERE deptno=101;
       
//102번 학과 학생중에서 최대키와 최소키를 출력
SELECT MAX(height), MIN(height)
       FROM student
       WHERE deptno=102;
       
//교수테이블에서 급여의 표준편차와 분산을 출력
SELECT stddev(sal), variance(sal) 
FROM professor;
       
//교수테이블에서 학과별로 교수 수와 보직수당을 받는 교수 수를 출력
SELECT deptno, COUNT(*), COUNT(comm)
FROM professor
GROUP BY deptno;

//전체학생을 소속학과별로 나누고, 같은 학과학생은 다시학년별로 그룹핑하여, 학과와 학년별 인원수, 평균몸무게를 출력
//단, 평균몸무게는 소수점이하 첫번째자리에서 반올림
SELECT deptno, grade, COUNT(*), round(avg(weight))
       FROM student
       GROUP by deptno, grade
       ORDER by 1,2;
       
//소속학과별로 교수 급여합계와 모든학과 교수들의 급여합계를 출력
SELECT deptno, SUM(sal)
FROM professor
GROUP BY ROLLUP(deptno);
       
//ROLLUP 연산자를 이용하여 학과 및 직급별 교수 수, 학과별 교수 수, 전체 교수 수를 출력
SELECT deptno, position, COUNT(*)
       FROM professor
       GROUP BY ROLLUP(deptno, position);
       
//CUBE 연산자를 이용하여 학과 및 직급별 교수 수, 학과별 교수 수, 전체 교수 수를 출력
SELECT deptno, position, COUNT(*)
FROM professor
GROUP BY CUBE(deptno, position);
       
//전체학생을학과와학년별로그룹화한후, 학과와학년별그룹 인원수, 학과별인원수, 
//각그룹조합에서학과와학년칼럼이사용되었는지여부를출력
SELECT deptno, grade, COUNT(*),
       GROUPING(deptno) grp_dno,
       GROUPING(grade) grp_grade
FROM student
GROUP BY ROLLUP(deptno, grade);

//grouping sets 함수 사용
SELECT  DEPTNO, GRADE, NULL, COUNT(*)
FROM STUDENT
GROUP BY DEPTNO, GRADE
UNION ALL
SELECT DEPTNO, NULL, TO_CHAR(BIRTHDATE, 'YYYY'), COUNT(*)
FROM STUDENT
GROUP BY DEPTNO, TO_CHAR(BIRTHDATE, 'YYYY');

//학과내에서 학년별 인원수와 태어난 년도별 인원수를 출력
SELECT deptno, grade, to_char(birthdate, 'yyyy') birthdate, COUNT(*)
FROM student
GROUP BY GROUPING SETS((deptno, grade), (deptno, to_char(birthdate, 'yyyy')));

//학생수가 4명이상인 학년에 대해서 학년, 학생수, 평균키, 평균 몸무게를 출력
//단, 평균키와 평균몸무게는 소수점첫번째자리에서 반올림
//출력순서는 평균키가 높은순부터 내림차순으로 출력
SELECT grade, COUNT(*), round(avg(height)) avg_height,
       round(avg(weight)) avg_weight
FROM student
GROUP BY grade
ORDER BY avg_height DESC;

SELECT grade, COUNT(*), round(avg(height)) avg_height,
       round(avg(weight)) avg_weight
FROM student
GROUP BY grade
HAVING COUNT(*)>4
ORDER BY avg_height DESC;

//학과별학생의 평균몸무게중 최대평균몸무게를 출력
SELECT deptno, AVG(weight)
FROM student
GROUP BY deptno;

SELECT max(AVG(weight)) max_weight
FROM student
GROUP BY deptno;

//학과별학생수가 최대 또는 최소인 학과의 학생수를 출력
SELECT MAX(count(studno)) max_cnt, MIN(count(studno)) min_cnt
FROM student
GROUP by deptno;

//칼럼(.) 사용
SELECT student.studno, student.name, student.deptno, department.dname
FROM student, department
WHERE student.deptno = department.deptno 
AND student.name= '전인하';

//칼럼을 이용하여 축약 가능
SELECT s.studno, s.name, s.deptno, d.dname
FROM student s, department d
WHERE s.deptno = d.deptno 
AND s.name= '전인하';

//몸무게가 80kg이상인 학생의 학번, 이름, 체중, 학과이름, 학과 위치를 출력
SELECT s.studno, s.name, d.dname, d.loc
FROM student s, department d
WHERE s.deptno=d.deptno
AND s.weight >= 80;

//위 조건에서 컴퓨터공학과에 속한 학생 출력
SELECT s.studno, s.name, d.dname, d.loc
FROM student s, department d
WHERE s.deptno=d.deptno
AND s.weight >= 80
AND d.dname='컴퓨터공학과';

//컴퓨터공학과 교수님들의 이름, 직급, 급여, 학과번호, 학과명, 학과위치 출력
SELECT p.name, p.position, p.sal, d.deptno, d.dname, d.loc
FROM professor p, department d
WHERE p.deptno=d.deptno
AND d.dname='컴퓨터공학과';

//오유석 학생의 이름, 키, 몸무게, 지도 교수번호, 지도 교수이름을 출력
SELECT s.name, s.height, s.weight, p.profno, p.name
FROM professor p, student s
WHERE p.profno=s.profno
AND s.name='오유석';

//전임강사들의 이름, 급여, 부서번호, 부서이름, 부서 위치를 출력
SELECT p.name, p.sal, p.deptno, d.dname, d.loc
FROM professor p, department d
where p.deptno=d.deptno
AND position='전임강사';