package org.example.registrationServiceTests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.api.helpers.SpecHelper;
import org.example.api.helpers.Specifications;
import org.example.jooq.JooqConfig;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterUserPositiveTest {
    SpecHelper specHelper = new SpecHelper();
    JSONObject jsonObj = new JSONObject(Stream.of(new String[][]{
            {"login", "tu"},
            {"password", "tutu"},
            {"email", "tututu"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    @Test
    @Order(1)
    public void registerNewUserTest() throws Exception {
        Response response = Specifications.postResponse(specHelper.specPostHelper("/register", jsonObj), 200);
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertEquals("tu", js.get("login").toString());
    }

    @Test
    @Order(3)
    public void deleteUserTest() throws Exception {
        JSONObject jsonResponse = new JSONObject(new HashMap<String, String>() {{
            put("login", jsonObj.get("login").toString());
        }});
        Response response = Specifications.deleteResponse(specHelper.specDeleteHelper("/delete", jsonResponse), 200);
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertEquals("tu", js.get("login").toString());
    }

    @Test
    @Order(2)
    public void getUsersFromBdTest() throws Exception {
        Result<Record> res = JooqConfig.getDataFromDB("users", "login = 'test'");
        Assertions.assertNotNull(res);
    }

}
