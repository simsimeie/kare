package com.example.kare.entity.routine.value;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.routine.dto.CycleDto;
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
    private CycleType rpeCycTpCd;
    private String monYn;
    private String tueYn;
    private String wedYn;
    private String thuYn;
    private String friYn;
    private String satYn;
    private String sunYn;
    private Integer wkDcn;

    private Cycle(CycleType cycleType, Integer cycleCount){
        if(null == cycleCount) throw new KBException("주당 일수 반복주기에는 주당 횟수 값이 들어와야 합니다.", ErrorCode.BAD_REQUEST);

        this.rpeCycTpCd = cycleType;
        this.wkDcn = cycleCount;
    }

    private Cycle(CycleType cycleType, String mon, String tue, String wed, String thu, String fri, String sat, String sun) {
        if(!(mon.equals("Y")
                || tue.equals("Y")
                || wed.equals("Y")
                || thu.equals("Y")
                || fri.equals("Y")
                || sat.equals("Y")
                || sun.equals("Y") )) throw new KBException("특정 요일 반복주기에는 수행할 요일이 1건 이상은 들어와야 합니다.", ErrorCode.BAD_REQUEST);

        this.rpeCycTpCd = cycleType;
        this.monYn = mon;
        this.tueYn = tue;
        this.wedYn = wed;
        this.thuYn = thu;
        this.friYn = fri;
        this.satYn = sat;
        this.sunYn = sun;
    }

    // ******** 생성 함수 ********
    public static Cycle createCycle(CycleDto cycleDto){
        checkValidCycleType(cycleDto);

        if(cycleDto.getRepeatCycleTypeCode() == CycleType.DAY){
            return new Cycle(
                    cycleDto.getRepeatCycleTypeCode()
                    , cycleDto.getMonday()
                    , cycleDto.getTuesday()
                    , cycleDto.getWednesday()
                    , cycleDto.getThursday()
                    , cycleDto.getFriday()
                    , cycleDto.getSaturday()
                    , cycleDto.getSunday()
            );
        }
        else if (cycleDto.getRepeatCycleTypeCode() == CycleType.TIMES) {
            return new Cycle(
                    cycleDto.getRepeatCycleTypeCode()
                    , cycleDto.getRepeatCycleNum()
            );
        } else {
            throw new KBException( cycleDto.getRepeatCycleTypeCode() + " 주기에 대한 정책이 없습니다.", ErrorCode.BAD_REQUEST);
        }
    }

    private static void checkValidCycleType(CycleDto cycleDto) {
        for(CycleType type : CycleType.values()){
            if(type == cycleDto.getRepeatCycleTypeCode()) return;
        }
        throw new KBException("존재하지 않는 주기 형태입니다.", ErrorCode.BAD_REQUEST);
    }
}
