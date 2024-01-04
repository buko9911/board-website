package hello.board.dto;


import hello.board.domain.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    private Long id;

    @NotEmpty(message ="회원 닉네임은 필수입니다.")
    private String userName;

    private String email;

    private String password;

    public void setMember(Member member) {
        this.id = member.getId();
        this.userName = member.getUserName();
        this.email = member.getEmail();
        this.password = member.getPassword();
    }

}
