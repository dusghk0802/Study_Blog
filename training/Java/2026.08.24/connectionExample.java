package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectionExample {
    public static void main(String[] args) {
        Connection conn = null;

        //1.JDBC Driver를 메모리로 로딩, DriverManger에 등록
        try {
            Class.forName("oracle.jdbc.OracleDriver");

            //연결하기
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe",
                    "java", "oracle");
            System.out.println("연결 성공");

            //DB작업

        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e ){
            e.printStackTrace();
        } finally {
            if (conn != null){
                try {
                    //연결 끊기
                    conn.close();
                    System.out.println("연결 끊김");
                } catch (SQLException e){}
            }
        }
    }
}
