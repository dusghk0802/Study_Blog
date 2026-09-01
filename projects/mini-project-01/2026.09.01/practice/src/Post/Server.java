package Post;

import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Server {

    public static void main(String[] args) {

        try {

            HttpServer server =
                    HttpServer.create(
                            new InetSocketAddress(8080),
                            0
                    );


            server.createContext("/api/posts", exchange -> {

                PostDAO postDAO = new PostDAO();

                List<Post> postList =
                        postDAO.selectPostList();


                StringBuilder json =
                        new StringBuilder();

                json.append("[");


                for (int i = 0; i < postList.size(); i++) {

                    Post post = postList.get(i);

                    json.append("{");

                    json.append("\"postId\":")
                            .append(post.getPostId())
                            .append(",");

                    json.append("\"title\":\"")
                            .append(
                                    escapeJson(
                                            post.getTitle()
                                    )
                            )
                            .append("\",");

                    json.append("\"viewCount\":")
                            .append(post.getViewCount())
                            .append(",");

                    json.append("\"likeCount\":")
                            .append(post.getLikeCount())
                            .append(",");

                    json.append("\"commentCount\":")
                            .append(post.getCommentCount());

                    json.append("}");


                    if (i < postList.size() - 1) {
                        json.append(",");
                    }

                }


                json.append("]");


                byte[] responseBytes =
                        json.toString()
                                .getBytes(StandardCharsets.UTF_8);


                exchange.getResponseHeaders().set(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );


                exchange.getResponseHeaders().set(
                        "Access-Control-Allow-Origin",
                        "*"
                );


                exchange.sendResponseHeaders(
                        200,
                        responseBytes.length
                );


                OutputStream outputStream =
                        exchange.getResponseBody();

                outputStream.write(responseBytes);

                outputStream.close();

            });


            server.start();


            System.out.println(
                    "CINEHUB 서버 실행 성공"
            );

            System.out.println(
                    "게시글 API : http://localhost:8080/api/posts"
            );


        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");

    }

}