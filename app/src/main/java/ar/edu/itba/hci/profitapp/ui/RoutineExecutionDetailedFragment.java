package ar.edu.itba.hci.profitapp.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Cycle;
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

    private NavController navController;


    private CountDownTimer countDownTimer;
    private long timeMilliSeconds;
    private boolean timerRunning;

    public RoutineExecutionDetailedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRoutineExecutionDetailedBinding.inflate(getLayoutInflater());
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
            cycleIndex = savedInstanceState.getInt("cycleIndex");
            exerciseIndex = savedInstanceState.getInt("exerciseIndex");
            currentCycleRepetition = savedInstanceState.getInt("currentCycleRepetition");
            timeMilliSeconds = savedInstanceState.getLong("timeMilliSeconds");
            timerRunning = savedInstanceState.getBoolean("timerRunning");
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
                                        AtomicInteger exerciseCounter = new AtomicInteger();

                                        routineCycles.forEach(c -> {
                                            exerciseCounter.addAndGet(c.getCycleExercises().size());
                                            c.getCycleExercises().forEach(ex -> {

                                                app.getExerciseRepository().getExerciseImages(ex.getExercise().getId()).observe(getViewLifecycleOwner(), imRes -> {
                                                    if (imRes.getStatus() == Status.SUCCESS) {
                                                        if (imRes.getData() != null && imRes.getData().getContent() != null) {
                                                            if (imRes.getData().getContent().size() > 0) {
                                                                ex.getExercise().setMedia(imRes.getData().getContent().get(0).getUrl());
                                                                exerciseCounter.decrementAndGet();
                                                                if (exerciseCounter.get() == 0) {
                                                                    if (!changedOrientation) {

                                                                        loadAndStart();
                                                                    } else {
                                                                        changedOrientation = false;
                                                                        binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
                                                                        binding.setCycle(routineCycles.get(cycleIndex));
                                                                        loadImage(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getExercise().getMedia());
                                                                        binding.remCycleRep.setText(Integer.toString(currentCycleRepetition));
                                                                        if (routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() > 0) {
                                                                            binding.pauseButton.setVisibility(View.VISIBLE);
                                                                            binding.counter.setVisibility(View.VISIBLE);
                                                                            startTimer();
                                                                        } else {
                                                                            binding.pauseButton.setVisibility(View.INVISIBLE);
                                                                            binding.counter.setVisibility(View.INVISIBLE);
                                                                        }

                                                                    }
                                                                    Log.d("TAG", "Exercise counter es 0");
                                                                }
                                                            }
                                                        }
                                                    } else {

                                                    }
                                                });
                                            });

                                        });
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

        //TODO refactor y modularizacion - No se hace por falta de tiempo
        binding.nextButton.setOnClickListener(v -> {
            Log.d("TAG", "cambiar");
            Log.d("TAG", Integer.toString(routineCycles.get(cycleIndex).getCycleExercises().size()));

            if (++exerciseIndex < routineCycles.get(cycleIndex).getCycleExercises().size()) {
                Log.d("TAG", routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getExercise().getName());
                binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
                loadImage(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getExercise().getMedia());
                if (routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() > 0) {
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
                    loadImage(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getExercise().getMedia());
                    if (routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() > 0) {
                        binding.pauseButton.setVisibility(View.VISIBLE);
                        binding.counter.setVisibility(View.VISIBLE);
                        timeMilliSeconds = routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() * 1000;
                        startTimer();
                    } else {
                        binding.pauseButton.setVisibility(View.INVISIBLE);
                        binding.counter.setVisibility(View.INVISIBLE);

                    }
                } else {
                    if (cycleIndex < routineCycles.size() - 1) { //Cambiar de ciclo
                        Log.d("TAG", "tengo que cambiar de ciclo");

                        binding.setCycle(routineCycles.get(++cycleIndex));
                        currentCycleRepetition = routineCycles.get(cycleIndex).getRepetitions() - 1;
                        binding.remCycleRep.setText(Integer.toString(currentCycleRepetition));
                        Log.d("TAG", routineCycles.get(cycleIndex).getName());
                        binding.setCycleExercise(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex));
                        loadImage(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getExercise().getMedia());
                        if (routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() > 0) {
                            binding.pauseButton.setVisibility(View.VISIBLE);
                            binding.counter.setVisibility(View.VISIBLE);
                            timeMilliSeconds = routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() * 1000;
                            startTimer();
                        } else {
                            binding.pauseButton.setVisibility(View.INVISIBLE);
                            binding.counter.setVisibility(View.INVISIBLE);

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                        builder.setMessage("Congratulations! You finished your routine"); //despues pasarlo a R.string.lo_que_sea
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                navController.navigate(R.id.action_routineExecutionDetailedFragment_to_routineDetailFragment);
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

        binding.stopButton.setOnClickListener(v -> {
            startStop();
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setCancelable(true);
            builder.setTitle(getResources().getString(R.string.close_routine));
            builder.setMessage(getResources().getString(R.string.close_routine_msg));
            builder.setPositiveButton(getResources().getString(R.string.confirm), (dialog, which) -> {
                navController.navigate(R.id.action_routineExecutionDetailedFragment_to_routineDetailFragment);
            });
            builder.setNegativeButton(getResources().getString(R.string.deny), (dialog, which) -> {
                startStop();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
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
        loadImage(routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getExercise().getMedia());
        binding.remCycleRep.setText(Integer.toString(currentCycleRepetition));
        if (routineCycles.get(cycleIndex).getCycleExercises().get(exerciseIndex).getDuration() > 0) {
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
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer() {
        if (countDownTimer != null) {
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

    public void loadImage(String url) {
        ImageView imageView = (ImageView) binding.exerciseImage;
        Picasso.get().load(url).into(imageView);
    }
}

