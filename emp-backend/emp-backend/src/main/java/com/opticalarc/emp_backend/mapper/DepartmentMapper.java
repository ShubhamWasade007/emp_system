package com.opticalarc.emp_backend.mapper;

import com.opticalarc.emp_backend.dto.DepartmentDto;
import com.opticalarc.emp_backend.entity.Department;
import org.springframework.stereotype.Service;

@Service
public class DepartmentMapper {

    public DepartmentDto departmentToDepartmentDto(Department department){
        return DepartmentDto.builder()
                .id(department.getId())
                .departmentName(department.getDepartmentName()). build();
    }


    public Department departmentDtoToDepartment(DepartmentDto departmentDto){
        return Department.builder()
                .id(departmentDto.getId())
                .departmentName(departmentDto.getDepartmentName()). build();
    }

}
