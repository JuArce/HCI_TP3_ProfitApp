package ar.edu.itba.hci.profitapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.stream.Collectors;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import ar.edu.itba.hci.profitapp.databinding.FragmentHomeBinding;
import ar.edu.itba.hci.profitapp.repository.RoutineRepository;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.ui.adapters.RoutinesCustomAdapter;
import ar.edu.itba.hci.profitapp.viewModel.RoutineViewModel;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModelFactory;

public class HomeFragment extends Fragment {
    private App app;
    private FragmentHomeBinding fragmentHomeBinding;
    private RoutinesCustomAdapter routinesAdapter;
    private RecyclerView recyclerView;
    private RoutineViewModel routineViewModel;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(getLayoutInflater());

        recyclerView = fragmentHomeBinding.routinesRecyclerView;

//        BottomNavigationView b = getActivity().findViewById(R.id.main_bottom_nav);
//        Menu m = b.getMenu();
//        m.findItem(R.id.homeFragment).setIcon(R.drawable.ic_baseline_fitness_center_24);

        return fragmentHomeBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View.OnClickListener favoriteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routine routine = (Routine) v.getTag();
                if(routine.getFavorite()) {
                    app.getRoutineRepository().addFavorite(routine.getId()).observe(getViewLifecycleOwner(), r->{});

                } else {
                    app.getRoutineRepository().deleteFavorite(routine.getId()).observe(getViewLifecycleOwner(), r->{});
                }
            }
        };

        routinesAdapter = new RoutinesCustomAdapter(new ArrayList<>(), favoriteClickListener);

        recyclerView.setAdapter(routinesAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//        });

        app = ((App) getActivity().getApplication());

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModelFactory<>(RoutineRepository.class, app.getRoutineRepository());
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);

        app.getRoutineRepository().getCurrentUserRoutines().observe(getViewLifecycleOwner(), r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if(r.getData() != null && r.getData().getContent() != null) {
                    app.getRoutineRepository().getFavorites(0,10).observe(getViewLifecycleOwner(), favRes -> {
                        if (favRes.getStatus() == Status.SUCCESS) {
                            if (favRes.getData() != null && favRes.getData().getContent() != null) {
                                r.getData().getContent().forEach(routine -> {
                                    if (favRes.getData().getContent().stream().map(Routine::getId).collect(Collectors.toList()).contains(routine.getId())) {
                                        routine.setFavorite(true);
                                    }
                                });
                                routinesAdapter.addRoutines(r.getData().getContent());
                                routinesAdapter.notifyDataSetChanged();
                            }
                        } else {

                        }
                    });
                }
            } else {
//                //defaultResourceHandler(r);
            }
        });
        /*
        routineViewModel.getRoutines().observe(getViewLifecycleOwner(), r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if(r.getData() != null && r.getData().getContent() != null) {
                    routinesAdapter.addRoutines(r.getData().getContent());
                    routinesAdapter.notifyDataSetChanged();
                }
            } else {
//                defaultResourceHandler(r);
            }
        });

         */

    }
}