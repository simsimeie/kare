package com.example.kare.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Period;

import static com.example.kare.entity.routine.QRoutineDetail.routineDetail;

@Repository
public class RoutineDetailRepoImpl implements RoutineDetailRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public RoutineDetailRepoImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public LocalDate findRoutineChangeDate(Integer routnSeq, String mmrId, LocalDate searchDate) {
        return jpaQueryFactory
                .select(routineDetail.routnChDt)
                .from(routineDetail)
                .where(routineDetail.routnSeq.eq(routnSeq)
                        .and(routineDetail.mmrId.eq(mmrId))
                        .and(changeDateCondition(routnSeq, mmrId, searchDate))
                ).fetchOne();
    }

    private BooleanExpression changeDateCondition(Integer routnSeq, String mmrId, LocalDate searchDate){

        // 미래
        if(Period.between(searchDate, LocalDate.now()).isNegative()) {
            return routineDetail.routnChDt.eq(
                    JPAExpressions
                            .select(routineDetail.routnChDt.max())
                            .from(routineDetail)
                            .where(routineDetail.routnSeq.eq(routnSeq)
                                    .and(routineDetail.mmrId.eq(mmrId)))
            );
        } else {
            return null;
        }
    }
}
