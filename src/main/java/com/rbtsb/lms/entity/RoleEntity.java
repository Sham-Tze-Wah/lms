package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbtsb.lms.pojo.RolePojo;
import com.rbtsb.lms.util.SqlDataType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@NamedNativeQuery(name = "RoleEntity.findByRoleName", query = "SELECT role_id as id, role_name as name FROM roles r WHERE r.role_name = ?1", resultSetMapping = "Mapping.RolePojo")
@SqlResultSetMapping(name = "Mapping.RolePojo", classes = @ConstructorResult(targetClass = RolePojo.class,
columns = {@ColumnResult(name = "id"), @ColumnResult(name = "name")}))

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

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    Set<AppUserEntity> appUsers;
}
