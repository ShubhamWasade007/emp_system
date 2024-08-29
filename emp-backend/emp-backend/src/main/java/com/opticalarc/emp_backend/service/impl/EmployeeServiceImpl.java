package com.opticalarc.emp_backend.service.impl;

import com.opticalarc.emp_backend.dto.*;
import com.opticalarc.emp_backend.entity.*;
import com.opticalarc.emp_backend.exception.PageNotFoundException;
import com.opticalarc.emp_backend.exception.ResourceNotFoundException;
import com.opticalarc.emp_backend.mapper.*;
import com.opticalarc.emp_backend.mapper.ProjectMapper;
import com.opticalarc.emp_backend.repository.*;
import com.opticalarc.emp_backend.service.EmployeeService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

   @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private EntityManager entityManager;


    @Override
    public List<EmployeeDto> findAllActiveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(employeeMapper::employeeToEmployeeDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        if (employeeRepository.findByEmailId(employeeDto.getEmailId()).isPresent()) {
            throw new ResourceNotFoundException("Already Have That Email");
        }
        Employee employee = employeeMapper.employeeDtoToEmployee(employeeDto);

        if (employeeDto.getAddress() != null){
            AddressDto addressDto = employeeDto.getAddress();
            Address address = addressMapper.addressDtoToAddress(addressDto);
            if (address.getId() == null){
                address = addressRepository.save(address);
            }
            employee.setAddress(address);
        }

        Project project = projectRepository.findByProjectName(employeeDto.getProject().getProjectName())
                .orElseGet(() -> {
                    Project project1 = new Project();
                    project1.setProjectName(employeeDto.getProject().getProjectName());
                    return projectRepository.save(project1);
                });

        employee.setProject(project);
        employee.setProject(employee.getProject());


        Set<Skill> skillSet = new HashSet<>();
        for(SkillDto skillDto : employeeDto.getSkill()) {
            Skill skill = skillRepository.findBySkillName(skillDto.getSkillName());
            if(skill==null)
            {
                Skill skill1 = new Skill();
                skill1.setSkillName(skillDto.getSkillName());
                 skillRepository.save(skill1);
                 skillSet.add(skill1);
            }
            else{
                skillSet.add(skill);
            }
        }
            employee.setSkills(skillSet);

        Department department = departmentRepository.findByDepartmentName(employeeDto.getDepartment().getDepartmentName())
                .orElseGet(() -> {
                    Department newDepartment = new Department();
                    newDepartment.setDepartmentName(employeeDto.getDepartment().getDepartmentName());
                    return departmentRepository.save(newDepartment);
                });

        employee.setDepartment(department);
        employee.setDepartment(employee.getDepartment());

        Employee sevedEmployee = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeDto(sevedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found By This Id: " + employeeId));
        return employeeMapper.employeeToEmployeeDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Exists With given id: " + employeeId));
        if (employeeDto.getFirstName() != null) {
            employee.setFirstName(employeeDto.getFirstName());
        }
        if (employeeDto.getLastName() != null) {
            employee.setLastName(employeeDto.getLastName());
        }
        if (employeeDto.getEmailId() != null) {
            employee.setEmailId(employeeDto.getEmailId());
        }
        //address update
        if (employeeDto.getAddress() != null) {
            Address existingAddress = employee.getAddress();
            AddressDto addressDto = employeeDto.getAddress();

            if (addressDto.getStreet() != null) {
                existingAddress.setStreet(addressDto.getStreet());
            }
            if (addressDto.getCity() != null) {
                existingAddress.setCity(addressDto.getCity());
            }
            if (addressDto.getState() != null) {
                existingAddress.setState(addressDto.getState());
            }
            if (addressDto.getZipCode() != null) {
                existingAddress.setZipCode(addressDto.getZipCode());
            }

            employee.setAddress(existingAddress);
        }

        // Update Projects
        if (employeeDto.getProject() != null) {
            ProjectDto projectDto = employeeDto.getProject();
            Project project = projectRepository.findByProjectName(projectDto.getProjectName())
                    .orElseGet(() -> {
                        Project project1 = projectMapper.projectDtoToProject(projectDto);
                        return projectRepository.save(project1);
                    });
            employee.setProject(project);
        }


        // update skill
        if (employeeDto.getSkill() != null) {
            Set<Skill> existingSkill = employee.getSkills();
            Set<Skill> updatedSkills = new HashSet<>();

            for (SkillDto skillDto : employeeDto.getSkill()) {
                Skill skill;
                if (skillDto.getId() != null) {
                    skill = skillRepository.findById(skillDto.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id " + skillDto.getId()));
                    if (skillDto.getSkillName() != null) {
                        skill.setSkillName(skillDto.getSkillName());
                    }
                } else {
                     skill = skillRepository.findBySkillName(skillDto.getSkillName());
                      if(skill == null) {
                         skill= Skill.builder().skillName(skillDto.getSkillName()).build();
                      }
                      else{
                          log.info("Skill Already present");
                      }
                }

                if (skill.getEmployees() == null) {
                    skill.setEmployees(new HashSet<>());
                }
                skill.getEmployees().add(employee);
                updatedSkills.add(skill);
            }

            for (Skill skill : existingSkill) {
                if (!updatedSkills.contains(skill)) {
                    updatedSkills.add(skill);
                }
            }
            employee.setSkills(updatedSkills);
        }

        // Update Department
        if (employeeDto.getDepartment() != null) {
            DepartmentDto departmentDto = employeeDto.getDepartment();
            Department department = departmentRepository.findByDepartmentName(departmentDto.getDepartmentName())
                    .orElseGet(() -> {
                        Department newDepartment = departmentMapper.departmentDtoToDepartment(departmentDto);
                        return departmentRepository.save(newDepartment);
                    });
            employee.setDepartment(department);
        }

        Employee updatedEmployeeObj = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeDto(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployeeBySoftDelete(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found By This Id: " + employeeId));
        for (Skill skill:employee.getSkills()){
            skill.getEmployees().remove(employee);
        }
        employee.setDeleted(true);
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public void deleteEmployeeByHardDelete(Long employeeId) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found By This Id: " + employeeId));
            for (Skill skill : employee.getSkills()) {
                skill.getEmployees().remove(employee);
            }
            employeeRepository.hardDeleteById(employeeId);
    }


    @Override
    public Page<EmployeeDto> getAllActiveEmployees(int page, int size, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc"))? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
/*        List<Employee> employees = employeeRepository.findAllSorted(sortBy, sortDir);*/
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        if(page >= employeePage.getTotalPages()){
            throw new PageNotFoundException("Page Not Found!");
        }
        int order = page * size + 1;  // Calculate initial order value based on page and size
        for (Employee employee : employeePage.getContent()) {
            employee.setSortOrder(order++);
            employeeRepository.save(employee);  // Save the updated sortOrder back to the database
        }
        return employeePage.map(employeeMapper::employeeToEmployeeDto);
   }

   //getAllEmployeeByDepartmentId
    @Override
    public List<EmployeeDto> getAllEmployeeByDepartmentId(Long id) {
        List<Employee> byDepartmentId = employeeRepository.findByDepartment_Id(id);
        if(byDepartmentId.isEmpty())
        {
            throw new ResourceNotFoundException("Given department id is not found, Please provide valid department id or " +
                    "In This Department Don't Have any Employee");
        }
        return byDepartmentId.stream().map(employeeMapper::employeeToEmployeeDto).collect(Collectors.toList());
    }

    //getAllEmployeeByProjectId
    @Override
    public List<EmployeeDto> getAllEmployeeByProjectId(Long id) {
        List<Employee> projectId = employeeRepository.findByProject_Id(id);
        if(projectId.isEmpty())
        {
            throw new ResourceNotFoundException("Given projectId is not found, Please provide valid project id or " +
                    "In This Project No Employee Assign");
        }
        return projectId.stream().map(employeeMapper::employeeToEmployeeDto).collect(Collectors.toList());
    }


}
