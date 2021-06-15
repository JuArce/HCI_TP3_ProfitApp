package ar.edu.itba.hci.profitapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutineDetailBinding;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutinesBinding;
import ar.edu.itba.hci.profitapp.repository.RoutineRepository;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.ui.adapters.RoutinesCustomAdapter;
import ar.edu.itba.hci.profitapp.viewModel.RoutineViewModel;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModelFactory;

public class RoutineDetailFragment extends Fragment {
    private App app;
    private FragmentRoutineDetailBinding fragmentRoutineDetailBinding;
    private RoutinesCustomAdapter routinesAdapter;
    private RecyclerView recyclerView;
    private RoutineViewModel routineViewModel;
    private Integer routineId;

    public RoutineDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRoutineDetailBinding = FragmentRoutineDetailBinding.inflate(getLayoutInflater());

//        recyclerView = fragmentRoutineDetailBinding.routinesRecyclerView;

        return fragmentRoutineDetailBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View.OnClickListener favoriteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                v.getTag()
            }
        };

        routinesAdapter = new RoutinesCustomAdapter(new ArrayList<>(), favoriteClickListener);

        app = ((App) getActivity().getApplication());
        RoutineActivity activity = (RoutineActivity) getActivity();
        routineId = activity.getRoutineId();

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModelFactory<>(RoutineRepository.class, app.getRoutineRepository());
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);

        routineViewModel.getRoutine(routineId).observe(getViewLifecycleOwner(), r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if(r.getData() != null && r.getData() != null) {
                    fragmentRoutineDetailBinding.setRoutine(r.getData());
                }
            } else {
//                defaultResourceHandler(r);
            }
        });

    }
}
