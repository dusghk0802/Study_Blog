package chap07.array1;

public class EnhancedForLoop {
    public static void main(String[] args) {
        String[] strArray = {"Java", "C", "Python", "JavaScript", "Android"};

        for (String s: strArray){
            System.out.println(s);
        }

        int[] arr = {1,2,3,4,5};
        //iter 단축키 -> 자동생성
        for (int i : arr) {
            System.out.println(i);
        }
    }
}
