package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbtsb.lms.constant.Course;
import com.rbtsb.lms.constant.Qualification;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.internal.Cascade;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.GenerationType.SEQUENCE;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Education")
@Builder
public class EducationEntity{

    //@GeneratedValue(generator = "uuid") //you need to make the dao throw error as it is generated and set into entity.
    //https://stackoverflow.com/questions/27672337/detached-entity-passed-to-persist-when-save-the-child-data (24 vote)
    //@GeneratedValue(generator="UUID")

//    @SequenceGenerator(
//            name = "edu_seq", //sequence name
//            sequenceName = "edu_seq", //catalogue schema
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = SEQUENCE, //strategy use to generate id
//            generator = "edu_seq" //generator type for the sequence
//    )
    @Id
    @Column(name="education_id", unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @NonNull
    @JsonIgnore
    private String educationId = UUID.randomUUID().toString();

    @Enumerated(value=EnumType.STRING)
    @Column(name="qualification")
    private Qualification qualification;

    @Column(name="institute")
    private String institute;

    @Enumerated(value=EnumType.STRING)
    @Column(name="course")
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER) //
    @JoinColumn(name="emp_id", referencedColumnName="emp_id")
    private EmployeeEntity employeeEntity;

//    @PrePersist
//    void preInsert(){
//        if(this.getEducationId()==null){
//            this.setEducationId(UUID.randomUUID().toString());
//        }
//    }
//    protected void onCreate() {
//        if (this.getEducationId().equals(null)) {
//            this.educationId = UUID.randomUUID().toString();
//        }
//    }
}
