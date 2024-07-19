package org.example.booksServiceTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.api.data.BookData;
import org.example.api.data.SearchQuery;
import org.example.jooq.JooqConfig;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.example.api.helpers.Specifications.*;

public class BooksServiceTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test()
    public void addNewBookTest() throws Exception {
        BookData bookData = new BookData("TestName", "TestAuthor", "1995");
        String json = mapper.writeValueAsString(bookData);
        Response response = postResponse(specBuilder("/books/create", json));
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(js.get("id").toString().isBlank(), "Нет ответа от сервера с ID");
    }

    @Test
    public void findBooksTest() throws Exception {
        SearchQuery searchQuery = new SearchQuery("HP");
        String json = mapper.writeValueAsString(searchQuery);
        Response response = postResponse(specBuilder("/books/search", json));
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(js.get("nameOfBook").toString().contains("HP"), "Книга не найдена");
    }

    @Test
    public void deleteBookTest() throws Exception {
        SearchQuery searchQuery = new SearchQuery("TestName");
        String json = mapper.writeValueAsString(searchQuery);
        Response response = deleteResponse(specBuilder("/books", json));
        JsonPath js = new JsonPath(response.asString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(js.get("nameOfBook").toString().isBlank(), "Нет ответа от сервера");
    }

    @Test
    public void getBooksFromBdTest() throws Exception {
        Result<Record> res = JooqConfig.getDataFromDB("books", "name = 'TestNameOfBook'");
        Assertions.assertNotNull(res, "В БД нет книги с таким названием");
    }

    @Test
    public void addBookWhichAlreadyExists() throws Exception {
        BookData bookData = new BookData("HP", "Rouling", "1990");
        String json = mapper.writeValueAsString(bookData);
        Response response = postResponse(specBuilder("/books/create", json));
        Assertions.assertEquals(409, response.statusCode());
        Assertions.assertEquals(response.asString(), "Book already exists", "Книга не существует");
    }

    @Test
    public void addBookNoToken() throws Exception {
        BookData bookData = new BookData("HP", "Rouling", "1990");
        String json = mapper.writeValueAsString(bookData);
        Response response = postResponse(specBuilderNoToken("/books/create", json));
        Assertions.assertEquals(401, response.statusCode());
        Assertions.assertEquals(response.asString(), "Token expired", "Пользователь авторизован");
    }
}
