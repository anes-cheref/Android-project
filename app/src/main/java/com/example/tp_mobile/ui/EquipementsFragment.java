package com.example.tp_mobile.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp_mobile.R;
import com.example.tp_mobile.adapter.ApplianceAdapter;
import com.example.tp_mobile.adapter.DetailedEquipmentsAdapter;
import com.example.tp_mobile.api.PHApiClient;
import com.example.tp_mobile.api.PHApiService;
import com.example.tp_mobile.databinding.FragmentEquipementsBinding;
import com.example.tp_mobile.databinding.FragmentResidentsBinding;
import com.example.tp_mobile.model.Habitat;
import com.example.tp_mobile.model.Keys;
import com.example.tp_mobile.ui.login.LoginActivity;
import com.example.tp_mobile.ui.reservations.ReservationsViewModel;
import com.example.tp_mobile.ui.residents.ResidentsViewModel;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EquipementsFragment extends Fragment {

    private FragmentEquipementsBinding binding;
    private ReservationsViewModel reservationsViewModel;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reservationsViewModel = new ViewModelProvider(requireActivity()).get(ReservationsViewModel.class);
        binding = FragmentEquipementsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        if (reservationsViewModel.getHabitat() == null) {
            PHApiService apiService = PHApiClient.getClient().create(PHApiService.class);
            apiService.getHabitat(token).enqueue(new Callback<Habitat>() {
                @Override
                public void onResponse(Call<Habitat> call, Response<Habitat> response) {
                    reservationsViewModel.setHabitat(response.body());
                }

                @Override
                public void onFailure(Call<Habitat> call, Throwable t) {
                    Toast.makeText(getActivity(), "Récuperation des équipements échouée", Toast.LENGTH_SHORT)
                            .show();
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_equipementsFragment_to_navigation_home);
                }
            });
        }
        Habitat habitat = reservationsViewModel.getHabitat();
        binding.equipmentsList.setAdapter(new DetailedEquipmentsAdapter(getActivity(),
                R.layout.item_detailed_equipment, habitat.getEquipements(),
                (v, eq) -> {
                    reservationsViewModel.setEquipement(eq);
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_equipementsFragment_to_detailReservationFragment);
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}