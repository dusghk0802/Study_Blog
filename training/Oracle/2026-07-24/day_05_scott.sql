//사원과 매니저의 사번 연결 출력
SELECT * FROM emp;
SELECT e.ename ｜｜'의 사번은'｜｜ e.empno ｜｜'이고, 매니저는'｜｜ 
       m.ename ｜｜'이고 사번은'｜｜ m.empno ｜｜'입니다'
FROM emp e, emp m
where e.mgr = m.empno;
