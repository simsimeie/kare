package com.example.kare.domain.signup.controller;

import com.example.kare.domain.signup.dto.SignUpRequestDto;
import com.example.kare.domain.signup.dto.SignUpResponseDto;
import com.example.kare.domain.signup.service.SignupService;
import com.example.kare.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;
    @PostMapping("/signup/member")
    public SignUpResponseDto signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
        return signupService.signup(signUpRequestDto);
    }

}
