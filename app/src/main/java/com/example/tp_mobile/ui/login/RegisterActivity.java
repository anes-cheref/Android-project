package com.example.tp_mobile.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.tp_mobile.R;
import com.example.tp_mobile.ResumeActivity;
import com.example.tp_mobile.adapter.CountryAdapter;
import com.example.tp_mobile.model.Country;
import com.example.tp_mobile.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bouton pour revenir à la page de connexion
        Button backToLogin = findViewById(R.id.back_to_login);
        backToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        Spinner spinner = findViewById(R.id.mobile_origin);

        List<Country> items = List.of(
                new Country(R.drawable.flag_fr, "France", 33),
                new Country(R.drawable.flag_ci, "Côte d'ivoire", 225),
                new Country(R.drawable.flag_usa, "Etats-Unis", 1),
                new Country(R.drawable.flag_russia, "Russie", 7)

        );

        CountryAdapter adapter = new CountryAdapter(RegisterActivity.this,
                R.layout.item_country, items);

        // Associer l'adaptateur au spinner
        spinner.setAdapter(adapter);

        EditText fullnameInput = findViewById(R.id.fullname);
        EditText emailInput = findViewById(R.id.email);
        EditText cPwdInput = findViewById(R.id.confirm_password);
        EditText passwordInput = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.register_button);
        EditText phoneInput = findViewById(R.id.phone);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = fullnameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString();
                String c_pwd = cPwdInput.getText().toString();
                String phone = phoneInput.getText().toString().trim();

                if (!isValidFullname(fullname)) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.user_must_be_letters), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.invalid_email_register), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPassword(password)) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.invalid_pwd_register), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(c_pwd)) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.non_similar_pwd), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(RegisterActivity.this, getString(R.string.user_registered), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, ResumeActivity.class);
                intent.putExtra("EXTRA_FULLNAME", fullname);
                intent.putExtra("EXTRA_EMAIL", email);
                intent.putExtra("EXTRA_PHONE", phone);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isValidFullname(String fullname) {
        String regex = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$"; // Autorise les lettres + accents + espaces
        return Pattern.matches(regex, fullname) && fullname.length() > 2;
    }

    private boolean isValidPassword(String password) {
        // Regex : Au moins 8 caractères, 1 lettre, 1 chiffre, 1 caractère spécial
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(regex, password);
    }

    // Vérifie si l'email contient bien un "@"
    private boolean isValidEmail(String email) {
        return email.contains("@");
    }
}