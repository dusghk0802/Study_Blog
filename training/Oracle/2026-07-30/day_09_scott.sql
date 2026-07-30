//계층적질의문을 사용하여 부서테이블에서 학과,학부,단과대학을 검색하여 단과대,학부학과순으로
//top-down 형식의 계층구조로 출력
//단, 시작데이터는10번부서
SELECT deptno, dname, college 
FROM department
START WITH deptno = 10
CONNECT BY PRIOR deptno = college;

//계층적질의문을사용하여부서테이블에서학과,학부,단과대학을검색하여학과,학부 단대순으로
//bottom-up 형식의 계층구조로 출력
//단, 시작데이터는 102번부서
SELECT deptno, dname, college 
FROM department
START WITH deptno = 102
CONNECT BY PRIOR college = deptno;

//계층적질의문을 사용하여 서테이블에서 부서이름을 검색하여 단대, 학부, 학과순의 top-down 형식으로 출력
//단, 시작데이터는 ‘공과대학’이고, 각 레벨별로 우측으로 2칸 이동하여 출력
SELECT level, lpad(' ', (level-1)*2) ｜｜dname 조직도
FROM department
START WITH dname = '공과대학'
CONNECT BY PRIOR deptno = college;

//4칸 이동시
SELECT level, lpad(' ', (level-1)*4) ｜｜dname 조직도
FROM department
START WITH dname = '공과대학'
CONNECT BY PRIOR deptno = college;

//계층적 질의문을 사용하여 부서테이블에서 dname 칼럼을 단대, 학부, 학과순으로 top-down 형식의
//계층구조로 ‘정보미디어학부’를 제외하고 출력
SELECT deptno, college, dname, loc
FROM department
WHERE dname != '정보미디어학부'
START WITH college is null
CONNECT BY PRIOR deptno = college;

//****사용시
SELECT deptno, college, lpad('********', (level-1)*4)｜｜ dname, loc
FROM department
WHERE dname != '정보미디어학부'
START WITH college is null
CONNECT BY PRIOR deptno = college;

//계층적 질의문을 사용하여 부서테이블에서 dname 칼럼을 단대, 학부, 학과순으로 top-down 형식의
//계층구조로 ‘정보미디어학부’와‘정보미디어학부’에 속한모든학과를 제외하고 출력
SELECT deptno, college, dname, loc
FROM department
START WITH college is null
CONNECT BY PRIOR deptno = college
AND dname != '정보미디어학부';

//계층적 질의문을 사용하여 Level이 1인 최상위로우(ROOT Node)의 정보를 얻을 수가 있다. 
SELECT lpad(' ', 4*(level-1))｜｜ ename 사원명, empno 사번, 
connect_by_root empno 최상위사번, level
FROM emp
START WITH job = UPPER('president')
CONNECT BY PRIOR empno = mgr;

//계층적 질의문을 사용하여 로우의 최하위 레벨(Leaf) 여부를 반환
//최하위레벨1, 아니면0 
SELECT lpad(' ', 4*(level-1))｜｜ ename 사원명, empno 사번, 
connect_by_isleaf leaf_yn, level
FROM emp
START WITH job = UPPER('president')
CONNECT BY NOCYCLE PRIOR empno = mgr;

//계층적 질의문을 사용하여 현재Row까지의 PATH 정보를 쉽게 얻어올 수가 있다.
SELECT lpad(' ', 4*(level-1))｜｜ ename 사원명, empno 사번, 
sys_connect_by_path(ename,'/') path
FROM emp
START WITH job = UPPER('president')
CONNECT BY NOCYCLE PRIOR empno = mgr;

//계층적 질의문을 사용하여 Leaf Node만 전체 PATH 정보가 나오도록 할수가 있다. 
SELECT LEVEL, SYS_CONNECT_BY_PATH(ename,'/') PATH 
FROM emp
where CONNECT_BY_ISLEAF = 1
START WITH mgr is null
CONNECT BY PRIOR empno = mgr;

//(ORDERBY를 사용하였을 경우와 결과가다르므로주의해야함)
//TREE의 상관관계를 그대로 유지하면서 내부요소를 정렬
SELECT lpad(' ', 4*(level-1))｜｜ ename 사원명, ename ename2, empno 사번, LEVEL
FROM emp
START WITH job = UPPER('president')
CONNECT BY NOCYCLE PRIOR empno = mgr
ORDER SIBLINGS BY ename;

// 계층적 질의문을 응용하여 아래와 같은 결과가 나오도록 출력
SELECT e.empno, e.ename, e.job, e.mgr, m.ename "매니저 이름", LEVEL, e.ename "사원명", 
SYS_CONNECT_BY_PATH(e.ename,'-') "사원 PATH"
FROM emp e, emp m
WHERE e.mgr = m.empno(+)
START WITH e.mgr IS NULL
CONNECT BY PRIOR e.empno = e.mgr;

//학과별 최대키를 구하고 최대키를 가진 학생의 학과명, 최대키, 이름, 키를 출력
//서재진 키 186으로 변경
UPDATE student
SET height = 186
WHERE name = '서재진';

SELECT d.dname 학과명, m.max_height 최대키, s.name 이름, s.height 키
FROM department d, student s, 
     (SELECT deptno, max(height) max_height
      FROM student
      group by deptno) m
where d.deptno = s.deptno
AND s.deptno = m.deptno
AND s.height = m.max_height;


