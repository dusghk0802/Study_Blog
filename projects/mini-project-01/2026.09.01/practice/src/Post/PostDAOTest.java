package Post;

import java.util.List;

public class PostDAOTest {

    public static void main(String[] args) {

        PostDAO postDAO = new PostDAO();


        // 게시글 목록 조회
        System.out.println("====게시글 목록 조회 ====");

        List<Post> postList = postDAO.selectPostList();

        if (postList.isEmpty()) {

            System.out.println("등록된 게시글이 없습니다.");

        } else {

            for (Post post : postList) {

                System.out.println("게시글 번호 : " + post.getPostId());
                System.out.println("제목 : " + post.getTitle());
                System.out.println("조회수 : " + post.getViewCount());
                System.out.println("추천수 : " + post.getLikeCount());
                System.out.println("댓글수 : " + post.getCommentCount());
                System.out.println("작성일 : " + post.getCreatedAt());
                System.out.println("--------------------");
            }
        }


        // 게시글 작성
        System.out.println();
        System.out.println("====게시글 작성 ====");

        Post newPost = new Post();

        newPost.setBoardId(1);
        newPost.setCategoryId(null);
        newPost.setAuthorId(1);
        newPost.setMovieId(null);

        newPost.setTitle("테스트 게시글");
        newPost.setContent("게시글 작성 테스트입니다.");
        newPost.setStatus("PUBLISHED");

        int result = postDAO.insertPost(newPost);

        if (result > 0) {
            System.out.println("게시글 작성 성공");
        } else {
            System.out.println("게시글 작성 실패");
        }


        // 게시글 목록 다시 조회
        System.out.println();
        System.out.println("====작성 후 게시글 목록 조회 ====");

        postList = postDAO.selectPostList();

        if (postList.isEmpty()) {

            System.out.println("등록된 게시글이 없습니다.");

        } else {

            for (Post post : postList) {

                System.out.println("게시글 번호 : " + post.getPostId());
                System.out.println("제목 : " + post.getTitle());
                System.out.println("조회수 : " + post.getViewCount());
                System.out.println("추천수 : " + post.getLikeCount());
                System.out.println("댓글수 : " + post.getCommentCount());
                System.out.println("작성일 : " + post.getCreatedAt());
                System.out.println("--------------------");
            }
        }


        // 게시글 상세 조회
        if (!postList.isEmpty()) {

            int postId = postList.get(0).getPostId();

            System.out.println();
            System.out.println("====게시글 상세 조회 ====");

            Post post = postDAO.selectPost(postId);

            if (post == null) {

                System.out.println("게시글이 존재하지 않습니다.");

            } else {

                System.out.println("게시글 번호 : " + post.getPostId());
                System.out.println("게시판 번호 : " + post.getBoardId());
                System.out.println("카테고리 번호 : " + post.getCategoryId());
                System.out.println("작성자 번호 : " + post.getAuthorId());
                System.out.println("영화 번호 : " + post.getMovieId());
                System.out.println("제목 : " + post.getTitle());
                System.out.println("내용 : " + post.getContent());
                System.out.println("조회수 : " + post.getViewCount());
                System.out.println("추천수 : " + post.getLikeCount());
                System.out.println("댓글수 : " + post.getCommentCount());
                System.out.println("상태 : " + post.getStatus());
                System.out.println("작성일 : " + post.getCreatedAt());
                System.out.println("수정일 : " + post.getUpdatedAt());
            }


            // 게시글 수정
            System.out.println();
            System.out.println("====게시글 수정 ====");

            Post updatePost = new Post();

            updatePost.setPostId(postId);
            updatePost.setCategoryId(null);
            updatePost.setMovieId(null);
            updatePost.setTitle("수정된 게시글 제목");
            updatePost.setContent("게시글 내용이 수정되었습니다.");

            int updateResult = postDAO.updatePost(updatePost);

            if (updateResult > 0) {
                System.out.println("게시글 수정 성공");
            } else {
                System.out.println("게시글 수정 실패");
            }
        }
    }
}