package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbtsb.lms.constant.Course;
import com.rbtsb.lms.constant.Qualification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Education")
public class EducationEntity {
    @Id
    @Column(name="education_id", unique = true, nullable = false)
    //@GeneratedValue(generator = "uuid") //you need to make the dao throw error as it is generated and set into entity.
    //https://stackoverflow.com/questions/27672337/detached-entity-passed-to-persist-when-save-the-child-data (24 vote)
    @NonNull
    @JsonIgnore
    private String educationId;

    @Enumerated(value=EnumType.STRING)
    @Column(name="qualification")
    private Qualification qualification;

    @Column(name="institute")
    private String institute;

    @Enumerated(value=EnumType.STRING)
    @Column(name="course")
    private Course course;
    
    @ManyToOne()
    @JoinColumn(name="emp_id", referencedColumnName="emp_id")
    private EmployeeEntity employeeEntity;
}
