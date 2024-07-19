package org.example.api.data;

public class DeleteReaderData {
    private String searchQuery;

    public DeleteReaderData(String id) {
        this.searchQuery = id;
    }

    public String getSearchQuery() {

        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
