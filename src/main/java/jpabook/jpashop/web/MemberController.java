package jpabook.jpashop.web;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.form.MemberForm;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public String memberList(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    @GetMapping("/new")
    public String addForm(Model model) {
        MemberForm memberForm = new MemberForm();
        model.addAttribute("memberForm", memberForm);
        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String addMember(@Valid @ModelAttribute("memberForm") MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        } else {
            Member member = new Member();
            member.setName(memberForm.getName());
            member.setAddress(new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode()));
            memberService.save(member);
            return "redirect:/";
        }
    }
}
