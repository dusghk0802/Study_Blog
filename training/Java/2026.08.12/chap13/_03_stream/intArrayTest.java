package chap13._03_stream;

//중간 연산 : filter, map, sorted, distinct, skp, ...
//최종 연산 : count, min, max, sum, FprEAch, anyMatch, allMatch..

import java.lang.reflect.Array;
import java.util.Arrays;

public class intArrayTest {
    public static void main(String[] args) {
        int [] arr = {1,2,3,4,5};

        int sumVal = Arrays.stream(arr).sum();
        int count = (int) Arrays.stream(arr).count();

        System.out.println(sumVal);
        System.out.println(count);
    }
}
