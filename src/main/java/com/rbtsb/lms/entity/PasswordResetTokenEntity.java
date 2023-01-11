package com.rbtsb.lms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

import static com.rbtsb.lms.util.DateTimeUtil.calculateExpirationDate;

@Entity
@Data
@Table(name="password_reset_token")
@NoArgsConstructor
public class PasswordResetTokenEntity {
    private static final int EXPIRATION_TIME = 10;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name="password_reset_token_id", unique = true, nullable = false)
    private String id = UUID.randomUUID().toString();
    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app_user_id",
            nullable = false,
            foreignKey =  @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
    private AppUserEntity appUserEntity;

    public PasswordResetTokenEntity(AppUserEntity appUserEntity, String token){
        super();
        this.token = token;
        this.appUserEntity = appUserEntity;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    public PasswordResetTokenEntity(String token){
        super();
        this.token = token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }
}
