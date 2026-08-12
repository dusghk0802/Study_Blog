package chap12._02_collection._03_hashset;

import java.util.HashSet;

public class HashSetTest {
    public static void main(String[] args) {

        HashSet<String> hashSet = new HashSet<String>();
        hashSet.add(new String("박코사"));
        hashSet.add(new String("이코사"));
        hashSet.add(new String("김코사"));
        hashSet.add(new String("강감찬"));
        hashSet.add(new String("강감찬"));

        System.out.println(hashSet);
    }
}
