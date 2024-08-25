package com.opticalarc.emp_backend.mapper;

import com.opticalarc.emp_backend.dto.SkillDto;
import com.opticalarc.emp_backend.entity.Skill;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SkillMapper {


    public Set<Skill> skillDtoToSkill(Set<SkillDto> skillDto){
        Set<Skill> skill = new HashSet<>();
        for(SkillDto skillDto1 : skillDto)
        {
            skill.add(Skill.builder().id(skillDto1.getId()).skillName(skillDto1.getSkillName()).build());
        }
        return  skill;
    }

    public Set<SkillDto> skillToSkillDto(Set<Skill> skills){
        Set<SkillDto> skillDto = new HashSet<>();
        for(Skill skill : skills)
        {
            skillDto.add(SkillDto.builder().id(skill.getId()).skillName(skill.getSkillName()).build());
        }
       return  skillDto;
    }


}
