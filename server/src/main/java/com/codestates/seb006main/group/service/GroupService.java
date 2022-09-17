package com.codestates.seb006main.group.service;

import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.group.dto.GroupDto;
import com.codestates.seb006main.group.entity.Group;
import com.codestates.seb006main.group.mapper.GroupMapper;
import com.codestates.seb006main.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupDto.Response createGroup(GroupDto.Post postDto) {
        Group group = groupMapper.postDtoToGroup(postDto);
        groupRepository.save(group);
        return groupMapper.groupToResponseDto(group);
    }

    public GroupDto.Response readGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow();
        return groupMapper.groupToResponseDto(group);
    }

    public MultiResponseDto readAllGroups(PageRequest pageRequest) {
        Page<Group> groupPage = groupRepository.findAll(pageRequest);
        List<Group> groupList = groupPage.getContent();
        return new MultiResponseDto(groupMapper.groupListToResponseDtoList(groupList), groupPage);
    }

    public GroupDto.Response updateGroup(GroupDto.Patch patchDto) {
        return null;
    }

    public void deleteGroup(Long groupId) {
        // TODO: 삭제 대신 비활성화
        groupRepository.deleteById(groupId);
    }
}
