package com.example.tp_mobile.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp_mobile.R;
import com.example.tp_mobile.adapter.ApplianceAdapter;
import com.example.tp_mobile.adapter.DetailedEquipmentsAdapter;
import com.example.tp_mobile.api.PHApiClient;
import com.example.tp_mobile.api.PHApiService;
import com.example.tp_mobile.data.LoginDataSource;
import com.example.tp_mobile.databinding.FragmentHomeBinding;
import com.example.tp_mobile.keystore.KeystoreHelper;
import com.example.tp_mobile.keystore.KeystorePreference;
import com.example.tp_mobile.model.Equipement;
import com.example.tp_mobile.model.Habitat;
import com.example.tp_mobile.model.Keys;
import com.example.tp_mobile.ui.login.LoginActivity;
import com.example.tp_mobile.ui.reservations.ReservationsViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ReservationsViewModel reservationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        reservationsViewModel = new ViewModelProvider(requireActivity()).get(ReservationsViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String token = null;
        if (getActivity() != null) {
            token = getActivity().getIntent().getStringExtra(Keys.TOKEN);
            if (token == null) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                Toast.makeText(getActivity(), "Il faut vous connecter", Toast.LENGTH_SHORT)
                        .show();
                getActivity().finish();
                return;
            }
        }

        PHApiService apiService = PHApiClient.getClient().create(PHApiService.class);
        apiService.getHabitat(token).enqueue(new Callback<Habitat>() {
            @Override
            public void onResponse(Call<Habitat> call, Response<Habitat> response) {
                Log.d("HomeFragment", "Showing Habitation");
                Habitat habitat = response.body();
                binding.habitat.residentName.setText(habitat.getNom());

                binding.habitat.floor.setText(String.format("%s", habitat.getEtage()));
                binding.habitat.equipmentsNb.setText(getString(R.string.s_equipements,
                        String.valueOf(habitat.getEquipements().size())));
                ;
                int totalWattage = habitat.getEquipements().stream()
                        .mapToInt(Equipement::getWattage) // Extrait l'attribut wattage
                        .sum();
                binding.habitat.conso.setText(getString(R.string.total_wattage_d, totalWattage));
                ApplianceAdapter applianceAdapter = new ApplianceAdapter(habitat.getEquipements());
                binding.habitat.equipmentsImages.setAdapter(applianceAdapter);

                binding.equipmentsList.setAdapter(new DetailedEquipmentsAdapter(getActivity(),
                        R.layout.item_detailed_equipment, response.body().getEquipements(),
                        (v, eq) -> {}));

                reservationsViewModel.setHabitat(habitat);
            }

            @Override
            public void onFailure(Call<Habitat> call, Throwable t) {

            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    KeystoreHelper.getEncryptionSecret();
                    KeystorePreference keystorePreference = new KeystorePreference(getContext());
                    LoginDataSource.logout(keystorePreference);
                } catch (Exception e) {}
                startActivity(new Intent(getActivity(), LoginActivity.class));
                Toast.makeText(getActivity(), "Vous avez été déconnecté", Toast.LENGTH_SHORT)
                        .show();
                getActivity().finish();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}