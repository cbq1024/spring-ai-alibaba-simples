package com.mcddhub.demo03.controller;

import com.mcddhub.demo03.function.MockWeatherService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo03/function")
public class FunctionCallingController {

    private final MockWeatherService mockWeatherService;
    private final ChatClient chatClient;

    public FunctionCallingController(MockWeatherService mockWeatherService, ChatClient.Builder builder) {
        this.mockWeatherService = mockWeatherService;
        this.chatClient = builder.build();
    }

    @GetMapping("/weather-service")
    public String weatherService(String subject) {
        return chatClient.prompt()
            .function("getWeatherFunction", "根据城市查询天气", mockWeatherService)
            .user(subject)
            .call()
            .content();
    }

    @GetMapping("/order-detail")
    public String orderDetail() {
        return chatClient.prompt()
            .functions("getOrderFunction")
            .user("帮我查询一下订单, 用户编号为 1001, 订单编号为 2001")
            .call()
            .content();
    }


}
