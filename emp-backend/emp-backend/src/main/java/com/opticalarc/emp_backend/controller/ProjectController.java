package com.opticalarc.emp_backend.controller;

import com.opticalarc.emp_backend.dto.DepartmentDto;
import com.opticalarc.emp_backend.dto.ProjectDto;
import com.opticalarc.emp_backend.mapper.ProjectMapper;
import com.opticalarc.emp_backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/employees/project")
public class ProjectController {


    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProject(){
        List<ProjectDto> project = projectService.getAllProject();
        return new ResponseEntity<>(project, HttpStatus.OK);
    }


}
