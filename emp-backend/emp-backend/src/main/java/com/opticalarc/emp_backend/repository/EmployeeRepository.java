package com.opticalarc.emp_backend.repository;

import com.opticalarc.emp_backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmailId(String emailId);

    List<Employee> findByDepartment_Id(Long departmentId);

    List<Employee> findByProject_Id( Long projectId);


}
