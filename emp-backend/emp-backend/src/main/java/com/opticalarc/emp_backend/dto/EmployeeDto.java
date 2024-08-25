package com.opticalarc.emp_backend.dto;

import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobileNo;
    private AddressDto address;
    private ProjectDto project;
    private Set<SkillDto> skill;
    private DepartmentDto department;
}

