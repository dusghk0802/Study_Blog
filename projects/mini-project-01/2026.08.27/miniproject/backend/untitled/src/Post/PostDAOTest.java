package Post;

import java.util.List;

public class PostDAOTest {

    public static void main(String[] args) {

        PostDAO postDAO = new PostDAO();

        List<Post> postList =
                postDAO.selectPostList();

        if (postList.isEmpty()) {
            System.out.println("등록된 게시글이 없습니다.");

        } else {

            for (Post post : postList) {
                System.out.println("게시글 번호 : " + post.getPostId());
                System.out.println("제목 : " + post.getTitle());
                System.out.println("조회수 : " + post.getViewCount());
                System.out.println("추천수 : " + post.getLikeCount());
                System.out.println("댓글수 : " + post.getCommentCount());
                System.out.println("--------------------");
            }

        }

    }
}