package com.mcddhub.demo01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/demo01")
public class ChatClientController {

    private static final Logger logger = LoggerFactory.getLogger(ChatClientController.class);

    private final ChatClient chatClient;

    public ChatClientController(ChatClient.Builder builder, SimpleLoggerAdvisor loggerAdvisor) {
        this.chatClient = builder
            .defaultAdvisors(loggerAdvisor)
//            .defaultSystem("You are a friendly chat bot that answers question in the voice of a {voice}")
            .build();
    }

    @GetMapping("/sync")
    public String chat(String input) {
        return this.chatClient
            .prompt()
            .user(input)
            .call()
            .content();
    }

    @GetMapping("/streams")
    public String stream(String input) {
        Flux<String> content = this.chatClient
            .prompt()
            .user(input)
            .stream()
            .content();
        return Objects.requireNonNull(content.collectList().block())
            .stream()
            .reduce((a, b) -> a + b)
            .orElseThrow(() -> new RuntimeException("No streams available"));
    }

    @GetMapping("/return-chat-response")
    public String returnChatResponse(String input) {
        ChatResponse chatResponse = this.chatClient
            .prompt()
            .user(input)
            .call()
            .chatResponse();
        logger.info("the chat response: {}", chatResponse);
        logger.info("the chat response`s results: : {}", chatResponse.getResults());
        logger.info("the chat response`s metadata: {}", chatResponse.getMetadata().toString());

        return chatResponse.toString();
    }

    @GetMapping("/return-entity")
    public List<ActorFilms> returnChatEntity() {
        return this.chatClient.prompt()
            .user("Generate the filmography for a random actor.")
            .call()
            .entity(new ParameterizedTypeReference<>() {
            });
    }

    @GetMapping("/completion")
    public Map<String, String> completion(
        @RequestParam(value = "input", defaultValue = "Tell me a joker") String input,
        @RequestParam(value = "voice", defaultValue = "Pirate") String voice) {
        return Map.of("completion", this.chatClient
            .prompt()
            .system(sp -> sp.param("voice", voice))
            .user(input)
            .call()
            .content());
    }
}
