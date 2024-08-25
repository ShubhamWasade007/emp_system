package com.opticalarc.emp_backend.controller;

import com.opticalarc.emp_backend.dto.DepartmentDto;
import com.opticalarc.emp_backend.repository.DepartmentRepository;
import com.opticalarc.emp_backend.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
     public ResponseEntity<List<DepartmentDto>> getAllDepartment(){
        List<DepartmentDto> department = departmentService.getAllDepartment();
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

}
