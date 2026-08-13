package chap15;

import java.io.FileWriter;
import java.io.IOException;

public class _06_FileWriterTest {
    public static void main(String[] args) {

        try (
            FileWriter fw = new FileWriter("writer.txt")){
            fw.write('A');
            char buf[] = {'B', 'C', 'D', 'E', 'F', 'G'};

            fw.write(buf);
            fw.write("안녕하세요, 날씨가 좋습니다");
            fw.write(buf, 1, 2);
            fw.write("65");

        } catch (IOException e) {
           e.printStackTrace();
        }
        System.out.println("출력이 완료됨");
    }
}
