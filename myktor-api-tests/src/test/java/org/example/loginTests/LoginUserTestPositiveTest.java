package org.example.loginTests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.api.helpers.SpecHelper;
import org.example.api.helpers.Specifications;
import org.example.jooq.JooqConfig;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginUserTestPositiveTest {
    SpecHelper specHelper = new SpecHelper();
    JSONObject jsonObj = new JSONObject(Stream.of(new String[][]{
            {"login", "test"},
            {"password", "test"},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    @Test
    @Order(1)
    public void loginUserTest() throws Exception {
        Response response = Specifications.postResponse(specHelper.specPostHelper("/login", jsonObj), 200);
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertFalse(js.get("token").toString().isBlank());
    }

    @Test
    @Order(2)
    public void getUsers() throws Exception {
        Result<Record> res = JooqConfig.getDataFromDB("users", "login = 'test'");
        Assertions.assertNotNull(res);
    }

}
