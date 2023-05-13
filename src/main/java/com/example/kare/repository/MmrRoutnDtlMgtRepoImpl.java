package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.example.kare.entity.routine.QMmrRoutnDtlMgt;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.kare.entity.routine.QMmrRoutnDtlMgt.mmrRoutnDtlMgt;

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
    public Optional<MmrRoutnDtlMgt> findValidRoutineDetail(Integer routnSeq, String mmrId, LocalDate searchDate) {
        return Optional.ofNullable(jpaQueryFactory
                .select(mmrRoutnDtlMgt)
                .from(mmrRoutnDtlMgt)
                .where(mmrRoutnDtlMgt.routnSeq.eq(routnSeq)
                        .and(mmrRoutnDtlMgt.mmrId.eq(mmrId))
                        .and(changeDateCondition(routnSeq, mmrId, searchDate))
                ).fetchOne());
    }

    @Override
    public List<MmrRoutnDtlMgt> findValidRoutineDetailList(String mmrId, LocalDate startDate, LocalDate endDate, Set<Integer> routnSeqSet) {
        List<Tuple> tuples = findValidRoutineDetailCondition(mmrId, startDate, routnSeqSet);

        return jpaQueryFactory
                .select(mmrRoutnDtlMgt)
                .from(mmrRoutnDtlMgt)
                .where(mmrRoutnDtlMgt.mmrId.eq(mmrId)
                        .and(mmrRoutnDtlMgt.routnChDt.loe(endDate))
                        .and(changeDateConditions(tuples))
                )
                .orderBy(mmrRoutnDtlMgt.routnSeq.asc(), mmrRoutnDtlMgt.routnChDt.asc())
                .fetch();
    }

    public List<Tuple> findValidRoutineDetailCondition(String mmrId, LocalDate startDate, Set<Integer> routnSeqSet) {
        return jpaQueryFactory
                .select(mmrRoutnDtlMgt.routnSeq, mmrRoutnDtlMgt.routnChDt.max())
                .from(mmrRoutnDtlMgt)
                .where(mmrRoutnDtlMgt.mmrId.eq(mmrId)
                        .and(mmrRoutnDtlMgt.routnChDt.loe(startDate))
                        .and(mmrRoutnDtlMgt.routnSeq.in(routnSeqSet))
                )
                .groupBy(mmrRoutnDtlMgt.routnSeq)
                .fetch();
    }

    private BooleanExpression changeDateCondition(Integer routnSeq, String mmrId, LocalDate searchDate) {
        QMmrRoutnDtlMgt mmrRoutnDtlMgtSub = new QMmrRoutnDtlMgt("mmrRoutnDtlMgtSub");

        return mmrRoutnDtlMgt.routnChDt.eq(JPAExpressions
                .select(mmrRoutnDtlMgtSub.routnChDt.max())
                .from(mmrRoutnDtlMgtSub)
                .where(mmrRoutnDtlMgtSub.routnSeq.eq(routnSeq)
                        .and(mmrRoutnDtlMgtSub.mmrId.eq(mmrId))
                        .and(mmrRoutnDtlMgtSub.routnChDt.loe(searchDate))
                )
        );
    }

    private BooleanExpression changeDateConditions(List<Tuple> conditions) {
        if (conditions.isEmpty()) return null;

        BooleanExpression combinedExpression = Expressions.asBoolean(false).isTrue();

        for (Tuple tuple : conditions) {
            BooleanExpression appendExpression =
                    mmrRoutnDtlMgt.routnSeq.eq(tuple.get(0, Integer.class))
                            .and(mmrRoutnDtlMgt.routnChDt.goe(tuple.get(1, LocalDate.class)));

            combinedExpression = combinedExpression.or(appendExpression);
        }
        return combinedExpression;
    }


}
