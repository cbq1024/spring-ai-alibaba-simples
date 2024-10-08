package com.mcddhub.demo02.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
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

    @GetMapping("/single-by-string")
    public String singleByString(String input) {
        ChatResponse response = chatModel.call(new Prompt(input));
        return response.getResult().getOutput().getContent();
    }

    @GetMapping("/single-by-prompt")
    public String singleByPrompt(String input) {
        Prompt prompt = new Prompt(
            input,
            DashScopeChatOptions.builder().build());
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getContent();
    }

    @GetMapping("/chat-with-qwen-plus")
    public String chatWithQwenPlus(String input) {
        return this.chatModel
            .call(new Prompt(
                "Generate the names of 5 famous pirates.",
                DashScopeChatOptions.builder()
                    .withModel("qwen-plus")
                    .withTemperature(0.4F).build()).getContents());
    }

    @GetMapping("/stream")
    public String stream(String input) {
        StringBuilder builder = new StringBuilder();
        Flux<ChatResponse> responseFlux = chatModel.stream(new Prompt(input));
        responseFlux.toStream().toList().forEach(resp -> builder.append(resp.getResult().getOutput().getContent()));
        return builder.toString();
    }
}
