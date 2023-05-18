package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.constant.AchieveStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.example.kare.entity.routine.QMmrRoutnAhvHis.mmrRoutnAhvHis;

@Repository
public class MmrRoutnAchHisRepoImpl implements MmrRoutnAchHisRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public MmrRoutnAchHisRepoImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MmrRoutnAhvHis> findRoutnAchList(String mmrId, LocalDate startDate, LocalDate endDate, Set<Integer> routnSeqSet) {
        return jpaQueryFactory.select(mmrRoutnAhvHis)
                .from(mmrRoutnAhvHis)
                .where(mmrRoutnAhvHis.mmrId.eq(mmrId)
                        .and(mmrRoutnAhvHis.routnAhvDt.between(startDate, endDate))
                        .and(mmrRoutnAhvHis.routnSeq.in(routnSeqSet))
                )
                .fetch();
    }
}
