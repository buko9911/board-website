package hello.board.controller;


import hello.board.dto.MemberForm;
import hello.board.dto.MemberLoginForm;
import hello.board.dto.MemberResponse;
import hello.board.dto.PostResponse;
import hello.board.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/login")
    public String loginForm(Model model) {
        model.addAttribute("memberLoginForm", new MemberLoginForm());
        log.info("login get mapping");
        return "members/loginForm";
    }

    @GetMapping("/members/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/members/login")
    public String login(@ModelAttribute MemberLoginForm memberLoginForm, HttpSession httpSession, Model model,
                        RedirectAttributes redirectAttributes) {

        log.info("login post mapping");
        MemberResponse memberResponse = memberService.login(memberLoginForm);
        if (memberResponse != null) {
            httpSession.setAttribute("memberId", memberResponse.getId());
            httpSession.setAttribute("memberEmail", memberResponse.getEmail());
            httpSession.setAttribute("userName", memberResponse.getUserName());

            log.info("login success");
            return "redirect:/";
        } else {
            log.info("login fail");
            boolean isFail = true;
            model.addAttribute("isFail", isFail);
            return "members/loginForm";
        }
    }


    @GetMapping("/members/new")
    public String createFrom(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createForm";
    }

    @PostMapping("/members/new")
    public String create(@ModelAttribute MemberForm memberForm, RedirectAttributes redirectAttributes) {

        boolean notDuplication = memberService.save(memberForm);
        if(notDuplication){
            redirectAttributes.addAttribute("status", true);
            return "redirect:/members/login";
        }
        else{
            redirectAttributes.addAttribute("duplicate", true);
            return "redirect:/members/new";
        }
    }

    @GetMapping("/members/post/{memberId}")
    public String viewMemberPostForm(@PathVariable("memberId") Long id, Model model,HttpSession session) {

        log.info("memberId={}", id);
        List<PostResponse> postResponseList = memberService.findOneWithPost(id);
        model.addAttribute("postList", postResponseList);
        model.addAttribute("memberId", id);
        model.addAttribute("sessionMemberId",session.getAttribute("memberId"));
        return "members/postList";
    }

    @GetMapping("/members/info/{memberId}")
    public String infoForm(@PathVariable("memberId") Long id,Model model) {
        MemberResponse memberResponse = memberService.findOne(id);

        model.addAttribute("member", memberResponse);
        return "members/infoForm";
    }

    @GetMapping("/members/edit/{memberId}")
    public String editForm(@PathVariable("memberId") Long id, Model model, HttpSession session) {

        MemberForm memberForm = new MemberForm();
        memberForm.setId(id);
        model.addAttribute("memberForm", memberForm);
        model.addAttribute("sessionMemberId", session.getAttribute("memberId"));
        return "members/editForm";
    }

    @PostMapping("/members/edit/{memberId}")
    public String edit(@PathVariable("memberId") Long id,@ModelAttribute MemberForm memberForm,RedirectAttributes redirectAttributes) {
        memberForm.setId(id);
        memberService.memberUpdate(memberForm);
        log.info("memberupdate");
        redirectAttributes.addAttribute("editStatus", true);
        return "redirect:/members/login";
    }
}
