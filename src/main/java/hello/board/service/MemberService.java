package hello.board.service;

import hello.board.domain.Member;
import hello.board.domain.Post;
import hello.board.dto.MemberForm;
import hello.board.dto.MemberLoginForm;
import hello.board.dto.MemberResponse;
import hello.board.dto.PostResponse;
import hello.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse login(MemberLoginForm memberLoginForm) {
        String email = memberLoginForm.getEmail();
        Member member = memberRepository.findByEmail(email);

        if (member != null) {
            if (member.getPassword().equals(memberLoginForm.getPassword())) {
                MemberResponse memberResponse = new MemberResponse();
                memberResponse.setMember(member);

                return memberResponse;
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    @Transactional
    public boolean save(MemberForm memberForm) {

        if(!isDuplicate(memberForm.getEmail())){
            Member member= new Member();

            member.setUserName(memberForm.getUserName());
            member.setEmail(memberForm.getEmail());
            member.setPassword(memberForm.getPassword());

            memberRepository.save(member);
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public MemberResponse findOne(Long id){
        MemberResponse memberResponse = new MemberResponse();

        Member member = memberRepository.findOne(id);

        /**
         * memberResponse 의 postList 안채움
         */
        memberResponse.setMember(member);

        return memberResponse;
    }

    @Transactional
    public List<PostResponse> findOneWithPost(Long id){
        List<PostResponse> postResponseList = new ArrayList<>();

        Member member = memberRepository.findOneWithPost(id);
        log.info("$$$");
        for (Post post : member.getPostList()) {
            PostResponse postResponse = new PostResponse();
            postResponse.setPost(post);
            postResponseList.add(postResponse);
        }


        log.info("check memberResponse's postList");
        if(postResponseList.size()!=0) {
            log.info("postResponseList.get(0).getTitle()={}", postResponseList.get(0).getTitle());
        }
        return postResponseList;
    }

    @Transactional
    public List<MemberResponse> findAll(){
        List<Member> members = memberRepository.findAll();
        List<MemberResponse> memberResponses = new ArrayList<>();

        for (Member member : members) {
            MemberResponse memberResponse = new MemberResponse();
            memberResponse.setMember(member);
            memberResponses.add(memberResponse);
        }
        return memberResponses;
    }

    @Transactional
    public void memberUpdate(MemberForm memberForm) {
        Member member = memberRepository.findOne(memberForm.getId());
        log.info("update start");
        member.setUserName(memberForm.getUserName());
       /* member.setEmail(memberForm.getEmail());*/
        member.setPassword(memberForm.getPassword());

        log.info("{}", member.getPassword());

        log.info("update end");
    }

    boolean isDuplicate(String email) {
        if (memberRepository.findByEmail(email) == null) {
            return false;
        }
        else{
            return true;
        }
    }
}
