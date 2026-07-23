--사원 테이블 emp, 열레이블이 emloyee title, 콤마 공백 구분, 이름과 직무 job 연결 출력
SELECT * FROM emp;
SELECT ename ｜｜ ', ' ｜｜ job "Emplyee and Title"
FROM emp;

--사원테이블에서 사원번호, 이름, 급여 그리고 20%증가된 급여 모두 출력
SELECT * FROM emp;
SELECT empno, ename, sal, sal*1.2 as New_Salary
FROM emp;

--새로운 급여에서 예전 급여 빼기(increase 열 추가)
SELECT * FROM emp;
SELECT empno, ename, sal, sal*1.2 as New_Salary, sal*1.2-sal as increase
FROM emp;

--급여가 $2780이상을 받는 사원의 이름과 급여 출력
SELECT * FROM emp;
SELECT ename, sal
FROM emp
where sal >=2780;

--아래의 질의는 오류를 포함하고 있다. 수정해서 실행
--SELECT name, job, sal*12 AS yearly sal FROM emp;

SELECT * FROM emp;
SELECT ename, job, sal, sal*12 AS yearly_sal
FROM emp;

-- 사원 테이블에서 이름에 A를 포함하고 커미션을 받지 않는 사원의 사원 번호, 이름, 급여, 커미션을 출력
--(결과가 아래와 같이 나오도록)

//EMPNO ENAME SAL COMM
-------- ---------- ---------- ----------
//7698 BLAKE 2850
//7782 CLARK 2450

SELECT empno, ename, sal, comm
FROM emp
where ename like '%A%'
and comm is null;

--업무(job)가 MANAGER이거나 SALESMAN이며 급여가 $1500, $3000 또는 5000이 아닌 모든 사원에
--대해서 이름, 업무, 그리고 급여를 출력(결과가 아래와 같이 나오도록)

//ENAME JOB SAL
----------- --------- ----------
//JONES MANAGER 2975
//BLAKE MANAGER 2850

SELECT ename, job, sal
FROM emp
where (job= 'MANAGER' or job= 'SALESMAN')
and sal not in (1500, 3000, 5000);

--사원 테이블에서 사원 번호, 이름, 직업, 급여 그리고 22% 증가된 급여를 출력
--증가된 급여의 열 레이블은 New Salary(결과가 아래와 같이 나오도록)

//EMPNO ENAME JOB SAL New Salary Increase
------- ---------- --------- ---------- ------------- ----------
//7902 FORD ANALYST 3000 3660 660
//7788 SCOTT ANALYST 3000 3660 660
//7369 SMITH CLERK 800 976 176
//7900 JAMES CLERK 950 1159 209

SELECT empno, ename, job, sal, sal*1.22 as New_Salary, sal*1.22-sal as Increase
FROM emp;