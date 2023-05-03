package com.example.kare.repository;

import com.example.kare.entity.routine.RoutineDetail;
import com.example.kare.entity.routine.RoutineDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoutineDetailRepo extends JpaRepository<RoutineDetail, RoutineDetailId> {

    @Query( "select rdm " +
            "from RoutineDetail rdm " +
            "where rdm.routnSeq = :routnSeq " +
            "and rdm.mmrId = :mmrId " +
            "and rdm.routnChDt = (select max(rdm2.routnChDt) " +
                                        "from RoutineDetail rdm2 " +
                                        "where rdm2.routnSeq = :routnSeq " +
                                        "and rdm2.mmrId = :mmrId)"
    )
    RoutineDetail findActiveRoutineDetail(@Param("routnSeq")Integer routnSeq, @Param("mmrId")String mmrId);
}
