package chap07.array1;

public class BookArray1 {
    public static void main(String[] args) {
        Book[] library = new Book[5];

        for (int i = 0; library.length > i; i++) {
            System.out.println(library[i]);
        }

        library[0] = new Book("태백산맥1", "조정례");
        library[1] = new Book("태백산맥2", "조정례");
        library[2] = new Book("태백산맥3", "조정례");
        library[3] = new Book("태백산맥4", "조정례");
        library[4] = new Book("태백산맥5", "조정례");

        for (int i = 0; library.length > i; i++) {
            System.out.println(library[i]);
        }

        for (int i = 0; library.length > i; i++) {
            library[i].showBookInfo();
        }
    }
}