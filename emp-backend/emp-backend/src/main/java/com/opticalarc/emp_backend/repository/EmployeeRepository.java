package com.opticalarc.emp_backend.repository;

import com.opticalarc.emp_backend.dto.EmployeeDto;
import com.opticalarc.emp_backend.entity.Employee;
import jakarta.transaction.Transactional;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmailId(String emailId);

    List<Employee> findByDepartment_Id(Long departmentId);

    List<Employee> findByProject_Id( Long projectId);

    @Query(value = "CALL GetSortedEmployees(:sortBy, :sortDir)", nativeQuery = true)
    List<Employee> findAllSorted(@Param("sortBy") String sortBy, @Param("sortDir") String sortDir);
/*
    @Modifying
    @Query("UPDATE Employee e SET e.isDeleted = true WHERE e.id = ?1")
    void softDeleteById(Long employeeId);*/


    @Modifying
    @Transactional
    @Query("DELETE FROM Employee e WHERE e.id = ?1")
    void hardDeleteById(@Param("id") Long id);
/*
    @Query("SELECT e FROM SortedEmployees e")
    Page<Employee> findAllFromView(int page, int size, String sortBy, String sortDir);

    default org.springframework.data.domain.Page<Employee> findAllFromView(Pageable pageable) {
        return null;
    }*/


/*
    @Query("SELECT e FROM Employee e WHERE e.isDeleted = false")
    List<Employee> findAllActiveEmployees();

    @Modifying
    @Query("UPDATE Employee e SET e.deleted = true WHERE e.id = :id")
    void softDeleteEmployee(@Param("id") Long id);
*/

    // For pagination
    /*@Query("SELECT e FROM Employee e WHERE e.isDeleted = false")
    Page<Employee> findAllActiveEmployees(Pageable pageable);*/

}
