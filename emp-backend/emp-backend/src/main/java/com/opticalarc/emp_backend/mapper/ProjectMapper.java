package com.opticalarc.emp_backend.mapper;

import com.opticalarc.emp_backend.dto.DepartmentDto;
import com.opticalarc.emp_backend.dto.ProjectDto;
import com.opticalarc.emp_backend.entity.Department;
import com.opticalarc.emp_backend.entity.Project;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProjectMapper {


    public ProjectDto projectToProjectDto(Project projects){
        return ProjectDto.builder()
                .id(projects.getId())
                .projectName(projects.getProjectName()). build();
    }

    public Project projectDtoToProject(ProjectDto projectDto){
        return Project.builder()
                .id(projectDto.getId())
                .projectName(projectDto.getProjectName()). build();
    }

}
