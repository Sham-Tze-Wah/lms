package com.rbtsb.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Id
    @Column(name="qualification")
    private String qualification;

    @Id
    @Column(name="institute")
    private String institute;

    @Id
    @Column(name="course")
    private String course;
}
