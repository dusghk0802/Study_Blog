package chap11._01_object;

class Student{
    int studentId;
    String studentName;

    public Student(int studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return studentId + "," + studentName;
    }
    //논리적으로 같은 사람임 - 매서드 재정의

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student){
            Student std = (Student)obj;
            if (studentId == std.studentId)
                return true;
            else return false;
        }
        return false;
    }
    //hasCode

    @Override
    public int hashCode() {
        return studentId; //학번이 같으면 True
    }
}

public class EqualsTest {
    public static void main(String[] args) {
        Student studentLee = new Student(100, "이상원");
        Student studentLee2 = studentLee;
        Student studentSang = new Student(100, "이상원");

        if (studentLee == studentLee2)
            System.out.println("studentLee와 studentLee2의 주소는 같습니다.");
        else
            System.out.println("studentLee와 studentLee2의 주소는 다릅니다.");
        if (studentLee.equals(studentLee2))
            System.out.println("studentLee와 studentLee2의 주소는 동일합니다");
        else
            System.out.println("studentLee와 studentLee2의 주소는 동일하지 않습니다.");
        //--------------------------------------------------------------------------
        if (studentLee == studentSang)
            System.out.println("studentLee와 studentSang의 주소는 같습니다.");
        else
            System.out.println("studentLee와 studentSang의 주소는 다릅니다.");
        if (studentLee.equals(studentSang))
            System.out.println("studentLee와 studentSang의 주소는 동일합니다");
        else
            System.out.println("studentLee와 studentSang의 주소는 동일하지 않습니다.");

        //이미 재정의가 되어 있음.
        System.out.println("studentLee의 hasCode: " + studentLee.hashCode());
        System.out.println("studentSang의 hasCode: " + studentSang.hashCode());

        System.out.println("studentLee의 실제의 주소값: " + System.identityHashCode(studentLee));
        System.out.println("studentSang의 실제의 주소값: " + System.identityHashCode(studentSang));
    }
}
