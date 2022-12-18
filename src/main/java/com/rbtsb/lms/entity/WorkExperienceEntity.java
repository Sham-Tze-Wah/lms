package com.rbtsb.lms.entity;

import com.rbtsb.lms.constant.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "WorkExperience")
public class WorkExperienceEntity {

    @Id
    @Column(name="exp_id")
    private String expId;

    @Column(name="work_title")
    private Position workTitle;

    @Column(name="year_exp")
    private String yearsOfExperience;

    @Column(name="company_name")
    private String companyName;

    @Column(name = "date_joined") //in format of dd/mm/yyyy
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateJoined;

    @Column(name = "date_leave") //in format of dd/mm/yyyy
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLeave;
}
