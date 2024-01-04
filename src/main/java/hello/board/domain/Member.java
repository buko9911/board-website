package hello.board.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String userName;

    private String email;

    private String password;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    List<Post> postList = new ArrayList<>();
}
