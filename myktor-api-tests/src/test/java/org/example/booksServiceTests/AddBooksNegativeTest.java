package org.example.booksServiceTests;

import io.restassured.response.Response;
import org.example.api.helpers.SpecHelper;
import org.example.api.helpers.Specifications;
import org.jooq.tools.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddBooksNegativeTest {
    SpecHelper specHelper = new SpecHelper();
    JSONObject jsonObj = new JSONObject(Stream.of(new String[][]{
            {"nameOfBook", "HP"},
            {"author", "Rouling"},
            {"publicationYear", "1990"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    @Test
    @Order(1)
    public void addBookWhichAlreadyExists() throws Exception {
        Response response = Specifications.postResponse(specHelper.specPostHelper("/books/create", jsonObj), 409);
        Assertions.assertEquals(response.asString(), "Book already exists", "Книга не существует");
    }

    @Test
    @Order(2)
    public void addBookNoToken() throws Exception {
        Response response = Specifications.postResponse(specHelper.specPostHelperNoToken("/books/create", jsonObj), 409);
        Assertions.assertEquals(response.asString(), "Token expired", "Пользователь авторизован");
    }
}
