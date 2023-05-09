package com.example.kare.repository;

import com.example.kare.domain.today.dto.CycleDto;
import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.domain.today.dto.RoutineResDto;
import com.example.kare.entity.routine.QMmrRoutnAhvHis;
import com.example.kare.entity.routine.QMmrRoutnDtlMgt;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static com.example.kare.entity.routine.QMmrRoutnAhvHis.mmrRoutnAhvHis;
import static com.example.kare.entity.routine.QMmrRoutnMgt.mmrRoutnMgt;
import static com.example.kare.entity.routine.QMmrRoutnGrpMgt.mmrRoutnGrpMgt;
import static com.example.kare.entity.routine.QMmrRoutnDtlMgt.mmrRoutnDtlMgt;
import static com.example.kare.entity.routine.constant.CycleType.TIMES;

@Repository
public class MmrRoutnMgtRepoImpl implements MmrRoutnMgtRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public MmrRoutnMgtRepoImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public Integer findActiveRoutineNum(String mmrId) {
        LocalDate now = LocalDate.now();

        return jpaQueryFactory.select(mmrRoutnMgt.count())
                .from(mmrRoutnMgt)
                .join(mmrRoutnDtlMgt).on(mmrRoutnMgt.member.id.eq(mmrRoutnDtlMgt.mmrId).and(mmrRoutnMgt.routnSeq.eq(mmrRoutnDtlMgt.routnSeq)))
                .where(mmrRoutnMgt.member.id.eq(mmrId)
                        .and(mmrRoutnDtlMgt.endDate.goe(now))
                        .and(changeDateConditionForToday(mmrId, now))
                )
                .fetchOne()
                .intValue();
    }


    @Override
    public List<RoutineResDto> findTodayRoutines(String mmrId, LocalDate searchDate) {
        DayOfWeek dayOfWeek = searchDate.getDayOfWeek();

        List<RoutineResDto> routines =
                jpaQueryFactory
                        .select(Projections.bean(
                                        RoutineResDto.class
                                        , mmrRoutnMgt.routnSeq.as("routineSequence")
                                        , mmrRoutnMgt.routnNm.as("routineName")
                                        , Projections.bean(CycleDto.class
                                                , mmrRoutnDtlMgt.cycle.cycleCount
                                                , mmrRoutnDtlMgt.cycle.cycleType
                                                , mmrRoutnDtlMgt.cycle.mon
                                                , mmrRoutnDtlMgt.cycle.tue
                                                , mmrRoutnDtlMgt.cycle.wed
                                                , mmrRoutnDtlMgt.cycle.thu
                                                , mmrRoutnDtlMgt.cycle.fri
                                                , mmrRoutnDtlMgt.cycle.sat
                                                , mmrRoutnDtlMgt.cycle.sun).as("cycle")
                                        , Projections.bean(GoalDto.class
                                                , mmrRoutnDtlMgt.goal.goalType
                                                , mmrRoutnDtlMgt.goal.goalValue
                                                , mmrRoutnDtlMgt.goal.goalUnit).as("goal")
                                        , mmrRoutnGrpMgt.routnGrpSeq.as("routineGroupSequence")
                                        , mmrRoutnGrpMgt.routnGrpNm.as("routineGroupName")
                                        , mmrRoutnAhvHis.goal.goalValue.as("achievementValue")
                                        , new CaseBuilder()
                                            .when(mmrRoutnAhvHis.goal.goalValue.goe(mmrRoutnDtlMgt.goal.goalValue)
                                                .and(mmrRoutnAhvHis.goal.goalType.eq(mmrRoutnDtlMgt.goal.goalType)))
                                            .then(true)
                                            .otherwise(false).as("achievementStatus")
                                        , mmrRoutnMgt.soOrd.as("sortOrder")
                                        , mmrRoutnGrpMgt.soOdr.as("routineGroupSortOrder")
                                )
                        )
                        .from(mmrRoutnMgt)
                        .join(mmrRoutnDtlMgt)
                            .on(mmrRoutnDtlMgt.mmrId.eq(mmrRoutnMgt.member.id)
                            .and(mmrRoutnDtlMgt.routnSeq.eq(mmrRoutnMgt.routnSeq)))
                        .leftJoin(mmrRoutnAhvHis)
                            .on(mmrRoutnAhvHis.routnChDt.eq(mmrRoutnDtlMgt.routnChDt)
                            .and(mmrRoutnAhvHis.mmrId.eq(mmrRoutnDtlMgt.mmrId))
                            .and(mmrRoutnAhvHis.routnSeq.eq(mmrRoutnDtlMgt.routnSeq)))
                        .leftJoin(mmrRoutnGrpMgt)
                            .on(mmrRoutnMgt.routnSeq.eq(mmrRoutnGrpMgt.routnGrpSeq)
                            .and(mmrRoutnMgt.member.id.eq(mmrRoutnGrpMgt.member.id)))
                        .where(mmrRoutnMgt.member.id.eq(mmrId)
                            .and(mmrRoutnDtlMgt.endDate.goe(searchDate))
                            .and(cycleCondition(dayOfWeek))
                            .and(changeDateConditionForToday(mmrId, searchDate))
                        )
                        .fetch();

        return routines;
    }

    private BooleanExpression cycleCondition(DayOfWeek dayOfWeek) {

        if (dayOfWeek == DayOfWeek.MONDAY) {
            return mmrRoutnDtlMgt.cycle.mon.eq(true).or(mmrRoutnDtlMgt.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.TUESDAY) {
            return mmrRoutnDtlMgt.cycle.tue.eq(true).or(mmrRoutnDtlMgt.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.WEDNESDAY) {
            return mmrRoutnDtlMgt.cycle.wed.eq(true).or(mmrRoutnDtlMgt.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.TUESDAY) {
            return mmrRoutnDtlMgt.cycle.thu.eq(true).or(mmrRoutnDtlMgt.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.FRIDAY) {
            return mmrRoutnDtlMgt.cycle.fri.eq(true).or(mmrRoutnDtlMgt.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.SATURDAY) {
            return mmrRoutnDtlMgt.cycle.sat.eq(true).or(mmrRoutnDtlMgt.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            return mmrRoutnDtlMgt.cycle.sun.eq(true).or(mmrRoutnDtlMgt.cycle.cycleType.eq(TIMES));
        }

        return mmrRoutnDtlMgt.cycle.cycleType.eq(TIMES);
    }

    private BooleanExpression changeDateConditionForToday(String mmrId, LocalDate searchDate) {
        QMmrRoutnDtlMgt mmrRoutnDtlMgtSub = new QMmrRoutnDtlMgt("mmrRoutnDtlMgtSub");

        List<Tuple> subQueryTuples =
                jpaQueryFactory.select(mmrRoutnDtlMgtSub.routnSeq, mmrRoutnDtlMgtSub.routnChDt.max())
                        .from(mmrRoutnDtlMgtSub)
                        .where(mmrRoutnDtlMgtSub.mmrId.eq(mmrId)
                                .and(mmrRoutnDtlMgtSub.routnChDt.loe(searchDate))
                        )
                        .groupBy(mmrRoutnDtlMgtSub.routnSeq)
                        .fetch();

        BooleanExpression combinedExpression = Expressions.asBoolean(false).isTrue();

        for (Tuple tuple : subQueryTuples) {
            BooleanExpression combinedExpression2 = mmrRoutnDtlMgt.routnSeq.eq(tuple.get(0, Integer.class))
                    .and(mmrRoutnDtlMgt.routnChDt.eq(tuple.get(1, LocalDate.class)));

            combinedExpression = combinedExpression.or(combinedExpression2);
        }
        return combinedExpression;
    }


}
