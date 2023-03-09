package com.example.kare.domain.signup.service;

import com.example.kare.entity.member.constant.Sex;
import com.example.kare.domain.signup.dto.SignUpRequestDto;
import com.example.kare.entity.member.Member;
import com.example.kare.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class SignupServiceTest {
    @InjectMocks
    private SignupService signupService;
    @Mock
    private MemberRepository memberRepository;
    @Test
    @DisplayName("중복된 CI의 고객이 가입 시도할 때 이미 존재하는 회원이므로 RuntimeException 발생하는지 테스트")
    public void ciDuplicationTest(){
        given(memberRepository.findMemberByCi(any())).willReturn(Member.createMember("temp","김", LocalDate.now(), "01012341234", Sex.MALE));

        Exception exception = assertThrows(RuntimeException.class, ()->{
            // when
            signupService.signup(createBasicSignupRequestData());
        });
    }

    private SignUpRequestDto createBasicSignupRequestData(){
        return SignUpRequestDto.of(null, "홍길동", LocalDate.of(1989, Month.SEPTEMBER,23), "01012341234", Sex.MALE);
    }
}