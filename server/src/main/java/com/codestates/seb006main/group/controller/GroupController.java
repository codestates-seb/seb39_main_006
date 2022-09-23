//package com.codestates.seb006main.group.controller;
//
//import com.codestates.seb006main.dto.MultiResponseDto;
//import com.codestates.seb006main.group.dto.GroupDto;
//import com.codestates.seb006main.group.service.GroupService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
///**
// * TODO:
// *  group의  CRD는 1단계에서는 posts 가 맡게 된다.
// *  group에 대한 개별적인 요청은 수정 정도일 것.
// *  2단계에서 group 생성을 통한 posts의 생성
// *  혹은 일정의 생성을 통한 group 생성을 구현할 것.
// */
//
//@RequiredArgsConstructor
//@RequestMapping("/api/groups")
//@RestController
//public class GroupController {
//    private final GroupService groupService;
//
//    @PostMapping
//    public ResponseEntity postGroup(@RequestBody GroupDto.Post postDto) {
//        GroupDto.Response responseDto = groupService.createGroup(postDto);
//        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{group-id}")
//    public ResponseEntity getGroup(@PathVariable("group-id") Long groupId) {
//        GroupDto.Response responseDto = groupService.readGroup(groupId);
//        return new ResponseEntity(responseDto, HttpStatus.OK);
//    }
//
//    @GetMapping
//    public ResponseEntity getAllGroups(@PageableDefault(sort = "groupId", direction = Sort.Direction.DESC)Pageable pageable) {
//        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
//        MultiResponseDto responseDto = groupService.readAllGroups(pageRequest);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
//    @PatchMapping("/{group-id}")
//    public ResponseEntity patchGroup(@PathVariable("group-id") Long groupId,
//                                     @RequestBody GroupDto.Patch patchDto) {
//        GroupDto.Response responseDto = groupService.updateGroup(groupId, patchDto);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{group-id}")
//    public ResponseEntity deleteGroup(@PathVariable("group-id") Long groupId) {
//        groupService.deleteGroup(groupId);
//        return new ResponseEntity<>("정상적으로 삭제되었습니다.",HttpStatus.NO_CONTENT);
//    }
//}
