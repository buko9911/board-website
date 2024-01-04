package hello.board.controller;


import hello.board.domain.Comment;
import hello.board.domain.Post;
import hello.board.service.CommentService;
import hello.board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;



    @PostMapping("/comment/delete/{commentId}")
    public String delete(@PathVariable("commentId") Long id, RedirectAttributes redirectAttributes) {
        Long postId = commentService.delete(id);
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/read/{postId}";
    }
}
