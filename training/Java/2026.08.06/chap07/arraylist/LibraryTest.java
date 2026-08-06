package chap07.arraylist;

public class LibraryTest {
    public static void main(String[] args) {

        Library library = new Library();

        Book book1 = new Book("자바 입문", 30000);
        Book book2 = new Book("스프링 부트", 35000);
        Book book3 = new Book("데이터베이스", 25000);

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        library.showBookInfo();
    }
}
