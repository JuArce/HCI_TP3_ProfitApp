package ar.edu.itba.hci.profitapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    private CountDownTimer countDownTimer;
    private long timeMilliSeconds;
    private boolean timerRunning;

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
            timeMilliSeconds = savedInstanceState.getLong("timeMilliSeconds");
            timerRunning = savedInstanceState.getBoolean("timerRunning");
            changedOrientation = true;
        }


    /*
    * private CountDownTimer countDownTimer;
    private long timeMilliSeconds;
    private boolean timerRunning;*/


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
                                            binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
                                            binding.setCycle(routineCycles.get(cycleIndex));
                                            binding.remCycleRep.setText(Integer.toString(currentCycleRepetition));
                                            if(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() > 0) {
                                                binding.pauseButton.setVisibility(View.VISIBLE);
                                                binding.counter.setVisibility(View.VISIBLE);
                                                startTimer();
                                            } else {
                                                binding.pauseButton.setVisibility(View.INVISIBLE);
                                                binding.counter.setVisibility(View.INVISIBLE);
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
                if(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() > 0) {
                    binding.pauseButton.setVisibility(View.VISIBLE);
                    binding.counter.setVisibility(View.VISIBLE);
                    timeMilliSeconds = routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() * 1000;
                    startTimer();
                } else {
                    binding.pauseButton.setVisibility(View.INVISIBLE);
                    binding.counter.setVisibility(View.INVISIBLE);

                }
            } else { //Si ya no hay ejercicios de ese ciclo
                Log.d("TAG", "se acabaron los ejercicios");
                exerciseIndex = 0;
                if (currentCycleRepetition > 0) { //Mismo ciclo porque le quedan repeticiones
                    Log.d("TAG", "le quedan repeticiones al ciclo");
                    currentCycleRepetition--;
                    binding.remCycleRep.setText(Integer.toString(currentCycleRepetition));
                    binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
                } else {
                    if (cycleIndex < routineCycles.size() - 1) { //Cambiar de ciclo
                        Log.d("TAG", "tengo que cambiar de ciclo");

                        binding.setCycle(routineCycles.get(++cycleIndex));
                        currentCycleRepetition = routineCycles.get(cycleIndex).getRepetitions() - 1;
                        binding.remCycleRep.setText(Integer.toString(currentCycleRepetition));
                        Log.d("TAG", routineCycles.get(cycleIndex).getName());
                        binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
                        if(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() > 0) {
                            binding.pauseButton.setVisibility(View.VISIBLE);
                            binding.counter.setVisibility(View.VISIBLE);
                            timeMilliSeconds = routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() * 1000;
                            startTimer();
                        } else {
                            binding.pauseButton.setVisibility(View.INVISIBLE);
                            binding.counter.setVisibility(View.INVISIBLE);

                        }
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

        binding.pauseButton.setOnClickListener(v -> {
            startStop();
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("cycleIndex", cycleIndex);
        outState.putInt("exerciseIndex", exerciseIndex);
        outState.putInt("currentCycleRepetition", currentCycleRepetition);
        outState.putLong("timeMilliSeconds", timeMilliSeconds);
        outState.putBoolean("timerRunning", timerRunning);

    }

    public void loadAndStart() {
        currentCycleRepetition = routineCycles.get(cycleIndex).getRepetitions() - 1;
        binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
        binding.setCycle(routineCycles.get(cycleIndex));
        binding.remCycleRep.setText(Integer.toString(currentCycleRepetition));
        if(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() > 0) {
            binding.pauseButton.setVisibility(View.VISIBLE);
            binding.counter.setVisibility(View.VISIBLE);
            timeMilliSeconds = routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() * 1000;
            startTimer();
        } else {
            binding.pauseButton.setVisibility(View.INVISIBLE);
            binding.counter.setVisibility(View.INVISIBLE);
        }
    }

    public void startStop() {
        if(timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(timeMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeMilliSeconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        timerRunning = true;
        binding.pauseButton.setImageResource(R.drawable.ic_baseline_pause_24);
    }

    public void stopTimer() {
        countDownTimer.cancel();
        binding.pauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        timerRunning = false;
    }

    public void updateTimer() {
        int seconds = (int) timeMilliSeconds / 1000;
        String time = Integer.toString(seconds);

        binding.counter.setText(time + " s");
    }
}

