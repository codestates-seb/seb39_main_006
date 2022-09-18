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
import java.util.Optional;

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

    public GroupDto.Response updateGroup(Long groupId, GroupDto.Patch patchDto) {
        Group group = groupRepository.findById(groupId).orElseThrow();
        // TODO: Mapper에서 파싱 혹은 비즈니스 로직에서 파싱 일단은 후자로 함.

        Optional.ofNullable(patchDto.getHeadcount())
                .ifPresent(group::updateHeadcount);
        Optional.ofNullable(patchDto.getCloseDate())
                .ifPresent(group::updateCloseDate);

        groupRepository.save(group);
        return groupMapper.groupToResponseDto(group);
    }

    public void deleteGroup(Long groupId) {
        // TODO: 삭제 대신 비활성화
        groupRepository.deleteById(groupId);
    }
}
