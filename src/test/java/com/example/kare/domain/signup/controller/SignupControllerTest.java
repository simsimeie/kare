package com.example.kare.domain.signup.controller;

import com.example.kare.entity.member.constant.Sex;
import com.example.kare.domain.signup.dto.SignUpRequestDto;
import com.example.kare.domain.signup.dto.SignUpResponseDto;
import com.example.kare.domain.signup.service.SignupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(SignupController.class)
@SpringBootTest
@AutoConfigureMockMvc
class SignupControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private SignupService signupService;
    public SignupControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper objectMapper
    ){
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("CI가 없는 고객이 들어왔을 때, sign up 요청시 validation 위반으로 client 에러 발생")
    public void signUpValidationTest01() throws Exception {
        // given
        SignUpRequestDto ciIsNullMember = SignUpRequestDto.of(null, "홍길동", LocalDate.of(1989, Month.SEPTEMBER, 23), "01012341234", Sex.MALE);

        // when
        mvc.perform(
                post("/signup/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ciIsNullMember)))
                // then
                .andExpect(status().is4xxClientError());
        then(signupService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("이름 없는 고객이 들어왔을 때, sign up 요청시 validation 위반으로 client 에러 발생")
    public void signUpValidationTest02() throws Exception {
        // given
        SignUpRequestDto nameIsNullMember = SignUpRequestDto.of("ABCDE", null, LocalDate.of(1989, Month.SEPTEMBER, 23), "01012341234", Sex.MALE);
        // when
        mvc.perform(
                        post("/member/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nameIsNullMember)))
                // then
                .andExpect(status().is4xxClientError());
        then(signupService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("신규고객이 들어왔을 때, 정상적으로 회원 가입 완료")
    public void signUpTest01() throws Exception {
        //given
        given(signupService.signup(any())).willReturn(
                SignUpResponseDto.of("uuid","ABCDE", "KYH", LocalDate.of(2021, Month.SEPTEMBER, 25), "01012341234", Sex.FEMALE)
        );
        //when
        mvc.perform(
                        post("/member/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createBasicSignupRequestData())))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("uuid"))
                .andExpect(jsonPath("$.ci").value("ABCDE"))
                .andExpect(jsonPath("$.name").value("KYH"))
                .andExpect(jsonPath("$.birthDate").value("2021-09-25"))
                .andExpect(jsonPath("$.phoneNum").value("01012341234"))
                .andExpect(jsonPath("$.sex").value(Sex.FEMALE.toString()))
                .andDo(print());
        then(signupService).should(only()).signup(any());
    }

    private SignUpRequestDto createBasicSignupRequestData(){
        return SignUpRequestDto.of("ABCDE", "홍길동", LocalDate.of(1989, Month.SEPTEMBER,23), "01012341234", Sex.MALE);
    }


}