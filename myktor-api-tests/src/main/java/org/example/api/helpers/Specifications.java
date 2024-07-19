package org.example.api.helpers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Specifications {
    private static final String TOKEN = "e13180e0-2da2-4acb-97f4-dcca2b3d174b";
    private static final String URL = "http://127.0.0.1:8082";

    public static RequestSpecification specBuilder(String key, String json) {
        return new RequestSpecBuilder()
                .setBaseUri(URL + key)
                .addHeader("Content-Type", "application/json")
                .addHeader("Bearer-Authorization", TOKEN)
                .setBody(json)
                .build();
    }

    public static RequestSpecification specBuilderNoToken(String key, String json) {
        return new RequestSpecBuilder()
                .setBaseUri(URL + key)
                .addHeader("Content-Type", "application/json")
                .setBody(json)
                .build();
    }

    public static Response getResponse(RequestSpecification spec) {
        return RestAssured
                .given()
                .spec(spec)
                .get()
                .then()
                .extract().response();
    }

    public static Response postResponse(RequestSpecification spec) throws Exception {
        return RestAssured
                .given()
                .spec(spec)
                .log().all()
                .post()
                .then()
                .extract().response();
    }

    public static Response deleteResponse(RequestSpecification spec) throws Exception {
        return RestAssured
                .given()
                .spec(spec)
                .log().all()
                .delete()
                .then()
                .extract().response();
    }
}
