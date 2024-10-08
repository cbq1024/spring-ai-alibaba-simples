package com.mcddhub.demo02.controller;

import org.springframework.ai.image.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo02/images")
public class ImageModelController {
    private final ImageModel imageModel;

    public ImageModelController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping("/generate")
    public String image(String input) {
        ImageOptions options = ImageOptionsBuilder.builder()
            .withModel("wanx-v1")
            .build();
        ImagePrompt prompt = new ImagePrompt(input, options);
        ImageResponse response = imageModel.call(prompt);
        String result = response.getResult().getOutput().toString();
        return "redirect:" + result;
    }
}
