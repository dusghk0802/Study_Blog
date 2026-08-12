package chap13._03_stream;

import java.util.Arrays;
import java.util.List;

public class SortedTest {
    public static void main(String[] args) {
        List<String> fruits = Arrays.asList("사과", "바나나", "수박", "복숭아");
        System.out.println("기본 정렬 (오름차순으로 정렬)");

        fruits.stream()
                .sorted()
                .forEach(s ->System.out.println(s + " "));
        System.out.println();

/*        System.out.println("맞춤형 정렬 (문자열 길이에 따라 정렬): ");
        fruits.stream()
                .sorted((f1, f2) -> Integer.compare(f1.length(), f2.length()))
                .forEach(s -> System.out.println(s + " "));*/
        //전체 정렬해서 출력됨.

        System.out.println("맞춤형 정렬 (문자열 길이에 따라 정렬): ");
        fruits.stream()
                .sorted().limit(2)
                .forEach(s -> System.out.println(s + " "));
        //2개만 정렬해서 출력됨.
    }
}
