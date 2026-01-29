package com.apiorbit.lovableclone.mapper;

import com.apiorbit.lovableclone.dto.chat.ChatResponse;
import com.apiorbit.lovableclone.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(source = "chatMessages.chatEvents", target = "chatEvents")
    List<ChatResponse> fromListOfChatMessages(List<ChatMessage> chatMessages);
}
