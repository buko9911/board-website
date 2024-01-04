package hello.board.repository;


import hello.board.domain.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostRepository {
    private final EntityManager em;

    public void save(Post post){

        em.persist(post);
    }

    public Post findOne(Long id){
        log.info("em.find() in PostRepository");
        return em.find(Post.class, id);
    }

    public Post findOneWithMember(Long id) {
        String sql = "select p from Post p join fetch p.member where p.id = :postId";
        return em.createQuery(sql, Post.class).setParameter("postId", id).getResultList().get(0);
    }

    public Post findOneWithAll(Long id) {
        String sql = "select p from Post p join fetch p.member outer join fetch  p.commentList where p.id = :postId";
        List<Post> postList = em.createQuery(sql, Post.class).setParameter("postId", id).getResultList();
        if(postList.size()==0)
            return null;
        else
            return postList.get(0);
    }

    public List<Post> nextFindAll(Long lastId){
        log.info("****do fetch join in PostRepository nextFindAll****");
        String sql = "select p from Post p join fetch p.member where p.id < :lastId order by p.id desc";
        return em.createQuery(sql, Post.class)
                .setParameter("lastId",lastId)
                .setMaxResults(9)
                .getResultList();
    }

    public List<Post> firstFindAll(){
        log.info("****do fetch join in PostRepository firstFindAll****");
        String sql = "select p from Post p join fetch p.member order by p.id desc";
        return em.createQuery(sql, Post.class)
                .setFirstResult(0)
                .setMaxResults(9)
                .getResultList();
    }

    public List<Post> firstFindAllByKeyword(String keyword){

        log.info("****do fetch join like keyword in PostRepository****");
        String sql = "select p from Post p join fetch p.member where title like:keyword order by p.id desc ";
        return em.createQuery(sql, Post.class)
                .setParameter("keyword", "%" + keyword + "%")
                .setFirstResult(0)
                .setMaxResults(9)
                .getResultList();
    }

    public List<Post> nextFindAllByKeyword(String keyword,Long lastId){

        log.info("****do fetch join like keyword in PostRepository****");
        String sql = "select p from Post p join fetch p.member where p.id<:lastId and p.title like:keyword order by p.id desc ";
        return em.createQuery(sql, Post.class)
                .setParameter("lastId",lastId)
                .setParameter("keyword", "%" + keyword + "%")
                .setMaxResults(9)
                .getResultList();
    }


    public Long delete(Long id) {
        log.info("****do em.remove(post) in PostRepository");
        Post post = em.find(Post.class, id);
        log.info("point11111");
        Long memberId = post.getMember().getId();
        log.info("point22222");
        em.remove(post);
        log.info("point33333");
        return memberId;
    }
}
