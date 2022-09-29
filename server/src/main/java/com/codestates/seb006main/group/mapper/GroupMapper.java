package com.codestates.seb006main.group.mapper;

import com.codestates.seb006main.group.dto.GroupDto;
import com.codestates.seb006main.group.entity.Group;
import com.codestates.seb006main.util.Period;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    //    Group postDtoToGroup(GroupDto.Post postDto);
    default Group postDtoToGroup(GroupDto.Post postDto) {
        if (postDto == null) {
            return null;
        }

        Group.GroupBuilder group = Group.builder();

        group.location(postDto.getLocation());
        group.travelPeriod(new Period(
                LocalDate.parse(postDto.getStartDate()), LocalDate.parse(postDto.getEndDate())));
        group.headcount(postDto.getHeadcount());
        group.closeDate(LocalDate.parse(postDto.getCloseDate()));

        return group.build();
    }

    Group patchDtoToGroup(GroupDto.Patch patchDto);

    GroupDto.Response groupToResponseDto(Group group);

    List<GroupDto.Response> groupListToResponseDtoList(List<Group> groupList);
}
