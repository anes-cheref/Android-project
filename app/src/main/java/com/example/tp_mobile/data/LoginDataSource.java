package com.example.tp_mobile.data;

import com.example.tp_mobile.api.PHApiClient;
import com.example.tp_mobile.api.PHApiService;
import com.example.tp_mobile.data.model.LoggedInUser;
import com.example.tp_mobile.keystore.KeystorePreference;
import com.example.tp_mobile.model.Keys;
import com.example.tp_mobile.model.ResultLogin;
import com.example.tp_mobile.ui.login.LoginCallback;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public void login(String token, LoginCallback callback) {

        try {
            if (token != null) {
                PHApiService apiService = PHApiClient.getClient().create(PHApiService.class);
                apiService.checkToken(token).enqueue(new Callback<ResultLogin>() {
                    @Override
                    public void onResponse(Call<ResultLogin> call, Response<ResultLogin> response) {
                        ResultLogin resultLogin = response.body();
                        if (!resultLogin.getError()) {
                            LoggedInUser loggedInUser = new LoggedInUser(
                                resultLogin
                            );
                            callback.onSuccess(new Result.Success<>(loggedInUser));
                        }
                        else {
                            callback.onError(new Result.Error(new IOException("Token invalide")));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultLogin> call, Throwable t) {
                        callback.onError(new Result.Error(new IOException("Erreur de connexion", t)));
                    }
                });
            }
        } catch (Exception e) {
            callback.onError(new Result.Error(new IOException("Erreur de connexion", e)));
        }
    }

    public static void logout(KeystorePreference keystorePreference) {
        keystorePreference.deleteEncryptionSecret(Keys.TOKEN);
    }
}