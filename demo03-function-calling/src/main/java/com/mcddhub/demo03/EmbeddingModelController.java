package com.mcddhub.demo03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/demo03")
public class EmbeddingModelController {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddingModelController.class);


    private final EmbeddingModel embeddingModel;

    public EmbeddingModelController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/embed")
    public Map<String, EmbeddingResponse> embed(@RequestParam(value = "input", defaultValue = "Tell me a joke") String input) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(input));
        return Map.of("embed", embeddingResponse);
    }

    @GetMapping("/similar")
    public Map<String, EmbeddingResponse> similar() {
        EmbeddingResponse embeddingResponse = this.embeddingModel
            .embedForResponse(List.of("Hello World", "World is big and salvation is near"));
        logger.info("dimensions {}",this.embeddingModel.dimensions());
        return Map.of("embed", embeddingResponse);
    }
}
