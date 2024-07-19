package api.data;

public class SearchQuery {
    private String searchQuery;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public SearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
