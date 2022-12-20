package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.WorkExperienceEntity;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkExperienceRepo extends JpaRepository<WorkExperienceEntity, String> {

    @Query(value = "SELECT * FROM employee e, work_experience exp WHERE exp.emp_id = e.emp_id AND exp.emp_id = ?1",nativeQuery = true)
    public WorkExperienceEntity selectWorkExperienceByEmpId(String empId);
}
