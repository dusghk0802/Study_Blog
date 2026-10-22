//다중열에 의한 정렬
SELECT ename, job, deptno, sal
from emp
order by deptno, sal DESC;

//부서10과 30에 속하는 모든사원의 이름과 부서번호를 이름의 알파벳순으로 정렬되도록 출력
SELECT ename, deptno
from emp
where deptno in (10,30)
order by ename;

//1982년에 입사한 모든사원의 이름과 입사일을 구하는 질의문
SELECT ename, hiredate
from emp
where hiredate like '82%';

//보너스를 받는 모든사원에 대해서 이름, 급여 그리고 보너스를 출력
//단, 급여와 보너스에 대해서 내림차순 정렬
SELECT ename, sal, comm
from emp
where comm is not NULL
and comm <> 0
order by sal, comm DESC;

//보너스가 급여의 20% 이상이고 부서번호가 30인 많은 모든사원에 대해서 이름, 급여 그리고 보너스를 출력
SELECT ename, sal, comm
from emp
where comm >= sal*0.2
and deptno=30;

//'scott'사원 출력
SELECT * FROM emp
where lower(ename)='scott';

//이름이 J,M 또는 S으로 시작하는 모든 사원에 대해 사원의 이름을 대문자
//업무(JOB)는 첫번째 문자, 대문자로 나머지는 모두 소문자
//이름의 길이를 열레이블로 만들고 Name, Job, Length 부여
//사원명 순으로 정렬
SELECT upper(ename) "Name", initcap(job) "Job", Length(ename) "Length"
FROM emp
where ename like 'J%'
OR ename like 'M%'
OR ename like 'S%'
ORDER BY 1;

//사원명, 입사일, 입사한 요일 출력 (열 레이블은 day)
//결과는 월요일부터 시작하는 요일 순으로 정렬
SELECT ename, hiredate, to_char(hiredate, 'DAY') DAY
FROM emp
ORDER BY to_char(hiredate-1,'D');

//사원 테이블에서 2월에 입사한 사원을 출력(substr)
SELECT ename, substr(hiredate, 4,2)
FROM emp
where substr(hiredate, 4,2)='02';

//사원테이블의 사원명과 급여를 아래와 같은 포맷으로 출력(결과가 아래와 같이 나오도록)
//MONTHLY
--------------------------------------
//SMITH: 1 Month salary = 800
//ALLEN: 1 Month salary = 1600
//WARD: 1 Month salary = 1250

SELECT concat(ename, ':') ｜｜ concat('1 Month salary =', sal) Monthly 
FROM emp;

//직급이 'manager'인 사원을 검색하려고 아래와 같은 질의문을 작성하였으나, '선택된 레코드가 없습니다.'
//이유를 설명하고 직급이 'manager'인 사원을 검색하도록 질의문을 수정해 보세요.
SELECT ename, job
FROM emp
where job=upper('manager');
//소문자로 manager 검색 불가


//이름이 J,A, 또는 M으로 시작하는 모든 사원에 대해서
//첫번째 문자는 대문자로, 나머지는 모두 소문자로 나타나는 사원의 이름과 이름 길이를 출력
//각각의 열에 Name, Length라는 레이블을 부여하세요.(SUBSTR)
SELECT INITCAP(ename) name, Length(ename) "Length"
FROM emp
WHERE substr(ename, 1,1) in ('J', 'A', 'M');

//이름에 L이 두 자가 있고 부서가 30이거나 또는 관리자가 7782인 모든 사원의 이름을 출력
SELECT ename
FROM emp
where ename like '%LL%'
and deptno=30
or mgr=7782;


//이름에 L이 여러개 들은 사람 찾기
//SELECT ename, deptno, mgr, length(ename), Length(replace(ename, 'L', ''))
//FROM emp
//where length(ename)-Length(replace(ename, 'L', '')) = 3;
