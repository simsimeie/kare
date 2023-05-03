package com.example.kare.entity.member;

import com.example.kare.entity.member.constant.Sex;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {
    public static Member createMemberForTest(){

        return Member.createMember(
                UUID.randomUUID().toString(),
                "테스트",
                LocalDate.of(2000, Month.MARCH, 20),
                "010-1111-1111",
                Sex.MALE
        );
    }
}