package Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    public List<Post> selectPostList() {

        List<Post> postList = new ArrayList<>();

        String sql =
                "SELECT post_id, title, view_count, like_count, comment_count " +
                        "FROM posts " +
                        "ORDER BY created_at DESC";

        try {Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {Post post = new Post();
                post.setPostId(
                        rs.getInt("post_id")
                );

                post.setTitle(
                        rs.getString("title")
                );

                post.setViewCount(
                        rs.getInt("view_count")
                );

                post.setLikeCount(
                        rs.getInt("like_count")
                );

                post.setCommentCount(
                        rs.getInt("comment_count")
                );

                postList.add(post);
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
        return postList;
    }
}