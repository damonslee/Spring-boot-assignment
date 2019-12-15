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
@Table(name = "TB_USER")
public class UserEntity {

    @Id
    @Column(name = "user_id", length = 16)
    private String userId;

    @Column(length = 128)
    private String password;

    @Column(name = "last_login")
    private ZonedDateTime lastLogin;

    @Column(name = "reg_dtime", updatable = false)
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime regDtime;

    @Column(name = "upd_dtime")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime updDtime;
}
