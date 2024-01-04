package hello.board.dto;

import hello.board.domain.Post;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostForm {

    private Long id;

    private String title;

    private String content;

    public void setElement(Long id,String title,String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
