//카티션 곱 사용
SELECT studno, name, s.deptno, d.deptno, dname
FROM student s, department d;

//학생 테이블과 부서 테이블을 카테션곱을 한 결과를 출력
SELECT studno, name, s.deptno, d.deptno, dname
FROM student s CROSS JOIN department d;

//학생테이블과 부서테이블을 EQUI JOIN하여 학번, 이름, 학과번호, 소속학과이름, 학과위치를 출력
SELECT s.studno, s.name, s.deptno, d.dname, d.loc
FROM student s, department d
WHERE s.deptno=d.deptno;

SELECT s.studno, s.name, deptno, d.dname
FROM student s NATURAL JOIN department d;
//결과 값 위와 같음

//NATURAL JOIN을 B이용하여 4학년 학생의 이름, 학과번호, 학과 이름을 출력
SELECT s.studno, s.name, deptno, d.dname
FROM student s NATURAL JOIN department d
WHERE s.grade = 4;

//JOIN ~ USING 절을 이용하여 학번, 이름, 학과번호, 학과이름, 학과위치를 출력
SELECT s.studno, s.name, deptno, d.dname, d.loc
FROM student s JOIN department d
USING (deptno);

SELECT studno, name, deptno, dname, loc
FROM student JOIN department
USING (deptno);
//결과 값 위와 같음

//EQUI JOIN의 3가지방법을 이용하 여성이 ‘김’씨인 학생들의 이름, 학과이름, 학과위치를 출력
SELECT name, dname, loc 
FROM student s, department d
WHERE s.deptno = d.deptno
AND name like '김%';

SELECT name, dname, loc 
FROM student s 
INNER JOIN department d ON s.deptno = d.deptno
where name like '김%';
//결과 값 위와 같음

//학생의 학번, 이름, 학생의 몸무게, 지도교수 이름, 지도교수 이름, 지도교수 급여, 
//학과 이름, 학과 위치를 출력
SELECT s.profno, s.name, s.weight, p.name, p.sal, d.dname, d.loc
FROM student s, professor p, department d
WHERE s.profno = p.profno
AND s.deptno = d.deptno;

SELECT s.profno, s.name, s.weight, p.name, p.sal, d.dname, d.loc
FROM student s
INNER JOIN professor p
ON s.profno = p.profno
INNER JOIN department d
ON s.deptno = d.deptno;
//결과 값 위에와 같음

//전인하 학생의 학번, 이름, 학생의 몸무게, 지도교수 이름, 지도교수 이름, 지도교수 급여, 
//학과 이름, 학과 위치를 출력
SELECT s.profno, s.name, s.weight, p.name, p.sal, d.dname, d.loc
FROM student s, professor p, department d
WHERE s.profno = p.profno
AND s.deptno = d.deptno
AND s.name='전인하';

SELECT s.profno, s.name, s.weight, p.name, p.sal, d.dname, d.loc
FROM student s
INNER JOIN professor p
ON s.profno = p.profno
INNER JOIN department d
ON s.deptno = d.deptno
WHERE s.name='전인하';
//결과 값 위와 같음

//교수테이블과 급여등급 테이블을 NON-EQUI JOIN하여 교수별로 급여등급을 출력
SELECT p.profno, p.name, p.sal, s.grade
FROM professor p, salgrade s
WHERE p.sal between s.losal AND s.hisal;

//101번학과 교수의 교수번호, 이름, 급여, 급여등급을 출력
SELECT p.profno, p.name, p.sal, s.grade
FROM professor p, salgrade s
WHERE p.sal between s.losal AND s.hisal
AND p.deptno = 101;

//학생테이블과 교수테이블을 조인하여 이름, 학년, 지도교수의 이름, 직급을 출력
//단, 지도교수가 배정되지 않은 학생 이름도 함께 출력
SELECT s.name, s.grade, p.name, p.position
FROM student s, professor p
WHERE s.profno = p.profno(+)
ORDER BY p.profno;

//학생테이블과 교수테이블을 조인하여 이름, 학년, 지도교수이름, 직급을 출력
//단, 지도학생을 배정받지않은 교수이름도 함께 출력
SELECT s.name, s.grade, p.name, p.position
FROM student s, professor p
WHERE s.profno = p.profno(+)
UNION
SELECT s.name, s.grade, p.name, p.position
FROM student s, professor p
WHERE s.profno(+) = p.profno;

//LEFT OUTER JOIN 사용
SELECT s.name, s.grade, p.name, p.position
FROM student s 
LEFT OUTER JOIN professor p
ON s.profno = p.profno(+);

// RIGHT OUTER JOIN 사용
SELECT s.name, s.grade, p.name, p.position
FROM student s
RIGHT OUTER JOIN professor p
ON s.profno(+) = p.profno;

//FULL OUTER JOIN 사용
SELECT s.name, s.grade, p.name, p.position
FROM student s
FULL OUTER JOIN professor p
ON s.profno = p.profno;

//WHERE절 사용한 SELF JOIN
SELECT dept.dname ｜｜ '의 소속은' ｜｜ org.dname
FROM department dept, department org
WHERE dept.college = org.deptno;

//JOIN~ON절을 사용한 SELF JOIN
SELECT dept.dname ｜｜ '의 소속은' ｜｜ org.dname
FROM department dept
JOIN department org
ON dept.college = org.deptno;

//부서번호가201 이상인 부서이름과 상위부서의 이름을 출력
//WHERE절 사용한 SELF JOIN 경우
SELECT dept.dname ｜｜ '의 소속은' ｜｜ org.dname
FROM department dept, department org
WHERE dept.college = org.deptno
AND dept.deptno >=201;

//JOIN~ON절을 사용한 SELF JOIN 경우
SELECT dept.dname ｜｜ '의 소속은' ｜｜ org.dname
FROM department dept
JOIN department org
ON dept.college = org.deptno
AND dept.deptno >=201;

//위에 조건에서 deptno 조건을 추가하여 출력
SELECT dept.dname ｜｜ '의 소속은' ｜｜ org.dname, dept.deptno, org.deptno
FROM department dept, department org
WHERE dept.college = org.deptno;

//서브쿼리 사용
SELECT name, position
FROM professor
where position = (select position 
                  from professor 
                  where name = '전은지');
                  
//사용자아이디가‘ jun123’인 학생과 같은 학년인 학생의 학번, 이름, 학년을 출력
SELECT studno, name, grade
FROM student
WHERE grade = (SELECT grade
               FROM student
               WHERE userid = 'jun123');

//101번학과 학생들의 평균몸무게보다 몸무게가 적은 학생의 이름, 학과번호, 몸무게를 출력
SELECT name, deptno, weight
FROM student
WHERE weight<(SELECT AVG(weight)
              FROM student
              WHERE deptno=101)
ORDER BY deptno;

//위에 조건에서 학과 이름까지 출력
SELECT s.name, s.deptno, s.weight, d.dname
FROM student s, department d
WHERE s.deptno = d.deptno
AND weight < (SELECT AVG(weight)
              FROM student
              WHERE deptno=101)
ORDER BY s.deptno;

//학과별 학생수가 최대인 학과번호와 학생수를 출력
SELECT deptno 학과번호, count(*) 학생수
FROM student
GROUP BY deptno
HAVING COUNT(studno) = (SELECT MAX(COUNT(studno))
                        FROM student
                        GROUP BY deptno);

//학과별 학생수가 최대인 학과번호와 학과명, 학생수를 출력
SELECT d.deptno 학과번호, d.dname 학과명, count(s.studno) 학생수
FROM student s, department d
WHERE s.deptno = d.deptno
GROUP BY d.deptno, d.dname
HAVING COUNT(s.studno) = (SELECT MAX(COUNT(studno))
                          FROM student
                          GROUP BY deptno);
                          
//20101번 학생과 학년이 같고, 키는 20101번학생보다 큰학생의 이름, 학년, 키를 출력
SELECT name, grade, height
FROM student
WHERE grade = (SELECT grade
               FROM student
               where studno =20101)
AND height > (SELECT height
              FROM student
              WHERE studno =20101);