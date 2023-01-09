package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.WorkExperienceEntity;
import com.rbtsb.lms.pojo.WorkExperiencePojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperienceRepo extends JpaRepository<WorkExperienceEntity, String> {

    //next time return pojo or dto, else entity need to convert to pojo or dto
    @Query(value = "SELECT * FROM employee e, work_experience exp WHERE exp.emp_id = e.emp_id AND exp.emp_id = ?1",nativeQuery = true)
    public WorkExperienceEntity selectWorkExperienceByEmpId(String empId);

    //@Query(nativeQuery = false)
    @Query(value = "SELECT * FROM work_experience w WHERE w.emp_id = ?1", nativeQuery = true)
    List<WorkExperiencePojo> findByEmpId(String empId);
}
