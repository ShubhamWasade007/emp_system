package com.opticalarc.emp_backend.service;

import com.opticalarc.emp_backend.dto.EmployeeDto;
import com.opticalarc.emp_backend.exception.PageNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> findAllActiveEmployees();
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto getEmployeeById(Long employeeId);
    EmployeeDto updateEmployee(Long employeeId, EmployeeDto updateEmployee);
    void deleteEmployeeBySoftDelete(Long employeeId);
    void deleteEmployeeByHardDelete(Long employeeId);
    Page<EmployeeDto> getAllActiveEmployees(int page, int size, String sortBy, String sortDir) throws PageNotFoundException;

    List<EmployeeDto> getAllEmployeeByDepartmentId(Long id);

    List<EmployeeDto> getAllEmployeeByProjectId(Long id);


}
