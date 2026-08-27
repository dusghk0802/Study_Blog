package Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostDAO {
    public void selectPostList() {
        String sql = "SELECT post_id, title, view_count, like_count, comment_count " +
                "FROM posts " +
                "ORDER BY created_at DESC";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("게시글 번호 : " + rs.getInt("post_id"));
                System.out.println("제목 : " + rs.getString("title"));
                System.out.println("조회수 : " + rs.getInt("view_count"));
                System.out.println("추천수 : " + rs.getInt("like_count"));
                System.out.println("댓글수 : " + rs.getInt("comment_count"));
                System.out.println("-----------------");
            }
            if (!hasData) {
                System.out.println("등록된 게시글이 없습니다.");
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

