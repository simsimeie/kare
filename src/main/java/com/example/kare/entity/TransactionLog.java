package com.example.kare.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
public class TransactionLog {
    @Id
    @Column(name="transaction_id")
    private String id;
    private String url;
    private Integer status;
    @Column(length = 4000)
    private String request;
    @Column(length = 4000)
    private String response;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long elapseTime;
}
