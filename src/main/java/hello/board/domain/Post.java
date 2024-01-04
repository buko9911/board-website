package hello.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Slf4j
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Column(length = 500)
    private String content;

    private LocalDateTime boardCreateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();


    public void setMember(Member member){
        this.member=member;
        member.getPostList().add(this);
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
        log.info("addComment in Post");
        comment.setPost(this);

    }
}
