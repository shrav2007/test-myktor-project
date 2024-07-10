package org.example.readersServiceTests;

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
public class AddAndDeleteReadersPositiveTest {
    SpecHelper specHelper = new SpecHelper();
    JSONObject jsonObj = new JSONObject(Stream.of(new String[][]{
            {"name", "testName"},
            {"password", "testPassword"},
            {"phone", "919191919"},
            {"email", "test@test.ru"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    @Test
    @Order(1)
    public void addNewReaderTest() throws Exception {
        Response response = Specifications.postResponse(specHelper.specPostHelper("/readers/create", jsonObj), 200);
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertFalse(js.get("readerId").toString().isBlank(), "Не пришел ответ с readerID");
    }

    @Test
    @Order(3)
    public void findReadersTest() throws Exception {
        JSONObject jsonResponse = new JSONObject(new HashMap<String, String>() {{
            put("searchQuery", jsonObj.get("name").toString());
        }});
        Response response = Specifications.postResponse(specHelper.specPostHelper("/readers/search", jsonResponse), 200);
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertTrue(js.get("phone").toString().contains("919191919"), "Читатель с таким номером не найден");
    }

    @Test
    @Order(4)
    public void deleteReaderTest() throws Exception {
        String res = JooqConfig.getId("readers", "phone = '919191919'");
        JSONObject jsonResponse = new JSONObject(new HashMap<String, String>() {{
            put("searchQuery", res);
        }});
        Response response = Specifications.deleteResponse(specHelper.specDeleteHelper("/readers", jsonResponse), 200);
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertFalse(js.get("readerId").toString().isBlank(), "Не пришел ответ с readerID" );
    }

    @Test
    @Order(2)
    public void getReadersFromBdTest() throws Exception {
        Result<Record> res = JooqConfig.getDataFromDB("readers", "phone = '919191919'");
        Assertions.assertNotNull(res, "В БД нет такого читателя");
    }
}
