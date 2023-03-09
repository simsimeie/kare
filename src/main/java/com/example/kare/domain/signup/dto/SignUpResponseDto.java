package com.example.kare.domain.signup.dto;

import com.example.kare.entity.member.constant.Sex;
import com.example.kare.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class SignUpResponseDto {
    private String id;
    private String ci;
    private String name;
    private LocalDate birthDate;
    private String phoneNum;
    private Sex sex;

    public SignUpResponseDto(Member member){
        this.id = member.getId();
        this.ci = member.getCi();
        this.name = member.getName();
        this.birthDate = member.getBirthDate();
        this.phoneNum = member.getPhoneNum();
        this.sex = member.getSex();
    }

}
