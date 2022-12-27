package com.rbtsb.lms.repo;

import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.pojo.EmployeePojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeEntity, Integer> {
    @Transactional
    @Query(value="SELECT * FROM employee WHERE name=?1",nativeQuery = true)
    Optional<EmployeeEntity> getEmployeeByName(String name);

}
