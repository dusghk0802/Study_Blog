package chap15;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class _03_FileputStreamTest1 {
    public static void main(String[] args) {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream("input.txt");
            System.out.println(fis.read());
            System.out.println(fis.read());
            System.out.println(fis.read());
        } catch (FileNotFoundException e){
            System.out.println(e);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
        finally {
            try {
                fis.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("end");
        }
    }
}
