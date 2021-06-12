package ar.edu.itba.hci.profitapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.databinding.FragmentHomeBinding;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.ui.adapters.RoutinesCustomAdapter;

public class HomeFragment extends Fragment {
    private App app;
    private FragmentHomeBinding fragmentHomeBinding;
    private RoutinesCustomAdapter routinesAdapter;
    private RecyclerView recyclerView;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(getLayoutInflater());

        recyclerView = fragmentHomeBinding.routinesRecyclerView;

        return fragmentHomeBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        routinesAdapter = new RoutinesCustomAdapter(new ArrayList<>());

        recyclerView.setAdapter(routinesAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        app = ((App) getActivity().getApplication());


        app.getRoutineRepository().getRoutines().observe(getViewLifecycleOwner(), r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if(r.getData() != null && r.getData().getContent() != null) {
                    routinesAdapter.addRoutines(r.getData().getContent());
                    routinesAdapter.notifyDataSetChanged();
                }
            } else {
//                defaultResourceHandler(r);
            }
        });

    }
}