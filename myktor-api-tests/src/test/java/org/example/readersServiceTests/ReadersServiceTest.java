package org.example.readersServiceTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.api.data.DeleteReaderData;
import org.example.api.data.ReaderData;
import org.example.api.data.SearchQuery;
import org.example.jooq.JooqConfig;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.*;

import static org.example.api.helpers.Specifications.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReadersServiceTest {
    ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public void addNewReader() throws Exception {
        ReaderData readerData = new ReaderData("testNameForDelete", "999999", "testPassword", "test@test.ru");
        String json = mapper.writeValueAsString(readerData);
        postResponse(specBuilder("/readers/create", json));
    }

    @Test
    public void addNewReaderTest() throws Exception {
        ReaderData readerData = new ReaderData("testName", "919191919", "testPassword", "test@test.ru");
        String json = mapper.writeValueAsString(readerData);
        Response response = postResponse(specBuilder("/readers/create", json));
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(js.get("readerId").toString().isBlank(), "Не пришел ответ с readerID");
    }

    @Test
    public void findReadersTest() throws Exception {
        SearchQuery searchQuery = new SearchQuery("name");
        String json = mapper.writeValueAsString(searchQuery);
        Response response = postResponse(specBuilder("/readers/search", json));
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(js.get("phone").toString().contains("919191919"), "Читатель с таким номером не найден");
    }

    @Test
    public void deleteReaderTest() throws Exception {
        String res = JooqConfig.getId("readers", "phone = '999999'");
        DeleteReaderData readerData = new DeleteReaderData(res);
        String json = mapper.writeValueAsString(readerData);
        Response response = deleteResponse(specBuilder("/readers", json));
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(js.get("readerId").toString().isBlank(), "Не пришел ответ с readerID");
    }

    @Test
    public void getReadersFromBdTest() throws Exception {
        Result<Record> res = JooqConfig.getDataFromDB("readers", "phone = '919191919'");
        Assertions.assertNotNull(res, "В БД нет такого читателя");
    }


    @Test
    public void addReaderWhichAlreadyExists() throws Exception {
        ReaderData readerData = new ReaderData("testName", "123134234", "testPassword", "test@test.ru");
        String json = mapper.writeValueAsString(readerData);
        Response response = postResponse(specBuilder("/readers/create", json));
        Assertions.assertEquals(409, response.statusCode());
        Assertions.assertEquals(response.asString(), "Reader already exists", "Пользователь не существует");
    }

    @Test
    public void addReaderNoToken() throws Exception {
        ReaderData readerData = new ReaderData("testName", "123134234", "testPassword", "test@test.ru");
        String json = mapper.writeValueAsString(readerData);
        Response response = postResponse(specBuilderNoToken("/readers/create", json));
        Assertions.assertEquals(401, response.statusCode());
        Assertions.assertEquals(response.asString(), "Token expired", "Пользователь авторизован");
    }

    @AfterAll
    public void deleteReader() throws Exception {
        String res = JooqConfig.getId("readers", "phone = '919191919'");
        DeleteReaderData readerData = new DeleteReaderData(res);
        String json = mapper.writeValueAsString(readerData);
        deleteResponse(specBuilder("/readers", json));
    }
}