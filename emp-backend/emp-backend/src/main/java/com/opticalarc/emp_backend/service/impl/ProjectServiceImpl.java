package com.opticalarc.emp_backend.service.impl;

import com.opticalarc.emp_backend.dto.ProjectDto;
import com.opticalarc.emp_backend.entity.Project;
import com.opticalarc.emp_backend.mapper.ProjectMapper;
import com.opticalarc.emp_backend.repository.ProjectRepository;
import com.opticalarc.emp_backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<ProjectDto> getAllProject() {
            List<Project> projects = projectRepository.findAll();
            return projects.stream().map(projectMapper::projectToProjectDto)
                    .collect(Collectors.toList());
    }
}
