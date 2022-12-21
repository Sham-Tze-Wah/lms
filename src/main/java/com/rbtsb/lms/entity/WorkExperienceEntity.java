package com.rbtsb.lms.entity;

import com.rbtsb.lms.constant.Position;
import com.rbtsb.lms.util.SqlDataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "WorkExperience")
@Builder
public class WorkExperienceEntity {

    @Id
    @Column(name="exp_id", nullable = false, unique = true)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String expId = UUID.randomUUID().toString();

    @Column(name="work_title",  length= SqlDataType.VARCHAR64)
    private Position workTitle;

    @Column(name="year_exp",  length= SqlDataType.VARCHAR30)
    private String yearsOfExperience;

    @Column(name="company_name",  length= SqlDataType.VARCHAR64)
    private String companyName;

    @Column(name = "date_joined") //in format of dd/mm/yyyy
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateJoined;

    @Column(name = "date_leave") //in format of dd/mm/yyyy
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLeave;

    @ManyToOne
    @JoinColumn(name="emp_id", referencedColumnName = "emp_id")
    private EmployeeEntity employeeEntity;
}
