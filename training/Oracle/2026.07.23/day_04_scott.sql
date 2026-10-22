//사원 테이블에서 10번과 30번 부서에 근무하는 직원들의 이름 급여, 커미션, 급여+커미션을 출력
SELECT ename, sal, comm, sal+comm, 
       sal+nvl(comm,0) 총급여1, nvl(sal+comm, sal) 총급여2
FROM emp
where deptno in (10,30);

//사원의 이름과 보너스를 출력
//단 보너스를 받지 않는 사원을 "NO comm"을 출력(열레이블은 comm)
select ename, comm, nvl(to_char(comm),'No comm') comm, 
       nvl2(comm, to_char(comm), 'No comm') comm
from emp;

//위에 조건에서 간단하게 하면
SELECT ename, NVL2(comm,TO_CHAR(COMM),'No comm') comm
FROM emp;

//중복되지 않는 사원 테이블에서 job종류와 평균 급여, 급여의 합을 출력
SELECT COUNT(DISTINCT job), round(AVG(sal),2), sum(sal)
       FROM emp;
       
//사원 테이블에서 20번 부서에서 가장 작은 급여와 많은 급여를 출력
SELECT * FROM emp
WHERE deptno=20
ORDER BY sal DESC;


//사원테이블에서 가장 평균 급여가 높은 직업(job)을 출력
SELECT job, round(AVG(sal), 2) 
FROM emp
GROUP by job
order by 2 desc;

//사원 테이블에서 부서별 최대 급여와 최소 급여를 구하되 최대급여가 2980이상인 부서만 출력
SELECT deptno, MAX(sal), MIN(sal)
FROM emp
GROUP by deptno
HAVING MAX(sal)>=2980;

//사원테이블에서 'SCOTT'의 사번, 이름, 급여, 부서명, 부서위치를 출력(emp, dept)
SELECT e.empno, e.ename, e.sal, d.dname, d.loc
FROM emp e, dept d
WHERE e.deptno=d.deptno
AND e.ename='SCOTT';

//'CHICAGO'에 근무하는 전체 사원수와 최대급여, 최소급여를 출력
SELECT count(*), max(e.sal), min(e.sal)
FROM emp e, dept d
WHERE e.deptno=d.deptno
AND d.loc='CHICAGO';

//모든 사원의 최대급여, 최저 급여, 합계 그리고 평균 급여를 출력
//열레이블은 Maximum, Minimum, Sum 그리고 Average입니다. 소수점에서 결과를 반올림
//(아래와 같은 결과가 나오도록)
//Maximum Minimum Sum Average
//--------- ---------- ---------- ----------
//5000 800 5000 2073

SELECT MAX(sal) Maximum, MIN(sal) Minimum, SUM(sal) Sum, 
       round(AVG(sal),0) Average
FROM emp;

//ROLLUP 연산자를 이용하여 아래와 같이 부서별, 직업별 전체 사원수 및 전체 급여의 합계를 출력
//(아래와 같은 결과가 나오도록)
//DNAME JOB Total Emp Total Sal
-------------------- --------- ---------- ----------
//SALES CLERK 1 950
//SALES MANAGER 1 2850
//SALES SALESMAN 4 5600
//SALES 6 9400
//RESEARCH CLERK 2 1900
//RESEARCH ANALYST 2 6000
//RESEARCH MANAGER 1 2975
//RESEARCH 5 10875
//ACCOUNTING CLERK 1 1300
//ACCOUNTING MANAGER 1 2450
//ACCOUNTING PRESIDENT 1 5000

//DNAME JOB Total Emp Total Sal
-------------------- --------- ---------- ----------
//ACCOUNTING 3 8750
//14 29025

SELECT d.dname, e.job, count(*) "Total Emp", sum(sal) "Total Sal"
FROM emp e, dept d
WHERE e.deptno=d.deptno
GROUP BY ROLLUP(d.dname, e.job)
order by grouping(d.dname), d.dname desc, grouping(e.job);

//1980, 1981, 1982, 1983년에 입사한 전체 사원 수와 연도별 사원수를 출력하는 SQL 작성
//(적당한 열레이블을 부여하세요.)

//TOTAL 1980 1981 1982 1983
------------------------------------------------------------
//14 1 10 1 

SELECT count(empno) TOTAL, 
       sum(case when to_char(hiredate,'yy')=80 then 1 else 0 end) "1980",
       sum(case when to_char(hiredate,'yy')=81 then 1 else 0 end) "1981",
       sum(case when to_char(hiredate,'yy')=82 then 1 else 0 end) "1982",
       sum(case when to_char(hiredate,'yy')=83 then 1 else 0 end) "1983"
FROM emp;