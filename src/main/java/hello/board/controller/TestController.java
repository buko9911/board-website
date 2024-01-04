package hello.board.controller;

import hello.board.dto.MemberResponse;
import hello.board.service.MemberService;
import hello.board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final MemberService memberService;
    private final PostService postService;

    @RequestMapping("/test/{id}")
    @ResponseBody
    public String testfunc(@PathVariable("id") Long id) {
        MemberResponse memberResponse = memberService.findOne(id);

        log.info("test");
        log.info("memberResponse.getPostList()={}", memberResponse.getPostList().get(0).getTitle());

        return "ok";
    }
}
