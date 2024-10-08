package com.mcddhub.demo03.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.mcddhub.demo03.entity.Response;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MockWeatherService implements Function<MockWeatherService.Request, Response> {

    @Override
    public Response apply(Request request) {
        if (request.city().contains("杭州")) {
            return new Response(String.format("%s%s晴转多云, 气温 32 摄氏度。", request.date(), request.city()));
        }
        else if (request.city().contains("上海")) {
            return new Response(String.format("%s%s多云转阴, 气温 31 摄氏度。", request.date(), request.city()));
        }
        else {
            return new Response(String.format("暂时无法查询%s的天气状况。", request.city()));
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("根据日期和城市查询天气")
    public record Request(
        @JsonProperty(required = true, value = "city") @JsonPropertyDescription("城市, 比如杭州") String city,
        @JsonProperty(required = true, value = "date") @JsonPropertyDescription("日期, 比如 2024-08-22") String date) {
    }

}
