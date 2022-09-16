package com.codestates.seb006main.group.mapper;

import com.codestates.seb006main.group.dto.GroupDto;
import com.codestates.seb006main.group.entity.Group;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    Group postDtoToGroup(GroupDto.Post postDto);
    Group patchDtoToGroup(GroupDto.Patch patchDto);
    GroupDto.Response groupToResponseDto(Group group);
    List<GroupDto.Response> groupListToResponseDtoList(List<Group> groupList);
}
