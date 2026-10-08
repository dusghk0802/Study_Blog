create table users (
	userid          varchar2(50)	primary key, 
	username		    varchar2(50)	not null,
	userpassword	  varchar2(50)	not null,
	userage			    number(3)		  not null,
	useremail		    varchar2(50)	not null
);

desc users;

SELECT * FROM users;

SELECT userid, username, userpassword, userage, useremail FROM users;

INSERT INTO users(userid, username, userpassword, userage, useremail)
VALUES (?, ?, ?, ?, ?);

SELECT * FROM boards;
COMMIT;

desc boards;
UPDATE boards SET 
btitle = '작은사람',
bcontent = '작게 만든 사람',
bwriter = 'text'
WHERE bno = 1;

SELECT userid, username, userpassword, userage, useremail
FROM users
WHERE userid = 'winter2';

desc users;
SELECT * FROM users;



