package com.example.tp_mobile.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tp_mobile.ForgotPasswordActivity;
import com.example.tp_mobile.MainScreenActivity;
import com.example.tp_mobile.R;
import com.example.tp_mobile.api.PHApiClient;
import com.example.tp_mobile.api.PHApiService;
import com.example.tp_mobile.databinding.ActivityLoginBinding;
import com.example.tp_mobile.keystore.KeystoreHelper;
import com.example.tp_mobile.keystore.KeystorePreference;
import com.example.tp_mobile.model.Keys;
import com.example.tp_mobile.model.Token;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private KeystorePreference keystorePreference;

    String token;

    private final PHApiService apiService = PHApiClient.getClient().create(PHApiService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            KeystoreHelper.getEncryptionSecret();
            keystorePreference = new KeystorePreference(this);
//            keystorePreference.deleteEncryptionSecret(Keys.TOKEN);
        } catch (Exception e) {
            Log.d("Error CACHE", "Error trying to access cached data");
            keystorePreference = null;
        }

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(keystorePreference))
                .get(LoginViewModel.class);

        final TextInputEditText usernameEditText = (TextInputEditText) binding.username;
        final TextInputEditText passwordEditText = (TextInputEditText) binding.password;
        final Button loginButton = binding.login;

        if (keystorePreference != null) {
            token = keystorePreference.getEncryptionSecret(Keys.TOKEN);
            Log.d("CACHE", "token ?: " + token);
            if (token != null) loginViewModel.login(token, binding.loading);

        }

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginAction(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        binding.signupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        loginButton.setOnClickListener(view -> {
            loginAction(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());

        });
        binding.mdpOublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginAction(String username, String pwd) {
        apiService.login(username, pwd).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                token = response.body().getToken();
                loginViewModel.login(token, binding.loading);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(LoginActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                Log.e("API", "Error: " + t.getMessage());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
        intent.putExtra(Keys.TOKEN, token);
        startActivity(intent);
        setResult(Activity.RESULT_OK);
        finish();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}