package org.example.api.helpers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;

import java.util.Arrays;

public class Specifications {
    public static RequestSpecBuilder specBuilder(String uri) {
        return new RequestSpecBuilder()
                .setBaseUri(uri)
                .addHeader("Content-Type", "application/json")
                .addHeader("Bearer-Authorization", "e5c64aa9-74dc-4380-8629-c1c8d6c44d6e");
    }

    public static RequestSpecBuilder specBuilderNoToken(String uri) {
        return new RequestSpecBuilder()
                .setBaseUri(uri)
                .addHeader("Content-Type", "application/json");
    }

    public static Response getResponse(RequestSpecification spec, int... statusesArray) throws Exception {
        Response response = RestAssured
                .given()
                .spec(spec)
                .get()
                .then()
                .extract().response();

        return checkStatus(response, statusesArray);
    }

    public static Response postResponse(RequestSpecification spec, int... statusesArray) throws Exception {
        Response response = RestAssured
                .given()
                .spec(spec)
                .log().all()
                .post()
                .then()
                .extract().response();
        return checkStatus(response, statusesArray);
    }

    public static Response deleteResponse(RequestSpecification spec, int... statusesArray) throws Exception {
        Response response = RestAssured
                .given()
                .spec(spec)
                .log().all()
                .delete()
                .then()
                .extract().response();
        return checkStatus(response, statusesArray);
    }

    @SneakyThrows(Exception.class)
    public static Response checkStatus(Response response, int... statusesArray) throws Exception {
        if (statusesArray.length > 0) {
            int expectedStatus = Arrays.stream(statusesArray)
                    .findFirst()
                    .orElseThrow(() -> new Exception("Did not find the expected status"));
        }
        return response;
    }
}
