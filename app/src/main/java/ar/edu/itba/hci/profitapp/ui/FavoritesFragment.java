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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import ar.edu.itba.hci.profitapp.databinding.FragmentFavoritesBinding;
import ar.edu.itba.hci.profitapp.databinding.FragmentHomeBinding;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutinesBinding;
import ar.edu.itba.hci.profitapp.repository.RoutineRepository;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.ui.adapters.FavoritesCustomAdapter;
import ar.edu.itba.hci.profitapp.ui.adapters.RoutinesCustomAdapter;
import ar.edu.itba.hci.profitapp.viewModel.FavoritesViewModel;
import ar.edu.itba.hci.profitapp.viewModel.RoutineViewModel;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModelFactory;

public class FavoritesFragment extends Fragment {
    private App app;
    private FragmentFavoritesBinding fragmentFavoritesBinding;
    private FavoritesCustomAdapter favoritesAdapter;
    private RecyclerView recyclerView;
    private FavoritesViewModel favoritesViewModel;

    public FavoritesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFavoritesBinding = FragmentFavoritesBinding.inflate(getLayoutInflater());

        recyclerView = fragmentFavoritesBinding.favoritesRecyclerView;

        return fragmentFavoritesBinding.getRoot();

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
                    Snackbar.make(v, getResources().getString(R.string.fav_added), Snackbar.LENGTH_LONG).show();
                } else {
                    app.getRoutineRepository().deleteFavorite(routine.getId()).observe(getViewLifecycleOwner(), r->{});
                    Snackbar.make(v, getResources().getString(R.string.fav_removed), Snackbar.LENGTH_LONG).show();
                }
            }
        };

        favoritesAdapter = new FavoritesCustomAdapter(new ArrayList<>());

        recyclerView.setAdapter(favoritesAdapter);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        recyclerView.setHasFixedSize(true);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//        });

        app = ((App) getActivity().getApplication());

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModelFactory<>(RoutineRepository.class, app.getRoutineRepository());
        favoritesViewModel = new ViewModelProvider(this, viewModelFactory).get(FavoritesViewModel.class);

        favoritesViewModel.getFavorites().observe(getViewLifecycleOwner(), r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if(r.getData() != null && r.getData().getContent() != null) {
                    r.getData().getContent().forEach(routine -> routine.setFavorite(true));
                    favoritesAdapter.addRoutines(r.getData().getContent());
                    favoritesAdapter.notifyDataSetChanged();
                }
            } else {
//                defaultResourceHandler(r);
            }
        });

    }
}