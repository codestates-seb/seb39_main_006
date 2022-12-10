package com.codestates.seb006main.chat.service;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.chat.dto.RoomDto;
import com.codestates.seb006main.chat.entity.Room;
import com.codestates.seb006main.chat.repository.RoomRepository;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    public RoomDto.Response createRoom(RoomDto.Post postDto) {
        String senderName = memberRepository.findById(postDto.getMemberId()).orElseThrow().getDisplayName();
        String receiverName = memberRepository.findById(postDto.getOtherId()).orElseThrow().getDisplayName();

        Room room = Room.builder()
                .memberId(postDto.getMemberId())
                .otherId(postDto.getOtherId())
                // TODO: 채팅방의 제목으로 구분을 지어주는 것이 좋을까?
                .name("[" + senderName + "]님과 [" + receiverName + "]님의 채팅방")
                .build();
        roomRepository.save(room);

        return RoomDto.Response.builder()
                .roomId(room.getRoomId().toString())
                .name(room.getName())
                .memberId(room.getMemberId())
                .otherId(room.getOtherId())
                .build();
    }

    public RoomDto.Response getRoom(String roomId, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getMember().getMemberId();

        Room room = roomRepository.findById(UUID.fromString(roomId))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ROOM_NOT_FOUND));

        if (room.getMemberId() != memberId && room.getOtherId() != memberId) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }

        return RoomDto.Response.builder()
                .roomId(room.getRoomId().toString())
                .name(room.getName())
                .memberId(room.getMemberId())
                .otherId(room.getOtherId())
                .build();
    }

    // Room List를 구분할 필요가 없다.
    public RoomDto.ResponseList getRooms(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getMember().getMemberId();

        List<Room> sentRoomList = roomRepository.findSentRooms(memberId);
        List<Room> receivedRoomList = roomRepository.findReceivedRooms(memberId);

        return RoomDto.ResponseList.builder()
                .memberId(memberId)
                .sentRoomList(sentRoomList
                        .stream()
                        .map(room -> RoomDto.Response.builder()
                                .roomId(room.getRoomId().toString())
                                .name(room.getName())
                                .memberId(room.getMemberId())
                                .otherId(room.getOtherId())
                                .build()).collect(Collectors.toList()))
                .receivedRoomList(receivedRoomList
                        .stream()
                        .map(room -> RoomDto.Response.builder()
                                .roomId(room.getRoomId().toString())
                                .name(room.getName())
                                .memberId(room.getMemberId())
                                .otherId(room.getOtherId())
                                .build()).collect(Collectors.toList()))
                .build();

    }

    public RoomDto.Response checkRoom(String checkName, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getMember().getMemberId();

        Member member = memberRepository.findByDisplayName(checkName).orElseThrow();
        Long checkMemberId = member.getMemberId();

        Room room = roomRepository.findByBothId(memberId, checkMemberId)
                .orElseGet(() -> roomRepository.findByBothId(checkMemberId, memberId).orElseThrow());

        return RoomDto.Response.builder()
                .roomId(room.getRoomId().toString())
                .name(room.getName())
                .memberId(room.getMemberId())
                .otherId(room.getOtherId())
                .build();
    }
}
