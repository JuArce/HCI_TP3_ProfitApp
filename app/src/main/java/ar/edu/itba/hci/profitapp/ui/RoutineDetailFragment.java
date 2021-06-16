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
import java.util.List;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.CycleExercise;
import ar.edu.itba.hci.profitapp.api.model.Exercise;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutineDetailBinding;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutinesBinding;
import ar.edu.itba.hci.profitapp.repository.ExerciseRepository;
import ar.edu.itba.hci.profitapp.repository.RoutineRepository;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.ui.adapters.ExercisesCustomAdapter;
import ar.edu.itba.hci.profitapp.ui.adapters.RoutineCyclesCustomAdapter;
import ar.edu.itba.hci.profitapp.ui.adapters.RoutinesCustomAdapter;
import ar.edu.itba.hci.profitapp.viewModel.ExerciseViewModel;
import ar.edu.itba.hci.profitapp.viewModel.RoutineCycleViewModel;
import ar.edu.itba.hci.profitapp.viewModel.RoutineViewModel;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModelFactory;

import static android.os.Build.VERSION_CODES.R;

public class RoutineDetailFragment extends Fragment {
    private App app;

    private FragmentRoutineDetailBinding fragmentRoutineDetailBinding;

    private RoutinesCustomAdapter routinesAdapter;
    private RoutineCyclesCustomAdapter routineCyclesAdapter;
    private List<ExercisesCustomAdapter> exercisesCustomAdapters;


    private RoutineViewModel routineViewModel;
    private RoutineCycleViewModel routineCycleViewModel;

    private RecyclerView routineCyclesRecyclerView;
    private List<RecyclerView> cycleExercisesRecyclerView;

    private Integer routineId;

    public RoutineDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRoutineDetailBinding = FragmentRoutineDetailBinding.inflate(getLayoutInflater());

        routineCyclesRecyclerView = fragmentRoutineDetailBinding.cyclesRecyclerView;


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

        app = ((App) getActivity().getApplication());
        RoutineActivity activity = (RoutineActivity) getActivity();
        routineId = activity.getRoutineId();

//        routinesAdapter = new RoutinesCustomAdapter(new ArrayList<>(), favoriteClickListener);
        routineCyclesAdapter = new RoutineCyclesCustomAdapter(new ArrayList<>());

        ViewModelProvider.Factory routineViewModelFactory = new RepositoryViewModelFactory<>(RoutineRepository.class, app.getRoutineRepository());
        routineViewModel = new ViewModelProvider(this, routineViewModelFactory).get(RoutineViewModel.class);

        ViewModelProvider.Factory routineCycleViewModelFactory = new RepositoryViewModelFactory<>(RoutineRepository.class, app.getRoutineRepository());
        routineCycleViewModel = new ViewModelProvider(this, routineCycleViewModelFactory).get(RoutineCycleViewModel.class);

        routineCyclesRecyclerView.setAdapter(routineCyclesAdapter);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            routineCyclesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            routineCyclesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        routineCyclesRecyclerView.setHasFixedSize(true);

        routineViewModel.getRoutine(routineId).observe(getViewLifecycleOwner(), r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if(r.getData() != null && r.getData() != null) {
                    fragmentRoutineDetailBinding.setRoutine(r.getData());
                }
            } else {
//                defaultResourceHandler(r);
            }
        });

        routineCycleViewModel.getRoutineCycles(routineId).observe(getViewLifecycleOwner(), r -> {
            if(r.getStatus() == Status.SUCCESS) {
                if(r.getData() != null && r.getData().getContent() != null) {
                    List<Cycle> cycles = r.getData().getContent();
                    cycles.forEach(cycle -> {
                        ViewModelProvider.Factory exerciseViewModelFactory = new RepositoryViewModelFactory<>(ExerciseRepository.class, app.getExerciseRepository());
                        ExerciseViewModel exerciseViewModel = new ViewModelProvider(this, exerciseViewModelFactory).get(ExerciseViewModel.class);

                        List<CycleExercise> exercises = new ArrayList<>();
                        exerciseViewModel.getCycleExercises(cycle.getId()).observe(getViewLifecycleOwner(), res -> {
                            if(res.getStatus() == Status.SUCCESS) {
                                if(res.getData() != null && res.getData().getContent() != null) {
                                    exercises.addAll(res.getData().getContent());
                                    cycle.setCycleExercises(exercises);
                                    routineCyclesAdapter.addCycle(cycle);
                                    routineCyclesAdapter.notifyDataSetChanged();
                                    routineCyclesAdapter.getExercisesCustomAdapters().forEach(RecyclerView.Adapter::notifyDataSetChanged);
                                }
                            } else {

                            }
                        });
                    });
//                    routineCyclesAdapter.addCycles(r.getData().getContent());
//                    routineCyclesAdapter.notifyDataSetChanged();
                }
            } else {

            }
        });


    }
}
