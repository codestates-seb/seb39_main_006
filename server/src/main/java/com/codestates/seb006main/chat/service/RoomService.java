package com.codestates.seb006main.chat.service;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.chat.dto.ChatDto;
import com.codestates.seb006main.chat.dto.RoomDto;
import com.codestates.seb006main.chat.entity.Chat;
import com.codestates.seb006main.chat.entity.Room;
import com.codestates.seb006main.chat.mapper.RoomMapper;
import com.codestates.seb006main.chat.repository.ChatRepository;
import com.codestates.seb006main.chat.repository.RoomRepository;
import com.codestates.seb006main.config.websocket.StompHandler;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.repository.MemberRepository;
import com.codestates.seb006main.util.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final StompHandler stompHandler;
    private final ApplicationEventPublisher applicationEventPublisher;

    public RoomDto.Response createRoom(RoomDto.Post postDto, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();

        if (member.getMemberId() == postDto.getOtherId()) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }

        Member other = memberRepository.findById(postDto.getOtherId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        if (verifyRoom(member.getMemberId(), other.getMemberId())) {
            log.info("[" + member.getDisplayName() + "]님과 [" + other.getDisplayName() + "]님의 채팅방 개설");
            Room room = Room.builder()
                    .memberId(member.getMemberId())
                    .otherId(other.getMemberId())
                    .name("[" + member.getDisplayName() + "]님과 [" + other.getDisplayName() + "]님의 채팅방")
                    .build();
            roomRepository.save(room);

            DomainEvent.ChatEvent chatEvent = new DomainEvent.ChatEvent(room.getRoomId().toString(), room.getName(), member, other);
            applicationEventPublisher.publishEvent(new DomainEvent(this, chatEvent, DomainEvent.EventType.CREATE_ROOM));

            return roomMapper.roomToResponse(room);
        }

        log.info("방이 이미 존재하므로 요청만 보낸다.");
        Room room = roomRepository.findByBothId(member.getMemberId(), other.getMemberId())
                .orElseGet(() -> roomRepository.findByBothId(other.getMemberId(), member.getMemberId())
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ROOM_NOT_FOUND)));

        DomainEvent.ChatEvent chatEvent = new DomainEvent.ChatEvent(room.getRoomId().toString(), room.getName(), member, other);
        applicationEventPublisher.publishEvent(new DomainEvent(this, chatEvent, DomainEvent.EventType.CREATE_ROOM));

        return roomMapper.roomToResponse(room);
    }

    public RoomDto.Response getRoom(String roomId, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getMember().getMemberId();

        Room room = roomRepository.findById(UUID.fromString(roomId))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ROOM_NOT_FOUND));

        if (room.getMemberId() != memberId && room.getOtherId() != memberId) {
            log.info("채팅방 참가 권한 없음");
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }

        log.info("채팅방 참가");

        return roomMapper.roomToResponse(room);
    }

    public MultiResponseDto<ChatDto.Message> loadChat(String roomId, PageRequest pageRequest, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();

        Room room = roomRepository.findById(UUID.fromString(roomId))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ROOM_NOT_FOUND));

        if (member.getMemberId() != room.getMemberId() && member.getMemberId() != room.getOtherId()) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }

        LocalDateTime lastConnected = stompHandler.sessionMap.get(member.getEmail()).getLastConnected();
        // TODO: SQL 쿼리 수정으로 1. 내림차순으로 불러온 뒤, 2. 내부에서는 오름차순으로 정렬
        Page<Chat> chatPage = chatRepository.loadChatByRoomId(roomId, pageRequest, lastConnected);
        List<Chat> chatList = new ArrayList<>(chatPage.getContent());
        chatList.sort((Comparator.comparing(Chat::getChatId)));

        return new MultiResponseDto<>(
                chatList.stream()
                .map(chat -> ChatDto.Message.builder()
                        .roomId(chat.getRoomId())
                        .senderId(chat.getSenderId())
                        .message(chat.getMessage()).build())
                .collect(Collectors.toList()), chatPage);
    }

    // Room List를 구분할 필요가 없다.
    public RoomDto.ResponseList getRooms(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getMember().getMemberId();

        List<Room> sentRoomList = roomRepository.findSentRooms(memberId);
        List<Room> receivedRoomList = roomRepository.findReceivedRooms(memberId);

        log.info("채팅방 목록 불러오기");

        return RoomDto.ResponseList.builder()
                .memberId(memberId)
                .sentRoomList(sentRoomList
                        .stream()
                        .map(room -> RoomDto.Response.builder()
                                .roomId(room.getRoomId().toString())
                                .name(room.getName())
                                .memberId(room.getMemberId())
                                .otherId(room.getOtherId())
                                .build())
                        .collect(Collectors.toList()))
                .receivedRoomList(receivedRoomList
                        .stream()
                        .map(room -> RoomDto.Response.builder()
                                .roomId(room.getRoomId().toString())
                                .name(room.getName())
                                .memberId(room.getMemberId())
                                .otherId(room.getOtherId())
                                .build())
                        .collect(Collectors.toList()))
                .build();

    }

    public RoomDto.Response checkRoom(String checkName, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getMember().getMemberId();

        Long checkMemberId = memberRepository.findByDisplayName(checkName)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND))
                .getMemberId();

        Room room = roomRepository.findByBothId(memberId, checkMemberId)
                .orElseGet(() -> roomRepository.findByBothId(checkMemberId, memberId)
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ROOM_NOT_FOUND)));

        return roomMapper.roomToResponse(room);
    }

    public boolean verifyRoom(Long memberId, Long otherId) {
        return roomRepository.findByBothId(memberId, otherId).isEmpty() && roomRepository.findByBothId(otherId, memberId).isEmpty();
    }
    
    // TODO: 권한 체크
    public void checkPermission() {}
}
