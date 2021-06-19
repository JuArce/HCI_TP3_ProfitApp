package ar.edu.itba.hci.profitapp.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.CycleExercise;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutineExecutionSimpleBinding;
import ar.edu.itba.hci.profitapp.repository.Status;

public class RoutineExecutionSimplifiedFragment extends Fragment{
    private App app;

    private FragmentRoutineExecutionSimpleBinding binding;

    private NavController navController;

    private Integer routineId;
    private LinkedList<Cycle> routineCycles = new LinkedList<>();
    private LinkedList<CycleExercise> exercises = new LinkedList<>();
    private int exerciseIndex = 0;
    private boolean changedOrientation = false;

    public RoutineExecutionSimplifiedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRoutineExecutionSimpleBinding.inflate(getLayoutInflater());
        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = ((App) getActivity().getApplication());
        RoutineActivity activity = (RoutineActivity) getActivity();
        routineId = activity.getRoutineId();

        if (savedInstanceState != null) {
            exerciseIndex = savedInstanceState.getInt("exerciseIndex");
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
            if(++exerciseIndex < exercises.size()) {
                binding.setCycleExercise1(exercises.get(exerciseIndex));
                binding.remainingNumber.setText(Integer.toString(exercises.size() - exerciseIndex - 1));
                if(exerciseIndex + 1 < exercises.size()) {
                    binding.setCycleExercise2(exercises.get(exerciseIndex + 1));
                } else {
                    binding.secondExerciseCard.setVisibility(View.INVISIBLE);
                    binding.nextExercises.setVisibility(View.INVISIBLE);
                }
                if(exerciseIndex + 2 < exercises.size()) {
                    binding.setCycleExercise3(exercises.get(exerciseIndex + 2));
                } else {
                    binding.thirdExerciseCard.setVisibility(View.INVISIBLE);
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage("Congratulations! You finished your routine"); //despues pasarlo a R.string.lo_que_sea
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        navController.navigate(R.id.action_routineExecutionSimplifiedFragment_to_routineDetailFragment);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                Log.d("TAG", "salir");
            }
        });

        binding.stopButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setCancelable(true);
            builder.setTitle(getResources().getString(R.string.close_routine));
            builder.setMessage(getResources().getString(R.string.close_routine_msg));
            builder.setPositiveButton(getResources().getString(R.string.confirm), (dialog, which) -> {
                navController.navigate(R.id.action_routineExecutionSimplifiedFragment_to_routineDetailFragment);
            });
            builder.setNegativeButton(getResources().getString(R.string.deny), null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("exerciseIndex", exerciseIndex);
    }

    public void loadAndStart() {
        routineCycles.forEach(cycle -> {
            for (int i = 0; i < cycle.getRepetitions(); i++) {
                exercises.addAll(cycle.getCycleExercises());
            }
        });

        if(exerciseIndex < exercises.size()) {
            binding.setCycleExercise1(exercises.get(exerciseIndex));
            binding.remainingNumber.setText(Integer.toString(exercises.size() - exerciseIndex - 1));
        }
        if(exerciseIndex + 1 < exercises.size()) {
            binding.setCycleExercise2(exercises.get(exerciseIndex + 1));
        }
        if(exerciseIndex + 2 < exercises.size()) {
            binding.setCycleExercise3(exercises.get(exerciseIndex + 2));
        }

        /*
        currentCycleRepetition = routineCycles.get(cycleIndex).getRepetitions() - 1;
        binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
        binding.setCycle(routineCycles.get(cycleIndex));

         */
    }
}
