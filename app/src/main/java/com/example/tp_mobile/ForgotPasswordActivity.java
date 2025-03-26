package com.example.tp_mobile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Veuillez entrer un e-mail", Toast.LENGTH_SHORT).show();
                } else {
                    // Ici, vous pouvez appeler une fonction pour envoyer un e-mail de réinitialisation
                    Toast.makeText(ForgotPasswordActivity.this, "E-mail de réinitialisation envoyé", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}