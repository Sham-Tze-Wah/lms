package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserEntity {
    @Id
    @Column(name="app_user_id", unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @JsonIgnore
    @NonNull
    private String id = UUID.randomUUID().toString();

    @Column
    private String username;

    @Column
    private String password;

    @OneToOne
    @JoinColumn(name="empId", referencedColumnName="emp_id")
    private EmployeeEntity employeeEntity;

}
