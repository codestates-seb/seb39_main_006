package com.codestates.seb006main.chat.repository;

import com.codestates.seb006main.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    @Query(value = "select * from room where member_id=:memberId", nativeQuery = true)
    List<Room> findSentRooms(Long memberId);

    @Query(value = "select * from room where other_id=:memberId", nativeQuery = true)
    List<Room> findReceivedRooms(Long memberId);

    @Query(value = "select * from room where member_id=:memberId and other_id=:otherId", nativeQuery = true)
    Optional<Room> findByBothId(Long memberId, Long otherId);

//    private Map<String, ChatDto.Room> chatRoomMap;
//
//    @PostConstruct
//    private void init() {
//        chatRoomMap = new LinkedHashMap<>();
//    }
//
//    public List<ChatDto.Room> findAllRooms() {
//        List<ChatDto.Room> result = new ArrayList<>(chatRoomMap.values());
//        Collections.reverse(result);
//
//        return result;
//    }
//
//    public ChatDto.Room findRoomById(String id) {
//        return chatRoomMap.get(id);
//    }
//
//    public ChatDto.Room createChatRoomDto(String name) {
//        ChatDto.Room room = ChatDto.Room.create(name);
//        chatRoomMap.put(room.getRoomId(), room);
//
//        return room;
//    }
}
