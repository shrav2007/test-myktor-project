package org.example.registrationServiceTests;

import io.restassured.response.Response;
import org.example.api.helpers.SpecHelper;
import org.example.api.helpers.Specifications;
import org.jooq.tools.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegisterUserNegativeTest {
    SpecHelper specHelper = new SpecHelper();
    JSONObject jsonObj = new JSONObject(Stream.of(new String[][]{
            {"login", "test2"},
            {"password", "test2"},
            {"email", "test2"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    @Test
    public void registerUserAlreadyExistTest() throws Exception {
        Response response = Specifications.postResponse(specHelper.specPostHelper("/register", jsonObj), 409);
        Assertions.assertEquals("User already exists", response.asString());
    }
}
