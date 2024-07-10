package org.example.readersServiceTests;

import io.restassured.response.Response;
import org.example.api.helpers.SpecHelper;
import org.example.api.helpers.Specifications;
import org.jooq.tools.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddAndDeleteReadersNegativeTest {
    SpecHelper specHelper = new SpecHelper();
    JSONObject jsonObj = new JSONObject(Stream.of(new String[][]{
            {"name", "testName"},
            {"phone", "123134234"},
            {"password", "testPassword"},
            {"email", "test@test.ru"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    @Test
    @Order(1)
    public void addReaderWhichAlreadyExists() throws Exception {
        Response response = Specifications.postResponse(specHelper.specPostHelper("/readers/create", jsonObj), 409);
        Assertions.assertEquals(response.asString(), "Reader already exists", "Пользователь не существует");
    }

    @Test
    @Order(2)
    public void addReaderNoToken() throws Exception {
        Response response = Specifications.postResponse(specHelper.specPostHelperNoToken("/readers/create", jsonObj), 409);
        Assertions.assertEquals(response.asString(), "Token expired", "Пользователь авторизован");
    }
}
