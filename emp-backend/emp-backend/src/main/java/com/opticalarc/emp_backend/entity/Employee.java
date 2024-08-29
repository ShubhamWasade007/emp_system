package com.opticalarc.emp_backend.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE employees SET is_Deleted = true WHERE id=?")
@Where(clause = "is_Deleted = false")
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String firstName;

    private String lastName;

    private String emailId;

    private String mobileNo;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",referencedColumnName = "id", nullable = false)
    private Project project;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_skills",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @Column(name = "is_Deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;

    private Integer sortOrder;
}

