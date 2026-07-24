# 3일차 

## Oracle 정렬 및 함수

📌 학습일: 2026.07.22

📌 학습 내용: ORDER BY, 문자 함수, 숫자 함수, 날짜 함수, 형변환 함수

---

## 1. ORDER BY - 정렬

```sql
SELECT 컬럼명 FROM 테이블명 ORDER BY 컬럼명;
```

지정한 컬럼을 기준으로 오름차순 정렬

```sql
SELECT 컬럼명 FROM 테이블명 ORDER BY 컬럼명 DESC;
```

지정한 컬럼을 기준으로 내림차순 정렬

```sql
SELECT 컬럼명1, 컬럼명2 FROM 테이블명 ORDER BY 컬럼명1, 컬럼명2 DESC;
```

여러 컬럼을 기준으로 순차적으로 정렬

---

## 2. INITCAP - 첫 글자 대문자 변환

```sql
SELECT INITCAP(컬럼명) FROM 테이블명;
```

각 단어의 첫 글자는 대문자, 나머지는 소문자로 변환

---

## 3. LOWER - 소문자 변환

```sql
SELECT LOWER(컬럼명) FROM 테이블명;
```

문자열을 소문자로 변환

```sql
SELECT 컬럼명 FROM 테이블명 WHERE LOWER(컬럼명) = '문자';
```

컬럼 값을 소문자로 변환하여 조건과 비교

---

## 4. UPPER - 대문자 변환

```sql
SELECT UPPER(컬럼명) FROM 테이블명;
```

문자열을 대문자로 변환

```sql
SELECT 컬럼명 FROM 테이블명 WHERE 컬럼명 = UPPER('문자');
```

입력한 문자열을 대문자로 변환하여 조건과 비교

---

## 5. LENGTH - 문자 길이

```sql
SELECT LENGTH(컬럼명) FROM 테이블명;
```

문자열의 문자 개수를 반환

---

## 6. LENGTHB - 바이트 길이

```sql
SELECT LENGTHB(컬럼명) FROM 테이블명;
```

문자열이 차지하는 바이트 수를 반환

---

## 7. CONCAT - 문자열 연결

```sql
SELECT CONCAT(컬럼명1, 컬럼명2) FROM 테이블명;
```

두 문자열을 하나로 연결

```sql
SELECT CONCAT(CONCAT(컬럼명1, '문자열'), 컬럼명2) FROM 테이블명;
```

여러 문자열을 중첩하여 연결

---

## 8. SUBSTR - 문자열 추출

```sql
SELECT SUBSTR(컬럼명, 시작위치, 길이) FROM 테이블명;
```

문자열에서 지정한 위치부터 원하는 길이만큼 추출

```sql
SELECT 컬럼명 FROM 테이블명 WHERE SUBSTR(컬럼명, 시작위치, 길이) = '문자';
```

문자열의 특정 부분을 추출하여 조건으로 사용

---

## 9. INSTR - 문자 위치 검색

```sql
SELECT INSTR(컬럼명, '문자') FROM 테이블명;
```

문자열에서 지정한 문자의 위치를 반환

---

## 10. LPAD - 왼쪽 문자 채우기

```sql
SELECT LPAD(컬럼명, 길이, '문자') FROM 테이블명;
```

지정한 길이가 될 때까지 왼쪽에 문자를 채움

---

## 11. RPAD - 오른쪽 문자 채우기

```sql
SELECT RPAD(컬럼명, 길이, '문자') FROM 테이블명;
```

지정한 길이가 될 때까지 오른쪽에 문자를 채움

---

## 12. LTRIM - 왼쪽 문자 제거

```sql
SELECT LTRIM('문자열', '제거문자') FROM DUAL;
```

문자열 왼쪽 끝에 있는 지정 문자를 제거

---

## 13. RTRIM - 오른쪽 문자 제거

```sql
SELECT RTRIM('문자열', '제거문자') FROM DUAL;
```

문자열 오른쪽 끝에 있는 지정 문자를 제거

---

## 14. ROUND - 숫자 반올림

```sql
SELECT ROUND(숫자) FROM DUAL;
```

소수점 이하를 반올림하여 정수로 반환

```sql
SELECT ROUND(숫자, 자릿수) FROM DUAL;
```

지정한 소수점 자릿수까지 반올림

```sql
SELECT ROUND(숫자, -1) FROM DUAL;
```

1의 자리에서 반올림하여 10의 자리 단위로 반환

---

## 15. TRUNC - 숫자 절삭

```sql
SELECT TRUNC(숫자) FROM DUAL;
```

소수점 이하를 절삭

```sql
SELECT TRUNC(숫자, 자릿수) FROM DUAL;
```

지정한 소수점 자릿수 이후를 절삭

```sql
SELECT TRUNC(숫자, -1) FROM DUAL;
```

1의 자리 이하를 절삭하여 10의 자리 단위로 반환

---

## 16. MOD - 나머지 계산

```sql
SELECT MOD(숫자1, 숫자2) FROM DUAL;
```

숫자1을 숫자2로 나눈 나머지를 반환

---

## 17. CEIL - 올림

```sql
SELECT CEIL(숫자) FROM DUAL;
```

주어진 숫자보다 크거나 같은 가장 작은 정수를 반환

---

## 18. FLOOR - 내림

```sql
SELECT FLOOR(숫자) FROM DUAL;
```

주어진 숫자보다 작거나 같은 가장 큰 정수를 반환

---

## 19. 날짜 연산

```sql
SELECT 날짜컬럼 + 숫자 FROM 테이블명;
```

날짜에 지정한 일수를 더함

```sql
SELECT 날짜컬럼 - 숫자 FROM 테이블명;
```

날짜에서 지정한 일수를 뺌

```sql
SELECT 날짜1 - 날짜2 FROM 테이블명;
```

두 날짜 사이의 일수 차이를 계산

---

## 20. MONTHS_BETWEEN - 개월 수 계산

```sql
SELECT MONTHS_BETWEEN(날짜1, 날짜2) FROM DUAL;
```

두 날짜 사이의 개월 수를 계산

---

## 21. ADD_MONTHS - 개월 추가

```sql
SELECT ADD_MONTHS(날짜, 개월수) FROM DUAL;
```

기준 날짜에 지정한 개월 수를 더함

---

## 22. LAST_DAY - 해당 월의 마지막 날짜

```sql
SELECT LAST_DAY(날짜) FROM DUAL;
```

해당 날짜가 속한 달의 마지막 날짜를 반환

---

## 23. NEXT_DAY - 다음 요일

```sql
SELECT NEXT_DAY(날짜, '요일') FROM DUAL;
```

기준 날짜 이후 처음으로 오는 지정 요일의 날짜를 반환

---

## 24. SYSDATE - 현재 날짜

```sql
SELECT SYSDATE FROM DUAL;
```

현재 시스템의 날짜와 시간을 반환

---

## 25. 날짜 ROUND - 날짜 반올림

```sql
SELECT ROUND(날짜, 'DD') FROM DUAL;
```

날짜를 일 기준으로 반올림

```sql
SELECT ROUND(날짜, 'MM') FROM DUAL;
```

날짜를 월 기준으로 반올림

```sql
SELECT ROUND(날짜, 'YY') FROM DUAL;
```

날짜를 연 기준으로 반올림

---

## 26. 날짜 TRUNC - 날짜 절삭

```sql
SELECT TRUNC(날짜) FROM DUAL;
```

날짜의 시간 부분을 제거하여 00:00:00으로 절삭

---

## 27. TO_CHAR - 날짜를 문자로 변환

```sql
SELECT TO_CHAR(날짜컬럼, '날짜형식') FROM 테이블명;
```

날짜를 지정한 형식의 문자열로 변환

```sql
SELECT TO_CHAR(날짜컬럼, 'YY-MM') FROM 테이블명;
```

날짜에서 연도와 월만 지정된 형식으로 출력

```sql
SELECT TO_CHAR(날짜컬럼, 'YYYY/MM/DD HH24:MI:SS') FROM 테이블명;
```

날짜와 시간을 지정한 형식으로 출력

---

## 28. TO_CHAR - 날짜 언어 설정

```sql
SELECT TO_CHAR(날짜컬럼, 'MONTH DD, YYYY', 'NLS_DATE_LANGUAGE=ENGLISH') FROM 테이블명;
```

날짜의 월 또는 요일을 지정한 언어로 출력

---

## 29. TO_CHAR - 숫자를 문자로 변환

```sql
SELECT TO_CHAR(숫자, '9,999') FROM DUAL;
```

숫자를 지정한 형식의 문자열로 변환

---

## 30. TO_DATE - 문자를 날짜로 변환

```sql
SELECT TO_DATE('문자열', '날짜형식') FROM DUAL;
```

문자열을 DATE 자료형으로 변환

```sql
SELECT TO_DATE(SUBSTR(컬럼명, 시작위치, 길이), '날짜형식') FROM 테이블명;
```

문자열 일부를 추출한 후 DATE 자료형으로 변환

---

## 31. 날짜 사이의 일수 계산

```sql
SELECT TRUNC(SYSDATE - TO_DATE('날짜', '날짜형식')) FROM DUAL;
```

지정한 날짜부터 현재까지 경과한 일수를 계산

---

## 32. 날짜 사이의 개월 수 계산

```sql
SELECT ROUND(MONTHS_BETWEEN(SYSDATE, TO_DATE('날짜', '날짜형식'))) FROM DUAL;
```

지정한 날짜부터 현재까지 경과한 개월 수를 계산

---

## 33. 함수 결과에 별칭 지정

```sql
SELECT 함수(컬럼명) AS 별칭 FROM 테이블명;
```

함수로 처리한 결과에 원하는 컬럼 이름을 지정

```sql
SELECT 함수(컬럼명) "별칭" FROM 테이블명;
```

공백이나 대소문자를 유지하는 별칭을 지정

---

## 34. NLS_LANGUAGE - 세션 언어 설정

```sql
ALTER SESSION SET NLS_LANGUAGE = KOREAN;
```

현재 Oracle 세션의 언어 환경을 한국어로 설정

```sql
ALTER SESSION SET NLS_LANGUAGE = AMERICAN;
```

현재 Oracle 세션의 언어 환경을 영어로 설정

---

## 35. LIKE - 문자열 검색

```sql
SELECT 컬럼명 FROM 테이블명 WHERE 컬럼명 LIKE '문자%';
```

지정한 문자로 시작하는 데이터를 조회

```sql
SELECT 컬럼명 FROM 테이블명 WHERE 컬럼명 LIKE '%문자%';
```

지정한 문자가 포함된 데이터를 조회

---

## 36. IN - 여러 값 중 하나 검색

```sql
SELECT 컬럼명 FROM 테이블명 WHERE 컬럼명 IN (값1, 값2, 값3);
```

여러 값 중 하나와 일치하는 데이터를 조회

---

## 37. IS NOT NULL - NULL이 아닌 데이터 조회

```sql
SELECT 컬럼명 FROM 테이블명 WHERE 컬럼명 IS NOT NULL;
```

NULL 값이 아닌 데이터만 조회

---

## 38. 여러 조건 결합

```sql
SELECT 컬럼명 FROM 테이블명 WHERE 조건1 AND 조건2;
```

두 조건을 모두 만족하는 데이터를 조회

```sql
SELECT 컬럼명 FROM 테이블명 WHERE 조건1 OR 조건2;
```

두 조건 중 하나 이상을 만족하는 데이터를 조회

---

## 39. 함수 결과를 기준으로 정렬

```sql
SELECT 함수(컬럼명) FROM 테이블명 ORDER BY 함수(컬럼명);
```

함수로 변환한 결과를 기준으로 정렬

---

## 40. REPLACE - 문자 치환

```sql
SELECT REPLACE(컬럼명, '찾을문자', '변경문자') FROM 테이블명;
```

문자열에서 지정한 문자를 다른 문자로 변경

```sql
SELECT LENGTH(컬럼명) - LENGTH(REPLACE(컬럼명, '문자', '')) FROM 테이블명;
```

문자를 제거하기 전후의 문자열 길이를 비교하여 특정 문자의 개수를 계산
