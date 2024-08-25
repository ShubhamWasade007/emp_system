package com.opticalarc.emp_backend.service.impl;

import com.opticalarc.emp_backend.dto.DepartmentDto;
import com.opticalarc.emp_backend.entity.Department;
import com.opticalarc.emp_backend.entity.Employee;
import com.opticalarc.emp_backend.mapper.DepartmentMapper;
import com.opticalarc.emp_backend.repository.DepartmentRepository;
import com.opticalarc.emp_backend.repository.EmployeeRepository;
import com.opticalarc.emp_backend.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDto> getAllDepartment() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(departmentMapper::departmentToDepartmentDto)
                .collect(Collectors.toList());
    }
}
