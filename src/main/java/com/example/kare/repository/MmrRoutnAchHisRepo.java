package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.id.MmrRoutnAhvHisId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MmrRoutnAchHisRepo extends JpaRepository<MmrRoutnAhvHis, MmrRoutnAhvHisId>, MmrRoutnAchHisRepoCustom {
    Optional<MmrRoutnAhvHis> findById(MmrRoutnAhvHisId id);
    @Modifying(clearAutomatically = true)
    @Query("delete from MmrRoutnAhvHis mra where mra.routnSeq in :routnSeqList and mra.mmrId = :mmrId and mra.routnAhvDt = :reqDt")
    Integer bulkDeleteRoutineAchievement(@Param("routnSeqList") List<Integer> routnSeqList,
                                         @Param("mmrId")String mmrId,
                                         @Param("reqDt")LocalDate reqDt);
}
