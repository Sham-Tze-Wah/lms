package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbtsb.lms.util.SqlDataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.support.BeanDefinitionDsl;

import javax.persistence.*;
import java.util.Collection;
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

    @Column(name="username", length= SqlDataType.VARCHAR64)
    private String username;

    @Column(name="password", length=SqlDataType.VARCHAR2000)
    private String password;

    @Column(name = "matching_password", length = SqlDataType.VARCHAR2000)
    private String matchingPassword;

    @Column(name = "enabled")
    private boolean enabled = true;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "id", referencedColumnName = "app_user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "roleId", referencedColumnName = "role_id"))
    private Collection<RoleEntity> roles;

    @OneToOne
    @JoinColumn(name="empId", referencedColumnName="emp_id")
    private EmployeeEntity employeeEntity;

}
