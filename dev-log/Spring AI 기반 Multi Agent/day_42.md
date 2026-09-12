# 41일차 학습일지 - 리눅스 파일·디렉터리·링크와 사용자 관리

## 1. 파일의 종류

리눅스에서는 파일의 성격에 따라 여러 종류로 구분하며, `ls -l` 명령어의
출력 결과에서 첫 번째 문자로 파일 종류를 확인할 수 있다.

| 표시 문자 | 파일 종류      | 설명                                                  |
|:---------:|----------------|-------------------------------------------------------|
|    `-`    | 일반 파일      | 텍스트, 실행 파일 등 일반적인 데이터를 저장           |
|    `d`    | 디렉터리       | 파일과 하위 디렉터리를 관리하는 공간                  |
|    `l`    | 심볼릭 링크    | 다른 파일이나 디렉터리의 경로를 가리키는 링크         |
|    `c`    | 문자 장치 파일 | 키보드, 터미널처럼 문자 단위로 데이터를 처리하는 장치 |
|    `b`    | 블록 장치 파일 | 디스크처럼 블록 단위로 데이터를 처리하는 장치         |

## 2. 디렉터리와 경로

리눅스 파일 시스템의 최상위 디렉터리는 루트 디렉터리 `/`이며, `pwd`를
사용하면 현재 작업 디렉터리의 절대 경로를 확인할 수 있다. `cd`를 인자
없이 실행하면 현재 사용자의 홈 디렉터리로 이동한다.

- **상대 경로**: 현재 작업 디렉터리를 기준으로 위치를 표현
- **절대 경로**: 루트 디렉터리 `/`부터 시작하여 전체 위치를 표현
- `.`: 현재 디렉터리
- `..`: 상위 디렉터리
- `~`: 현재 사용자의 홈 디렉터리

``` bash
cd
pwd
/home/user

cd Downloads/
pwd
/home/user/Downloads

cd ../Pictures/
pwd
/home/user/Pictures
```

실습을 통해 `Downloads`에서 `../Pictures/`처럼 `..`을 이용하면 상위
디렉터리로 이동한 뒤 같은 위치의 다른 디렉터리로 이동할 수 있음을
확인하였다.

## 3. 디렉터리 생성 - mkdir

`mkdir(make directory)`은 새로운 디렉터리를 생성할 때 사용한다.

``` bash
mkdir animals/snake
```

상위 디렉터리가 존재하지 않는 상태에서 하위 디렉터리를 바로 생성하면
오류가 발생한다.

``` bash
mkdir fruits/apple
mkdir: cannot create directory 'fruits/apple': No such file or directory
```

이때 `-p` 옵션을 사용하면 필요한 상위 디렉터리까지 함께 생성할 수 있다.

``` bash
mkdir -p fruits/apple
```

`-p` 옵션은 이미 해당 경로가 존재하는 경우에도 오류 없이 처리할 수
있다는 점을 확인하였다.

## 4. 디렉터리 삭제 - rmdir

`rmdir(remove directory)`은 **비어 있는 디렉터리**를 삭제할 때 사용한다.

``` bash
rmdir cat
rmdir cow dog snake
```

내용이 남아 있는 디렉터리를 삭제하려고 하면 오류가 발생한다.

``` bash
rmdir fruits/
rmdir: failed to remove 'fruits/': Directory not empty
```

`-p` 옵션을 사용하면 하위 디렉터리를 삭제한 뒤 비어 있는 상위
디렉터리까지 연속해서 삭제할 수 있다.

``` bash
rmdir -p fruits/apple
```

## 5. 파일 생성과 내용 확인

`echo`와 리다이렉션 기호 `>`를 이용해 문자열을 파일에 저장하고,
`cat`으로 파일 내용을 확인하였다.

``` bash
echo "hello word" > greetings

cat greetings
hello word
```

또한 `touch`를 이용해 빈 파일을 생성하는 실습도 진행하였다.

``` bash
touch hihi/newfile
```

## 6. 파일과 디렉터리 복사 - cp

`cp(copy)`는 파일이나 디렉터리를 복사할 때 사용한다.

``` bash
cp greetings say_hello
```

여러 파일을 하나의 디렉터리로 복사할 수도 있다.

``` bash
mkdir temporary
cp greetings say_hello temporary/
```

디렉터리 자체와 내부 내용을 함께 복사할 때는 `cp -r`을 사용할 수 있다.

``` bash
cp -r 원본디렉터리 복사할디렉터리
```

## 7. 파일과 디렉터리 이동 및 이름 변경 - mv

`mv(move)`는 파일과 디렉터리의 **위치 이동**뿐 아니라 **이름 변경**에도
사용할 수 있다.

### 파일 이름 변경

``` bash
mv say_hello hello
mv greetings welcom
mv welcome WELCOME
```

리눅스는 대소문자를 구분하므로 `welcome`과 `WELCOME`을 서로 다른
이름으로 처리한다.

### 파일 이동

``` bash
mv temporary/hello ./
mv temporary/welcom ./welcome
```

여기서 `.`은 현재 작업 디렉터리를 의미한다.

### 디렉터리 이름 변경 및 이동

``` bash
mkdir haha
mv haha hoho
mv hoho temporary/
mv temporary/hoho ./hihi
```

이처럼 `mv` 하나로 파일과 디렉터리의 이동, 이름 변경, 이동과 동시에 이름
변경까지 처리할 수 있다는 점을 확인하였다.

## 8. 파일과 디렉터리 삭제 - rm

`rm(remove)`은 파일을 삭제할 때 사용한다.

### 일반 파일 삭제

``` bash
rm say_hello
```

삭제 전에 사용자에게 확인하도록 하려면 `-i` 옵션을 사용한다.

``` bash
rm -i hello
rm: remove regular file 'hello'? y
```

### 디렉터리 삭제

일반 `rm`으로 디렉터리를 삭제하려고 하면 오류가 발생한다.

``` bash
rm temporary/
rm: cannot remove 'temporary/': Is a directory
```

빈 디렉터리는 `rm -d`로 삭제할 수 있지만, 내부에 파일이 존재하면 삭제할
수 없다.

``` bash
rm -d hihi/
rm: cannot remove 'hihi/': Directory not empty
```

내부 파일과 하위 디렉터리까지 함께 삭제하려면 `-r` 옵션을 사용한다.

``` bash
rm -r hihi/
```

`rm -r`은 디렉터리 내부까지 재귀적으로 삭제하므로 삭제 대상을 정확히
확인한 뒤 사용하는 것이 중요하다.

## 9. 파일 정보와 아이노드

리눅스에서는 파일의 이름과 실제 파일 정보를 분리하여 관리한다.

- **아이노드(inode)**: 파일의 메타데이터와 데이터 블록 위치 등을
  저장하는 자료구조
- **아이노드 번호**: 각각의 아이노드를 식별하기 위한 번호
- **데이터 블록(data block)**: 파일의 실제 내용이 저장되는 영역
- **덴트리(dentry)**: 파일 이름과 아이노드를 연결하여 경로 탐색에
  사용되는 정보

`ls -li`와 `stat`을 사용해 실제 파일과 링크의 아이노드 번호와 링크 수를
확인하였다.

``` bash
ls -li
stat target
stat s-link
```

## 10. 소프트 링크

소프트 링크(심볼릭 링크)는 대상 파일이나 디렉터리의 **경로를 저장하여
가리키는 링크**이다.

``` bash
ln -s target s-link
```

생성 후 아이노드를 확인하면 원본 파일과 소프트 링크가 서로 다른
아이노드를 가진다는 것을 확인할 수 있었다.

``` text
131373 lrwxrwxrwx 1 user user  6 s-link -> target
131371 -rw-rw-r-- 1 user user 22 target
```

소프트 링크를 통해 원본 내용을 읽을 수도 있다.

``` bash
cat s-link
this is a target file
```

원본 파일의 내용을 수정하면 링크를 통해서도 수정된 내용을 확인할 수
있지만, 원본 파일의 이름이나 경로가 변경되면 기존 경로를 찾지 못해
링크가 깨진다.

``` bash
mv target moved_target

cat s-link
cat: s-link: No such file or directory
```

원본 파일의 이름을 다시 `target`으로 복원하자 소프트 링크도 정상적으로
동작하였다.

``` bash
mv moved_target target

cat s-link
this file is modified
```

## 11. 하드 링크

하드 링크는 기존 파일과 **같은 아이노드를 공유하는 또 하나의 파일
이름**을 만드는 방식이다.

``` bash
ln new-target h-link
```

`ls -li`로 확인한 결과 두 파일이 같은 아이노드를 공유하고 링크 수가
`2`로 증가하였다.

``` text
131376 -rw-rw-r-- 2 user user 26 h-link
131376 -rw-rw-r-- 2 user user 26 new-target
```

원본 파일의 내용을 변경하면 하드 링크에서도 같은 내용이 확인된다.

``` bash
echo "modified version of new target file" > new-target

cat new-target
modified version of new target file

cat h-link
modified version of new target file
```

원본 파일의 이름을 변경해도 하드 링크는 같은 아이노드를 계속 참조한다.

``` bash
mv new-target new_target2
```

원본 파일 이름을 삭제한 후에도 `h-link`가 남아 있기 때문에 데이터에 계속
접근할 수 있으며, 링크 수는 `1`로 감소하였다.

``` bash
rm new_target2

ls -li
131376 -rw-rw-r-- 1 user user 36 h-link
```

## 12. 소프트 링크와 하드 링크 비교

| 구분                  | 소프트 링크          | 하드 링크                                     |
|-----------------------|----------------------|-----------------------------------------------|
| 연결 방식             | 대상의 경로를 저장   | 같은 아이노드를 공유                          |
| 생성 명령어           | `ln -s`              | `ln`                                          |
| 아이노드              | 원본과 다른 아이노드 | 원본과 같은 아이노드                          |
| 디렉터리 연결         | 가능                 | 일반적으로 불가능                             |
| 다른 파일 시스템 연결 | 가능                 | 불가능                                        |
| 원본 이름·경로 변경   | 링크가 깨질 수 있음  | 영향 없이 접근 가능                           |
| 원본 파일명 삭제      | 링크가 깨질 수 있음  | 다른 하드 링크가 남아 있으면 데이터 접근 가능 |

소프트 링크는 Windows의 바로가기와 비슷하게 **경로를 가리키는 방식**,
하드 링크는 **하나의 아이노드에 여러 파일 이름이 연결된 방식**으로
이해할 수 있다.

## 13. 리눅스 사용자의 종류

리눅스 사용자는 크게 root 사용자, 시스템 사용자, 일반 사용자로 구분할 수
있다.

### root 사용자

리눅스 시스템에서 모든 권한을 가진 관리자 계정으로 시스템 설정, 파일
관리, 사용자 관리 등을 수행할 수 있다.

### 시스템 사용자

운영체제나 특정 서비스 및 프로그램을 실행하기 위해 시스템에서 사용하는
계정이다.

### 일반 사용자

root 사용자와 시스템 사용자를 제외한 일반적인 사용자 계정이다.

## 14. 관리자 권한과 사용자 전환

### su

다른 사용자 계정으로 전환할 때 사용한다.

``` bash
su 사용자명
```

### sudo

현재 사용자가 권한을 부여받은 경우 특정 명령어를 관리자 권한으로 실행할
수 있다.

``` bash
sudo 명령어
```

### runuser

지정한 사용자 권한으로 명령이나 셸을 실행할 때 사용한다.

``` bash
runuser -u 사용자명 -- 명령어
```

## 15. /etc/passwd를 이용한 사용자 정보 확인

사용자 계정의 기본 정보는 `/etc/passwd`에서 확인할 수 있다.

``` bash
cat /etc/passwd
```

공개 저장소 기록에서는 시스템 계정 전체 출력은 생략하고 구조만
확인하였다.

``` text
사용자이름:x:UID:GID:설명:홈디렉터리:로그인셸
```

각 항목은 `:`으로 구분되며 사용자 이름, UID, GID, 홈 디렉터리, 로그인 셸
등의 정보를 포함한다.

## 16. 사용자 계정 생성

`adduser`를 이용해 실습용 사용자 계정을 생성하였다.

``` bash
sudo adduser testuser
```

사용자를 생성하면 사용자와 그룹이 추가되고 홈 디렉터리가 생성되며
`/etc/skel`의 기본 설정 파일이 복사된다.

``` text
info: Adding new group `testuser' ...
info: Adding new user `testuser' with group `testuser' ...
info: Creating home directory `/home/testuser' ...
info: Copying files from `/etc/skel' ...
```

공개 저장소 기록을 위해 sudo 비밀번호 입력 프롬프트와 비밀번호 설정
과정은 생략하였다.

## 17. 생성된 사용자 홈 디렉터리 확인

생성한 사용자의 홈 디렉터리를 확인하였다.

``` bash
sudo ls -al /home/testuser/
```

``` text
.bash_logout
.bashrc
.profile
```

새 사용자의 홈 디렉터리에 기본 셸 설정 파일이 생성된 것을 확인하였으며,
로그인 화면에서도 새로 생성한 실습용 사용자가 표시되는 것을 확인하였다.

## 18. 사용자 계정 삭제 및 확인

실습용 사용자와 홈 디렉터리를 삭제하였다.

``` bash
sudo deluser --remove-home testuser
```

삭제 후 `/etc/passwd`와 `/etc/group`에서 사용자와 그룹이 제거되었는지
확인하였다.

``` bash
grep testuser /etc/passwd
grep testuser /etc/group
```

출력 결과가 없으므로 `testuser` 사용자와 그룹이 삭제되었음을 확인하였다.

이후 홈 디렉터리 상태를 확인했을 때 UID/GID `1001` 소유의 `.cache`
디렉터리가 남아 있는 것도 확인하였다.

``` text
/home/testuser
└── .cache
```

이를 통해 사용자 계정 정보가 삭제되더라도 특정 파일이나 디렉터리가 남아
있을 수 있으므로 삭제 후 상태를 다시 확인하는 과정이 필요하다는 점을
실습으로 확인하였다.

## 19. 오늘 사용한 주요 명령어 정리

| 명령어    | 기능                                  |
|-----------|---------------------------------------|
| `ls`      | 파일 및 디렉터리 목록 확인            |
| `pwd`     | 현재 작업 디렉터리 확인               |
| `cd`      | 작업 디렉터리 이동                    |
| `mkdir`   | 디렉터리 생성                         |
| `rmdir`   | 빈 디렉터리 삭제                      |
| `cp`      | 파일 및 디렉터리 복사                 |
| `mv`      | 파일·디렉터리 이동 또는 이름 변경     |
| `rm`      | 파일 및 디렉터리 삭제                 |
| `touch`   | 빈 파일 생성 또는 파일 시간 정보 갱신 |
| `cat`     | 파일 내용 확인                        |
| `stat`    | 파일의 상세 정보 확인                 |
| `ln -s`   | 소프트 링크 생성                      |
| `ln`      | 하드 링크 생성                        |
| `su`      | 다른 사용자로 전환                    |
| `sudo`    | 관리자 권한으로 명령 실행             |
| `runuser` | 지정한 사용자 권한으로 명령 실행      |
| `adduser` | 사용자 계정 생성                      |
| `deluser` | 사용자 계정 삭제                      |
| `grep`    | 특정 문자열이 포함된 내용 검색        |

## 20. 가장 기억에 남는 실습

### `mkdir -p`와 일반 `mkdir`의 차이

``` bash
mkdir fruits/apple
mkdir: cannot create directory 'fruits/apple': No such file or directory

mkdir -p fruits/apple
```

상위 디렉터리가 없으면 일반 `mkdir`로 하위 디렉터리를 바로 만들 수
없지만 `-p`를 사용하면 필요한 경로를 함께 생성할 수 있다는 것을 오류와
정상 실행을 비교하면서 확인하였다.

### `rm`과 `rmdir`의 차이

``` bash
rmdir fruits/
rmdir: failed to remove 'fruits/': Directory not empty

rm -r hihi/
```

`rmdir`은 비어 있는 디렉터리를 대상으로 하고, 내용이 있는 디렉터리를
내부까지 삭제할 때는 `rm -r`이 필요하다는 차이를 확인하였다.

### 소프트 링크가 깨지는 과정

``` bash
mv target moved_target

cat s-link
cat: s-link: No such file or directory
```

소프트 링크는 대상의 경로를 가리키기 때문에 대상의 이름이 변경되자
링크가 정상적으로 동작하지 않았고, 다시 원래 이름으로 복원하자 링크도
정상적으로 동작하였다.

### 하드 링크의 아이노드 공유

``` text
131376 -rw-rw-r-- 2 user user 36 h-link
131376 -rw-rw-r-- 2 user user 36 new-target
```

하드 링크는 파일 이름이 달라도 같은 아이노드를 공유하며, 한쪽 파일
이름을 변경하거나 삭제해도 다른 하드 링크를 통해 데이터에 접근할 수
있다는 점이 가장 확실하게 확인되었다.

### 사용자 생성과 삭제

``` bash
sudo adduser testuser
sudo deluser --remove-home testuser
```

사용자를 직접 생성하고 홈 디렉터리와 기본 설정 파일을 확인한 뒤 계정과
그룹을 삭제하는 과정까지 실습하였다. 삭제 후에도 남은 디렉터리가 있는지
다시 확인하는 과정이 필요하다는 점도 알게 되었다.

## 21. 배운 점

오늘은 리눅스 파일 시스템에서 파일과 디렉터리를 생성·복사·이동·삭제하는
기본 명령어를 실제로 실행하면서 각 명령어의 동작 차이를 확인하였다. 특히
상대 경로와 절대 경로를 이용해 디렉터리를 이동하면서 현재 작업 위치를
정확히 파악하는 것이 중요하다는 것을 알게 되었다.

`mkdir`, `rmdir`, `rm`, `mv` 등의 명령어는 옵션이나 대상의 상태에 따라
실행 결과가 달라졌으며, 오류 메시지를 직접 확인하면서 왜 명령이 실행되지
않는지 파악하는 과정도 중요하다는 것을 배웠다.

또한 `ls -li`와 `stat`으로 아이노드를 직접 비교하면서 소프트 링크는
원본과 다른 아이노드에서 대상의 경로를 가리키고, 하드 링크는 같은
아이노드를 공유한다는 차이를 실습 결과로 확인하였다. 특히 소프트 링크의
대상 파일 이름을 변경했을 때 링크가 깨지는 현상과 하드 링크의 원본 파일
이름을 삭제해도 데이터에 계속 접근할 수 있는 현상을 비교하면서 두 링크의
구조를 이해할 수 있었다.

마지막으로 `/etc/passwd`를 통해 사용자 정보의 구조를 확인하고,
`adduser`로 실습용 사용자를 생성한 뒤 홈 디렉터리와 기본 설정 파일을
확인하였다. 이후 `deluser --remove-home`으로 사용자를 삭제하고
`/etc/passwd`, `/etc/group`, 홈 디렉터리 상태를 다시 확인하면서 사용자
관리 과정도 실습하였다.

## 22. 느낀 점

처음에는 리눅스 명령어를 각각 외워야 한다고 생각했지만, 직접 실습해 보니
현재 디렉터리의 위치와 파일 상태를 먼저 확인하면 명령어가 왜 성공하거나
실패하는지 이해하기 쉬웠다.

특히 이번 실습에서는 단순히 명령어를 실행하는 것보다 오류가 발생한
상황을 정상 실행 결과와 비교하는 과정이 도움이 되었다. `mkdir`과
`mkdir -p`, `rmdir`과 `rm -r`, 소프트 링크와 하드 링크처럼 비슷해 보이는
기능도 직접 결과를 확인하니 차이가 더 명확하게 느껴졌다.

사용자 계정을 생성하고 삭제하는 실습에서는 파일 관리뿐 아니라 사용자와
그룹, 홈 디렉터리가 서로 연결되어 있다는 것도 확인할 수 있었다. 앞으로도
명령어 실행 전후에 `pwd`, `ls`, `stat`, `grep` 등을 활용해 현재 상태를
확인하는 습관을 들여야겠다.

## 23. 나의 한 줄 평

**파일과 디렉터리 관리부터 링크의 아이노드 구조와 사용자 생성·삭제까지
직접 확인하면서 리눅스의 기본 관리 흐름을 이해한 하루였다.**
