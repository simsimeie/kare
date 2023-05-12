package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.MmrRoutnGrpMgt;
import com.example.kare.entity.routine.id.MmrRoutnGrpMgtId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MmrRoutnGrpMgtRepo extends JpaRepository<MmrRoutnGrpMgt, MmrRoutnGrpMgtId> {
    @Query("select coalesce(max(rg.routnGrpSeq),0) + 1 from MmrRoutnGrpMgt rg where rg.member = :member")
    Integer findMaxRoutnGrpSeq(@Param("member") Member member);

    @Query("select coalesce(min(rg.soOdr),100) - 1 from MmrRoutnGrpMgt rg where rg.member = :member")
    Integer findMinSoOrd(@Param("member") Member member);

    List<MmrRoutnGrpMgt> findByMember(Member member);

}
