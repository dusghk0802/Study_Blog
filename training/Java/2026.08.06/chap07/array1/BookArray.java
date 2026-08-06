package chap07.array1;

public class BookArray {
    static void main() {
        Book[] library = new Book[5];

        for (int i = 0; library.length > i; i++) {
            System.out.println(library[i]);
        }
    }
}
