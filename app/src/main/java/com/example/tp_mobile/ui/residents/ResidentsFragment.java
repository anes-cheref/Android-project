package com.example.tp_mobile.ui.residents;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tp_mobile.R;
import com.example.tp_mobile.adapter.ResidentAdapter;
import com.example.tp_mobile.api.PHApiClient;
import com.example.tp_mobile.api.PHApiService;
import com.example.tp_mobile.databinding.FragmentResidentsBinding;
import com.example.tp_mobile.model.Habitat;
import com.example.tp_mobile.model.Keys;
import com.example.tp_mobile.ui.login.LoginActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResidentsFragment extends Fragment {

    private ResidentsViewModel residentsViewModel;
    private FragmentResidentsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        residentsViewModel = new ViewModelProvider(this).get(ResidentsViewModel.class);
        binding = FragmentResidentsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        return root;
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
        ListView residents_list = binding.residentsList;
        PHApiService apiService = PHApiClient.getClient().create(PHApiService.class);
        apiService.getHabitats(token).enqueue(new Callback<List<Habitat>>() {
            @Override
            public void onResponse(Call<List<Habitat>> call, Response<List<Habitat>> response) {
                Log.d("ResidentAct", "Habitats getted");
                residents_list.setAdapter(new ResidentAdapter(getActivity(),
                        R.layout.item_resident, response.body()));
            }

            @Override
            public void onFailure(Call<List<Habitat>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}