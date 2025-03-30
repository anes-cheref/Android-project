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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tp_mobile.R;
import com.example.tp_mobile.adapter.ApplianceAdapter;
import com.example.tp_mobile.adapter.DetailedEquipmentsAdapter;
import com.example.tp_mobile.api.PHApiClient;
import com.example.tp_mobile.api.PHApiService;
import com.example.tp_mobile.data.Appliances;
import com.example.tp_mobile.databinding.FragmentDetailReservationBinding;
import com.example.tp_mobile.databinding.FragmentEquipementsBinding;
import com.example.tp_mobile.model.ApplianceTimeSlot;
import com.example.tp_mobile.model.Equipement;
import com.example.tp_mobile.model.Habitat;
import com.example.tp_mobile.model.Keys;
import com.example.tp_mobile.model.TimeSlot;
import com.example.tp_mobile.ui.login.LoginActivity;
import com.example.tp_mobile.ui.reservations.ReservationsViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailReservationFragment extends Fragment {

    private FragmentDetailReservationBinding binding;
    private ReservationsViewModel reservationsViewModel;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reservationsViewModel = new ViewModelProvider(requireActivity()).get(ReservationsViewModel.class);
        binding = FragmentDetailReservationBinding.inflate(inflater, container, false);

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
                Toast.makeText(getActivity(), getString(R.string.must_be_connected), Toast.LENGTH_SHORT)
                        .show();
                getActivity().finish();
                return;
            }
        }
        Habitat habitat = reservationsViewModel.getHabitat();
        Equipement equipement = reservationsViewModel.getEquipement();
        TimeSlot timeSlot = reservationsViewModel.getTimeSlot();

        int applianceId = equipement.getId();
        int timeSlotId = timeSlot.getId();
        int order = 1; // Todo: A maj

        binding.habitat.residentName.setText(habitat.getNom());

        binding.habitat.floor.setText(String.format("%s", habitat.getEtage()));
        binding.habitat.equipmentsNb.setText(getString(R.string.s_equipements,
                String.valueOf(habitat.getEquipements().size())));
        ;
        int totalWattage = habitat.getEquipements().stream()
                .mapToInt(Equipement::getWattage) // l'att wattage
                .sum();
        binding.habitat.conso.setText(getString(R.string.total_wattage_d, totalWattage));
        ApplianceAdapter applianceAdapter = new ApplianceAdapter(habitat.getEquipements());
        binding.habitat.equipmentsImages.setAdapter(applianceAdapter);

        binding.equipement.equipmentName.setText(equipement.getName());
        binding.equipement.reference.setText(equipement.getReference());
        binding.equipement.equipmentConso.setText(getString(R.string.conso_info, equipement.getWattage()));
        Integer imgRes = Appliances.getItemId(equipement.getName());
        if (imgRes != -1) binding.equipement.equipmentImage.setImageResource(imgRes);

        double wattage = 0;
        for (ApplianceTimeSlot applianceTimeSlot: timeSlot.getApplianceTimeSlots())
            wattage += applianceTimeSlot.getEquipement().getWattage();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = String.format("%s - %s", dateFormat.format(timeSlot.getBegin()),
                dateFormat.format(timeSlot.getEnd()));
        String wattageText = String.format("%d/%dW", (int)wattage, timeSlot.getMaxWattage());
        binding.timeSlot.timeSlotText.setText(timeText);
        binding.timeSlot.wattageText.setText(wattageText);
        double part = wattage / timeSlot.getMaxWattage();
        if (part <= 0.3)
            binding.timeSlot.itemSlotContainer.setCardBackgroundColor(Color.GREEN);
        else if (part <= 0.7)
            binding.timeSlot.itemSlotContainer.setCardBackgroundColor(Color.rgb(255, 127, 0));
        else
            binding.timeSlot.itemSlotContainer.setCardBackgroundColor(Color.RED);

        binding.reserve.setOnClickListener(v -> {
            PHApiService apiService = PHApiClient.getClient().create(PHApiService.class);
            apiService.saveApplianceTimeSlot(token, applianceId, timeSlotId, order).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), R.string.time_slot_reserved, Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_detailReservationFragment_to_navigation_reservations);

                    } else {
                        Toast.makeText(getContext(), R.string.error_saving_time_slot, Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_detailReservationFragment_to_navigation_reservations);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_detailReservationFragment_to_navigation_reservations);
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}