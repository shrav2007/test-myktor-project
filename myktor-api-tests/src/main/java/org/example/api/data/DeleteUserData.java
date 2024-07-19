package org.example.api.data;

public class DeleteUserData {
    private String login;

    public DeleteUserData(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
