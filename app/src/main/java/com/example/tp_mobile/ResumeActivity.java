package com.example.tp_mobile;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResumeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resume);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView fullnameInput = findViewById(R.id.fullname);
        TextView emailInput = findViewById(R.id.email);
        TextView phoneInput = findViewById(R.id.phone);

        // ✅ Récupérer les données envoyées par RegisterActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String fullname = extras.getString("EXTRA_FULLNAME", "");
            String email = extras.getString("EXTRA_EMAIL", "");
            String phone = extras.getString("EXTRA_PHONE", "");

            // ✅ Afficher les données dans les champs
            fullnameInput.setText(fullname);
            emailInput.setText(email);
            phoneInput.setText(phone);
        }
    }
}