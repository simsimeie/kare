package com.example.kare.entity.routine.value;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.CycleDto;
import com.example.kare.entity.routine.constant.CycleType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Cycle {
    @Enumerated(EnumType.STRING)
    private CycleType cycleType;
    private boolean mon;
    private boolean tue;
    private boolean wed;
    private boolean thu;
    private boolean fri;
    private boolean sat;
    private boolean sun;
    private Integer cycleCount;

    private Cycle(CycleType cycleType, Integer cycleCount){
        if(null == cycleCount) throw new KBException("주당 일수 반복주기에는 주당 횟수 값이 들어와야 합니다.", ErrorCode.BAD_REQUEST);

        this.cycleType = cycleType;
        this.cycleCount = cycleCount;
    }

    private Cycle(CycleType cycleType, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun) {
        if(!(mon || tue || wed || thu || fri || sat || sun)) throw new KBException("특정 요일 반복주기에는 수행할 요일이 1건 이상은 들어와야 합니다.", ErrorCode.BAD_REQUEST);

        this.cycleType = cycleType;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    // ******** 생성 함수 ********
    public static Cycle createCycle(CycleDto cycleDto){
        checkValidCycleType(cycleDto);

        if(cycleDto.getCycleType() == CycleType.DAY){
            return new Cycle(
                    cycleDto.getCycleType()
                    , cycleDto.isMon()
                    , cycleDto.isTue()
                    , cycleDto.isWed()
                    , cycleDto.isThu()
                    , cycleDto.isFri()
                    , cycleDto.isSat()
                    , cycleDto.isSun()
            );
        } else if (cycleDto.getCycleType() == CycleType.TIMES) {
            return new Cycle(
                    cycleDto.getCycleType()
                    , cycleDto.getCycleCount()
            );
        } else {
            throw new KBException( cycleDto.getCycleType() + " 주기에 대한 정책이 없습니다.", ErrorCode.BAD_REQUEST);
        }
    }

    private static void checkValidCycleType(CycleDto cycleDto) {
        for(CycleType type : CycleType.values()){
            if(type == cycleDto.getCycleType()) return;
        }
        throw new KBException("존재하지 않는 주기 형태입니다.", ErrorCode.BAD_REQUEST);
    }
}
