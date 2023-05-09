package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static com.example.kare.entity.routine.QMmrRoutnDtlMgt.mmrRoutnDtlMgt;
import static com.querydsl.jpa.JPAExpressions.select;

@Repository
public class MmrRoutnDtlMgtRepoImpl implements MmrRoutnDtlMgtRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public MmrRoutnDtlMgtRepoImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public LocalDate findValidRoutineChangeDate(Integer routnSeq, String mmrId, LocalDate searchDate) {
        return jpaQueryFactory
                .select(mmrRoutnDtlMgt.routnChDt.max())
                .from(mmrRoutnDtlMgt)
                .where(mmrRoutnDtlMgt.routnSeq.eq(routnSeq)
                        .and(mmrRoutnDtlMgt.mmrId.eq(mmrId))
                        .and(mmrRoutnDtlMgt.routnChDt.loe(searchDate))
                )
                .fetchOne();
    }

    @Override
    public MmrRoutnDtlMgt findValidRoutineDetail(Integer routnSeq, String mmrId, LocalDate searchDate) {
        return jpaQueryFactory
                .select(mmrRoutnDtlMgt)
                .from(mmrRoutnDtlMgt)
                .where(mmrRoutnDtlMgt.routnSeq.eq(routnSeq)
                        .and(mmrRoutnDtlMgt.mmrId.eq(mmrId))
                        .and(changeDateCondition(routnSeq, mmrId, searchDate))
                ).fetchOne();
    }

    private BooleanExpression changeDateCondition(Integer routnSeq, String mmrId, LocalDate searchDate) {

        return mmrRoutnDtlMgt.routnChDt.eq(
                select(mmrRoutnDtlMgt.routnChDt.max())
                        .from(mmrRoutnDtlMgt)
                        .where(mmrRoutnDtlMgt.routnSeq.eq(routnSeq)
                                .and(mmrRoutnDtlMgt.mmrId.eq(mmrId))
                                .and(mmrRoutnDtlMgt.routnChDt.loe(searchDate))
                        )
        );
    }
}
