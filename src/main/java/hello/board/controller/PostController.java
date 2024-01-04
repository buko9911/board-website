package hello.board.controller;


import hello.board.domain.Comment;
import hello.board.dto.CommentForm;
import hello.board.dto.MemberLoginForm;
import hello.board.dto.PostForm;
import hello.board.dto.PostResponse;
import hello.board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping("/post/create")
    public String createPostForm(HttpSession session, Model model) {
        if (session.getAttribute("memberEmail") == null) {
            model.addAttribute("memberLoginForm", new MemberLoginForm());
            return "/members/loginForm";
        } else {
            model.addAttribute("postForm", new PostForm());
            return "/post/createPostForm";
        }
    }

    @PostMapping("/post/create")
    public String createPost(@ModelAttribute PostForm postForm, HttpSession session) {
        Object o = session.getAttribute("memberId");
        Long memberId = (Long) o;
        postService.save(postForm, memberId);
        return "redirect:/";
    }

    @GetMapping("/post/read/{postId}")
    public String readPost(@PathVariable("postId") Long id, Model model,HttpSession session) {
        PostResponse postResponse = postService.findOneWithAll(id);
        if(postResponse==null){
            model.addAttribute("errorMessage","이미 삭제된 글 입니다");
            return "/error/postRead";
        }
        log.info("post read");
        if (session.getAttribute("memberId") != null) {
            log.info("Session exist");
            model.addAttribute("sessionMemberId",(Long)(session.getAttribute("memberId")));
        }

        model.addAttribute("postResponse", postResponse);
        model.addAttribute("commentForm", new CommentForm());
        return "/post/readPostForm";
    }

    @PostMapping("/post/createComment/{postId}")
    public String createComment(@PathVariable("postId") Long id,
                                @ModelAttribute CommentForm commentForm,
                                HttpSession session,
                                Model model) {
        if (session.getAttribute("memberId") == null) {
            return "redirect:/members/login";
        } else {
            log.info("add comment");
            Comment comment = new Comment();
            comment.setContent(commentForm.getContent());
            comment.setLocalDateTime(LocalDateTime.now());
            comment.setUserName((String) session.getAttribute("userName"));
            comment.setMemberId((Long)(session.getAttribute("memberId")));
            PostResponse postResponse = postService.addComment(id, comment);

            return "redirect:/post/read/{postId}";
        }
    }

    @GetMapping("/post/edit/{memberId}/{postId}")
    public String editPostForm(@PathVariable("postId") Long postId,
                               @PathVariable("memberId") Long memberId, Model model,
                               HttpSession session) {
        PostResponse postResponse = postService.findOne(postId);
        PostForm postForm = new PostForm();
        postForm.setElement(postResponse.getId(), postResponse.getTitle(), postResponse.getContent());
        model.addAttribute("post", postForm);
        model.addAttribute("memberId", memberId);
        model.addAttribute("sessionMemberId",session.getAttribute("memberId"));
        return "/post/editForm";
    }

    @PostMapping("/post/edit/{memberId}/{postId}")
    public String editPost(@PathVariable("postId") Long id, @ModelAttribute PostForm postForm,
                           RedirectAttributes redirectAttributes) {

        postForm.setId(id);

        postService.postUpdate(postForm);
        log.info("post update complete");
        redirectAttributes.addAttribute("editStatus", true);
        return "redirect:/members/post/{memberId}";
    }

    @GetMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long id, RedirectAttributes redirectAttributes) {
        Long removedPostMemberId = postService.deletePost(id);
        String memberIdString = Long.toString(removedPostMemberId);
        redirectAttributes.addAttribute("memberId", removedPostMemberId);
        redirectAttributes.addAttribute("deleteStatus", true);
        return "redirect:/members/post/{memberId}";
    }
}
