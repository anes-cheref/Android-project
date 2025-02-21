package com.example.tp_mobile;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tp_mobile.adapter.CountryAdapter;
import com.example.tp_mobile.adapter.ResidentAdapter;
import com.example.tp_mobile.model.Country;
import com.example.tp_mobile.model.Equipement;
import com.example.tp_mobile.model.Resident;
import com.example.tp_mobile.ui.login.RegisterActivity;

import java.util.List;

public class ResidentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resident);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView residents_list = findViewById(R.id.residents_list);
        List<Resident> items = List.of(
                new Resident("Gaëtan Leclair", 1,
                        List.of(
                                new Equipement(R.drawable.cleaner),
                                new Equipement(R.drawable.washing_machine),
                                new Equipement(R.drawable.heater),
                                new Equipement(R.drawable.iron)
                        )),
                new Resident("Cédric Boudet", 1,
                        List.of(
                                new Equipement(R.drawable.washing_machine)
                        )),
                new Resident("Gaylord Thibodeaux", 2,
                        List.of(
                                new Equipement(R.drawable.cleaner),
                                new Equipement(R.drawable.iron)
                        )),
                new Resident("Adam Jacquinot", 3,
                        List.of(
                                new Equipement(R.drawable.cleaner),
                                new Equipement(R.drawable.washing_machine),
                                new Equipement(R.drawable.iron)
                        )),
                new Resident("Abel Fresnel", 3,
                        List.of(
                                new Equipement(R.drawable.heater)
                        ))
        );

        ResidentAdapter adapter = new ResidentAdapter(ResidentActivity.this,
                R.layout.item_resident, items);

        // Associer l'adaptateur au spinner
        residents_list.setAdapter(adapter);
    }
}