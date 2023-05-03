package com.example.kare.repository;

import com.example.kare.domain.today.dto.CycleDto;
import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.domain.today.dto.RoutineResponseDto;
import com.example.kare.entity.routine.QRoutineDetail;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static com.example.kare.entity.routine.QRoutine.routine;
import static com.example.kare.entity.routine.QRoutineGroup.routineGroup;
import static com.example.kare.entity.routine.QRoutineDetail.routineDetail;
import static com.example.kare.entity.routine.constant.CycleType.TIMES;

@Repository
public class RoutineRepoImpl implements RoutineRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;
    public RoutineRepoImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public Integer findActiveRoutineNum(String mmrId){
        LocalDate now = LocalDate.now();

        return jpaQueryFactory.select(routine.count())
                .from(routine)
                .join(routineDetail).on(routine.member.id.eq(routineDetail.mmrId).and(routine.routnSeq.eq(routineDetail.routnSeq)))
                .where(routine.member.id.eq(mmrId)
                        .and(routineDetail.endDate.goe(now))
                        .and(routineDetailChangeDateCondition(mmrId, now))
                )
                .fetchOne()
                .intValue();
    }



    @Override
    public List<RoutineResponseDto> findFutureRoutines(String mmrId, LocalDate searchDate) {
        DayOfWeek dayOfWeek = searchDate.getDayOfWeek();

        List<RoutineResponseDto> routines =
                jpaQueryFactory.select(Projections.bean(
                                RoutineResponseDto.class
                                , routine.routnSeq.as("routineSequence")
                                , routine.routnNm.as("routineName")
                                , Expressions.asString(dayOfWeek.toString()).as("dayOfWeek")
                                , Projections.bean(CycleDto.class
                                        , routineDetail.cycle.cycleCount
                                        , routineDetail.cycle.cycleType
                                        , routineDetail.cycle.mon
                                        , routineDetail.cycle.tue
                                        , routineDetail.cycle.wed
                                        , routineDetail.cycle.thu
                                        , routineDetail.cycle.fri
                                        , routineDetail.cycle.sat
                                        , routineDetail.cycle.sun).as("cycle")
                                , Projections.bean(GoalDto.class
                                        , routineDetail.goal.goalType
                                        , routineDetail.goal.goalValue
                                        , routineDetail.goal.goalUnit).as("goal")
                                , routineGroup.routnGrpSeq.as("routineGroupSequence")
                                , routineGroup.routnGrpNm.as("routineGroupName")
                                , routine.soOrd.as("sortOrder")
                        )
                )
                .from(routine)
                .join(routineDetail).on(routine.member.id.eq(routineDetail.mmrId).and(routine.routnSeq.eq(routineDetail.routnSeq)))
                .leftJoin(routineGroup).on(routine.routnSeq.eq(routineGroup.routnGrpSeq).and(routine.member.id.eq(routineGroup.member.id)))
                .where(routine.member.id.eq(mmrId)
                        .and(routineDetail.endDate.goe(searchDate))
                        .and(cycleCondition(dayOfWeek))
                        .and(routineDetailChangeDateCondition(mmrId, searchDate))
                )
                .fetch();

        return routines;
    }

    private BooleanExpression cycleCondition(DayOfWeek dayOfWeek) {

        if (dayOfWeek == DayOfWeek.MONDAY) {
            return routineDetail.cycle.mon.eq(true).or(routineDetail.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.TUESDAY) {
            return routineDetail.cycle.tue.eq(true).or(routineDetail.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.WEDNESDAY) {
            return routineDetail.cycle.wed.eq(true).or(routineDetail.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.TUESDAY) {
            return routineDetail.cycle.thu.eq(true).or(routineDetail.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.FRIDAY) {
            return routineDetail.cycle.fri.eq(true).or(routineDetail.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.SATURDAY) {
            return routineDetail.cycle.sat.eq(true).or(routineDetail.cycle.cycleType.eq(TIMES));
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            return routineDetail.cycle.sun.eq(true).or(routineDetail.cycle.cycleType.eq(TIMES));
        }

        return routineDetail.cycle.cycleType.eq(TIMES);
    }

    private BooleanExpression routineDetailChangeDateCondition(String mmrId, LocalDate searchDate) {
        QRoutineDetail routineHistorySub = new QRoutineDetail("routineDetailSub");

        List<Tuple> subQueryTuples =
                jpaQueryFactory.select(routineHistorySub.routnSeq, routineHistorySub.routnChDt.max())
                        .from(routineHistorySub)
                        .where(routineHistorySub.mmrId.eq(mmrId)
                                .and(routineHistorySub.routnChDt.loe(searchDate))
                        )
                        .groupBy(routineHistorySub.routnSeq)
                        .fetch();

        BooleanExpression combinedExpression = Expressions.asBoolean(false).isTrue();

        for (Tuple tuple : subQueryTuples) {
            BooleanExpression combinedExpression2 = routineDetail.routnSeq.eq(tuple.get(0, Integer.class))
                    .and(routineDetail.routnChDt.eq(tuple.get(1, LocalDate.class))) ;

            combinedExpression = combinedExpression.or(combinedExpression2);
        }
        return combinedExpression;
    }



}
