package com.hanaset.luke.entity;

import com.hanaset.luke.utils.ZonedDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_BANK")
public class BankEntity {

    @Id
    @Column(name = "institute_code", length = 10)
    private String instituteCode;

    @Column(name = "institute_name")
    private String instituteName;

    @Column(name = "reg_dtime", updatable = false)
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime regDtime;

    @Column(name = "upd_dtime")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime updDtime;
}
