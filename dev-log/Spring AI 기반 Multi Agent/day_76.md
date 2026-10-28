# 76일차

## 데이터베이스 (Database Design): PL/SQL 기본 구문, 커서, 프로시저, 사용자 정의 함수, 트리거, 잡 스케줄러

📌 학습일 : 2026.10.28

📌 학습 내용 : PL/SQL, DECLARE / BEGIN / EXCEPTION, DBMS_OUTPUT, SELECT INTO, 치환 변수(&), %TYPE, %ROWTYPE, 시퀀스, IF / ELSIF, CASE, Basic LOOP, WHILE, FOR, CONTINUE, 예외 처리, NO_DATA_FOUND, TOO_MANY_ROWS, RAISE_APPLICATION_ERROR, SQLCODE, SQLERRM, 암시적 커서 속성, 명시적 커서, 커서 FOR LOOP, FOR UPDATE / WHERE CURRENT OF, 저장 프로시저, IN / OUT 파라미터, SYS_REFCURSOR, 사용자 정의 함수, 트리거, BEFORE / AFTER, 문장 레벨 / 행 레벨 트리거, :OLD / :NEW, DBMS_SCHEDULER, user_scheduler_job_run_details

---

#### 1. PL/SQL 기본 구조

PL/SQL은 Oracle's Procedural Language extension to SQL의 약자로, SQL에 변수 정의, 조건 처리(IF), 반복 처리(LOOP, WHILE, FOR)를 더한 오라클 내장 절차형 언어다.

| 구분 | 키워드 | 내용 |
| --- | --- | --- |
| 선언부 | `DECLARE` | 변수, 상수, 커서 선언 (선택) |
| 실행부 | `BEGIN ~ END` | 값 할당, SQL 실행, 제어문 (필수) |
| 예외부 | `EXCEPTION` | 에러 발생 시 처리 (선택) |

```sql
DECLARE
  vno   number(4);
  vname varchar2(20);
BEGIN
  vno   := 100;                         -- 할당은 :=
  vname := 'kglim';
  DBMS_OUTPUT.PUT_LINE(vname || '입니다');   -- Java의 System.out.println()
END;
```

- **DBMS_OUTPUT 결과 확인:** 보기 → DBMS 출력 창 → `+` 버튼으로 접속을 연결해야 출력이 보인다.
- **SELECT INTO:** PL/SQL 안의 SELECT는 결과를 반드시 `INTO 변수`에 담아야 하며, 결과는 정확히 1행이어야 한다.
- **치환 변수 `&`:** `where empno=&empno`처럼 쓰면 실행할 때 값을 입력받는다 (Java의 Scanner 역할).

#### 2. 변수와 타입

| 선언 방법 | 예 | 의미 |
| --- | --- | --- |
| 직접 지정 | `v_empno number(10)` | 타입과 길이를 직접 지정 |
| `%TYPE` | `v_empno emp.empno%TYPE` | emp 테이블 empno 컬럼과 같은 타입 |
| `%ROWTYPE` | `v_row emp%ROWTYPE` | emp 테이블 한 행 전체 구조, `v_row.ename`처럼 사용 |
| 초기값 | `v_count number := 10` | 선언과 동시에 값 할당 |
| NOT NULL | `v_valid boolean not null := true` | NULL 불가, 초기값 필수 |

`%TYPE`과 `%ROWTYPE`을 쓰면 테이블 컬럼의 타입이나 길이가 바뀌어도 PL/SQL 코드를 고칠 필요가 없다.

```sql
DECLARE
  v_empno emp.empno%TYPE;
BEGIN
  select empno_seq.nextval into v_empno from dual;   -- 시퀀스 값을 변수에 담기
  insert into empdml(empno, ename) values(v_empno, '홍길동');
  commit;
END;
```

#### 3. 제어문

| 구분 | 문법 | Java 비교 |
| --- | --- | --- |
| 조건문 | `IF 조건 THEN ~ ELSIF 조건 THEN ~ ELSE ~ END IF;` | `if ~ else if ~ else` (ELSEIF가 아니라 **ELSIF**) |
| CASE | `CASE WHEN 조건 THEN 값 ... ELSE 값 END` | `switch` |
| Basic LOOP | `LOOP ~ EXIT WHEN 조건; END LOOP;` | `do ~ while` |
| WHILE | `WHILE 조건 LOOP ~ END LOOP;` | `while` |
| FOR | `FOR i IN 1..100 LOOP ~ END LOOP;` | `for(int i=1; i<=100; i++)` |
| CONTINUE | `CONTINUE WHEN 조건;` | `continue` (11g부터 지원) |

```sql
DECLARE
  total number := 0;
BEGIN
  FOR i IN 1..100 LOOP
    total := total + i;
  END LOOP;
  DBMS_OUTPUT.PUT_LINE('1~100 총합 : ' || total);   -- 5050
END;
```

#### 4. 예외 처리

| 예외 | 발생 상황 |
| --- | --- |
| `NO_DATA_FOUND` | SELECT INTO 결과가 0행 |
| `TOO_MANY_ROWS` | SELECT INTO 결과가 2행 이상 (동명이인 등) |
| `OTHERS` | 위에서 처리하지 않은 모든 예외 |
| `RAISE_APPLICATION_ERROR(-20000 ~ -20999, '메시지')` | 사용자가 직접 예외를 만들어 강제로 발생 |

```sql
DECLARE
  v_ename emp.ename%TYPE := '&p_ename';
BEGIN
  DELETE emp WHERE ename = v_ename;
  IF SQL%NOTFOUND THEN
    RAISE_APPLICATION_ERROR(-20002, 'my no data found');
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('에러 번호 : ' || SQLCODE);
    DBMS_OUTPUT.PUT_LINE('에러 내용 : ' || SQLERRM);
END;
```

`SQLCODE`는 에러 번호, `SQLERRM`은 에러 메시지를 돌려주며, 예외가 나면 `ROLLBACK`으로 작업을 되돌린다.

#### 5. 커서 (CURSOR)

지금까지의 SQL은 전체 행을 한 번에 처리하는 **집합 단위** 처리였다면, 커서는 여러 행을 **한 행씩** 꺼내 조건에 따라 다르게 처리할 때 사용한다.

**암시적 커서 속성 (가장 최근 SQL 문장 기준)**

| 속성 | 의미 |
| --- | --- |
| `SQL%ROWCOUNT` | 영향을 받은 행의 수 |
| `SQL%FOUND` | 1행 이상 영향을 받았으면 TRUE |
| `SQL%NOTFOUND` | 영향을 받은 행이 없으면 TRUE |
| `SQL%ISOPEN` | 실행 후 바로 닫히므로 항상 FALSE |

**명시적 커서 vs 커서 FOR LOOP**

| 방식 | 흐름 | 특징 |
| --- | --- | --- |
| 명시적 커서 | `OPEN` → `FETCH INTO` → `EXIT WHEN %NOTFOUND` → `CLOSE` | 열고, 읽고, 닫는 과정을 직접 제어 |
| 커서 FOR LOOP | `FOR rec IN 커서 LOOP ~ END LOOP;` | OPEN, FETCH, CLOSE를 자동 처리 (Java의 향상된 for문) |

**실습 : 직종별 급여 계산 (건설회사 salary_info)**

| 직종 | 급여 계산 방식 |
| --- | --- |
| 정규직 | 월급 그대로 |
| 시간직 | 시간 × 시간급 |
| 일용직 | 시간급 + 식대 |

```sql
declare
   cursor sal_cursor is
   select empno, ename, job_type, monthly, hours, hourly, meal from salary_info;
   v_empno salary_info.empno%TYPE;
   ...
   v_pay number;
begin
    open sal_cursor;
    loop
        fetch sal_cursor into v_empno, v_ename, v_job_type, v_monthly, v_hours, v_hourly, v_meal;
        exit when sal_cursor%NOTFOUND;
        if v_job_type = '정규직' then    v_pay := nvl(v_monthly,0);
        elsif v_job_type = '시간직' then v_pay := nvl(v_hours,0) * nvl(v_hourly,0);
        elsif v_job_type = '일용직' then v_pay := nvl(v_hourly,0) + nvl(v_meal,0);
        else v_pay := 0;
        end if;
        DBMS_OUTPUT.PUT_LINE('사번 :' || v_empno || ' 직종 : ' || v_job_type || ' 급여 : ' || v_pay);
    end loop;
    close sal_cursor;
end;
```

행마다 직종이라는 조건을 보고 계산 방법을 바꿔야 하기 때문에 집합 단위 SQL 한 문장으로는 어렵고, 커서로 한 행씩 처리해야 한다.

**조별 커서 활용 아이디어 :** 핸드폰·카드 요금 정산, 구매 이력에 따른 고객 등급 갱신, 접속 정보 기반 회원 처리, 매주 금요일 상품 할인율 적용

#### 6. 저장 프로시저 (Procedure)

블록으로 작성한 PL/SQL은 저장되지 않지만, 프로시저로 만들면 **객체로 DB에 저장**되어 필요할 때마다 이름으로 불러 쓸 수 있다.

| 장점 | 내용 |
| --- | --- |
| 네트워크 트래픽 감소 | 긴 SQL 대신 프로시저 이름만 전송 |
| 보안 | 네트워크에 SQL 구문이 그대로 노출되지 않음 |
| 재사용 | 자주 쓰는 쿼리를 모듈화 |

| 파라미터 | 의미 |
| --- | --- |
| `IN` (기본값, 생략 가능) | 호출할 때 값을 반드시 넘김 |
| `OUT` | 호출한 쪽에 값을 돌려줌 |
| `OUT SYS_REFCURSOR` | 여러 행의 SELECT 결과를 애플리케이션(Java)에 돌려줌 |

```sql
create or replace procedure app_get_emplist
(
  vempno IN  emp.empno%TYPE,
  vename OUT emp.ename%TYPE,
  vsal   OUT emp.sal%TYPE
)
is
BEGIN
  select ename, sal into vename, vsal from emp where empno = vempno;
END;

DECLARE
  out_ename emp.ename%TYPE;
  out_sal   emp.sal%TYPE;
BEGIN
  app_get_emplist(7902, out_ename, out_sal);
  DBMS_OUTPUT.put_line('출력값 : ' || out_ename || '-' || out_sal);
END;
```

```sql
-- 여러 행 결과 돌려주기
CREATE OR REPLACE PROCEDURE usp_EmpList
( p_sal IN number, p_cursor OUT SYS_REFCURSOR )
IS
BEGIN
  OPEN p_cursor FOR SELECT empno, ename, sal FROM emp WHERE sal > p_sal;
END;

var out_cursor REFCURSOR
exec usp_EmpList(2000, :out_cursor)
print out_cursor;
```

INSERT 프로시저에서는 성공하면 `p_outmsg := 'success'`, 실패하면 `EXCEPTION WHEN OTHERS THEN p_outmsg := SQLERRM; ROLLBACK;`으로 결과 메시지를 OUT 파라미터에 담아 돌려주었다.

실행은 `exec 프로시저명(값);` 또는 `BEGIN 프로시저명; END;`로 한다.

#### 7. 잡 스케줄러 (DBMS_SCHEDULER)

직종별 급여 계산 커서를 `proc_calculate_salary` 프로시저로 만들어 salary_result에 INSERT하고, 프로시저를 정해진 시간에 자동 실행하도록 잡을 등록했다.

```sql
BEGIN
  DBMS_SCHEDULER.CREATE_JOB(
    job_name        => 'LUNCH_TIME_JOB',
    job_type        => 'STORED_PROCEDURE',
    job_action      => 'LUNCH_TIME',
    start_date      => SYSTIMESTAMP,
    repeat_interval => 'FREQ=DAILY;BYHOUR=14;BYMINUTE=4;BYSECOND=0',
    enabled         => TRUE,
    comments        => '매일 오후 14시 4분 점심시간 알림');
END;
```

| 기능 | 방법 |
| --- | --- |
| 실행 주기 | `FREQ=DAILY / WEEKLY / MONTHLY` + `BYHOUR`, `BYMINUTE`, `BYMONTHDAY` |
| 시간 변경 | `DBMS_SCHEDULER.SET_ATTRIBUTE(name, 'repeat_interval', 값)` |
| 즉시 실행 | `DBMS_SCHEDULER.RUN_JOB('잡이름')` |
| 삭제 | `DBMS_SCHEDULER.DROP_JOB('잡이름', force => TRUE)` |
| 잡 상태 | `user_scheduler_jobs` (enabled, state, run_count, failure_count, next_run_date) |
| 실행 결과 | `user_scheduler_job_run_details` (status SUCCEEDED / FAILED, error#, additional_info) |

잡은 백그라운드 세션에서 실행되기 때문에 프로시저 안의 `DBMS_OUTPUT` 메시지는 화면에 나오지 않으며, 실행 여부는 테이블에 INSERT된 데이터나 `user_scheduler_job_run_details`로 확인해야 한다.

#### 8. 사용자 정의 함수 (Function)

`to_char()`, `sum()`처럼 오라클이 제공하는 함수 외에 필요한 함수를 직접 만들 수 있으며, 프로시저와 달리 **반드시 RETURN 값이 있고 SELECT 문 안에서 사용**할 수 있다.

```sql
create or replace function f_get_dname
(vempno emp.empno%TYPE)
return varchar2
is
  v_dname dept.dname%TYPE;
BEGIN
  select dname into v_dname
  from dept
  where deptno = (select deptno from emp where empno = vempno);
  return v_dname;
END;

select empno, ename, deptno, f_get_dname(empno) from emp where empno = 7788;
```

| 함수 | 기능 |
| --- | --- |
| `f_max_sal(deptno)` | 부서별 최고 급여 반환 → `where sal = f_max_sal(10)` |
| `f_callname(empno)` | 사원 이름 뒤에 '님'을 붙여 반환 |
| `f_get_dname(empno)` | 사번으로 부서 이름 반환 |

| 구분 | 프로시저 | 함수 |
| --- | --- | --- |
| 반환값 | 없음 (OUT 파라미터로 전달) | RETURN으로 반드시 1개 반환 |
| 호출 | `exec`, PL/SQL 블록 | SELECT, WHERE 등 SQL 문 안 |
| 주 용도 | 데이터 처리 작업 | 계산, 값 조회 |

#### 9. 트리거 (Trigger)

트리거는 테이블에 INSERT, UPDATE, DELETE가 일어나면 **자동으로 실행되는 저장 프로시저**로, 사용자가 직접 실행할 수 없다.

```sql
CREATE [OR REPLACE] TRIGGER 트리거명
{BEFORE | AFTER} {INSERT | UPDATE [OF 컬럼] | DELETE} ON 테이블명
[FOR EACH ROW [WHEN 조건]]
BEGIN ... END;
```

| 구분 | 의미 |
| --- | --- |
| BEFORE | DML 실행 **전**에 동작 (검사, 차단, 값 세팅) |
| AFTER | DML 실행 **후**에 동작 (이력 기록, 다른 테이블 반영) |
| 문장 레벨 (FOR EACH ROW 없음) | DML 한 문장에 1번 실행 |
| 행 레벨 (FOR EACH ROW) | 영향을 받은 행마다 1번씩 실행 |
| `INSERTING / UPDATING / DELETING` | 어떤 DML로 트리거가 실행됐는지 구분 |

**:OLD와 :NEW (행 레벨 트리거에서만 사용)**

| 문장 | :OLD | :NEW |
| --- | --- | --- |
| INSERT | NULL | 삽입된 새 값 |
| UPDATE | 갱신 전 원래 값 | 갱신된 새 값 |
| DELETE | 삭제 전 원래 값 | NULL |

MS-SQL에서는 :OLD가 `deleted`, :NEW가 `inserted` 테이블에 해당한다.

**실습 1 : 감사 로그 (emp2 → emp_audit)**

```sql
create or replace trigger emp_audit_tr
 after insert or update or delete on emp2
 for each row
begin
 if inserting then
      insert into emp_audit values(emp_audit_tr.nextval, user, 'inserting', sysdate, :old.deptno, :new.deptno);
 elsif updating then
      insert into emp_audit values(emp_audit_tr.nextval, user, 'updating', sysdate, :old.deptno, :new.deptno);
 elsif deleting then
      insert into emp_audit values(emp_audit_tr.nextval, user, 'deleting', sysdate, :old.deptno, :new.deptno);
 end if;
end;
```

`update emp2 set deptno = 20 where deptno = 10;`을 실행했을 때 FOR EACH ROW가 없으면 emp_audit에 1건만 쌓였고, FOR EACH ROW를 넣으면 바뀐 행 수만큼 쌓였다.

**실습 2 : BEFORE 트리거로 입력 차단 (tri_order)**

| 트리거 | 조건 | 결과 |
| --- | --- | --- |
| trigger_order (문장 레벨) | 현재 시간이 11:00 ~ 16:00이 아니면 | `RAISE_APPLICATION_ERROR(-20002, '허용시간 오류 쉬세요')` |
| tri_order2 (행 레벨) | `:NEW.ord_code`가 'desktop'이 아니면 | `RAISE_APPLICATION_ERROR(-20002, '제품코드 오류')` |

**실습 3 : 입고 → 재고 자동 반영 (t_01 → t_02)**

| 트리거 | 동작 |
| --- | --- |
| insert_t_01 | 입고(t_01) INSERT 시 `:NEW.no, :NEW.pname`으로 재고(t_02) INSERT |
| update_t_01 | 입고 제품명 변경 시 `where no = :OLD.no` 재고 UPDATE |
| delete_tri_01 | 입고 삭제 시 `where no = :OLD.no` 재고 DELETE |

트리거 정보는 `user_triggers`에서 확인하고, `DROP TRIGGER`로 삭제하거나 `ALTER TRIGGER 이름 DISABLE / ENABLE`로 잠시 끌 수 있다.

#### 10. 현업 활용 시나리오 (커서 + 프로시저 + 잡 / 트리거)

| 구분 | 시나리오 | 핵심 기술 |
| --- | --- | --- |
| 배치 | 휴면회원 자동 전환 (매일 새벽 2시, 1년 이상 미접속) | 커서 `FOR UPDATE` + `WHERE CURRENT OF`, dormant_log 기록 |
| 배치 | 전일 매출 마감 집계 (매일 0시 30분) | 어제 날짜 DELETE 후 INSERT로 재실행 시 중복 방지, `EXCEPTION`에서 ROLLBACK 후 RAISE |
| 트리거 | 회원정보 변경·삭제 이력 보관 | AFTER UPDATE OR DELETE, `:OLD` 값을 members_hist에 저장 |
| 트리거 | 게시판 PK 자동 채번 + 등록·수정 정보 자동 입력 | BEFORE INSERT OR UPDATE, `:NEW.board_id := board_seq.NEXTVAL` |

```sql
CREATE OR REPLACE PROCEDURE dormant_proc IS
  CURSOR dormant_cur IS
    SELECT member_id, name FROM members
    WHERE status = 'ACTIVE' AND last_login < ADD_MONTHS(SYSDATE, -12)
    FOR UPDATE;
BEGIN
  FOR r IN dormant_cur LOOP
    UPDATE members SET status = 'DORMANT' WHERE CURRENT OF dormant_cur;
    INSERT INTO dormant_log VALUES(dormant_log_seq.NEXTVAL, r.member_id, r.name, SYSDATE);
  END LOOP;
  COMMIT;
END;
```

- **FOR UPDATE:** 커서가 읽은 행을 잠가 처리 중 다른 세션이 바꾸지 못하게 한다.
- **WHERE CURRENT OF:** 커서가 지금 가리키는 행만 정확히 UPDATE한다.
- **BEFORE 트리거:** `:NEW` 값을 저장 전에 바꿀 수 있지만, AFTER 트리거에서는 `:NEW`에 값을 넣을 수 없다.

#### 핵심 정리

- PL/SQL은 선언부(DECLARE), 실행부(BEGIN), 예외부(EXCEPTION)로 구성되며 할당은 `:=`, 출력은 `DBMS_OUTPUT.PUT_LINE`을 사용한다.
- PL/SQL 안의 SELECT는 `INTO`로 변수에 담아야 하고, 결과가 0행이면 NO_DATA_FOUND, 2행 이상이면 TOO_MANY_ROWS 예외가 발생한다.
- `%TYPE`, `%ROWTYPE`을 쓰면 테이블 구조가 바뀌어도 코드를 고칠 필요가 없다.
- 커서는 여러 행을 한 행씩 꺼내 조건별로 다르게 처리할 때 쓰며, 커서 FOR LOOP는 OPEN, FETCH, CLOSE를 자동으로 처리한다.
- 프로시저는 DB에 저장되는 객체로 네트워크 트래픽 감소와 보안에 유리하고, IN / OUT / SYS_REFCURSOR 파라미터를 사용한다.
- 함수는 반드시 RETURN 값이 있고 SELECT 문 안에서 호출할 수 있다.
- DBMS_SCHEDULER로 프로시저를 주기적으로 실행하며, 성공 여부는 `user_scheduler_job_run_details`의 status로 확인한다.
- 트리거는 DML에 의해 자동 실행되며, FOR EACH ROW 유무에 따라 문장 레벨과 행 레벨로 나뉘고 행 레벨에서만 :OLD / :NEW를 쓸 수 있다.
- 현업에서 트리거는 이력 보관, 공통 컬럼 자동 세팅처럼 단순하지만 빠지면 안 되는 작업에 주로 사용한다.

---

PL/SQL 기본 구문부터 커서, 프로시저, 함수, 트리거, 잡 스케줄러까지 하루에 이어서 배우면서 SQL을 프로그래밍 언어처럼 다룰 수 있다는 것을 알게 되었다.
</br>직종별 급여 계산 실습에서 행마다 직종을 보고 계산 방법을 바꿔야 해서 집합 단위 SQL 한 문장으로는 어렵다는 것을 느꼈고, 이럴 때 커서로 한 행씩 처리한다는 것을 이해했다.
</br>감사 로그 트리거를 만들면서 시퀀스 이름 대신 트리거 이름을 써서 ORA-02289 에러가 났고, 테이블 컬럼 수와 VALUES의 값 개수가 맞지 않아 ORA-00913 에러가 나는 것도 직접 겪으면서 에러 메시지의 줄 번호와 내용을 먼저 읽는 습관이 중요하다는 것을 배웠다.
</br>같은 UPDATE 문이라도 FOR EACH ROW가 없으면 이력이 1건, 있으면 바뀐 행 수만큼 쌓이는 것을 보며 문장 레벨과 행 레벨 트리거의 차이를 확실히 알 수 있었다.
</br>잡 스케줄러로 등록한 프로시저의 DBMS_OUTPUT 메시지가 화면에 나오지 않아 처음에는 실패한 줄 알았지만, 잡은 백그라운드 세션에서 실행되기 때문이라는 것과 user_scheduler_job_run_details로 성공 여부를 확인해야 한다는 것을 알게 되었다.
</br>휴면회원 전환, 전일 매출 집계, 변경 이력 보관, 공통 컬럼 자동 입력처럼 현업에서 실제로 쓰는 시나리오를 만들어 보면서, 배치는 다시 돌려도 결과가 같아야 하고 트리거는 단순하지만 빠지면 안 되는 작업에 써야 한다는 기준을 세울 수 있었다.
</br>프로젝트에서도 커서와 프로시저, 트리거를 적용할 수 있는 부분을 찾아 반영해 보고 싶다.
