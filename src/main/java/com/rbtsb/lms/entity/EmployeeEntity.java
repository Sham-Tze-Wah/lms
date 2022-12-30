package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbtsb.lms.constant.Position;
import com.rbtsb.lms.constant.Role;
import com.rbtsb.lms.util.SqlDataType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Employee")
@Builder
public class EmployeeEntity {

    //@GeneratedValue(generator = "uuid") //you need to make the dao throw error as it is generated and set into entity.
    //https://stackoverflow.com/questions/27672337/detached-entity-passed-to-persist-when-save-the-child-data (24 vote)
    @Id
    @Column(name="emp_id", unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @JsonIgnore
    @NonNull
    private String empId = UUID.randomUUID().toString();

    @Column(name="name", nullable = false, unique=true, length = SqlDataType.VARCHAR100)
    @NonNull
    private String name;

    @Column(name="phone_no", nullable = false, length = SqlDataType.VARCHAR100)
    @NonNull
    private String phoneNo;

    @Column(name="email", length= SqlDataType.VARCHAR64, nullable=false)
    @NonNull
    private String email;

    @Column(name="address", length= SqlDataType.VARCHAR100)
    @NonNull
    private String address;

    @Column(name="position", length= SqlDataType.VARCHAR64)
    @Enumerated(value=EnumType.STRING)
    @NonNull
    private Position position;

    @Column(name="role", length= SqlDataType.VARCHAR64)
    @Enumerated(value=EnumType.STRING)
    @NonNull
    private Role role;

    @Column(name = "date_joined") //in format of dd/mm/yyyy
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private Date dateJoined;

    @Column(name = "date_leave") //in format of dd/mm/yyyy
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLeave;

//    @PrePersist
//    public void onCreate(){
//        if(!this.empId.isEmpty()){
//            this.empId = empId;
//        }
//        else{
//            this.empId = UUID.randomUUID().toString();
//        }
//    }

//
}
