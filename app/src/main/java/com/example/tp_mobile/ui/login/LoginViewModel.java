package com.example.tp_mobile.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;

import com.example.tp_mobile.data.LoginRepository;
import com.example.tp_mobile.data.Result;
import com.example.tp_mobile.data.model.LoggedInUser;
import com.example.tp_mobile.R;
import com.example.tp_mobile.keystore.KeystorePreference;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;


    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String token, ProgressBar loading) {
        loading.setVisibility(View.VISIBLE);
        loginRepository.login(token, new LoginCallback() {
            @Override
            public void onSuccess(Result<LoggedInUser> result) {
                Log.d("LoginAct", "Logged in successfully");
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onError(Result.Error error) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
                loading.setVisibility(View.GONE);
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}