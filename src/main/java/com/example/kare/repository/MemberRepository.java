package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

    Member findMemberByCi(String ci);
}
