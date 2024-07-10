package org.example.loginTests;

import io.restassured.response.Response;
import org.example.api.helpers.SpecHelper;
import org.example.api.helpers.Specifications;
import org.jooq.tools.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserNotFoundNegativeTest {
    SpecHelper specHelper = new SpecHelper();
    JSONObject jsonObj = new JSONObject(Stream.of(new String[][]{
            {"login", "NotFound"},
            {"password", "NotFound"},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    @Test
    public void loginUserDoesNotExistTest() throws Exception {
        Response response = Specifications.postResponse(specHelper.specPostHelper("/login", jsonObj), 409);
        Assertions.assertEquals(response.asString(), "User  not found", "Пользователь сущесвует");
    }
}
