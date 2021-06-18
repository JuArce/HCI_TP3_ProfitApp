package ar.edu.itba.hci.profitapp.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.CycleExercise;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutineExecutionDetailedBinding;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutineExecutionSimpleBinding;
import ar.edu.itba.hci.profitapp.repository.Status;

public class RoutineExecutionSimplifiedFragment extends Fragment{
    private App app;

    private FragmentRoutineExecutionSimpleBinding binding;

    private Integer routineId;
    private LinkedList<Cycle> routineCycles = new LinkedList<>();
    private LinkedList<CycleExercise> exercises = new LinkedList<>();
    private int cycleIndex = 0;
    private int exerciseIndex = 0;
    private int currentCycleRepetition = 0;
    private boolean changedOrientation = false;

    public RoutineExecutionSimplifiedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRoutineExecutionSimpleBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = ((App) getActivity().getApplication());
        RoutineActivity activity = (RoutineActivity) getActivity();
        routineId = activity.getRoutineId();

        if (savedInstanceState != null) {
            cycleIndex = savedInstanceState.getInt("cycleIndex");
            exerciseIndex = savedInstanceState.getInt("exerciseIndex");
            currentCycleRepetition = savedInstanceState.getInt("currentCycleRepetition");
            changedOrientation = true;
        }

        app.getRoutineRepository().getRoutineCycles(routineId).observe(getViewLifecycleOwner(), r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if (r.getData() != null && r.getData().getContent() != null) {
                    AtomicInteger counter = new AtomicInteger();

                    routineCycles.addAll(r.getData().getContent());
                    counter.addAndGet(routineCycles.size());
                    routineCycles.forEach(cycle -> {
                        app.getExerciseRepository().getCycleExercises(cycle.getId()).observe(getViewLifecycleOwner(), res -> {
                            if (res.getStatus() == Status.SUCCESS) {
                                if (res.getData() != null && res.getData().getContent() != null) {
                                    counter.decrementAndGet();
                                    cycle.setCycleExercises(res.getData().getContent());

                                    if (counter.get() == 0) {
                                        loadAndStart();
                                    }
                                }
                            } else {

                            }
                        });
                    });
                }
            } else {

            }
        });

        binding.nextButton.setOnClickListener(v -> {

        });

    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("cycleIndex", cycleIndex);
        outState.putInt("exerciseIndex", exerciseIndex);
        outState.putInt("currentCycleRepetition", currentCycleRepetition);
    }

    public void loadAndStart() {
        routineCycles.forEach(cycle -> {
            for (int i = 0; i < cycle.getRepetitions(); i++) {
                exercises.addAll(cycle.getCycleExercises());
            }
        });

        /*
        currentCycleRepetition = routineCycles.get(cycleIndex).getRepetitions() - 1;
        binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
        binding.setCycle(routineCycles.get(cycleIndex));
         */
    }
}
