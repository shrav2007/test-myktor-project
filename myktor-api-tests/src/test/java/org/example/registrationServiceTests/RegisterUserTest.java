package org.example.registrationServiceTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.api.data.DeleteUserData;
import org.example.api.data.RegistrationData;
import org.example.jooq.JooqConfig;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.*;

import static org.example.api.helpers.Specifications.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterUserTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    @Order(1)
    public void registerNewUserTest() throws Exception {
        RegistrationData registrationData = new RegistrationData("tu", "tutu", "tututu");
        String jsonObj = mapper.writeValueAsString(registrationData);
        Response response = postResponse(specBuilder("/register", jsonObj));
        JsonPath js = new JsonPath(response.asString());
        Result<Record> res = JooqConfig.getDataFromDB("users", "login = 'tu'");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(res.getValue(0, "login"), js.get("login"), "Пользователь не зарегистрирован");
    }

    @Test
    @Order(3)
    public void deleteUserTest() throws Exception {
        DeleteUserData userData = new DeleteUserData("tu");
        String json = mapper.writeValueAsString(userData);
        Response response = deleteResponse(specBuilder("/delete", json));
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("tu", js.get("login").toString(), "Пользователь не удален");
    }

    @Test
    @Order(2)
    public void getUsersFromBdTest() throws Exception {
        Result<Record> res = JooqConfig.getDataFromDB("users", "login = 'test'");
        Assertions.assertNotNull(res, "В БД не найден такой пользователь");
    }

    @Test
    @Order(4)
    public void registerUserAlreadyExistTest() throws Exception {
        RegistrationData registrationData = new RegistrationData("test2", "test2", "test2");
        String jsonObj = mapper.writeValueAsString(registrationData);
        Response response = postResponse(specBuilder("/register", jsonObj));
        Assertions.assertEquals(409, response.statusCode());
        Assertions.assertEquals("User already exists", response.asString(), "Пользователь не существует");
    }
}
