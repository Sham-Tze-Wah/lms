package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "boss")
public class BossEntity {

    @Id
    @Column(name="boss_id", unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String bossId = UUID.randomUUID().toString();

    @OneToOne()
    @JoinColumn(name="emp_id", referencedColumnName = "emp_id", unique = true)
    private EmployeeEntity employeeEntity;
}
