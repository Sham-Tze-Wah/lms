//package com.rbtsb.lms.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.GenericGenerator;
//
//import javax.persistence.*;
//import java.util.UUID;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Entity
//@Table(name = "user_roles")
//public class UserRolesEntity {
//
//    @Id
//    @Column(name="user_roles_id", unique = true, nullable = false)
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
//    @JsonIgnore
//    private String id = UUID.randomUUID().toString();
//
//    @ManyToOne
//    @JoinColumn(name = "app_user_id", referencedColumnName = "app_user_id")
//    private AppUserEntity appUserEntity;
//
//    @ManyToOne
//    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
//    private RoleEntity roleEntity;
//}
