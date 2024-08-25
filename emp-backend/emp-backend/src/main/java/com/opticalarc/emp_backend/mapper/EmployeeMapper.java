package com.opticalarc.emp_backend.mapper;

import com.opticalarc.emp_backend.dto.EmployeeDto;
import com.opticalarc.emp_backend.entity.Employee;
/*import com.opticalarc.emp_backend.entity.Project;
import com.opticalarc.emp_backend.entity.Skill;*/
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMapper {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private ProjectMapper projectMapper ;
    @Autowired
    private SkillMapper skillMapper;
   @Autowired
    private DepartmentMapper departmentMapper;


    public EmployeeDto employeeToEmployeeDto(Employee employee){
        return EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .emailId(employee.getEmailId())
                .mobileNo(employee.getMobileNo())
                .address(addressMapper.addressToAddressDto(employee.getAddress()))
                .project(projectMapper.projectToProjectDto(employee.getProject()))
                .skill(skillMapper.skillToSkillDto(employee.getSkills()))
                .department(departmentMapper.departmentToDepartmentDto(employee.getDepartment())).build();
    }

    public Employee employeeDtoToEmployee(EmployeeDto employeeDto){
        return Employee.builder()
                .id(employeeDto.getId())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .emailId(employeeDto.getEmailId())
                .mobileNo(employeeDto.getMobileNo())
                .address(addressMapper.addressDtoToAddress(employeeDto.getAddress()))
               .project(projectMapper.projectDtoToProject(employeeDto.getProject()))
                .skills(skillMapper.skillDtoToSkill(employeeDto.getSkill()))
                .department(departmentMapper.departmentDtoToDepartment(employeeDto.getDepartment())).build();
    }
}
