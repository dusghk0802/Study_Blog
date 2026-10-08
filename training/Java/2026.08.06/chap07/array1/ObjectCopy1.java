package chap07.array1;

public class ObjectCopy1 {
    public static void main(String[] args) {

        Book[] bookArray1 = new Book[3];
        Book[] bookArray2 = new Book[3];

        bookArray1[0] = new Book("데미안1","헤르만 헤세");
        bookArray1[1] = new Book("데미안2","헤르만 헤세");
        bookArray1[2] = new Book("데미안3","헤르만 헤세");

        System.arraycopy(bookArray1, 0, bookArray2, 0, 3);
        for (int i = 0; i < bookArray2.length; i++) {
            bookArray1[i].showBookInfo();
        }
    }
}
