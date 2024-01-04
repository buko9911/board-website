package hello.board.repository;

import hello.board.domain.Comment;
import hello.board.domain.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CommentRepository {

    private final EntityManager em;

    public Long delete(Long id) {
        Comment comment = em.find(Comment.class, id);
        Long postId = comment.getPost().getId();
        em.remove(comment);
        return postId;
    }
}
