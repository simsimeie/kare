package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.MmrRoutnGrpMgt;
import com.example.kare.entity.routine.id.MmrRoutnGrpMgtId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MmrRoutnGrpMgtRepo extends JpaRepository<MmrRoutnGrpMgt, MmrRoutnGrpMgtId> {
    @Query("select coalesce(max(rg.routnGrpSeq),0) + 1 from MmrRoutnGrpMgt rg where rg.member = :member")
    Integer findMaxRoutnGrpSeq(@Param("member") Member member);

    @Query("select coalesce(min(r.soOdr),100) - 1 from MmrRoutnGrpMgt r where r.member = :member")
    Integer findMinSoOrd(@Param("member") Member member);
}
