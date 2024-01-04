package hello.board.dto;

import hello.board.domain.Member;
import hello.board.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MemberResponse {


    private Long id;

    private String userName;

    private String email;

    private String password;

    private List<PostResponse> postList = new ArrayList<>();

    public void setMember(Member member){
        this.id = member.getId();
        this.userName = member.getUserName();
        this.email = member.getEmail();
        this.password = member.getPassword();
    }

}
