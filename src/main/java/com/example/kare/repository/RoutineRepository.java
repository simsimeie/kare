package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long>, RoutineRepositoryMyBatis {

    // TODO : 로직 고쳐야 함
    @Query("select case when min(r.displayOrder) >= min(g.displayOrder)" +
            "           then (min(g.displayOrder)-1)" +
            "           else (min(r.displayOrder)-1)" +
            " end" +
            " from Routine r left outer join r.linkRoutineGroup l" +
            "                left outer join l.group g" +
            " where r.member = :member_id " +
            " group by r.member")
    Integer findRoutineDisplayLeastValue(@Param("member_id") Member member);

    Optional<Routine> findRoutineByIdAndMemberId(Long routineId, String memberId);
}
