package com.opticalarc.emp_backend.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {

    private Long id;
    private String skillName;

}
