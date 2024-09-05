package com.opticalarc.emp_backend.service.impl;

import com.opticalarc.emp_backend.dto.*;
import com.opticalarc.emp_backend.entity.*;
import com.opticalarc.emp_backend.exception.PageNotFoundException;
import com.opticalarc.emp_backend.exception.ResourceNotFoundException;
import com.opticalarc.emp_backend.mapper.*;
import com.opticalarc.emp_backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl underTest;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @Mock
    private EmployeeMapper employeeMapperMock;

    @Mock
    private SkillRepository skillRepositoryMock;

    @Mock
    private SkillMapper skillMapperMock;

    @Mock
    private AddressRepository addressRepositoryMock;

    @Mock
    private AddressMapper addressMapperMock;

    @Mock
    private ProjectMapper projectMapperMock;

    @Mock
    private ProjectRepository projectRepositoryMock;

    @Mock
    private DepartmentRepository departmentRepositoryMock;

    @Mock
    private DepartmentMapper departmentMapperMock;




    @BeforeEach
    void setUp() {MockitoAnnotations.openMocks(this);}




    @Test
    void test_findAllActiveEmployees() {
        when(employeeRepositoryMock.findAll()).thenReturn(List.of(createEmployee()));
        when(employeeMapperMock.employeeToEmployeeDto(createEmployee())).thenReturn(createEmployeeDto());

        List<EmployeeDto> result = underTest.findAllActiveEmployees();

        assertEquals(1, result.size());
        verify(employeeRepositoryMock, times(1)).findAll();
    }


    @Test
    void test_createEmployee_IfEmailIsAlreadyPresent() {

        when(employeeRepositoryMock.findByEmailId(createEmployeeDto().getEmailId())).thenReturn(Optional.of(createEmployee()));
        assertThrows(ResourceNotFoundException.class, () -> underTest.createEmployee(createEmployeeDto()));

        verify(employeeRepositoryMock, times(1)).findByEmailId(createEmployeeDto().getEmailId());
    }

    @Test
    void test_createEmployee(){
        Address address = Address.builder().zipCode("6776").build();

        when(employeeMapperMock.employeeDtoToEmployee(any(EmployeeDto.class))).thenReturn(createEmployee());
        when(addressMapperMock.addressDtoToAddress(any(AddressDto.class))).thenReturn(address);
        when(projectRepositoryMock.findByProjectName(createProjectDto().getProjectName())).thenReturn(Optional.of(createProject()));
        when(skillRepositoryMock.findBySkillName(any())).thenReturn(createSkill());
        when(departmentRepositoryMock.findByDepartmentName(createDepartmentDto().getDepartmentName())).thenReturn(Optional.of(createDepartment()));
        when(employeeRepositoryMock.save(any(Employee.class))).thenReturn(createEmployee());
        when(employeeMapperMock.employeeToEmployeeDto(any(Employee.class))).thenReturn(createEmployeeDto());

        EmployeeDto result = underTest.createEmployee(createEmployeeDto());

        assertNotNull(result);
        verify(employeeRepositoryMock, times(1)).save(any(Employee.class));
    }

    @Test
    void test_createEmployee_ProjectNotExists_CreatesNewProject(){
        Project project = createProject();
        project.setProjectName(createEmployeeDto().getProject().getProjectName());

        when(employeeMapperMock.employeeDtoToEmployee(any())).thenReturn(createEmployee());
        when(addressMapperMock.addressDtoToAddress(any())).thenReturn(createAddress());
        when(projectRepositoryMock.findByProjectName(any())).thenReturn(Optional.empty());
        when(projectRepositoryMock.save(any(Project.class))).thenReturn(project);

        EmployeeDto result = underTest.createEmployee(createEmployeeDto());

       verify(projectRepositoryMock, times(1)).findByProjectName(any());
    }



    @Test
    void test_getEmployeeById() {

      Long employeeId = 1L;
      when(employeeRepositoryMock.findById(any())).thenReturn(Optional.of(createEmployee()));
      when(employeeMapperMock.employeeToEmployeeDto(createEmployee())).thenReturn(createEmployeeDto());

      EmployeeDto result = underTest.getEmployeeById(employeeId);

      verify(employeeRepositoryMock, times(1)).findById(employeeId);
    }

    @Test
    void test_getEmployeeById_EmployeeNotFound(){

        Long employeeId = 1L;
        when(employeeRepositoryMock.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> underTest.getEmployeeById(employeeId));

        verify(employeeRepositoryMock, times(1)).findById(employeeId);
    }




    @Test
    void test_UpdateEmployee() {

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("Java");

        when(employeeRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(createEmployee()));
        when(skillRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(skill));
        when(skillRepositoryMock.save(any(Skill.class))).thenReturn(skill);

        EmployeeDto result = underTest.updateEmployee(1l, createEmployeeDto());
        verify(employeeRepositoryMock, times(1)).findById(1l);
    }

    @Test
    void test_updateEmployee_CreateNewSkill(){

        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(1L)
                .firstName("yyzy")
                .lastName("zzxz")
                .emailId("wwwzw")
                .mobileNo("1234567890")
                .address(createAddressDto())
                .department(createDepartmentDto())
                .project(createProjectDto())
                .skill(Set.of(SkillDto.builder().skillName("abc").build()))
                .build();
        Skill skill = Skill.builder().build();
        when(employeeRepositoryMock.findById(any())).thenReturn(Optional.of(createEmployee()));
        when(projectRepositoryMock.findByProjectName(any())).thenReturn(Optional.of(createProject()));
        when(skillRepositoryMock.findBySkillName(createSkill().getSkillName())).thenReturn(skill);
        EmployeeDto result = underTest.updateEmployee(1L, employeeDto);
       verify(employeeRepositoryMock, times(1)).findById(any());
    }

    @Test
    void test_updateEmployee_whenSkillIsAlreadyPresent(){

        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(1L)
                .firstName("yyzy")
                .lastName("zzxz")
                .emailId("wwwzw")
                .mobileNo("1234567890")
                .address(createAddressDto())
                .department(createDepartmentDto())
                .project(createProjectDto())
                .skill(Set.of(SkillDto.builder().skillName("abc").build()))
                .build();
        Skill skill = Skill.builder().skillName("abc").build();
        when(employeeRepositoryMock.findById(any())).thenReturn(Optional.of(createEmployee()));
        when(projectRepositoryMock.findByProjectName(any())).thenReturn(Optional.of(createProject()));
        when(skillRepositoryMock.findBySkillName(any())).thenReturn(skill);
        underTest.updateEmployee(1L, employeeDto);
    }

    @Test
    void test_updateEmployee_EmployeeNotFound(){

        Long employeeId = 1L;
        when(employeeRepositoryMock.findById(employeeId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, ()-> underTest.updateEmployee(employeeId, createEmployeeDto()));

        verify(employeeRepositoryMock,times(1)).findById(employeeId);
    }

    @Test
    void test_updateEmployee_SkillNotFound(){

        Long employeeId = 1L;
        SkillDto invalidSkillDto = new SkillDto();
        invalidSkillDto.setId(100L);
        invalidSkillDto.setSkillName("Python");
        Set<SkillDto> skillDtoSet = new HashSet<>();
        skillDtoSet.add(invalidSkillDto);
        createEmployeeDto().setSkill(skillDtoSet);

        when(employeeRepositoryMock.findById(employeeId)).thenReturn(Optional.of(createEmployee()));
        when(skillRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> underTest.updateEmployee(1L, createEmployeeDto()));

        verify(employeeRepositoryMock, times(1)).findById(employeeId);

    }

    @Test
    void test_deleteEmployeeBySoftDelete_WithException() {

        Long employeeId = 1L;
        when(employeeRepositoryMock.findById(employeeId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()-> underTest.deleteEmployeeBySoftDelete(employeeId));

        verify(employeeRepositoryMock,times(1)).findById(employeeId);
    }
    @Test
    public void test_deleteEmployeeBySoftDelete() {

        Employee employee = createEmployee();
        employee.setSkills(new HashSet<>());

        Skill skill1 = createSkill();
        skill1.setEmployees(new HashSet<>());

        Skill skill2 = new Skill();
        skill2.setId(2L);
        skill2.setSkillName("Python");
        skill2.setEmployees(new HashSet<>());

        employee.getSkills().add(skill1);
        employee.getSkills().add(skill2);
        skill1.getEmployees().add(employee);
        skill2.getEmployees().add(employee);

        when(employeeRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepositoryMock).deleteById(1L);

        underTest.deleteEmployeeBySoftDelete(1L);

        verify(employeeRepositoryMock, times(1)).deleteById(1L);

    }

    @Test
    void test_deleteEmployeeByHardDelete_WithException() {

        Long employeeId = 1L;
        when(employeeRepositoryMock.findById(employeeId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()-> underTest.deleteEmployeeByHardDelete(employeeId));

        verify(employeeRepositoryMock,times(1)).findById(employeeId);
    }

    @Test
    public void test_deleteEmployeeByHardDelete() {

        Employee employee = createEmployee();
        employee.setSkills(new HashSet<>());

        Skill skill1 = createSkill();
        skill1.setEmployees(new HashSet<>());

        Skill skill2 = new Skill();
        skill2.setId(2L);
        skill2.setSkillName("Python");
        skill2.setEmployees(new HashSet<>());

        employee.getSkills().add(skill1);
        employee.getSkills().add(skill2);
        skill1.getEmployees().add(employee);
        skill2.getEmployees().add(employee);

        when(employeeRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepositoryMock).hardDeleteById(1L);

        underTest.deleteEmployeeByHardDelete(1L);

        verify(employeeRepositoryMock, times(1)).hardDeleteById(1L);

    }

    @Test
    void test_getAllActiveEmployeesByPage() {

        int page = 0;
        int size = 5;
        String sortBy = "id";
        String sortDir =  "asc";
        Employee employee = new Employee();
        employee = createEmployee();

        List<Employee> employees = Arrays.asList(employee);
        Page<Employee> employeePage = new PageImpl<>(employees);

        when(employeeRepositoryMock.findAll(any(Pageable.class))).thenReturn(employeePage);
        when(employeeMapperMock.employeeToEmployeeDto(employee)).thenReturn(createEmployeeDto());

        Page<EmployeeDto> result = underTest.getAllActiveEmployees(page, size, sortBy, sortDir);

        assertNotNull(result);
        verify(employeeRepositoryMock, times(1)).findAll(any(Pageable.class));

    }

    @Test
    void test_getAllActiveEmployees_PageNotFound(){

        int page = 1;
        int size = 5;
        String sortBy = "id";
        String sortDir = "asc";

        List<Employee> employees = Collections.emptyList();
        Page<Employee> employeePage = new PageImpl<>(employees, PageRequest.of(0, size), 0);

        when(employeeRepositoryMock.findAll(any(Pageable.class))).thenReturn(employeePage);

        assertThrows(PageNotFoundException.class, ()-> underTest.getAllActiveEmployees(page, size, sortBy, sortDir));

        verify(employeeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void test_getAllEmployeeByDepartmentId() {

        Long departmentId = 1L;
        Employee employee = new Employee();
        employee = createEmployee();

        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepositoryMock.findByDepartment_Id(departmentId)).thenReturn(employees);
        when(employeeMapperMock.employeeToEmployeeDto(employee)).thenReturn(createEmployeeDto());

        List<EmployeeDto> result = underTest.getAllEmployeeByDepartmentId(departmentId);

        assertNotNull(result);
        verify(employeeRepositoryMock, times(1)).findByDepartment_Id(departmentId);
    }

    @Test
    void test_getAllEmployeeByDepartmentId_InvalidDepartmentId(){
        Long departmentId = 1L;
        when(employeeRepositoryMock.findByDepartment_Id(departmentId)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, ()-> underTest.getAllEmployeeByDepartmentId(departmentId));
        verify(employeeRepositoryMock, times(1)).findByDepartment_Id(departmentId);
    }

    @Test
    void test_getAllEmployeeByProjectId() {

        Long projectId = 1L;
        Employee employee = new Employee();
        employee = createEmployee();

        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepositoryMock.findByProject_Id(projectId)).thenReturn(employees);
        when(employeeMapperMock.employeeToEmployeeDto(employee)).thenReturn(createEmployeeDto());

        List<EmployeeDto> result = underTest.getAllEmployeeByProjectId(projectId);

        assertNotNull(result);
        verify(employeeRepositoryMock, times(1)).findByProject_Id(projectId);
    }

    @Test
    void test_getAllEmployeeByProjectId_InvalidProject(){
        Long projectId = 1L;
        when(employeeRepositoryMock.findByProject_Id(projectId)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, ()-> underTest.getAllEmployeeByProjectId(projectId));
        verify(employeeRepositoryMock, times(1)).findByProject_Id(projectId);
    }


    private EmployeeDto createEmployeeDto(){
        return EmployeeDto.builder()
                .id(1L)
                .firstName("yyzy")
                .lastName("zzxz")
                .emailId("wwwzw")
                .mobileNo("1234567890")
                .address(createAddressDto())
                .department(createDepartmentDto())
                .project(createProjectDto())
                .skill(createSkillDto())
                .build();
    }

    private AddressDto createAddressDto(){
        return AddressDto.builder()
                .id(1L)
                .street("123weew")
                .city("casd")
                .state("wawcsdc")
                .zipCode("231564")
                .build();
    }

    private DepartmentDto createDepartmentDto(){
        return DepartmentDto.builder()
                .id(1L)
                .departmentName("IT")
                .build();
    }

    private ProjectDto createProjectDto(){
        return ProjectDto.builder()
                .id(1L)
                .projectName("EM")
                .build();
    }

    private Set<SkillDto> createSkillDto(){
        SkillDto skillDto = SkillDto.builder()
                .id(1L)
                .skillName("java")
                .build();
        return Set.of(skillDto);
    }

    private Employee createEmployee(){
        return Employee.builder()
                .id(1L)
                .firstName("yyzy")
                .lastName("zzxz")
                .emailId("wwwzw")
                .mobileNo("1234567890")
                .address(createAddress())
                .department(createDepartment())
                .project(createProject())
                .skills(Set.of(createSkill()))
                .build();
    }

    private Address createAddress(){
        return Address.builder()
                .id(1L)
                .street("123weew")
                .city("casd")
                .state("wawcsdc")
                .zipCode("231564")
                .build();
    }

    private Department createDepartment(){
        return Department.builder()
                .id(1L)
                .departmentName("IT")
                .build();
    }

    private Project createProject(){
        return Project.builder()
                .id(1L)
                .projectName("EM")
                .build();
    }

    private Skill createSkill(){
        Skill skill = new Skill();
        Employee employee = Employee.builder().id(1L).firstName("abc").build();
        skill.setEmployees(Set.of(employee));
        skill.setSkillName("java");
//        Skill skill = Skill.builder()
//                .id(1L)
//                .skillName("java")
//                .employees(Set.of(Employee.builder().id(1L).firstName("abc").build()))
//                .build();
        return skill;
    }
}