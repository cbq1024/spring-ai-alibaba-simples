package com.mcddhub.demo03.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import com.mcddhub.demo03.entity.Response;
import com.mcddhub.demo03.function.MockOrderService;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class AppConfig {
    DashScopeApi dashScopeApi = new DashScopeApi(System.getenv("DASHSCOPE_API_KEY"));

    @Bean
    public EmbeddingModel embeddingModel() {
        return new DashScopeEmbeddingModel(
            dashScopeApi,
            MetadataMode.EMBED,
            DashScopeEmbeddingOptions.builder()
                .withModel("text-embedding-v2")
                .build());
    }

    @Bean
    @Description("根据用户编号和订单编号查询订单信息")
    public Function<MockOrderService.Request, Response> getOrderFunction(MockOrderService mockOrderService) {
        return mockOrderService::getOrder;
    }
}
