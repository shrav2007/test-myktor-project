package org.example.api.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookData {
    private String id;
    private String nameOfBook;
    private String author;
    private String publicationYear;
}

