package org.example.booksServiceTests;

import api.helpers.RestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import jooq.JooqConfig;
import okhttp3.Response;
import org.example.api.data.BookData;
import org.example.api.data.SearchQuery;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class BooksServiceTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test()
    public void addNewBookTest() throws Exception {
        BookData bookData = new BookData("TestName", "TestAuthor", "1995");
        String json = mapper.writeValueAsString(bookData);
        okhttp3.Response response = RestUtils.postResponse(json, "/books/create");
        Result<Record> res = jooq.JooqConfig.getDataFromDB("books", "name = 'TestName'");
        Assertions.assertNotNull(response.body());
        JsonPath js = new JsonPath(response.body().string());

        Assertions.assertEquals(200, response.code());
        Assertions.assertEquals(Objects.requireNonNull(res.getValue(0, "id")).toString(), js.get("id"), "ID не совпадает");
    }

    @Test
    public void findBooksTest() throws Exception {
        SearchQuery searchQuery = new SearchQuery("HP");
        String json = mapper.writeValueAsString(searchQuery);
        okhttp3.Response response = RestUtils.postResponse(json, "/books/search");
        Assertions.assertNotNull(response.body());
        JsonPath js = new JsonPath(response.body().string());

        Assertions.assertEquals(200, response.code());
        Assertions.assertTrue(js.get("nameOfBook").toString().contains("HP"), "Книга не найдена");
    }

    @Test
    public void deleteBookTest() throws Exception {
        SearchQuery searchQuery = new SearchQuery("TestName");
        String json = mapper.writeValueAsString(searchQuery);
        Result<Record> res = JooqConfig.getDataFromDB("books", "name = 'TestName'");
        String nameFromDB = Objects.requireNonNull(res.getValue(0, "name")).toString();
        Response response = RestUtils.deleteResponse(json, "/books");
        Assertions.assertNotNull(response.body());
        JsonPath js = new JsonPath(response.body().string());


        Assertions.assertEquals(200, response.code());
        Assertions.assertFalse(js.get("nameOfBook").toString().isBlank(), "Нет ответа от сервера");
        Assertions.assertEquals(nameFromDB, js.get("nameOfBook"), "Наименование не совпадает");
    }

    @Test
    public void addBookWhichAlreadyExists() throws Exception {
        BookData bookData = new BookData("HP", "Rouling", "1990");
        String json = mapper.writeValueAsString(bookData);
        Response response = RestUtils.postResponse(json, "/books/create");
        Result<Record> res = jooq.JooqConfig.getDataFromDB("books", "name = 'HP'");

        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(409, response.code());
        Assertions.assertEquals(response.body().string(), "Book already exists", "Книга не существует");
        Assertions.assertEquals(res.getValue(0, "name"), "HP", "Логин не совпадает");
        Assertions.assertEquals(res.getValue(0, "author"), "Rouling", "Пароль не совпадает");
        Assertions.assertEquals(res.getValue(0, "publication_year"), "1990", "Email не совпадает");
    }

    @Test
    public void addBookNoToken() throws Exception {
        BookData bookData = new BookData("HP", "Rouling", "1990");
        String json = mapper.writeValueAsString(bookData);
        Response response = RestUtils.postResponseNoToken(json, "/books/create");

        Assertions.assertEquals(401, response.code());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(response.body().string(), "Token expired", "Пользователь авторизован");
    }
}
