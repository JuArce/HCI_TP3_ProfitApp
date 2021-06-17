package ar.edu.itba.hci.profitapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.stream.Collectors;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import ar.edu.itba.hci.profitapp.databinding.FragmentHomeBinding;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutinesBinding;
import ar.edu.itba.hci.profitapp.repository.RoutineRepository;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.ui.adapters.RoutinesCustomAdapter;
import ar.edu.itba.hci.profitapp.viewModel.FavoritesViewModel;
import ar.edu.itba.hci.profitapp.viewModel.RoutineViewModel;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModelFactory;

public class RoutinesFragment extends Fragment {
    private App app;
    private FragmentRoutinesBinding fragmentRoutinesBinding;
    private RoutinesCustomAdapter routinesAdapter;
    private RecyclerView recyclerView;
    private RoutineViewModel routineViewModel;
    private FavoritesViewModel favoritesViewModel;

    public RoutinesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRoutinesBinding = FragmentRoutinesBinding.inflate(getLayoutInflater());

        recyclerView = fragmentRoutinesBinding.routinesRecyclerView;

        return fragmentRoutinesBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = ((App) getActivity().getApplication());

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModelFactory<>(RoutineRepository.class, app.getRoutineRepository());
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);

        ViewModelProvider.Factory favoriteViewModelFactory = new RepositoryViewModelFactory<>(RoutineRepository.class, app.getRoutineRepository());
        favoritesViewModel = new ViewModelProvider(this, favoriteViewModelFactory).get(FavoritesViewModel.class);

        View.OnClickListener favoriteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routine routine = (Routine) v.getTag();
                Log.d("TAG", Integer.toString(routine.getId()));
                if(routine.getFavorite()) {
                    favoritesViewModel.addFavorite(routine.getId());

                } else {
                    favoritesViewModel.deleteFavorite(routine.getId());
                }
            }
        };

        routinesAdapter = new RoutinesCustomAdapter(new ArrayList<>(), favoriteClickListener);

        recyclerView.setAdapter(routinesAdapter);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        recyclerView.setHasFixedSize(true);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//        });

        routineViewModel.getRoutines().observe(getViewLifecycleOwner(), r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if (r.getData() != null && r.getData().getContent() != null) {
                    favoritesViewModel.getFavorites().observe(getViewLifecycleOwner(), favRes -> {
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
//                defaultResourceHandler(r);
            }
        });

    }
}