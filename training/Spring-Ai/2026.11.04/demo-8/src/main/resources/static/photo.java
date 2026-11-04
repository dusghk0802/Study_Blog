package com.example.demo.dto;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/*
게시판 글쓰기 + 파일 업로드

create table photo(
  name
  age
  image > 파일 저장이 아니고 > 파일 이름 > 1.jpg
  
  
  큰타입 (x)
  >>clob
  >>blob
)

실제 파일은 (Web server 디스크)
AWS > S3 (파일 서버) > 3차 .....
S3 서버 : vue , react 를 배포 

파일 업로드
1. 파일서버(S3) > 파일 write  > File , inputStream ....outputStream
2. 정보 > DB > insert > 파일이름 , 파일용량 , 확장자 

Spring > DTO 통해서 > 파일 객체를 받아요
파일을 받을 수 있는 클래스 타입 : CommonsMultipartFile 타입
*/

//테이블 생성 .... 
public class Photo {
	private String name;
	private int age;
	private String image;
	
	//파일을 담을 수 있는 객체 
	//POINT////////////////////////////////////////////////////////
	private CommonsMultipartFile file; //업로드한 파일정보 받는다 
	//단 file 이름과 <input type="file" name="file">
	//enctype="multipart/form-data"
	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}
	/////////////////////////////////////////////////////////////
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	
	
	
}
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/*
게시판 글쓰기 + 파일 업로드

create table photo(
  name
  age
  image > 파일 저장이 아니고 > 파일 이름 > 1.jpg
  
  
  큰타입 (x)
  >>clob
  >>blob
)

실제 파일은 (Web server 디스크)
AWS > S3 (파일 서버) > 3차 .....
S3 서버 : vue , react 를 배포 

파일 업로드
1. 파일서버(S3) > 파일 write  > File , inputStream ....outputStream
2. 정보 > DB > insert > 파일이름 , 파일용량 , 확장자 

Spring > DTO 통해서 > 파일 객체를 받아요
파일을 받을 수 있는 클래스 타입 : CommonsMultipartFile 타입
*/

//테이블 생성 .... 
public class Photo {
	private String name;
	private int age;
	private String image;
	
	//파일을 담을 수 있는 객체 
	//POINT////////////////////////////////////////////////////////
	private CommonsMultipartFile file; //업로드한 파일정보 받는다 
	//단 file 이름과 <input type="file" name="file">
	//enctype="multipart/form-data"
	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}
	/////////////////////////////////////////////////////////////
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	
	
	
}