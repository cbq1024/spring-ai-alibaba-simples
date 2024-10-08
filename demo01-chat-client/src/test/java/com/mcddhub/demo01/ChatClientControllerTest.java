package com.mcddhub.demo01;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatClientControllerTest {
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void should_get_1_joker() {
        given()
            .when().get("/demo01/sync?input=给我讲个笑话吧")
            .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void should_get_3_joker() {
        given()
            .when().get("/demo01/streams?input=给我讲个笑话吧")
            .then().statusCode(HttpStatus.SC_OK);
    }
}