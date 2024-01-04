package hello.board.service;


import hello.board.domain.Post;
import hello.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public Long delete(Long id) {
        Long postId = commentRepository.delete(id);
        return postId;
    }
}
