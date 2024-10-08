package com.mcddhub.demo03.function;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.mcddhub.demo03.entity.Response;
import org.springframework.stereotype.Service;

@Service
public class MockOrderService {

    public Response getOrder(Request request) {
        String productName = "尤尼克斯羽毛球拍";
        return new Response(String.format("%s 的订单编号为 %s, 购买的商品为: %s", request.userId, request.orderId, productName));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Request(
        @JsonProperty(required = true,
            value = "orderId") @JsonPropertyDescription("订单编号, 比如 1001***") String orderId,
        @JsonProperty(required = true,
            value = "userId") @JsonPropertyDescription("用户编号, 比如 2001***") String userId) {
    }


}
