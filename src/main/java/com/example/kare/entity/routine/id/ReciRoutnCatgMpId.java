package com.example.kare.entity.routine.id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ReciRoutnCatgMpId implements Serializable {
    private Integer reciRoutnCatgSeq;
    private Integer reciRoutnSeq;

    public ReciRoutnCatgMpId(Integer reciRoutnCatgSeq, Integer reciRoutnSeq) {
        this.reciRoutnCatgSeq = reciRoutnCatgSeq;
        this.reciRoutnSeq = reciRoutnSeq;
    }
}
