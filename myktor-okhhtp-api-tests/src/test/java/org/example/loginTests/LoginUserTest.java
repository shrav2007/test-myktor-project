package org.example.loginTests;

import api.data.UserLoginData;
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

public class LoginUserTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void LoginNewUserTest() throws IOException, SQLException {
        UserLoginData userLoginData = new UserLoginData("test", "test");
        String json = mapper.writeValueAsString(userLoginData);
        Response response = RestUtils.postResponseNoToken(json, "/login");
        Result<Record> res = JooqConfig.getDataFromDB("tokens", "login = 'test'");
        Assertions.assertNotNull(response.body());
        JsonPath js = new JsonPath(response.body().string());

        Assertions.assertEquals(200, response.code());
        Assertions.assertEquals(res.getValue(0, "token"), js.get("token"), "Логин не совпадает");
    }

    @Test
    public void loginUserDoesNotExistTest() throws IOException {
        UserLoginData loginData = new UserLoginData("NotFound", "NotFound");
        String json = mapper.writeValueAsString(loginData);
        Response response = RestUtils.postResponseNoToken(json, "/login");

        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(400, response.code());
        Assertions.assertEquals(response.body().string(), "User  not found", "Пользователь сущесвует");
    }
}
