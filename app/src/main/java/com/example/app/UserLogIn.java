package com.example.app;

import com.example.app.API.models.User;

public class UserLogIn {
    private static User loggedUser;

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        UserLogIn.loggedUser = loggedUser;
    }
}
