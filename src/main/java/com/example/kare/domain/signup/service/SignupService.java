package com.example.kare.domain.signup.service;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.entity.member.Member;
import com.example.kare.domain.signup.dto.SignUpRequestDto;
import com.example.kare.domain.signup.dto.SignUpResponseDto;
import com.example.kare.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignupService {
    private final MemberRepository memberRepository;

    // dto를 argument, return type으로 지정하면, 서비스가 한정된 목적으로 사용될 수밖에 없다.
    // entity를 argument, return type으로 지정하면, 서비스의 재사용성은 증가하나, 컨트롤러에 변환 로직이 들어가야 하고 OSIV를 true로 설정해야 할 수 있다.
    @Transactional
    public SignUpResponseDto signup(SignUpRequestDto signUpRequestDto){
        Optional<Member> alreadyExistMember = Optional.ofNullable(memberRepository.findMemberByCi(signUpRequestDto.getCi()));
        if(alreadyExistMember.isPresent()){
            throw new KBException("이미 가입한 회원정보가 존재합니다.", ErrorCode.BAD_REQUEST);
        }
        Member member = signUpRequestDto.toEntity();
        return new SignUpResponseDto(memberRepository.save(member));
    }
}
