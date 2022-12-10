package com.codestates.seb006main.chat.controller;

import com.codestates.seb006main.chat.dto.RoomDto;
import com.codestates.seb006main.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@RestController
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/rooms")
    public ResponseEntity postRoom(@RequestBody RoomDto.Post postDto) {
        log.info("# Create Chat Room, senderId: " + postDto.getMemberId() + ", receiverId: " + postDto.getOtherId());
        RoomDto.Response response = roomService.createRoom(postDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/rooms/{room-id}")
    public ResponseEntity getRoom(@PathVariable("room-id") String roomId, Authentication authentication) {
        log.info("# get Chat Room, roomID : " + roomId);
        RoomDto.Response response = roomService.getRoom(roomId, authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/rooms/check")
    public ResponseEntity checkRoom(@RequestParam String checkName, Authentication authentication) {
        log.info("# Check Chat Room Exist, checkName: " + checkName);
        RoomDto.Response response = roomService.checkRoom(checkName, authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/rooms")
    public ResponseEntity getRooms(Authentication authentication) {
        log.info("# All Chat Rooms");
        RoomDto.ResponseList response = roomService.getRooms(authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
