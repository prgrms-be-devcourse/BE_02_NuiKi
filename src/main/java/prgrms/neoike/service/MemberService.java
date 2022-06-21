package prgrms.neoike.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prgrms.neoike.service.dto.memberdto.MemberDto;
import prgrms.neoike.domain.member.Member;
import prgrms.neoike.repository.MemberRepository;
import prgrms.neoike.service.dto.memberdto.MemberResponse;
import prgrms.neoike.service.converter.MemberMapper;


import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse join(MemberDto memberDto) {
        validateDuplicatedMember(memberDto.email());
        Member member = MemberMapper.mapMember(memberDto);
        Member savedMember = memberRepository.save(member);
        return MemberMapper.mapMemberResponse(savedMember.getId(), savedMember.getEmail().getEmail());
    }

    private void validateDuplicatedMember(String email) {
        Optional<Member> foundMember = memberRepository.findByEmail(email);
        if (foundMember.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
    }
}