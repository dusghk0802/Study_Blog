package chap15;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class _05_FileReaderTest {
    public static void main(String[] args) {
        try (
            FileReader fr = new FileReader("reader.txt")){
            int i;
            while ((i = fr.read()) !=-1) {
                System.out.print((char)i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
