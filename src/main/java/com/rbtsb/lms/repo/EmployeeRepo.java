package com.rbtsb.lms.repo;

import com.rbtsb.lms.constant.Position;
import com.rbtsb.lms.constant.Role;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.pojo.EmployeePojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Transactional
@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeEntity, String> {

    //next time return pojo or dto, else entity need to convert to pojo or dto
    @Transactional
    //@Query(value="SELECT * FROM employee WHERE name=?1",nativeQuery = true)
    Optional<EmployeeEntity> findByName(String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="UPDATE employee SET name = ?1, address = ?2, phone_no = ?3, date_joined = ?4," +
            "date_leave = ?5, position = ?6 WHERE emp_id = ?7", nativeQuery = true)
    Integer updateByEmployee(
            String name,
            String email,
            String address,
            String phoneNo,
            Date dateJoined,
            Date dateLeave,
            Position position,
            String id
    );
}
