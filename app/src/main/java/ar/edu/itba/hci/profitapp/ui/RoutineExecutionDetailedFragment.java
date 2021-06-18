package ar.edu.itba.hci.profitapp.ui;

import android.content.Context;
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
import androidx.navigation.fragment.NavHostFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.CycleExercise;
import ar.edu.itba.hci.profitapp.api.model.Exercise;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutineExecutionDetailedBinding;
import ar.edu.itba.hci.profitapp.repository.Status;

public class RoutineExecutionDetailedFragment extends Fragment {
    private App app;

    private FragmentRoutineExecutionDetailedBinding binding;

    private Integer routineId;
    private LinkedList<Cycle> routineCycles = new LinkedList<>();
    private int cycleIndex = 0;
    private int exerciseIndex = 0;
    private int currentCycleRepetition = 0;
    private boolean changedOrientation = false;

    public RoutineExecutionDetailedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRoutineExecutionDetailedBinding.inflate(getLayoutInflater());

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

//        routineViewModel.getRoutine(routineId).observe(getViewLifecycleOwner(), r -> {
//            if (r.getStatus() == Status.SUCCESS) {
//                if (r.getData() != null && r.getData() != null) {
//                    app.getRoutineRepository().getFavorites(0, 10).observe(getViewLifecycleOwner(), favRes -> { //TODO definir viewModel vs rep
//                        if (favRes.getStatus() == Status.SUCCESS) {
//                            if (favRes.getData() != null && favRes.getData().getContent() != null) {
//                                if (favRes.getData().getContent().stream().map(Routine::getId).collect(Collectors.toList()).contains(r.getData().getId())) {
//                                    r.getData().setFavorite(true);
//                                }
//                            }
//                        } else {
//
//                        }
//                        fragmentRoutineDetailBinding.setRoutine(r.getData());
//                    });
//                } else {
////                defaultResourceHandler(r);
//                }
//            }
//        });

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
                                        if (!changedOrientation) {
                                            loadAndStart();
                                        } else {
                                            changedOrientation = false;
                                            try {
                                                binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
                                                binding.setCycle(routineCycles.get(cycleIndex));
                                            } catch(Exception e) {
                                                Log.d("ERROR", Integer.toString(exerciseIndex));
                                            }
                                        }
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
            Log.d("TAG", "cambiar");
            Log.d("TAG", Integer.toString(routineCycles.get(cycleIndex).getCycleExercises().size()));

            if (++exerciseIndex < routineCycles.get(cycleIndex).getCycleExercises().size()) {
                Log.d("TAG", routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getExercise().getName());
                binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
            } else {
                Log.d("TAG", "se acabaron los ejercicios");
                exerciseIndex = 0;
                if (currentCycleRepetition > 0) {
                    Log.d("TAG", "le quedan repeticiones al ciclo");

                    currentCycleRepetition--;
                } else {
                    if (cycleIndex < routineCycles.size() - 1) {
                        Log.d("TAG", "tengo que cambiar de ciclo");

                        binding.setCycle(routineCycles.get(++cycleIndex));
                        Log.d("TAG", routineCycles.get(cycleIndex).getName());
                        binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
                    } else {
                        //TODO popup para salir
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                        builder.setMessage("Congratulations! You finished your routine"); //despues pasarlo a R.string.lo_que_sea
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                RoutineExecutionDetailedFragmentDirections.actionRoutineExecutionDetailedFragmentToRoutineDetailFragment();
//                                NavHostFragment.findNavController(this).navigate(R.id.action_routineExecutionDetailedFragment_to_routineDetailFragment);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                        Log.d("TAG", "salir");
                    }
                }
            }

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
        currentCycleRepetition = routineCycles.get(cycleIndex).getRepetitions() - 1;
        binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
        binding.setCycle(routineCycles.get(cycleIndex));
    }
}

