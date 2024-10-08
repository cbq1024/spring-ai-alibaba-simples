package com.mcddhub.demo02.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/demo02/chats")
public class ChatModelController {

    private final ChatModel chatModel;

    public ChatModelController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/single")
    public String chat(String input) {
        ChatResponse response = chatModel.call(new Prompt(input));
        return response.getResult().getOutput().getContent();
    }

    @GetMapping("/stream")
    public String stream(String input) {
        StringBuilder builder = new StringBuilder();
        Flux<ChatResponse> stream = chatModel.stream(new Prompt(input));
        stream.toStream().toList().forEach(resp -> builder.append(resp.getResult().getOutput().getContent()));
        return builder.toString();
    }
}
