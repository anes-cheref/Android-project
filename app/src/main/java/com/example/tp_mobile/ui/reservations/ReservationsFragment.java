package com.example.tp_mobile.ui.reservations;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.tp_mobile.R;
import com.example.tp_mobile.adapter.TimeSlotAdapter;
import com.example.tp_mobile.api.PHApiClient;
import com.example.tp_mobile.api.PHApiService;
import com.example.tp_mobile.databinding.FragmentReservationsBinding;
import com.example.tp_mobile.model.Keys;
import com.example.tp_mobile.model.TimeSlot;
import com.example.tp_mobile.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationsFragment extends Fragment {

    private ReservationsViewModel reservationsViewModel;
    private FragmentReservationsBinding binding;
    private String token = null;
    private CalendarView calendarView;
    private RecyclerView availableTimeSlots;
    private TimeSlotAdapter adapter;
    private List<TimeSlot> timeSlotList = new ArrayList<>();
    private List<TimeSlot> filteredTimeSlots = new ArrayList<>();
    private int currentYear, currentMonth;
    private PHApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        reservationsViewModel = new ViewModelProvider(requireActivity()).get(ReservationsViewModel.class);

        binding = FragmentReservationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
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

        calendarView = binding.calendarView;
        availableTimeSlots = binding.availableTimeSlots;
        apiService = PHApiClient.getClient().create(PHApiService.class);

        adapter = new TimeSlotAdapter(getActivity(), filteredTimeSlots, (v, timeSlot) -> {
            reservationsViewModel.setTimeSlot(timeSlot);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_navigation_reservations_to_equipementsFragment);

        });
        availableTimeSlots.setLayoutManager(new GridLayoutManager(getContext(), 2));
        availableTimeSlots.setAdapter(adapter);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            if (year != currentYear || month + 1 != currentMonth) {
                currentYear = year;
                currentMonth = month + 1;
                fetchTimeSlots(String.format(Locale.getDefault(), "%04d-%02d", currentYear, currentMonth), dayOfMonth);
            }
            else filterTimeSlotsForDay(dayOfMonth);
        });

        // Charger les créneaux du mois actuel au démarrage
        Calendar calendar = Calendar.getInstance();
        String currentMonth = String.format(Locale.getDefault(), "%04d-%02d",
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        fetchTimeSlots(currentMonth, calendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchTimeSlots(String month, Integer dayOfMonth) {
        apiService.getTimeSlots(token, month).enqueue(new Callback<List<TimeSlot>>() {
            @Override
            public void onResponse(Call<List<TimeSlot>> call, Response<List<TimeSlot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    timeSlotList.clear();
                    timeSlotList.addAll(response.body());
                    if (dayOfMonth != null) filterTimeSlotsForDay(dayOfMonth);
                } else {
                    Toast.makeText(getContext(), "Erreur de récupération des créneaux",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TimeSlot>> call, Throwable t) {
                Toast.makeText(getContext(), "Erreur réseau: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d("ReservationsFragment", t.getMessage());
            }
        });
    }

    private void filterTimeSlotsForDay(int dayOfMonth) {
        filteredTimeSlots.clear();

        filteredTimeSlots.addAll(getTimeSlotsByDate(dayOfMonth));
        adapter.notifyDataSetChanged();
    }

    private List<TimeSlot> getTimeSlotsByDate(int dayOfMonth) {
        ArrayList<TimeSlot> timeSlots = new ArrayList<>();
        for (TimeSlot slot : timeSlotList) {
            Calendar slotCalendar = Calendar.getInstance();
            slotCalendar.setTime(slot.getBegin());
            if (slotCalendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                timeSlots.add(slot);
            }
        }
        return timeSlots;
    }

}