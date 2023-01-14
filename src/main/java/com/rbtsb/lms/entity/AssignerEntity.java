package com.rbtsb.lms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "assigner")
@AllArgsConstructor
@NoArgsConstructor
public class AssignerEntity { //for the manager
    @Id
    @Column(name="assigner_id", nullable = false, unique = true)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String assignerId = UUID.randomUUID().toString();

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "date_assign")
    private Date date;

    @OneToOne
    @JoinColumn(name="emp_id", referencedColumnName = "emp_id")
    private EmployeeEntity employeeEntity; //Assigner details

}
