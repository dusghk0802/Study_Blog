SELECT * FROM emp;
SELECT * FROM dept;

--PL-SQL
--PL/SQL 은 Oracle's Procedural Language extension to SQL. 의 약자 입니다. 
--SQL문장에서 변수정의, 조건처리(IF), 반복처리(LOOP, WHILE, FOR)등을 지원하며, 
--오라클 자체에 내장되어 있는Procedure Language입니다
--DECLARE문을 이용하여 정의되며, 선언문의 사용은 선택 사항입니다. 
--PL/SQL 문은 블록 구조로 되어 있고PL/SQL 자신이 컴파일 엔진을 가지고 있습니다.

--Tool > 보기 > DBMS 출력창 > + 버튼 클릭 > 사용자 접속(개발자)
--DBMS 출력창 : 이클립스 console 창

--pl-sql (java : System.out.println()) 결과 확인
--DBMS 출력 창에서

--1.pl-sql 블럭 단위 실행
BEGIN
  DBMS_OUTPUT.PUT_LINE('HELLO WORLD');
END;

--pl-sql
--선언부(변수) 
--실행부(변수 값을 할당 , 제어구문)
--예외부(Exception)

DECLARE --선언
  vno number(4);
  vname varchar2(20);
BEGIN
  vno := 100; -- 할당 >  String s; s = "홍길동"
  vname := 'kglim';
  DBMS_OUTPUT.PUT_LINE(vno); --화면 출력
  DBMS_OUTPUT.PUT_LINE(vname || '입니다');
END;

--변수 선언 방법 (타입)
--DECLARE
--v_job varchar2(10);
--v_count number := 10; --초기값 설정
--v_date date := sysdate + 7; --초기값 설정
--v_valid boolean not null := true
--------------------------------------------------------------------------------
DECLARE
  vno number(4);
  vname varchar2(20);
BEGIN
   select empno ,ename
      into vno , vname --pl-sql 사용하는 구분 (into) . 실행결과 [ 변수에 담기 ]
   from emp
   where empno=&empno; --& 자바 scanner  역할 (입력값 받기)
   
   DBMS_OUTPUT.PUT_LINE('변수값 : ' || vno || '/' || vname);
END;
 
--실습 테이블 만들기
create table pl_test(
no number , name varchar2(20) , addr varchar2(50));

DECLARE
  v_no number := '&NO';
  v_name varchar2(20) := '&NAME';
  v_addr varchar2(50) := '&ADDR';
BEGIN
  insert into pl_test(no,name,addr)
  values(v_no , v_name , v_addr);
  commit;
END;

select * from pl_test;


desc emp;

select * from pl_test;  
--변수 제어하기(타입)
--1.1 타입 : v_empno number(10)
--1.2 타입 : v_empno emp.empno%TYPE  (emp 테이블에 있는 empno 컬럼의 타입 사용)
--1.3 타입 : v_row emp%ROWTYPE (v_row 변수는 emp 테이블 모든 컬럼 타입 정보)
--                    v_row.empno , v_row.ename

--QUIZ
--두개의 정수를 입력받아서 그 합을   변수에 담아서 출력




DECLARE
  v_no1 number := 100;
  v_no2 number := 200;
  result number;
BEGIN
    result := v_no1 + v_no2;
    DBMS_OUTPUT.PUT_LINE('result : ' || result);
END;

--------------------------------------------------------------------------------
DECLARE
  v_emprow emp%ROWTYPE; 
BEGIN
  select *
    into v_emprow -- empno , ename , ,..... deptno
  from emp
  where empno=7788;
  
  DBMS_OUTPUT.PUT_LINE(v_emprow.empno || '-' || v_emprow.ename || '-' || v_emprow.sal);
END;

--------------------------------------------------------------------------------
create sequence empno_seq
increment by 1
start with 8000
maxvalue 9999
nocycle
nocache;

DECLARE
  v_empno emp.empno%TYPE;
BEGIN
  select empno_seq.nextval
    into v_empno
  from dual;
  
  insert into empdml(empno ,ename)
  values(v_empno,'홍길동');
  commit;
END;

select * from empdml;

create table empdml
as
    select * from emp where 1=2;

--여기까지 변수 선언 , 타입 , 값 할당
--------------------------------------------------------------------------------
--pl-sql 제어문
DECLARE
  vempno emp.empno%TYPE;
  vename emp.ename%TYPE;
  vdeptno emp.deptno%TYPE;
  vname varchar2(20) := null;
BEGIN
    select empno , ename , deptno
        into vempno , vename , vdeptno --변수
    from emp
    where empno=7788;
    --제어문 if(조건문){실행문}
    IF(vdeptno = 10) THEN vname := 'ACC'; -- if(vdeptno==10) { vname = "ACC"}
    ELSIF(vdeptno=20) THEN vname := 'IT'; --ELSEIF
    ELSIF(vdeptno=30) THEN vname := 'SALES';
    END IF;
    DBMS_OUTPUT.PUT_LINE('당신의 직종은 : ' || vname);
END;

--IF() THEN 실행문
--ELSIF() THEN 실행문
--ELSIF() THEN 실행문
--ELSE 실행문 (선택)

--사번이 7788번인 사원의 사번 , 이름 , 급여를 변수에 담고
--변수에 담긴 급여가 2000 이상이면 '당신의 급여는 BIG' 출력하고
--그렇지 않으면(ELSE) '당신의 급여는 SMALL' 이라고 출력하세요

DECLARE
  vempno emp.empno%TYPE;
  vename emp.ename%TYPE;
  vsal   emp.sal%TYPE;
BEGIN
  select empno , ename , sal
      into vempno , vename , vsal
  from emp
  where empno=7788;
  --제어문 if(조건문){실행문}
    IF(vsal >=  2000) THEN 
         DBMS_OUTPUT.PUT_LINE('당신의 급여는 BIG ' || vsal);
    ELSE
         DBMS_OUTPUT.PUT_LINE('당신의 급여는 SMALL ' || vsal);
    END IF;
 END;
 
 -------------------------------------------------------------------------------
 --CASE 
DECLARE
  vempno emp.empno%TYPE;
  vename emp.ename%TYPE;
  vdeptno emp.deptno%TYPE;
  v_name varchar2(20);
BEGIN
     select empno, ename , deptno
        into vempno, vename , vdeptno
     from emp
     where empno=7788;
     
--    v_name := CASE vdeptno
          --                WHEN 10  THEN 'AA'
          --                WHEN 20  THEN 'BB'
          --                WHEN 30  THEN 'CC'
          --                WHEN 40  THEN 'DD'
      --              END;

    v_name := CASE 
                              WHEN vdeptno=10  THEN 'AA'
                              WHEN vdeptno in(20,30)  THEN 'BB'
                              WHEN vdeptno=40  THEN 'CC'
                              ELSE 'NOT'
               END;
    DBMS_OUTPUT.PUT_LINE('당신의 부서명:' || v_name);            
END;
--------------------------------------------------------------------------------
--pl-sql (반복문)
--Basic loop
/*
LOOP
  문자;
  EXIT WHEN (조건식)
END LOOP
*/
DECLARE
  n number :=0;
BEGIN
  LOOP
    DBMS_OUTPUT.PUT_LINE('n value : ' || n);
    n := n + 1;
    EXIT WHEN n > 5;
  END LOOP;
END;

/*
WHILE(n < 6)
LOOP
   실행문;
END LOOP
*/
DECLARE
  num number := 0;
BEGIN
  WHILE(num < 6)
    LOOP
      DBMS_OUTPUT.PUT_LINE('num 값 : ' || num);
      num := num +1;
    END LOOP;
END;

--for
--java for(int i = 0 ; i <= 10 ; i++) {}
BEGIN
  FOR i IN 0..10 LOOP
    DBMS_OUTPUT.PUT_LINE(i);
  END LOOP;
END;

--위 FOR 문을 사용해서 (1~100까지 합) 구하세요
DECLARE
total number :=0;
BEGIN
  FOR i IN 1..100 LOOP
    total := total + i;
  END LOOP;
  DBMS_OUTPUT.PUT_LINE('1~100 총합 : ' || total);
END;

--11g 이전 (continue (x))
--11g (continue 추가)
DECLARE
  total number := 0;
BEGIN
  FOR i IN 1..100 LOOP
    DBMS_OUTPUT.PUT_LINE('변수 : ' || i);
    CONTINUE WHEN i > 5; --skip
    total := total + i; -- 1 , 2 , 3 , 4, 5
  END LOOP;
    DBMS_OUTPUT.PUT_LINE('합계 : ' || total);
END;
--------------------------------------------------------------------------------
--활용
DECLARE
    v_empno emp.empno%TYPE;
    v_name  emp.ename%TYPE := UPPER('&name');
    v_sal   emp.sal%TYPE;
    v_job   emp.job%TYPE;
    v_deptno emp.deptno%TYPE;
BEGIN
  select empno , job ,sal , deptno
    into v_empno, v_job , v_sal , v_deptno
  from emp
  where ename = v_name;
  
  IF v_job IN('MANAGER','ANALYST') THEN
     v_sal := v_sal * 1.5;
  ELSE
     v_sal := v_sal * 1.2;
  END IF;
  
  update emp
  set sal = v_sal
  where deptno=v_deptno;
  
  DBMS_OUTPUT.PUT_LINE(SQL%ROWCOUNT || '개의 행이 갱신 되었습니다');
  
  --예외처리
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
       DBMS_OUTPUT.PUT_LINE(v_name || '는 자료가 없습니다');
    WHEN TOO_MANY_ROWS THEN
       DBMS_OUTPUT.PUT_LINE(v_name || '는 동명 이인입니다');
    WHEN OTHERS THEN
       DBMS_OUTPUT.PUT_LINE('기타 에러가 발생했습니다');
  END;

SELECT * FROM EMP;

SELECT * FROM EMP where ename='smith';
ROLLBACK;
/*
질의는 하나의 행만 RETURN 해야 합니다. PL/SQL 블록 내의 SELECT 문장은 다음 규칙을
적용하는 Embedded SQL 의 ANSI 범주에 속합니다. 질의의 결과는 하나의 행만을 RETURN 해
야  하고  하나의  행  이상  또는  행이  없는  것은  에러를  생성합니다.  PL/SQL 은
NO_DATA_FOUND 와 TOO_MANY_ROWS 를 예외로 블록의 예외 섹션에서 추적할 수 있는 표준 예
외를 조성하여 처리 합니다.
*/
select * from emp where ename='SMITH';
rollback;

-- pl-sql 기본 구문  END

--변수 , 연산자 , 제어문 (PL-SQL 하기 위한 기본 학습)
--암기 할 필요 없어요 ... 

--------------------------------------------------------------------------------
-- cursor , procedure , function , Trigger 고급자원 
-- 이 내용은 프로젝트에 반영 되었으면 합니다 
-- 그럼 프로젝트가 고급 ..... 으로 올라가요^^


--[ 커서 ]
--지금까지 집합 단위의 데이터 처리 (전체 row를 대상으로)

--[CURSOR]
--1.  [행단위]로 데이터를 처리하는 방법을 제공
--2.  여러건의 데이터를 처리하는 처리하는 방법을 제공 (한 건이상의  row가지고 놀기)
 
--사원급여테이블(건설회사)
--정규직 , 일용일 ,시간직 

--사번 , 이름 , 직종명 , 월급 , 시간 , 시간급 ,   식대
-- 10   홍길동  정규직   120    null   null     null
-- 11   김유신  시간직   null   10      100     null
-- 12   이순신  일용일   null   null    120     10





최종 출력 (판단의 기준이 : 직종 조건 .. row 단위)
사번 , 이름 , 이번달 급여총액
10    홍길동    (월급 :  120 )
11    김유신    (시간*시간급 : 10 *100)
12    이순신    (시간급 +식대 : 120+10)

--정규직
--월급
--
--일용직
--시간 * 시간급
--
--시간직
--시간급 + 식대


--한 행씩 접근해서 직종을 기준으로 계산방법

--if 정규직  > 월급 (총급여)
--elsif 시간직 > 시간 * 시간급 (총급여)
--elsif 일용직 > 시간급 + 식대 (총급여)
 
 
 
--SQL CURSOR 의 속성을 사용하여 SQL 문장의 결과를 테스트할 수 있다.
--[종 류 설 명]
  --SQL%ROWCOUNT 가장 최근의 SQL 문장에 의해 영향을 받은 행의 수
  --SQL%FOUND 가장 최근의 SQL 문장이 하나 또는 그 이상의 행에 영향을 미친다면 TRUE 로 평가한다.
  --SQL%NOTFOUND 가장 최근의 SQL 문장이 어떤 행에도 영향을 미치지 않았다면 TRUE 로  평가한다.
  --SQL%ISOPEN PL/SQL 이 실행된 후에 즉시 암시적 커서를 닫기 때문에 항상 FALSE 로 평가된다.
  
/*
   DECLARE
          CURSOR 커서이름 IS 문자(커서가 실행할 쿼리)
   BEGIN
         OPEN 커서이름 (커서가 가지고 있는 쿼리를 실행)
             
         FETCH 커서이름 INTO 변수명들...
          --커서로 부터 데이터를 읽어서 원하는 변수에 저장
         CLOSE 커서이름 (커서닫기) 
   END


*/
DECLARE
  vempno emp.empno%TYPE;
  vename emp.ename%TYPE;
  vsal   emp.sal%TYPE;
  CURSOR c1  IS select empno,ename,sal from emp where deptno=30;
BEGIN
    OPEN c1; --커서가 가지고 있는 문장 실행
    LOOP  --데이터 row 건수 만큼 반복
      --Memory
      /*
        7499 ALLEN 1600
        7521 WARD 1250
        7654 MARTIN 1250
        7698 BLAKE 2850
        7844 TURNER 1500
        7900 JAMES 950
      */
      FETCH c1 INTO vempno , vename, vsal;
      EXIT WHEN c1%NOTFOUND; --더이상 row 가 없으면 탈출
        DBMS_OUTPUT.PUT_LINE(vempno || '-' || vename || '-'|| vsal);
    END LOOP;
    CLOSE c1;
END;
-------------------------------------------------------
--위 표현을 좀 더 간단하게
--java (for(emp e : emplist){}
DECLARE
  CURSOR emp_curr IS  select empno ,ename from emp;
BEGIN
    FOR emp_record IN emp_curr  --row 단위로 emp_record변수 할당  for(Emp e : emplist)
    LOOP
      EXIT WHEN  emp_curr%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(emp_record.empno || '-' || emp_record.ename);
    END LOOP;
    CLOSE emp_curr;
END;

--------------------------------------------------------------------------------
--java (for(emp e : emplist){}

DECLARE
  vemp emp%ROWTYPE; --Type 정의
  CURSOR emp_curr IS  select empno ,ename from emp;
BEGIN
  FOR vemp IN emp_curr  --row 단위로 emp_record변수 할당
    LOOP
      EXIT WHEN  emp_curr%NOTFOUND;
      DBMS_OUTPUT.PUT_LINE(vemp.empno || '-' || vemp.ename);
    END LOOP;
    CLOSE emp_curr;
END;
-------------------------------------------------
DECLARE
  v_sal_total NUMBER(10,2) := 0;
  CURSOR emp_cursor
  IS SELECT empno,ename,sal FROM emp
     WHERE deptno = 20 AND job = 'CLERK'
     ORDER BY empno;
BEGIN
  DBMS_OUTPUT.PUT_LINE('사번 이 름 급 여');
  DBMS_OUTPUT.PUT_LINE('---- ---------- ----------------');
  FOR emp_record IN emp_cursor 
  LOOP
      v_sal_total := v_sal_total + emp_record.sal;
      DBMS_OUTPUT.PUT_LINE(RPAD(emp_record.empno,6) || RPAD(emp_record.ename,12) ||
                           LPAD(TO_CHAR(emp_record.sal,'$99,999,990.00'),16));
  END LOOP;
      DBMS_OUTPUT.PUT_LINE('----------------------------------');
      DBMS_OUTPUT.PUT_LINE(RPAD(TO_CHAR(20),2) || '번 부서의 합 ' ||
      LPAD(TO_CHAR(v_sal_total,'$99,999,990.00'),16));
END;

--------------------------------------------------------------------------------
select * from emp where deptno=20 and job='CLERK';


create table cursor_table
as
  select * from emp where 1=2;

select * from cursor_table;  


alter table cursor_table
add totalsum number;

desc cursor_table;

--문제
--emp 테이블에서  사원들의  사번 , 이름 , 급여를 가지고
--와서 cursor_table insert 를 하는데 totalsum 은 급여 + comm 통해서
--부서번호가 10인 사원은 totalsum에 급여 정보만 넣으세요
--데이터 처리
--
insert into CURSOR_TABLE(empno,ename,sal,totalsum)
    select empno , ename , sal , sal+nvl(comm,0)
    from emp where deptno=20;

select *  from CURSOR_TABLE;
commit;

delete from cursor_table;
commit;

select * from cursor_table;


DECLARE
    result number := 0;
    CURSOR emp_curr IS select empno, ename, sal,deptno,comm from emp;
  BEGIN
    FOR vemp IN emp_curr   --row 단위로 emp_record 변수에 할당
    LOOP
        EXIT WHEN emp_curr%NOTFOUND;
        IF(vemp.deptno = 20) THEN 
              result := vemp.sal+nvl(vemp.comm,0);
              insert into cursor_table(empno, ename, sal,deptno,comm,totalsum) 
              values (vemp.empno,vemp.ename, vemp.sal,vemp.deptno,vemp.comm,result);
        ELSIF(vemp.deptno = 10) THEN 
              result := vemp.sal;
              insert into cursor_table(empno, ename, sal,deptno,comm,totalsum) 
              values (vemp.empno,vemp.ename, vemp.sal,vemp.deptno,vemp.comm,result);
        ELSE
            DBMS_OUTPUT.PUT_LINE('ETC');
        END IF;
     END LOOP;   
  END;

--필요하다면 커서 안에서 rollback 과 commit 구현해야 한다
rollback;
commit;
--------------------------------------------------------------------------------

직종명	           급여계산 방식
정규직	           월급 그대로 지급
시간직	           시간 × 시간급
일용직	           시간급 + 식대

create table salary_info
(
    empno number,
    ename varchar2(20),
    job_type varchar2(20),  --정규직 , 시간직 , 일용직
    monthly number, --월급
    hours number,   --근무시간
    hourly number,  --시간당 급여
    meal number     --식대
);

insert into salary_info values(10,'홍길동','정규직',120,null,null,null);
insert into salary_info values(11,'김유신','시간직',null,10,100,null);
insert into salary_info values(12,'이순신','일용직',null,null,120,10);
commit;

select * from salary_info;


--------------------------------------------------------------------------------
declare
   cursor sal_cursor is  
   select empno, ename, job_type , monthly, hours , hourly, meal 
   from salary_info;
   
   v_empno salary_info.empno%TYPE;
   v_ename salary_info.ename%TYPE;
   v_job_type salary_info.job_type%TYPE;
   v_monthly salary_info.monthly%TYPE;
   v_hours salary_info.hours%TYPE;
   v_hourly salary_info.hourly%TYPE;
   v_meal salary_info.meal%TYPE;
   v_pay  number; --각각 사원에 한 급여 정보 
begin
    open sal_cursor; --커서 실행 (쿼리문)
    
    loop
        fetch sal_cursor into v_empno,v_ename,v_job_type,v_monthly,v_hours,v_hourly,v_meal;
        exit when sal_cursor%NOTFOUND;
        
        --로직 (요구사항)
        --직종별 급여 다르게 ..정규직, 시간직, 일용직
        if v_job_type = '정규직' then
             v_pay := nvl(v_monthly,0);
        elsif v_job_type = '시간직' then
             v_pay := nvl(v_hours,0) * nvl(v_hourly,0);
        elsif v_job_type = '일용직' then
             v_pay := nvl(v_hourly,0) + nvl(v_meal,0);
        else 
             v_pay := 0;
        end if;
        
        DBMS_OUTPUT.PUT_LINE('사번 :' || v_empno || '직종 : ' || v_job_type ||  '급여 : ' || v_pay);
        
    end loop;
    close sal_cursor;
end;

SELECT * FROM salary_info;

--------------------------------------------------------------------------------
--이커서 매월 마지막 주 금요일 12시 실행
--정보가 급여테이블 insert ....

create table salary_result(
    empno number,
    ename varchar2(50),
    job_type varchar2(20),
    pay number,
    paid_date date default sysdate
);

create or replace procedure proc_calculate_salary
is
   cursor sal_cursor is  
   select empno, ename, job_type , monthly, hours , hourly, meal 
   from salary_info;
   
   v_empno salary_info.empno%TYPE;
   v_ename salary_info.ename%TYPE;
   v_job_type salary_info.job_type%TYPE;
   v_monthly salary_info.monthly%TYPE;
   v_hours salary_info.hours%TYPE;
   v_hourly salary_info.hourly%TYPE;
   v_meal salary_info.meal%TYPE;
   v_pay  number; --각각 사원에 한 급여 정보 
begin
    open sal_cursor; --커서 실행 (쿼리문)
    
    loop
        fetch sal_cursor into v_empno,v_ename,v_job_type,v_monthly,v_hours,v_hourly,v_meal;
        exit when sal_cursor%NOTFOUND;
        
        --로직 (요구사항)
        --직종별 급여 다르게 ..정규직, 시간직, 일용직
        if v_job_type = '정규직' then
             v_pay := nvl(v_monthly,0);
        elsif v_job_type = '시간직' then
             v_pay := nvl(v_hours,0) * nvl(v_hourly,0);
        elsif v_job_type = '일용직' then
             v_pay := nvl(v_hourly,0) + nvl(v_meal,0);
        else 
             v_pay := 0;
        end if;
        
       -- DBMS_OUTPUT.PUT_LINE('사번 :' || v_empno || '직종 : ' || v_job_type ||  '급여 : ' || v_pay);
       -- 실 급여 테이블에 insert
       insert into salary_result(empno, ename, job_type, pay)
       values(v_empno, v_ename, v_job_type,v_pay);
    end loop;
    commit;
end;

-------------------------------------------------------------------------------
--실행방법

BEGIN
  proc_calculate_salary;
END;

select * from salary_result;
--------------------------------------------------------------------------------
/*
커서를 프로젝트 반영 한다면 (어떤 상황에 쓰면 좋을 것 같다)
1조 핸드폰 요금 처리 , 카드요금  (사용내용도 , 결제)
2조 배민 같은 경우 고객의 구매이력 취합에 대한 고객 등급 갱신 ...
3조 회원에 대한 접속에 대한 정보를 커서를 확인하고 ... 별도의 처리 ... 
4조 할인율을 적용하는 테이블있고 ..... 상품테이블 상품 할일율을 적용 매주 금요일
5조
*/
--------------------------------------------------------------------------------

select * from cursor_table order by deptno;


--PL-SQL 트랜잭션 및 예외 처리하기
 DECLARE
    v_ename emp.ename%TYPE := '&p_ename';
    v_err_code NUMBER;
    v_err_msg VARCHAR2(255);
    BEGIN
          DELETE emp WHERE ename = v_ename;
          
          IF SQL%NOTFOUND THEN
                 RAISE_APPLICATION_ERROR(-20002,'my no data found'); --사용자가 예외 만들기 (강제 예외 던지기)
          END IF;
       EXCEPTION 
        WHEN OTHERS THEN
            ROLLBACK;
              v_err_code := SQLCODE;
              v_err_msg := SQLERRM;
              DBMS_OUTPUT.PUT_LINE('에러 번호 : ' || TO_CHAR(v_err_code));
              DBMS_OUTPUT.PUT_LINE('에러 내용 : ' || v_err_msg);
      END;
        
rollback;        
select* from emp where ename ='JONES';        
        
select * from c_dept;
select *from SYS.USER_CONSTRAINTS where TABLE_NAME ='C_DEPT';

select * from c_emp;
select *from SYS.USER_CONSTRAINTS where TABLE_NAME ='C_EMP';        
select * from emp where ename ='KING';

delete from c_dept where deptno=300;


delete from emp where ename='aaa';
--------------------------------------------------------------------------------
--지금까지 만들었는 작업이 영속적으로 저장 되지 않았다
--crerate table , create view 
--내가 위에서 만든 [커서]를 영속적으로 저장 (객체)
--객체 형태로 저장 해놓으면 그 다음번에 코딩하지 않고 [불러 사용]

--Oracle : subprogram(procedure)
--Ms-sql : procedure

--자주 사용되는 쿼리를 모듈화 시켜서 객체로 저장하고
--필요한 시점에 불러(호출) 해서 사용하겠다

--sp
--usp

create or replace procedure usp_emplist   --create or replace (생성 가능 , 수정 가능)
is
  BEGIN
    update emp
    set job = 'TTT'
    where deptno=30;
  END;

--실행방법
execute usp_emplist;

select * from emp where deptno=30;
rollback;

--MS (c# , vb.net - ms-sql) : 모든 90% 으로 프로시져 ..
--JAVA 활용할 수 있는 프레임워크 

--procedure  장점
--기존 : APP(emp.java > select .... 구문)    ->네트워크 > DB연결 > selet... > DB에
--지금 : APP(emp.java > usp_emplist 구문)    ->네트워크 > DB연결 > usp_emplist > DB에

--1.장점 : 네트워크 트래픽 감소(시간 단축)
--2.장점 : 보안 (네트워크 상에서 ...보안 요소)해결


--procedure 
--parameter  사용가능
--종류 : INPUT  , OUTPUT
create or replace procedure usp_update_emp
(vempno emp.empno%TYPE)
is
  BEGIN
    update emp
    set sal = 0
    where empno = vempno;
    
  END;
--실행방법
exec usp_update_emp(7788);

select * from emp where empno = 7788;
rollback;

--------------------------------------------------------------------------------
create or replace procedure usp_getemplist
(vempno emp.empno%TYPE)
is
  --내부에서 사용하는 변수
  vname emp.ename%TYPE;
  vsal  emp.sal%TYPE;
  BEGIN
      select ename, sal
        into vname , vsal
      from emp
      where empno=vempno;
      
      DBMS_OUTPUT.put_line('이름은 : ' || vname);
      DBMS_OUTPUT.put_line('급여는 : ' || vsal);
  END;

exec usp_getemplist(7902);
exec usp_getemplist(7788);
--------------------------------------------------------------------------------
-- procedure  는 parameter  종류 2가지
--1. input paramter : 사용시 반드시  입력          (IN : 생략하는 default)
--2. output parmater : 사용시 입력값을 받지 않아요 (OUT)
create or replace procedure app_get_emplist
(
  vempno IN emp.empno%TYPE,
  vename OUT emp.ename%TYPE,
  vsal   OUT emp.sal%TYPE
)
is
  BEGIN
    select ename, sal
      into vename , vsal
    from emp
    where empno=vempno;
  END;

--오라클 실행 테스트
DECLARE
  out_ename emp.ename%TYPE;
  out_sal   emp.sal%TYPE;
BEGIN
   app_get_emplist(7902,out_ename,out_sal);
   DBMS_OUTPUT.put_line('출력값 : ' || out_ename || '-' || out_sal);
END;




--오후에 자바 코드에서 실습 하겠습니다 
CREATE OR REPLACE PROCEDURE usp_EmpList
(
  p_sal IN number,
  p_cursor OUT SYS_REFCURSOR --APP 사용하기 위한 타입 (한건이상의 데이터 select 내부적으로 cursor 사용
)
IS
 BEGIN
     OPEN p_cursor
     FOR  SELECT empno, ename, sal FROM EMP WHERE sal > p_sal;
  END;


create table usp_emp
as
    select * from emp;

alter table usp_emp
add constraint pk_usp_emp_empno primary key(empno);

select * from SYS.USER_CONSTRAINTS where table_name='USP_EMP';



CREATE OR REPLACE PROCEDURE usp_insert_emp
(
 vempno IN emp.empno%TYPE,
 vename IN emp.ename%TYPE,
 vjob   IN emp.job%TYPE,
 p_outmsg OUT VARCHAR2
 )
 IS
   BEGIN
      INSERT INTO USP_EMP(empno , ename, job) VALUES(vempno ,vename , vjob);
      COMMIT;
        p_outmsg := 'success';
        EXCEPTION WHEN OTHERS THEN
        p_outmsg := SQLERRM;
        ROLLBACK;
    END;

DECLARE
  out_msg varchar2(200);
BEGIN
   usp_insert_emp(2000,'홍길동','IT',out_msg);
   DBMS_OUTPUT.put_line('출력값 : ' || out_msg);
END;

select * from usp_emp;
--------------------------------------------------------------------------------
create or replace procedure usp_EmpList
(
  p_sal IN number,
  p_cursor OUT SYS_REFCURSOR -- APP 사용하기 타입
)
is
    begin
        open p_cursor 
        for select empno, ename ,sal from emp where sal > p_sal;
    end;



    var out_cursor REFCURSOR
    exec usp_EmpList(2000,:out_cursor)
    print out_cursor;

--------------------------------------------------------------------------------
create or replace procedure usp_Insert_Emp
(
   vempno IN emp.empno%TYPE,
   vename IN emp.ename%TYPE,
   vjob   IN emp.job%TYPE,
   p_outmsg OUT varchar2
)
is 
    begin
         insert into emp(empno,ename,job) values(vempno,vename,vjob);
         commit;
         p_outmsg :='success'; --할당은 이모티콘 :=
         EXCEPTION WHEN OtHERS THEN
         p_outmsg :=SQLERRM;
         rollback;
    end;
    
  alter table emp
  add constraint pk_emp_empno primary key(empno);

  select * from user_constraints where table_name ='EMP';

-----------------------------------
--매일 아침 10시에 실행되는 프로시저를 만들어 주세요
--프로시저의 내용은 emp 테이블에서 한건 데이터를 넣는 프로시저...

--create proc...
--매일 오후 12시 35분에 실행되는 프로시저 점심시간입니다..화면출력

CREATE OR REPLACE PROCEDURE lunch_time
IS
BEGIN
    insert into emp(empno) values(9999);
END;

BEGIN
    DBMS_SCHEDULER.CREATE_JOB(
        job_name        => 'LUNCH_TIME_JOB',
        job_type        => 'STORED_PROCEDURE',
        job_action      => 'LUNCH_TIME',
        start_date      => SYSTIMESTAMP,
        repeat_interval => 'FREQ=DAILY;BYHOUR=14;BYMINUTE=4;BYSECOND=0',
        enabled         => TRUE,
        comments        => '매일 오후 14시 4분 점심시간 알림'
    );
END;

SELECT job_name,
       status,
       TO_CHAR(actual_start_date, 'YYYY-MM-DD HH24:MI:SS') AS 실행시간,
       run_duration,
       error#,
       additional_info
FROM user_scheduler_job_run_details
WHERE job_name = 'LUNCH_TIME_JOB'
ORDER BY actual_start_date DESC;


---------------------기본 procedure END-----------------------------------------

/*
select    5
from      1
where     2
group by  3
having    4
order by  6

함수  + join
*/

--[사용자 정의 함수]
--to_char() , sum() 오라클에서 제공
--사용자가 직접 필요한 함수를 만들어 사용가능
--사용방법은 다른 함수사용법과 동일
--사용자 정의 함수 paramter  정의 , return 값
create or replace function f_max_sal
(s_deptno emp.deptno%TYPE)
return number   -- public int f_max_sal(int deptno) {  return 10}
is
  max_sal emp.sal%TYPE;
BEGIN
      select max(sal)
          into max_sal
      from emp
      where deptno = s_deptno;
      return max_sal;
END;

---
select * from emp where sal = f_max_sal(10);

select max(sal) , f_max_sal(30) from emp;
--
create or replace function f_callname
(vempno emp.empno%TYPE)
return varchar2 -- public String f_callname() {  String  v_name; return "홍길동"}
is
  v_name emp.ename%TYPE;
BEGIN
    select ename || '님'
      into v_name
    from emp
    where empno=vempno;
    return v_name;
END;

select f_callname(7788) from dual;

select empno, ename , f_callname(7788) , sal
from emp
where empno=7788;

select empno, ename , f_callname(empno) , sal
from emp
where empno=7788;

--함수 
--parmater  사번을 입력받아서 사번에 해당되는 부서이름을 리턴하는 함수
create or replace function f_get_dname
(vempno emp.empno%TYPE)
return varchar2
is
    v_dname dept.dname%TYPE;
  BEGIN
    select dname
      into v_dname
    from dept
    where deptno = (select deptno from emp where empno=vempno);
    return v_dname;
  END;

select empno , ename ,deptno, f_get_dname(empno)
from emp 
where empno=7788;
--------------------------function END------------------------------------------

--자기 부서의 평균월급다 더 많은 월급을 받는 사원의 사번 , 이름 , 부서번호 , 
--부서별 평균월급을 출력하세요
--만약에 부서와 부서별 평균월급 담고 있는 테이블 제공

---------------------------------------------------------------------------
SELECT * FROM emp;
delete from emp where empno=9999;
commit;

select e.empno, e.ename, e.deptno, e.sal, s.avgsal
from emp e join (select deptno, trunc(avg(sal), 0) as avgsal from emp group by deptno) s
on e.deptno = s.deptno
where e.sal > s.avgsal;

--[트리거 : Trigger]
--트리거(trigger)의 사전적인 의미는 방아쇠나 (방아쇠를) 쏘다, 발사하다,
--(사건을) 유발시키다라는 의미가 있다.
 
--[입고]    [재고]     [출고]
 
--입고 INSERT (내부적으로 [트랜잭션]이 동작)
--재고 INSERT
--위험부담 : lock
 
 
--PL/SQL에서의 트리거 역시 방아쇠가 당겨지면 자동으로 총알이 발사되듯이
--어떠한 이벤트가 발생하면 그에 따라 다른 작업이 자동으로 처리되는 것을 의미한다.
/*
트리거란 특정 테이블의 데이터에 변경이 가해졌을 때 자동으로 수행되는
[저장 프로시저]라고 할 수 있다.
앞서 배운 저장 프로시저는 필요할 때마다 사용자가 직접
 EXECUTE 명령어로 호출해야 했다.
하지만 트리거는 이와 달리 테이블의
데이터가 INSERT, UPDATE, DELETE 문에 의해 변경되어질 때
[ 자동으로 수행되므로 이 기능을 이용하며 여러 가지 작업 ] 을 할 수 있다.
이런 이유로 트리거를 사용자가 직접 실행시킬 수는 없다.
 
 --자동(insert, update ,delete)이벤트가 발생하면  자동으로  실행되는 procedure ...
 
 
 
--BEFORE : 테이블에서 DML 실행되기 전에 트리거가 동작
--AFTER :  테이블에서 DML 실행후에 트리거 동작
 
Syntax
CREATE [OR REPLACE] TRIGGER trigger_name
{BEFORE | AFTER} triggering_event [OF column1, . . .] ON table_name
[FOR EACH ROW [WHEN trigger_condition]
trigger_body;
 
trigger_name TRIGGER 의 식별자
  BEFORE | AFTER DML 문장이 실행되기 전에 TRIGGER 를 실행할 것인지 실행된
  후에 TRIGGER 를 실행할 것인지를 정의
triggering_event 
TRIGGER 를 실행하는 DML(INSERT,UPDATE,DELETE)문을 기술한다.
 
OF column TRIGGER 가 실행되는 테이블에서 COLUMN 명을 기술한다.
 
table_name TRIGGER 가 실행되는 테이블 이름
 
FOR EACH ROW 이 옵션을 사용하면 
행 레벨 트리거가 되어 triggering 문장
에 의해 영향받은 행에 대해 각각 한번씩 실행하고 사용하지
않으면 문장 레벨 트리거가 되어 DML 문장 당 한번만 실행된다.
 
 
  TRIGGER 에서 OLD 와 NEW
    행 레벨 TRIGGER 에서만 사용할 수 있는 예약어로 트리거 내에서 현재 처리되고 있는 행
    을 액세스할 수 있다. 즉 두개의 의사 레코드를 통하여 이 작업을 수행할 수 있다. :OLD
    는 INSERT 문에 의해 정의되지 않고 :NEW 는 DELETE 에 대해 정의되지 않는다. 그러나
    UPDATE 는 :OLD 와 :NEW 를 모두 정의한다. 아래의 표는 OLD 와 NEW 값을 정의한 표이다. 
    문장 :OLD :NEW
    INSERT 모든 필드는 NULL 로 정의 문장이 완전할 때 삽입된 새로운 값
    UPDATE 갱신하기 전의 원래 값 문장이 완전할 때 갱신된 새로운 값
    DELETE 행이 삭제되기 전의 원래 값 모든 필드는 NULL 이다.
 
DROP TRIGGER 명령어로 트리거를 삭제할 수 있고 TRIGGER 를 잠시 disable 할 수 있다.
DROP TRIGGER trigger_name;
ALTER TRIGGER trigger_name {DISABLE | ENABLE};
TRIGGER 와 DATA DICTIONARY
TRIGGER 가 생성될 때 소스 코드는 데이터 사전 VIEW 인 user_triggers 에 저장된다. 이
VIEW 는 TRIGGER_BODY, WHERE 절, 트리거링 테이블, TRIGGER 타입을 포함 한다.
 
*/
create table tri_emp
as
  select empno , ename from emp where 1=2;


select * from tri_emp;

create or replace trigger tri_01
after insert on tri_emp
BEGIN -- 자동 동작할 내용
    DBMS_OUTPUT.PUT_LINE('신입사원 입사');
END;

insert into tri_emp(empno,ename) values(100,'홍길동');
select * from tri_emp;



create or replace trigger tri_02
after update on tri_emp
BEGIN
  DBMS_OUTPUT.PUT_LINE('신입사원 수정');
END;


--테이블에 trigger 정보
select * from user_triggers where table_name='TRI_EMP';


insert into tri_emp values(100,'김유신');

update tri_emp
set ename='아하'
where empno=100;


--delete 트리거 : tri_emp
--사원테이블 삭제 (화면 출력)
create or replace trigger tri_03
after delete on tri_emp
BEGIN
  DBMS_OUTPUT.PUT_LINE('신입사원 삭제');
END;

insert into tri_emp values(200,'홍길동');
update tri_emp set ename='변경' where empno= 200;
delete from tri_emp where empno=200;
-----------------------------------------------------------------------------
--예제1)

--테이블에 INSERT, UPDATE, DELETE 를 할 때 user, 구분(I,U,D), sysdate 를 기록하는 
--테이블(emp_audit)에 내용을 저장한다.
--FOR EACH ROW 이 옵션을 사용하면 
--행 레벨 트리거가 되어 triggering 문장
--에 의해 영향받은 행에 대해 각각 한번씩 실행하고 사용하지
--않으면 문장 레벨 트리거가 되어 DML 문장 당 한번만 실행된다.


drop sequence emp_audit_tr;
drop table emp_audit;


create sequence emp_audit_tr
 increment by 1
 start with 1
 maxvalue 999999
 minvalue 1
 nocycle
 nocache;

create table emp_audit(
 e_id number(6) constraint emp_audit_pk primary key,
 e_name varchar2(30),
 e_gubun varchar2(10),
 e_date date);


drop table emp2;
create table emp2
as
    select * from emp;

--------------------------------------------------------------------------------    
CREATE SEQUENCE emp_audit_seq
START WITH 1
INCREMENT BY 1
NOCACHE;

create or replace trigger emp_audit_tr
 after insert or update or delete on emp2
 --for each row
begin
 if inserting then
      insert into emp_audit
      values(emp_audit_seq.nextval, user, 'inserting', sysdate);
 elsif updating then
      insert into emp_audit
      values(emp_audit_seq.nextval, user, 'updating', sysdate);
 elsif deleting then
      insert into emp_audit
      values(emp_audit_seq.nextval, user, 'deleting', sysdate);
 end if;
end;

-- for each row 선언 안했을 때 (명령어 한 번에 대하여 한 건으로 기록된다.)
select * from emp2;
rollback;
update emp2 
set deptno = 20
where deptno = 10;

select * from emp_audit;

delete from emp2 where deptno = 20;

select * from emp_audit;

rollback;

-- for each row 선언 했을 때(명령어 한 번에 변경된 행만큼 기록된다.)
create or replace trigger emp_audit_tr
 after insert or update or delete on emp2
 for each row
begin
 if inserting then
      insert into emp_audit
      values(emp_audit_tr.nextval, user, 'inserting', sysdate);
 elsif updating then
      insert into emp_audit
      values(emp_audit_tr.nextval, user, 'updating', sysdate);
 elsif deleting then
      insert into emp_audit
      values(emp_audit_tr.nextval, user, 'deleting', sysdate);
 end if;
end;

select * from emp2;
update emp2 set deptno = 20 where deptno = 10;

select * from emp_audit;


--------------------------------------------------------------------

--INSERT, UPDATE, DELETE로 변경되는 내용에 대하여 전/후 데이터를 기록한다.
--OLD , NEW
--INSERTED , DELETED 다른 DB

drop table emp_audit;

create table emp_audit (
 id number(6) constraint emp_audit_pk primary key,
 name varchar2(30),
 gubun varchar2(10),
 wdate date,
 etc1 varchar2(20),  -- old
 etc2 varchar2(20)   -- new
);


create or replace trigger emp_audit_tr
 after insert or update or delete on emp2
 for each row
begin
 if inserting then
       insert into emp_audit
       values(emp_audit_tr.nextval, user, 'inserting', sysdate, :old.deptno, :new.deptno);
 elsif updating then
        insert into emp_audit
        values(emp_audit_tr.nextval, user, 'updating', sysdate, :old.deptno, :new.deptno);
 elsif deleting then
        insert into emp_audit
        values(emp_audit_tr.nextval, user, 'deleting', sysdate, :old.deptno, :new.deptno);
 end if;
end;


select * from emp2;


--insert
insert into emp2(empno,ename,deptno) values (9999,'홍길동',50);
select * from emp_audit;

--update
update emp2 set deptno=10
where empno=9999;
select * from emp_audit;

--delete
delete from emp2 where empno=9999;
select * from emp_audit;

--------------------------------------------------------------------------------
--트리거의 활용
create table tri_order
(
  no number,
  ord_code varchar2(10),
  ord_date date
);

--before 트리거의 동작시점이 실제 tri_order 테이블 insert 되기 전에
--트리거 먼저 동작 그 이후 insert 작업
create or replace trigger trigger_order
before insert on tri_order
BEGIN
  IF(to_char(sysdate,'HH24:MM') not between '11:00' and '16:00') THEN
     RAISE_APPLICATION_ERROR(-20002, '허용시간 오류 쉬세요');
  END IF;
END;

insert into tri_order values(1,'notebook',sysdate);
select * from tri_order;
commit;
rollback;
--트리거 삭제
drop trigger trigger_order;




--POINT
--PL_SQL 두개의 가상데이터(테이블) 제공
--:OLD > 트리거가 처리한 레코드의 원래 값을 저장 (ms-sql (deleted)
--:NEW > 새값을 포함                          (ms-sql (inserted)

create or replace trigger tri_order2
before insert on tri_order
for each row
BEGIN
  IF(:NEW.ord_code) not in('desktop') THEN
     RAISE_APPLICATION_ERROR(-20002, '제품코드 오류');
  END IF;
END;

select * from tri_order;

--오류 (desktop)
insert into tri_order values(200,'notebook',sysdate);

insert into tri_order values(200,'desktop',sysdate);

select * from tri_order;
commit;

--------------------------------------------------------------------------------
--입고 , 재고

create table t_01 --입고
(
  no number,
  pname varchar2(20)
);

create table t_02 --재고
(
  no number,
  pname varchar2(20)
);

--입고 데이터 들어오면 같은 데이터를 재고 입력
create or replace trigger insert_t_01
after insert on t_01
for each row
BEGIN
  insert into t_02(no, pname)
  values(:NEW.no ,:NEW.pname);
END;

--입고
insert into t_01 values(1,'notebook');

select * from t_01;
select * from t_02;

-- 입고 제품이 변경 (재고 변경)
create or replace trigger update_t_01
after update on t_01
for each row
BEGIN
  update t_02
  set pname = :NEW.pname
  where no = :OLD.no;
END;

update t_01
set pname = 'notebook2'
where no = 1;

select * from t_01;

select * from t_02;

--delete 트리거 만들어 보세요 
--입고 데이터 delete from t_01 where no =1 삭제 되면 재고 삭제
create or replace trigger delete_tri_01
after delete on t_01
for each row
BEGIN
  delete from t_02
  where no=:OLD.no;
END;

delete from t_01 where no=1;

select* from t_01;
select* from t_02;

commit;

---------------------------------------------
--휴면회원 자동 전환 (매일 새벽 2시)
CREATE TABLE members(
  member_id   NUMBER PRIMARY KEY,
  name        VARCHAR2(30),
  email       VARCHAR2(50),
  status      VARCHAR2(10) DEFAULT 'ACTIVE',
  last_login  DATE
);

CREATE TABLE dormant_log(
  log_id        NUMBER PRIMARY KEY,
  member_id     NUMBER,
  name          VARCHAR2(30),
  dormant_date  DATE
);

CREATE SEQUENCE dormant_log_seq NOCACHE;

INSERT INTO members VALUES(1, '홍길동', 'hong@test.com', 'ACTIVE', ADD_MONTHS(SYSDATE, -13));
INSERT INTO members VALUES(2, '김철수', 'kim@test.com',  'ACTIVE', ADD_MONTHS(SYSDATE, -2));
INSERT INTO members VALUES(3, '이영희', 'lee@test.com',  'ACTIVE', ADD_MONTHS(SYSDATE, -15));
COMMIT;

CREATE OR REPLACE PROCEDURE dormant_proc IS
  CURSOR dormant_cur IS
    SELECT member_id, name
    FROM members
    WHERE status = 'ACTIVE'
      AND last_login < ADD_MONTHS(SYSDATE, -12)
    FOR UPDATE;
  v_cnt NUMBER := 0;
BEGIN
  FOR r IN dormant_cur LOOP
    UPDATE members
    SET status = 'DORMANT'
    WHERE CURRENT OF dormant_cur;

    INSERT INTO dormant_log
    VALUES(dormant_log_seq.NEXTVAL, r.member_id, r.name, SYSDATE);

    v_cnt := v_cnt + 1;
  END LOOP;
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('휴면 전환 건수 : ' || v_cnt);
END;


BEGIN
  DBMS_SCHEDULER.CREATE_JOB(
    job_name        => 'DORMANT_JOB',
    job_type        => 'STORED_PROCEDURE',
    job_action      => 'DORMANT_PROC',
    start_date      => SYSTIMESTAMP,
    repeat_interval => 'FREQ=DAILY; BYHOUR=2; BYMINUTE=0; BYSECOND=0',
    enabled         => TRUE);
END;

--확인
EXEC DBMS_SCHEDULER.RUN_JOB('DORMANT_JOB');
SELECT * FROM members;       -- 1번, 3번 회원이 DORMANT
SELECT * FROM dormant_log;

--전일 매출 마감 집계
CREATE TABLE sales_orders(
  order_id    NUMBER PRIMARY KEY,
  order_date  DATE,
  product_id  VARCHAR2(10),
  amount      NUMBER,
  status      VARCHAR2(10)        -- PAID, CANCEL
);

CREATE TABLE daily_sales(
  sales_date    DATE,
  product_id    VARCHAR2(10),
  order_cnt     NUMBER,
  total_amount  NUMBER,
  CONSTRAINT daily_sales_pk PRIMARY KEY(sales_date, product_id)
);

INSERT INTO sales_orders VALUES(1, SYSDATE-1, 'P001', 30000, 'PAID');
INSERT INTO sales_orders VALUES(2, SYSDATE-1, 'P001', 30000, 'PAID');
INSERT INTO sales_orders VALUES(3, SYSDATE-1, 'P002', 15000, 'PAID');
INSERT INTO sales_orders VALUES(4, SYSDATE-1, 'P002', 15000, 'CANCEL');
INSERT INTO sales_orders VALUES(5, SYSDATE,   'P001', 30000, 'PAID');
COMMIT;

CREATE OR REPLACE PROCEDURE daily_sales_proc IS
  v_date DATE := TRUNC(SYSDATE) - 1;

  CURSOR sales_cur IS
    SELECT product_id, COUNT(*) AS cnt, SUM(amount) AS total
    FROM sales_orders
    WHERE order_date >= v_date
      AND order_date <  v_date + 1
      AND status = 'PAID'
    GROUP BY product_id;
BEGIN
  DELETE FROM daily_sales WHERE sales_date = v_date;   -- 재실행 대비

  FOR r IN sales_cur LOOP
    INSERT INTO daily_sales
    VALUES(v_date, r.product_id, r.cnt, r.total);
  END LOOP;
  COMMIT;
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    RAISE;
END;


BEGIN
  DBMS_SCHEDULER.CREATE_JOB(
    job_name        => 'DAILY_SALES_JOB',
    job_type        => 'STORED_PROCEDURE',
    job_action      => 'DAILY_SALES_PROC',
    start_date      => SYSTIMESTAMP,
    repeat_interval => 'FREQ=DAILY; BYHOUR=0; BYMINUTE=30; BYSECOND=0',
    enabled         => TRUE);
END;

--확인
EXEC DBMS_SCHEDULER.RUN_JOB('DAILY_SALES_JOB');
SELECT * FROM daily_sales;   -- P001 2건 60000, P002 1건 15000

--회원정보 변경·삭제 이력 보관
CREATE TABLE members_hist(
  hist_id      NUMBER PRIMARY KEY,
  member_id    NUMBER,
  name         VARCHAR2(30),
  email        VARCHAR2(50),
  status       VARCHAR2(10),
  dml_type     VARCHAR2(10),
  changed_by   VARCHAR2(30),
  changed_at   DATE
);

CREATE SEQUENCE members_hist_seq NOCACHE;

CREATE OR REPLACE TRIGGER members_hist_tr
  AFTER UPDATE OR DELETE ON members
  FOR EACH ROW
DECLARE
  v_type VARCHAR2(10);
BEGIN
  IF UPDATING THEN
    v_type := 'UPDATE';
  ELSE
    v_type := 'DELETE';
  END IF;

  INSERT INTO members_hist
  VALUES(members_hist_seq.NEXTVAL, :OLD.member_id, :OLD.name, :OLD.email,
         :OLD.status, v_type, USER, SYSDATE);
END;

--확인
UPDATE members SET email = 'kim_new@test.com' WHERE member_id = 2;
DELETE FROM members WHERE member_id = 3;
SELECT * FROM members_hist;   -- 변경 전 이메일, 삭제된 회원 정보가 남아 있음

--게시판 PK 자동 채번 + 등록·수정 정보 자동 입력
CREATE TABLE board(
  board_id    NUMBER PRIMARY KEY,
  title       VARCHAR2(100),
  content     VARCHAR2(4000),
  created_by  VARCHAR2(30),
  created_at  DATE,
  updated_by  VARCHAR2(30),
  updated_at  DATE
);

CREATE SEQUENCE board_seq NOCACHE;

CREATE OR REPLACE TRIGGER board_audit_tr
  BEFORE INSERT OR UPDATE ON board
  FOR EACH ROW
BEGIN
  IF INSERTING THEN
    IF :NEW.board_id IS NULL THEN
      :NEW.board_id := board_seq.NEXTVAL;
    END IF;
    :NEW.created_by := USER;
    :NEW.created_at := SYSDATE;
  END IF;

  :NEW.updated_by := USER;
  :NEW.updated_at :=
 SYSDATE;
END;

--확인
INSERT INTO board(title, content) VALUES('첫 글', '안녕하세요');   -- 번호, 작성자, 일시가 자동으로 들어감
UPDATE board SET title = '첫 글(수정)' WHERE board_id = 1;          -- 수정자, 수정일이 자동으로 갱신됨
SELECT * FROM board;