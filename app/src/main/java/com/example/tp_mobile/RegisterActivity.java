package com.example.tp_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tp_mobile.adapter.CountryAdapter;
import com.example.tp_mobile.model.Country;
import com.example.tp_mobile.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etEmail, etMdp, etNumero;
    private Button btnInscription;
    private static final String URL_REGISTER = "http://10.0.2.2:8888/server/register.php";

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

        // String[] phoneCodes = {"+33", "+225", "+226"};
        // ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phoneCodes);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                registerUser(fullname, email, password, phone);


            }
        });
    }

    private void registerUser(String fullname, String email, String password, String phone) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                response -> {
                    Toast.makeText(RegisterActivity.this, "Réponse brute : " + response, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");

                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();

                        if (message.contains("Inscription réussie")) {
                            Intent intent = new Intent(RegisterActivity.this, ResumeActivity.class);
                            intent.putExtra("EXTRA_FULLNAME", fullname);
                            intent.putExtra("EXTRA_EMAIL", email);
                            intent.putExtra("EXTRA_PHONE", phone);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(RegisterActivity.this, "Erreur de parsing JSON : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(RegisterActivity.this, "Erreur d'inscription : " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", fullname);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                return params;
            }
        };

        // Ajouter la requête à la file d'attente Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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