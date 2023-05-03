package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Routine;
import com.example.kare.entity.routine.RoutineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoutineRepo extends JpaRepository<Routine, RoutineId>, RoutineRepoCustom {
    @Query("select coalesce(max(r.routnSeq),0) + 1 from Routine r where r.member = :member")
    Integer findMaxRoutineId(@Param("member") Member member);

    @Query("select coalesce(min(r.soOrd),100) - 1 from Routine r where r.member = :member")
    Integer findMinSortOrder(@Param("member") Member member);
}
