package com.mcddhub.demo01;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RestController
@RequestMapping("/demo01")
public class HelloWorldController {

    private final ChatClient chatClient;

    public HelloWorldController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String chat(String input) {
        return this.chatClient
            .prompt()
            .user(input)
            .call()
            .content();
    }

    @GetMapping("/stream")
    public String stream(String input) {
        Flux<String> content = this.chatClient
            .prompt()
            .user(input)
            .stream()
            .content();
        return Objects.requireNonNull(content.collectList().block())
            .stream()
            .reduce((a, b) -> a + b)
            .get();
    }
}
