package hello.board.repository;

import hello.board.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findByEmail(String email) {
        String sql = "select m from Member m where m.email = :email";
        List<Member> members = em.createQuery(sql, Member.class).setParameter("email", email).getResultList();
        if(members.size()==0)
            return null;
        else
            return members.get(0);
    }

    public Member findOne(Long id){
        log.info("****em.find() in MemberRepository****");
        return em.find(Member.class, id);

    }

    public Member findOneWithPost(Long id){
        log.info("****do fetch join in MemberRepository****");
        String sql = "select m from Member m outer join fetch m.postList where m.id = :memberId";
        return em.createQuery(sql, Member.class).setParameter("memberId",id).getSingleResult();
    }

    public List<Member> findAll(){
        String sql = "select m from Member m";
        List<Member> members = em.createQuery(sql, Member.class).getResultList();
        return members;
    }

}
