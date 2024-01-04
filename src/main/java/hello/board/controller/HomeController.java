package hello.board.controller;

import hello.board.dto.PostResponse;
import hello.board.dto.SearchForm;
import hello.board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @RequestMapping("/")
    public String home(Model model, HttpSession session,
                       @RequestParam(required = false,name="keyword") String keyword,
                       @RequestParam(required = false,name="lastId") Long lastId){


        log.info("session.getMemberId()={}", session.getAttribute("memberId"));
        log.info("HomeController");
        List<PostResponse> postResponseList;

        if(keyword==null) {
            postResponseList = postService.findAll(lastId);
        }
        else{
            log.info("keyword 입력");
            postResponseList = postService.findAllByKeyword(keyword,lastId);
            model.addAttribute("status", true);

        }
        model.addAttribute("postList", postResponseList);

        SearchForm searchForm = new SearchForm();
        searchForm.setKeyword(keyword);
        model.addAttribute("searchForm",searchForm);

        if(lastId==null){
            log.info("first page");
            model.addAttribute("isFirst", true);
        }

        if (session.getAttribute("memberId") != null) {
           model.addAttribute("sessionMemberId",session.getAttribute("memberId"));
        }

        return "welcomePage";
    }
}
