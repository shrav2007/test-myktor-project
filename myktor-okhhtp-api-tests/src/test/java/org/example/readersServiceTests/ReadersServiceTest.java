package org.example.readersServiceTests;

import api.helpers.RestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import okhttp3.Response;
import org.example.api.data.DeleteReaderData;
import org.example.api.data.ReaderData;
import org.example.api.data.SearchQuery;
import org.example.jooq.JooqConfig;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.*;

import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReadersServiceTest {

    ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public void addNewReader() throws Exception {
        ReaderData readerData = new ReaderData("testNameForDelete", "999999", "testPassword", "test@test.ru");
        String json = mapper.writeValueAsString(readerData);
        RestUtils.postResponse(json, "/readers/create");
    }

    @Test
    public void addNewReaderTest() throws Exception {
        ReaderData readerData = new ReaderData("testName", "919191919", "testPassword", "test@test.ru");
        String json = mapper.writeValueAsString(readerData);
        Response response = RestUtils.postResponse(json, "/readers/create");
        Result<Record> res = jooq.JooqConfig.getDataFromDB("readers", "phone = '919191919'");
        Assertions.assertNotNull(response.body());
        JsonPath js = new JsonPath(response.body().string());

        Assertions.assertEquals(200, response.code());
        Assertions.assertEquals(Objects.requireNonNull(res.getValue(0, "id")).toString(), js.get("readerId"), "ID не совпадает");
    }

    @Test
    public void findReadersTest() throws Exception {
        SearchQuery searchQuery = new SearchQuery("name");
        String json = mapper.writeValueAsString(searchQuery);
        Response response = RestUtils.postResponse(json, "/readers/search");
        JsonPath js = new JsonPath(response.body().string());

        Assertions.assertEquals(200, response.code());
        Assertions.assertTrue(js.get("phone").toString().contains("919191919"), "Читатель с таким номером не найден");
    }

    @Test
    public void deleteReaderTest() throws Exception {
        String res = JooqConfig.getId("readers", "phone = '999999'");
        DeleteReaderData readerData = new DeleteReaderData(res);
        String json = mapper.writeValueAsString(readerData);
        Response response = RestUtils.deleteResponse(json, "/readers");
        JsonPath js = new JsonPath(response.body().string());

        Assertions.assertEquals(200, response.code());
        Assertions.assertFalse(js.get("readerId").toString().isBlank(), "Нет ответа от сервера");
        Assertions.assertEquals(res, js.get("readerId"), "ID не совпадает");
    }

    @Test
    public void addReaderWhichAlreadyExists() throws Exception {
        ReaderData readerData = new ReaderData("testName", "123134234", "testPassword", "test@test.ru");
        String json = mapper.writeValueAsString(readerData);
        Response response = RestUtils.postResponse(json, "/readers/create");

        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(409, response.code());
        Assertions.assertEquals("Reader already exists", response.body().string(), "Пользователь не существует");
    }

    @Test
    public void addReaderNoToken() throws Exception {
        ReaderData readerData = new ReaderData("testName", "123134234", "testPassword", "test@test.ru");
        String json = mapper.writeValueAsString(readerData);
        Response response = RestUtils.postResponseNoToken(json, "/readers/create");
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(401, response.code());
        Assertions.assertEquals(response.body().string(), "Token expired", "Пользователь авторизован");
    }

    @AfterAll
    public void deleteReader() throws Exception {
        String res = JooqConfig.getId("readers", "phone = '919191919'");
        DeleteReaderData readerData = new DeleteReaderData(res);
        String json = mapper.writeValueAsString(readerData);
        RestUtils.deleteResponse(json, "/readers");
    }
}