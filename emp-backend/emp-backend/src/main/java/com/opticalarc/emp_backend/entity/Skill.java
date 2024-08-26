package com.opticalarc.emp_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String skillName;

    @ManyToMany(mappedBy = "skills",cascade = CascadeType.ALL)
    private Set<Employee> employees;

}
