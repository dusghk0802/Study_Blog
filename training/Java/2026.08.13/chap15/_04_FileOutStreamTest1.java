package chap15;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class _04_FileOutStreamTest1 {
    public static void main(String[] args) {
        try (
            FileOutputStream fos = new FileOutputStream("output.txt")){
                fos.write (65);
                fos.write (66);
                fos.write (67);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("출력이 완료되었습니다.");
    }
}
