//ORDER BY 절 사용
Select name, grade, tel
from student
order by name;
 
Select name, grade, tel
from student
order by grade DESC;

//학생테이블에서 ‘김영균’학생의 이름, 사용자 아이디를 출력
//사용자 아이디의 첫문자를 대문자로 변환하여 출력
Select name, userid, initcap(userid)
from student
where name='김영균';

//학생테이블에서 학번이 ‘20101’인 학생의 사용자 아이디를 소문자와 대문자로 변환하여 출력
Select userid, lower(userid), upper(userid)
from student
where studno=20101;

//부서테이블에서 부서 이름의 길이를 문자수와 바이트수로 각각 출력
SELECT dname, length(dname), lengthb(dname)
FROM department;

//문자조작 함수 CONCAT함수
SELECT CONCAT(CONCAT(name, '의 직책은'), position)
FROM professor;

//학생 테이블에서 1학년 학생의 주민등록번호에서 생년월일과 태어난 달을 추출
//이름, 주민번호, 생년월일, 태어난달을 출력
SELECT name, idnum, substr(idnum, 1,6) birth_date, substr(idnum, 3,2) birth_mon
FROM student
WHERE grade='1';

//substr 이용해서 위 결과에서 여학생만 출력
SELECT name, idnum, substr(idnum, 1,6) birth_date, substr(idnum, 3,2) birth_mon
FROM student
WHERE grade='1'
AND substr(idnum,7,1)=2;

SELECT name, idnum, substr(idnum, 1,6) birth_date, substr(idnum, 3,2) birth_mon
FROM student
WHERE grade='1'
AND substr(idnum,7,1) in (2,4);

//부서테이블의 부서이름 칼럼에서 ‘과’글자의 위치를 출력
SELECT dname, instr(dname, '과')
FROM department;

//교수 테이블에서 직급칼럼의 왼쪽에‘*’문자를 삽입하여 10바이트로 출력
//교수 아이디칼럼은 오른쪽에 ‘+’문자를 삽입하여 12바이트로 출력
SELECT position, lpad(position, 10,'*') lpad_position, userid, 
rpad(userid, 12, '*') rpad_userid
FROM professor;

//문자조작함수 LTRIM, RTRIM 함수 사용
SELECT ltrim('xyxXxyLASTWORD', 'xy')
FROM dual;

SELECT rtrim('TURTURNERyxXxy', 'xy')
FROM dual;

//부서테이블에서 부서 이름의 마지막 글자인 ‘과’를 삭제하여 출력
SELECT dname, rtrim(dname, '과')
FROM department;

//교수테이블에서 101학과 교수의 일급을 계산(월근무일은22일)
//소수점첫째자리와 셋째자리에서 반올림한값과 소숫점왼쪽첫째자리에서 반올림한 값을 출력
SELECT name, sal, sal/22, round(sal/22), round(sal/22,2), round(sal/22,-1)
FROM professor
WHERE deptno=101;

//교수테이블에서 101학과 교수의 일급을 계산(월근무일은22일)
//소수점첫째자리와 셋째자리에서 절삭한값과 소숫점왼쪽첫째자리에서 절삭한값을 출력
SELECT name, sal, sal/22, trunc(sal/22), round(sal/22,2), trunc(sal/22,-1)
FROM professor
WHERE deptno=101;

SELECT name, sal, sal/22, trunc(sal/22), trunc(sal/22,2), trunc(sal/22,-1)
FROM professor
WHERE deptno=101;

//교수테이블에서 101번학과 교수의 급여를 보직수당으로 나눈 나머지를 계산하여 출력
SELECT name, sal, comm, mod(sal,comm)
FROM professor
WHERE deptno=101;

//19.7보다 큰 정수중에서 가장 작은 정수와12.345보다 작은 정수중에서 가장 큰 정수를 출력
SELECT ceil(19.7), floor(12.345)
FROM dual;

//교수번호가 9908인 교수의 입사일을 기준으로 입사30일후와 60일후의 날짜를 출력
SELECT name, hiredate, hiredate+30, hiredate+60
FROM professor
WHERE profno=9908;

//입사한지 120개월미만인 교수의 교수번호, 입사일, 입사일로부터 현재일까지의 개월수
//입사일에서 6개월후의 날짜를 출력
SELECT profno, hiredate, months_between(sysdate, hiredate) tenure,
add_months(hiredate, 6) review
FROM professor
WHERE months_between(sysdate, hiredate) < 120;

//위에서 360개월미만인 교수 조건일 경우
SELECT profno, hiredate, months_between(sysdate, hiredate) tenure,
add_months(hiredate, 6) review
FROM professor
WHERE months_between(sysdate, hiredate) < 360;

//오늘이 속한 달의 마지막 날짜와 다가오는 일요일의 날짜를 출력
ALTER SESSION SET nls_language=korean;
SELECT sysdate, last_day(sysdate), next_day(sysdate, '일')
FROM dual;

ALTER SESSION SET nls_language=american;
SELECT sysdate, last_day(sysdate), next_day(sysdate, 1)
FROM dual;

//시간정보를 생략한경우, ROUND 함수와 TRUNC 함수의 결과를 비교
SELECT To_char(sysdate, 'yy/mm/dd hh24:mi:ss') normal, 
to_char(trunc(sysdate), 'yy/mm/dd hh24:mi:ss') trunc, 
to_char(round(sysdate), 'yy/mm/dd hh24:mi:ss') round
FROM dual;

//101번 학과 교수들의 입사일을 일, 월, 년을 기준으로 반올림하여 출력
SELECT To_char(hiredate, 'yy/mm/dd hh24:mi:ss') hiredate,
To_char(round(hiredate, 'dd'), 'yy/mm/dd') round_dd,
To_char(round(hiredate, 'mm'), 'yy/mm/dd') round_mm,
To_char(round(hiredate, 'yy'), 'yy/mm/dd') round_yy
FROM professor
WHERE deptno=101;
//년은 7월 1일, 월은 16일 기준으로 반올림됨.

//학생테이블에서 전인하 학생의 학번과 생년월일 중에서 년월만 출력
SELECT studno, to_char(birthdate, 'YY-MM') birthdate
FROM student
WHERE name='전인하';

//학생테이블에서 102번학과 학생의 이름, 학년, 생년월일을 출력
SELECT name, grade, to_char(birthdate, 'day Month dd, yyyy') birthdate
FROM student
where deptno=102;

//교수테이블에서 101번학과 교수의 이름과 입사일을 출력
SELECT name, to_char(hiredate, 'month dd, yyyy hh24:mi:ss PM') hiredate
FROM professor
WHERE deptno =101;

//위에 조건에서 월만 영어로 표시
SELECT name, to_char(hiredate, 'month dd, yyyy hh24:mi:ss','NLS_DATE_LANGUAGE=ENGLISH') 
｜｜to_char(hiredate, 'pm', 'NLS_DATE_LANGUAGE=korean') hiredate
FROM professor
WHERE deptno =101;

//교수테이블에서 101번학과 교수들의 이름, 직급, 입사일을 출력
SELECT name, position, to_char(hiredate, 'Mon "the" ddth "of" yyyy') hiredate 
FROM professor
where deptno=101;

//보직수당을 받는 교수들의 이름, 급여, 보직수당, 급여와 보직수당을 더한값에 12를 곱한결과를 연봉으로 출력
SELECT name, sal, comm, to_char((sal+comm)*12, '9,999') anual_sal 
FROM professor
where comm is not null;

//TO_NUMBER 함수 사용
SELECT name, hiredate
FROM professor
where hiredate=To_date('6월 01, 01', 'month dd, yy');

//주민등록번호에서 생년월일을 추출하여‘YY/MM/DD’ 형태로 출력
SELECT to_char(to_date(substr(idnum, 1,6), 'yymmdd'), 'yy/mm/dd') birthday
FROM student;

//내가 출생한지 며칠째인지 출력, 열레이블은 Lived day
select trunc(sysdate-to_date('1999/08/02','yyyy/mm/dd')) lived_day
FROM dual;

//위에 조건에서 절삭하여 월, 일 출력
select trunc(sysdate-to_date('1999/08/02','yyyy/mm/dd')) "lived_day",
round(months_between(sysdate, '1999/08/02')) "Lived Month"
from dual;

