package Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    // 게시글 목록 조회
    public List<Post> selectPostList() {

        List<Post> postList = new ArrayList<>();

        String sql = "SELECT post_id, title, view_count, like_count, comment_count, created_at " +
                        "FROM posts " +
                        "ORDER BY created_at DESC";

        try {Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Post post = new Post();

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

                post.setCreatedAt(
                        rs.getString("created_at")
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


    // 게시글 상세 조회
    public Post selectPost(int postId) {

        Post post = null;

        String sql = "SELECT post_id, board_id, category_id, author_id, movie_id, " +
                        "title, content, view_count, like_count, comment_count, " +
                        "status, created_at, updated_at " +
                        "FROM posts " +
                        "WHERE post_id = ?";

        try {Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, postId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                post = new Post();

                post.setPostId(
                        rs.getInt("post_id")
                );

                post.setBoardId(
                        rs.getInt("board_id")
                );


                // category_id는 NULL이 가능함
                Number categoryId = (Number) rs.getObject("category_id");

                if (categoryId == null) {
                    post.setCategoryId(null);
                } else {
                    post.setCategoryId(categoryId.intValue());
                }


                post.setAuthorId(
                        rs.getInt("author_id")
                );


                // movie_id는 NULL이 가능함
                Number movieId = (Number) rs.getObject("movie_id");

                if (movieId == null) {
                    post.setMovieId(null);
                } else {
                    post.setMovieId(movieId.intValue());
                }


                post.setTitle(
                        rs.getString("title")
                );

                post.setContent(
                        rs.getString("content")
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

                post.setStatus(
                        rs.getString("status")
                );

                post.setCreatedAt(
                        rs.getString("created_at")
                );

                post.setUpdatedAt(
                        rs.getString("updated_at")
                );
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return post;
    }


    // 게시글 작성
    public int insertPost(Post post) {

        int result = 0;

        String sql = "INSERT INTO posts (" +
                        "board_id, category_id, author_id, movie_id, " +
                        "title, content, status" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, post.getBoardId());


            if (post.getCategoryId() == null) {
                pstmt.setNull(2, Types.NUMERIC);
            } else {
                pstmt.setInt(2, post.getCategoryId());
            }


            pstmt.setInt(3, post.getAuthorId());


            if (post.getMovieId() == null) {
                pstmt.setNull(4, Types.NUMERIC);
            } else {
                pstmt.setInt(4, post.getMovieId());
            }


            pstmt.setString(5, post.getTitle());

            pstmt.setString(6, post.getContent());

            pstmt.setString(7, post.getStatus());


            result = pstmt.executeUpdate();


            pstmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    // 게시글 수정
    public int updatePost(Post post) {

        int result = 0;

        String sql = "UPDATE posts " +
                        "SET category_id = ?, " +
                        "movie_id = ?, " +
                        "title = ?, " +
                        "content = ?, " +
                        "updated_at = SYSDATE " +
                        "WHERE post_id = ?";

        try {Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);


            if (post.getCategoryId() == null) {
                pstmt.setNull(1, Types.NUMERIC);
            } else {
                pstmt.setInt(1, post.getCategoryId());
            }


            if (post.getMovieId() == null) {
                pstmt.setNull(2, Types.NUMERIC);
            } else {
                pstmt.setInt(2, post.getMovieId());
            }


            pstmt.setString(3, post.getTitle());

            pstmt.setString(4, post.getContent());

            pstmt.setInt(5, post.getPostId());


            result = pstmt.executeUpdate();


            pstmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}