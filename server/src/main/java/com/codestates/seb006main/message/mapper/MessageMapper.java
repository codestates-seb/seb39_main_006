package com.codestates.seb006main.message.mapper;

import com.codestates.seb006main.message.dto.MessageDto;
import com.codestates.seb006main.message.entity.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDto.Response messageToResponseDto(Message message);
    List<MessageDto.Response> messageListToResponseDtoList(List<Message> messageList);
}
