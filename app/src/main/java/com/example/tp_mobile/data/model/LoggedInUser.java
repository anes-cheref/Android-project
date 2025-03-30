package com.example.tp_mobile.data.model;

import com.example.tp_mobile.model.ResultLogin;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String displayName;
    private ResultLogin user;

    public LoggedInUser(ResultLogin resultLogin) {
        this.user = resultLogin;
        this.displayName = user.getFirst();
    }

    public ResultLogin getUser() { return user; }

    public String getDisplayName() {
        return displayName;
    }
}