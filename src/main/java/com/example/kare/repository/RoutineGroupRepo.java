package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.RoutineGroup;
import com.example.kare.entity.routine.RoutineGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoutineGroupRepo extends JpaRepository<RoutineGroup, RoutineGroupId> {
    @Query("select coalesce(max(rg.routnGrpSeq),0) + 1 from RoutineGroup rg where rg.member = :member")
    Integer findMaxRoutnGrpSeq(@Param("member") Member member);

    @Query("select coalesce(min(r.soOdr),100) - 1 from RoutineGroup r where r.member = :member")
    Integer findMinSoOrd(@Param("member") Member member);
}
