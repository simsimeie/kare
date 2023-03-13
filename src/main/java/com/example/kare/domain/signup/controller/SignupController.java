package com.example.kare.domain.signup.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.signup.dto.SignUpRequestDto;
import com.example.kare.domain.signup.dto.SignUpResponseDto;
import com.example.kare.domain.signup.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;
    @PostMapping("/signup/member")
    public ResponseDto<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
        return ResponseDto.of(signupService.signup(signUpRequestDto));
    }

}
