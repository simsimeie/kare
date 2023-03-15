package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoutineRepoistory extends JpaRepository<Routine, Long> {

    @Query("select min(r.displayOrder)-1 from Routine r where r.member = :member_id group by r.member")
    Integer findRoutineDisplayLeastValue(@Param("member_id") Member member);

    Optional<Routine> findRoutineByIdAndMemberId(Long routineId, String memberId);
}
