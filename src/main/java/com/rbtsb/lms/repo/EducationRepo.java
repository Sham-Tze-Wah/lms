package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.EducationEntity;
import com.rbtsb.lms.pojo.EducationPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface EducationRepo extends JpaRepository<EducationEntity, String> {

    //next time return pojo or dto, else entity need to convert to pojo or dto
    @Query(value = "SELECT * FROM education e, employee emp WHERE e.emp_id = emp.emp_id AND e.emp_id = ?1 ", nativeQuery = true)
    EducationEntity selectEducationByEmpId(String empId);

    @Query(value = "SELECT * FROM education e WHERE e.emp_id = ?1", nativeQuery = true)
    List<EducationEntity> findByEmpId(String empId); //
}
