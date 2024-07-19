package org.example.api.data;

public class BookData {
    private String nameOfBook;
    private String author;
    private String publicationYear;

    public BookData(String nameOfBook, String author, String publicationYear) {
        this.nameOfBook = nameOfBook;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    public String getNameOfBook() {
        return nameOfBook;
    }

    public void setNameOfBook(String nameOfBook) {
        this.nameOfBook = nameOfBook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }
}

