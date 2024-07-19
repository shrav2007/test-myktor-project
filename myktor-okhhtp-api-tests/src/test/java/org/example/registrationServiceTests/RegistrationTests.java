package org.example.registrationServiceTests;

import api.data.DeleteUserData;
import api.data.RegistrationData;
import api.helpers.RestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import jooq.JooqConfig;
import okhttp3.Response;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

public class RegistrationTests {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void registerUserTest()
            throws IOException, SQLException {
        RegistrationData registrationData = new RegistrationData("testNEW", "test876786", "test");
        String json = mapper.writeValueAsString(registrationData);
        Response response = RestUtils.postResponseNoToken(json, "/register");
        Result<Record> res = JooqConfig.getDataFromDB("users", "login = 'testNEW'");
        assert response.body() != null;
        JsonPath js = new JsonPath(response.body().string());

        Assertions.assertEquals(200, response.code());
        Assertions.assertEquals(res.getValue(0, "login"), js.get("login"), "Логин не совпадает");
        Assertions.assertEquals(res.getValue(0, "password"), js.get("password"), "Пароль не совпадает");
        Assertions.assertEquals(res.getValue(0, "email"), js.get("email"), "Email не совпадает");
    }

    @Test
    public void deleteUserTest() throws IOException, SQLException {
        DeleteUserData userData = new DeleteUserData("testNEW");
        String json = mapper.writeValueAsString(userData);
        Response response = RestUtils.deleteResponse(json, "/delete");
        Result<Record> res = JooqConfig.getDataFromDB("users", "login = 'testNEW'");

        Assertions.assertEquals(200, response.code());
        Assertions.assertTrue(res.isEmpty(), "В базе данных данный логин присутствует");
    }

    @Test
    public void registerUserAlreadyExistTest() throws Exception {
        RegistrationData registrationData = new RegistrationData("test2", "test2", "test2");
        String json = mapper.writeValueAsString(registrationData);
        Response response = RestUtils.postResponseNoToken(json, "/register");

        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(409, response.code());
        Assertions.assertEquals("User already exists", response.body().string(), "Пользователь не существует");
    }
}
