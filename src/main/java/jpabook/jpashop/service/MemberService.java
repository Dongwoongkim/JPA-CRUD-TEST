package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void save(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> list = memberRepository.findByName(member.getName());
        if (!list.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
    public Member findById(Long id) {
        return memberRepository.findById(id);
    }
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
    public List<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }
}
