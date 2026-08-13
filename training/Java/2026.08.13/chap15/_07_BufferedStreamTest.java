package chap15;

import java.io.*;

public class _07_BufferedStreamTest {
    public static void main(String[] args) {

        long millisecond = 0;
        try (FileInputStream fis = new FileInputStream("a.zip");
             FileOutputStream fos = new FileOutputStream("copy.zip");
             BufferedInputStream bis = new BufferedInputStream(fis);
             BufferedOutputStream bos = new BufferedOutputStream(fos)){

            millisecond = System.currentTimeMillis();

            int i;
            while((i = bis.read()) != -1){
                bos.write(i);
            }
            millisecond = System.currentTimeMillis() - millisecond;
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("파일을 복사하는데" + millisecond + "소요되었습니다.");
    }
}
