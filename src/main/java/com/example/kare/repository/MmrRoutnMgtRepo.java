package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.MmrRoutnMgt;
import com.example.kare.entity.routine.id.MmrRoutnMgtId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MmrRoutnMgtRepo extends JpaRepository<MmrRoutnMgt, MmrRoutnMgtId>, MmrRoutnMgtRepoCustom {
    @Query("select coalesce(max(r.routnSeq),0) + 1 from MmrRoutnMgt r where r.member = :member")
    Integer findMaxRoutnSeq(@Param("member") Member member);

    @Query("select coalesce(min(r.soOrd),100) - 1 from MmrRoutnMgt r where r.member = :member")
    Integer findMinSoOrd(@Param("member") Member member);

    @Modifying(clearAutomatically = true)
    @Query("update MmrRoutnMgt r set r.routnGrpSeq = null where r.member.id = :mmrId and r.routnGrpSeq = :routnGrpSeq")
    Integer bulkUpdateRoutnGrpSeqToNull(@Param("routnGrpSeq")Integer routnGrpSeq, @Param("mmrId")String mmrId);
}
