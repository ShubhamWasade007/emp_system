package com.opticalarc.emp_backend.controller;

import com.opticalarc.emp_backend.dto.EmployeeDto;
import com.opticalarc.emp_backend.response.ApiResponse;
import com.opticalarc.emp_backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController  {

    @Autowired
    private EmployeeService employeeService;

    //Get All Employee
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployee(){
        List<EmployeeDto> employees = employeeService.findAllActiveEmployees();
        return ResponseEntity.ok(employees);
    }

    //create Employee
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto){
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    //Get Employee By id
    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long employeeId){
        EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employeeDto);
    }

    //update employee
    @PatchMapping("{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody EmployeeDto updateEmployee){
        EmployeeDto employeeDto =  employeeService.updateEmployee(employeeId, updateEmployee);
        return  ResponseEntity.ok(employeeDto);
    }

    //delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable("id") Long employeeId){
       employeeService.deleteEmployeeBySoftDelete(employeeId);
        return new  ResponseEntity<ApiResponse>(new ApiResponse("Employee Deleted Successfully!",true),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteEmployees(@PathVariable("id") Long employeeId){
        employeeService.deleteEmployeeByHardDelete(employeeId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Employee Deleted permanently",true),HttpStatus.OK);
    }

    //getAllEmployeeByDeptid
    @GetMapping("/department/{id}")
    public ResponseEntity<List<EmployeeDto>> getAllEmployeeByDepartmentId(@PathVariable("id") Long id){
        List<EmployeeDto> departmentById = employeeService.getAllEmployeeByDepartmentId(id);
        return new ResponseEntity<>(departmentById,HttpStatus.OK);
    }

    //getAllEmployeeByProjectId
    @GetMapping("/project/{id}")
    public ResponseEntity<List<EmployeeDto>> getAllEmployeeByProjectId(@PathVariable("id") Long id){
        List<EmployeeDto> projectId = employeeService.getAllEmployeeByProjectId(id);
        return new ResponseEntity<>(projectId,HttpStatus.OK);
    }



    //Get by Page
    @GetMapping("/page")
    public ResponseEntity<Page<EmployeeDto>> findAllActiveEmployees(@RequestParam(value = "pageNo",defaultValue = "0",required = false) int page,
                                                                  @RequestParam(value = "pageSize",defaultValue = "10",required = false)int size,
                                                                  @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                                  @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir){
        Page<EmployeeDto> employeePage = employeeService.getAllActiveEmployees(page, size, sortBy, sortDir);
        return new ResponseEntity<>(employeePage, HttpStatus.OK);
    }
}
