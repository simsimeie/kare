package com.example.kare.entity.member;

import com.example.kare.entity.BaseTimeEntity;
import com.example.kare.entity.member.constant.Sex;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @Column(name = "member_id")
    private String id;
    private String ci;
    private String name;
    private LocalDate birthDate;
    private String phoneNum;
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @PrePersist
    private void generateMemberId(){
        this.id = UUID.randomUUID().toString();
    }


    public static Member createMember(String ci, String name, LocalDate birthDate, String phoneNum, Sex sex) {
        Member member = new Member();
        member.setCi(ci);
        member.setName(name);
        member.setBirthDate(birthDate);
        member.setPhoneNum(phoneNum);
        member.setSex(sex);
        return member;
    }
}
