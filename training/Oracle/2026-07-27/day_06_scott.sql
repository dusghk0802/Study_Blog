//Blake와 같은 부서에있는모든사원에 대해서 사원이름과 입사일을 디스플레이
SELECT ename, hiredate
FROM emp
WHERE deptno = (SELECT deptno
                FROM emp
                WHERE initcap(ename) = 'Blake');
                
//평균급여이상을 받는 모든사원에 대해서 사원번호와 이름을 디스플레이
//단, 출력은 급여 내림차순정렬
SELECT empno, ename, sal 
FROM emp
WHERE sal > (SELECT AVG(sal)
             FROM emp)
ORDER BY sal DESC;

//커미션을 받은 사원의 부서번호와 급여에 일치하는 사원의 이름, 부서번호, 급여를 출력
SELECT ename, deptno, sal
FROM emp
WHERE (deptno, sal) in (SELECT deptno, sal
                        FROM emp
                        WHERE comm IS NOT NULL);

