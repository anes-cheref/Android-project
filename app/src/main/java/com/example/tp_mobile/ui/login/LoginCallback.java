package com.example.tp_mobile.ui.login;

import com.example.tp_mobile.data.Result;
import com.example.tp_mobile.data.model.LoggedInUser;

public interface LoginCallback {
    void onSuccess(Result<LoggedInUser> result);
    void onError(Result.Error error);
}