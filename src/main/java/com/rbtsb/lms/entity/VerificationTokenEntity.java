package com.rbtsb.lms.entity;

import com.rbtsb.lms.pojo.RolePojo;
import com.rbtsb.lms.pojo.VerificationTokenPojo;
import com.rbtsb.lms.util.DateTimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

//@NamedNativeQuery(name = "VerificationTokenEntity.findByToken", query = "SELECT verification_token_id AS id, expiration_time AS time, token, app_user_id AS user_id  FROM verification_token WHERE token = ?1", resultSetMapping = "Mapping.VerificationTokenPojo, Mapping.")
//@SqlResultSetMapping(name = "Mapping.VerificationTokenPojo",
//        classes = @ConstructorResult(targetClass = VerificationTokenPojo.class,
//        columns = {@ColumnResult(name = "id"), @ColumnResult(name = "time"), @ColumnResult(name = "token"), @ColumnResult(name = "user_id")},
//        ))

@Entity
@Table(name="verification_token")
@Data
@NoArgsConstructor
public class VerificationTokenEntity {
    //Expiration time is 10 min
    private static final int EXPIRATION_TIME = 10;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "verification_token_id", unique=true, nullable=false)
    private String id = UUID.randomUUID().toString();

    @Column(name="token", unique=true, nullable=false)
    private String token;

    @Column(name="expiration_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app_user_id",
            nullable = false,
            foreignKey =  @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private AppUserEntity appUserEntity;

    public VerificationTokenEntity(AppUserEntity appUserEntity, String token){
        super();
        this.token = token;
        this.appUserEntity = appUserEntity;
        this.expirationTime = DateTimeUtil.calculateExpirationDate(EXPIRATION_TIME);
    }

    public VerificationTokenEntity(String token){
        super();
        this.token = token;
        this.expirationTime = DateTimeUtil.calculateExpirationDate(EXPIRATION_TIME);
    }
}
