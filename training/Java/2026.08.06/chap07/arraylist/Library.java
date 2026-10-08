package chap07.arraylist;

import java.util.ArrayList;

public class Library {
    ArrayList<Book> bookList;

    public Library(){
        bookList = new ArrayList<>();
    }

    public void addBook(Book book) {
        bookList.add(book);
    }

    public void showBookInfo(){
        for (Book book : bookList) {
            System.out.println("책 제목 : " + book.getTitle() + ", 가격 : " + book.getPrice() + "원");
        }
    }
}
