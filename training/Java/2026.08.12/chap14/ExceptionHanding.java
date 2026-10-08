package chap14;

import chap13._03_stream.FilterTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ExceptionHanding {
    public static void main(String[] args) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream("a.txt");
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        System.out.println("여기에서도 수행됩니다.");
    }
}
