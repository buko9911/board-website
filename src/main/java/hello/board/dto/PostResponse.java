package hello.board.dto;

import hello.board.domain.Comment;
import hello.board.domain.Member;
import hello.board.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostResponse {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime boardCreateTime;

    private String userName;

    private List<Comment> commentList = new ArrayList<>();

    public void setPost(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.boardCreateTime = post.getBoardCreateTime();
        this.userName = post.getMember().getUserName();
        this.commentList = post.getCommentList();
    }



}
