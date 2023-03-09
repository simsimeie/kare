package com.example.kare.domain.signup.dto;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.member.constant.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class SignUpRequestDto {
    @NotEmpty
    private String ci;
    @NotEmpty
    private String name;
    private LocalDate birthDate;
    @NotEmpty
    private String phoneNum;
    private Sex sex;

    public Member toEntity(){
        return Member.createMember(
                this.getCi()
                , this.getName()
                , this.getBirthDate()
                , this.getPhoneNum()
                , this.getSex());
    }

}
