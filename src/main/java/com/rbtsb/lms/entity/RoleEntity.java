package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbtsb.lms.util.SqlDataType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Roles")
@Builder
public class RoleEntity {
    @Id
    @Column(name="role_id", unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @JsonIgnore
    @NonNull
    private String roleId = UUID.randomUUID().toString();

    @Column(name="role_name", nullable = false, unique = true, length= SqlDataType.VARCHAR100)
    private String roleName;


}
