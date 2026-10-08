package chap11._01_object;

class Book{
    int bookNumber;
    String bookTitle;

    public Book(int bookNumber, String bookTitle) {
        this.bookNumber = bookNumber;
        this.bookTitle = bookTitle;
    }

    @Override
    public String toString() {
        return bookTitle + "," + bookNumber;
    }
}
public class ToStringEX {
    public static void main(String[] args) {
        Book book1 = new Book(200,"개미");
        System.out.println(book1);
        System.out.println(book1.toString());
        //16진수 주소값 출력 -> 재정의하여 주소값 변겅됨
    }
}
