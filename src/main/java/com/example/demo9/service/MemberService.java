package com.example.demo9.service;

import com.example.demo9.entity.Member;
import com.example.demo9.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;

  // 회원에 가입한 자료를 DB에 저장처리(먼저 이메일 중복처리를 한다.)
  public Member saveMember(Member member) {
    // 회원 이메일 중복처리
    validateDuplicationMember(member);

    // 회원 가입처리
    return memberRepository.save(member);
  }

  private void validateDuplicationMember(Member member) {
    Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());

    //findMember.get()메소드는 해당 레코드를 찾아오게 된다. 이것은 아래 if문으로 많이 사용한다.
    if(findMember.isPresent()) {  // 이메일로 검색한 결과가 있다면 true이다. 즉, 이미 등록된 회원이다.
      System.out.println("검색된 회원명 : " + findMember.get().getName() + "은 이미 존재하는 회원입니다.");
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

  public Member getMemberDto(String email) {
    return memberRepository.findByEmail(email).orElseThrow();
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 없습니다." + email));

    log.info("=============[로그인 사용자] : " + member);

    return User.builder()
            .username(member.getEmail())
            .password(member.getPassword())
            .roles(member.getRole().toString())
            .build();
  }

}
