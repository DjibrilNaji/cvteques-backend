package com.cvteques.mapper;

import com.cvteques.dto.CvDto;
import com.cvteques.dto.UserDto;
import com.cvteques.entity.Cv;
import com.cvteques.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class UserMapper {
  public static UserDto toDto(User user) {
    UserDto dto = new UserDto();
    dto.setId(user.getId());
    dto.setFirstname(user.getFirstname());
    dto.setLastname(user.getLastname());
    dto.setEmail(user.getEmail());
    dto.setRole(user.getRole());
    dto.setSchool(user.getSchool());

    return dto;
  }

  public static CvDto toCvDto(Cv cv) {
    CvDto dto = new CvDto();
    dto.setId(cv.getId());
    dto.setTitle(cv.getTitle());
    dto.setUrl(cv.getUrl());
    dto.setIntervenantId(cv.getIntervenantId());
    dto.setUpdatedAt(cv.getUpdatedAt());
    return dto;
  }
}
