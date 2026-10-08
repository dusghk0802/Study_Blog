package chap13._03_stream;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FilterTest {
    public static void main(String[] args) {

        Predicate<Integer> isPositive = x -> x > 0;

        List<Integer> numbers = Arrays.asList(5, -10, 20, -2, 0, 7);
        Stream<Integer> s = numbers.stream().filter(isPositive);
        s.forEach(n -> System.out.println(n));
    }
}
