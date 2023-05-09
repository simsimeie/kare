package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.example.kare.entity.routine.id.MmrRoutnDtlMgtId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MmrRoutnDtlMgtRepo extends JpaRepository<MmrRoutnDtlMgt, MmrRoutnDtlMgtId>, MmrRoutnDtlMgtRepoCustom {

    @Query( "select rdm " +
            "from MmrRoutnDtlMgt rdm " +
            "where rdm.routnSeq = :routnSeq " +
            "and rdm.mmrId = :mmrId " +
            "and rdm.routnChDt = (select max(rdm2.routnChDt) " +
                                        "from MmrRoutnDtlMgt rdm2 " +
                                        "where rdm2.routnSeq = :routnSeq " +
                                        "and rdm2.mmrId = :mmrId)"
    )
    MmrRoutnDtlMgt findActiveRoutineDetail(@Param("routnSeq")Integer routnSeq, @Param("mmrId")String mmrId);
}
