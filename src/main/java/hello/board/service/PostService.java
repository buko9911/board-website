package hello.board.service;

import hello.board.domain.Comment;
import hello.board.domain.Member;
import hello.board.domain.Post;
import hello.board.dto.PostForm;
import hello.board.dto.PostResponse;
import hello.board.repository.MemberRepository;
import hello.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(PostForm postForm,Long member_id) {
        Member member = memberRepository.findOne(member_id);
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setBoardCreateTime(LocalDateTime.now());
        post.setMember(member);
        postRepository.save(post);

    }

    @Transactional
    public PostResponse findOne(Long id) {
        Post post = postRepository.findOneWithMember(id);
        if(post!=null){
            PostResponse postResponse = new PostResponse();
            postResponse.setPost(post);
            return postResponse;
        }
        else{
            return null;
        }
    }

    @Transactional
    public PostResponse findOneWithAll(Long id){
        Post post = postRepository.findOneWithAll(id);
        if(post!=null){
            PostResponse postResponse = new PostResponse();
            postResponse.setPost(post);
            return postResponse;
        }
        else{
            return null;
        }
    }

    @Transactional
    public List<PostResponse> findAll(Long lastId){

        List<Post> postList;
        if(lastId==null){
            log.info("postRepository.firstFindAll()");
            postList = postRepository.firstFindAll();
        }
        else {
            postList = postRepository.nextFindAll(lastId);

        }

        List<PostResponse> postResponseList = new ArrayList<>();
        for (Post post : postList) {
            PostResponse postResponse = new PostResponse();
            postResponse.setPost(post);
            postResponseList.add(postResponse);
        }
        return postResponseList;
    }

    @Transactional
    public List<PostResponse> findAllByKeyword(String keyword,Long lastId){

        List<Post> postList;
        if(lastId==null){
            log.info("postRepository.firstFindAllByKeyword()");
            postList = postRepository.firstFindAllByKeyword(keyword);
        }
        else {
            postList = postRepository.nextFindAllByKeyword(keyword,lastId);

        }


        List<PostResponse> postResponseList = new ArrayList<>();
        for (Post post : postList) {
            PostResponse postResponse = new PostResponse();
            postResponse.setPost(post);
            postResponseList.add(postResponse);
        }
        return postResponseList;
    }


    @Transactional
    public PostResponse addComment(Long id, Comment comment) {
        Post post = postRepository.findOne(id);
        post.addComment(comment);

        PostResponse postResponse = new PostResponse();
        postResponse.setPost(post);
        log.info("add Comment end");
        return postResponse;
    }

    @Transactional
    public void postUpdate(PostForm postForm) {
        Post post = postRepository.findOne(postForm.getId());

        log.info("postUpdate 변경 감지");
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());

    }

    @Transactional
    public Long deletePost(Long id) {
        Long memberId = postRepository.delete(id);
        log.info("point44444");
        return memberId;
    }
}
