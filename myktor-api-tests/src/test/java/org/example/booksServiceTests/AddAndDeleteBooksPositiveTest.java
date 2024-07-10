package org.example.booksServiceTests;

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
public class AddAndDeleteBooksPositiveTest {
    SpecHelper specHelper = new SpecHelper();
    JSONObject jsonObj = new JSONObject(Stream.of(new String[][]{
            {"nameOfBook", "TestName"},
            {"author", "TestAuthor"},
            {"publicationYear", "1995"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    @Test
    @Order(1)
    public void addNewBookTest() throws Exception {
        Response response = Specifications.postResponse(specHelper.specPostHelper("/books/create", jsonObj), 200);
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertFalse(js.get("id").toString().isBlank(), "Нет ответа от сервера с ID");
    }

    @Test
    @Order(3)
    public void findBooksTest() throws Exception {
        JSONObject jsonResponse = new JSONObject(new HashMap<String, String>() {{
            put("searchQuery", "HP");
        }});
        Response response = Specifications.postResponse(specHelper.specPostHelper("/books/search", jsonResponse), 200);
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertTrue(js.get("nameOfBook").toString().contains("HP"), "Книга не найдена");
    }

    @Test
    @Order(4)
    public void deleteBookTest() throws Exception {
        JSONObject jsonResponse = new JSONObject(new HashMap<String, String>() {{
            put("searchQuery", jsonObj.get("nameOfBook").toString());
        }});
        Response response = Specifications.deleteResponse(specHelper.specDeleteHelper("/books", jsonResponse), 200);
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertFalse(js.get("id").toString().isBlank(), "Нет ответа от сервера");
    }

    @Test
    @Order(2)
    public void getBooksFromBdTest() throws Exception {
        Result<Record> res = JooqConfig.getDataFromDB("books", "name = 'TestNameOfBook'");
        Assertions.assertNotNull(res, "В БД нет книги с таким названием");
    }

}
