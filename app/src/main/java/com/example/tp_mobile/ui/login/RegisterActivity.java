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
import com.example.tp_mobile.ui.login.LoginActivity;

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

        // Créer un tableau avec les options
        String[] phoneCodes = {"+33", "+225", "+226"};

        // Créer un ArrayAdapter avec les options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phoneCodes);

        // Spécifier l'apparence du spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
                    Toast.makeText(RegisterActivity.this, "Nom invalide ❌ (Seulement des lettres)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "Email invalide ❌", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPassword(password)) {
                    Toast.makeText(RegisterActivity.this, "Mot de passe invalide ❌", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(c_pwd)) {
                    Toast.makeText(RegisterActivity.this, "Mot de passe non similaire ❌", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(RegisterActivity.this, "Inscription réussie ✅", Toast.LENGTH_SHORT).show();
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