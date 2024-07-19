package org.example.loginTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.api.data.UserLoginData;
import org.example.jooq.JooqConfig;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.example.api.helpers.Specifications.postResponse;
import static org.example.api.helpers.Specifications.specBuilder;

public class LoginUserTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void loginUserTest() throws Exception {
        UserLoginData loginData = new UserLoginData("test", "test");
        String json = mapper.writeValueAsString(loginData);
        Response response = postResponse(specBuilder("/login", json));
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(js.get("token").toString().isBlank(), "Нет ответа с токеном");
    }

    @Test
    public void getUsers() throws Exception {
        Result<Record> res = JooqConfig.getDataFromDB("users", "login = 'test'");
        Assertions.assertNotNull(res, "Пользователь с таким логином не существует");
    }

    @Test
    public void loginUserDoesNotExistTest() throws Exception {
        UserLoginData loginData = new UserLoginData("NotFound", "NotFound");
        String json = mapper.writeValueAsString(loginData);
        Response response = postResponse(specBuilder("/login", json));
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals(response.asString(), "User  not found", "Пользователь сущесвует");
    }
}
