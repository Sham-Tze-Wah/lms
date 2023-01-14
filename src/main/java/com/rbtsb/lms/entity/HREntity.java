package com.rbtsb.lms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "hr")
public class HREntity {

    @Id
    @Column(name="hr_id", unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String hrId = UUID.randomUUID().toString();

    @OneToOne()
    @JoinColumn(name="emp_id", referencedColumnName = "emp_id")
    private EmployeeEntity employeeEntity; //HR details

//    private //assigned by who (manager) - one HR to

//    private Collection<LeaveEntity> assignedLeaveList; //assigned task
}
