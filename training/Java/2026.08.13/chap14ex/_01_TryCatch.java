package chap14ex;

public class _01_TryCatch {
    public static void main(String[] args) {
        //컴파일 오류(compile Error), 실행 오류(Runtime Error), 예외(Exception)
/*        System.out.println(3/0);

        int[] arr = new int[3];
        arr[5] = 100;*/
        try {
            System.out.println(3/0);
/*            Object obj = "test";
            System.out.println((int)obj);*/

        } catch (Exception e) {
            System.out.println("문제가 발생했어요 : " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("프로그램 정상 종료");

    }
}
