package com.example.tp_mobile.data;

import com.example.tp_mobile.data.model.LoggedInUser;
import com.example.tp_mobile.keystore.KeystorePreference;
import com.example.tp_mobile.model.Keys;
import com.example.tp_mobile.ui.login.LoginCallback;

import java.io.IOException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private final LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;
    private KeystorePreference keystorePreference = null;
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource, KeystorePreference keystorePreference) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
            if (keystorePreference != null)
                instance.keystorePreference = keystorePreference;
        }
        return instance;
    }

    private void setLoggedInUser(LoggedInUser user, String token) {
        this.user = user;
        keystorePreference.saveEncryptionSecret(Keys.TOKEN, token);
    }

    public void login(String token, LoginCallback loginCallback) {
        // gérer la réponse du login qui n'est pas synchrone
        dataSource.login(token, new LoginCallback() {
            @Override
            public void onSuccess(Result<LoggedInUser> result) {
                setLoggedInUser(((Result.Success<LoggedInUser>) result).getData(), token);
                loginCallback.onSuccess(result);
            }

            @Override
            public void onError(Result.Error error) {
                loginCallback.onError(new Result.Error(new IOException("Token invalide")));
            }
        });
    }
}