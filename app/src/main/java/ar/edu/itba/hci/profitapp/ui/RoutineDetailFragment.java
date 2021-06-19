package ar.edu.itba.hci.profitapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import ar.edu.itba.hci.profitapp.databinding.FragmentRoutineDetailBinding;
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

public class RoutineDetailFragment extends Fragment {
    private App app;

    private FragmentRoutineDetailBinding fragmentRoutineDetailBinding;

    private RoutinesCustomAdapter routinesAdapter;
    private RoutineCyclesCustomAdapter routineCyclesAdapter;
    private List<ExercisesCustomAdapter> exercisesCustomAdapters;

    private boolean canExecute = true;


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

        app = ((App) getActivity().getApplication());
        RoutineActivity activity = (RoutineActivity) getActivity();
        routineId = activity.getRoutineId();

//        routinesAdapter = new RoutinesCustomAdapter(new ArrayList<>(), favoriteClickListener);
//        routineCyclesAdapter = new RoutineCyclesCustomAdapter(new ArrayList<>());

        ViewModelProvider.Factory routineViewModelFactory = new RepositoryViewModelFactory<>(RoutineRepository.class, app.getRoutineRepository());
        routineViewModel = new ViewModelProvider(this, routineViewModelFactory).get(RoutineViewModel.class);

        ViewModelProvider.Factory routineCycleViewModelFactory = new RepositoryViewModelFactory<>(RoutineRepository.class, app.getRoutineRepository());
        routineCycleViewModel = new ViewModelProvider(this, routineCycleViewModelFactory).get(RoutineCycleViewModel.class);

//        routineCyclesRecyclerView.setAdapter(routineCyclesAdapter);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            routineCyclesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            routineCyclesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        routineCyclesRecyclerView.setHasFixedSize(true);

        routineViewModel.getRoutine(routineId).observe(getViewLifecycleOwner(), r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if (r.getData() != null && r.getData() != null) {
                    app.getRoutineRepository().getFavorites(0, 10).observe(getViewLifecycleOwner(), favRes -> {
                        if (favRes.getStatus() == Status.SUCCESS) {
                            if (favRes.getData() != null && favRes.getData().getContent() != null) {
                                if (favRes.getData().getContent().stream().map(Routine::getId).collect(Collectors.toList()).contains(r.getData().getId())) {
                                    r.getData().setFavorite(true);
                                }
                            }
                        } else {

                        }
                        fragmentRoutineDetailBinding.setRoutine(r.getData());
                    });
                } else {
//                defaultResourceHandler(r);
                }
            }
        });

        app.getRoutineRepository().getRoutineCycles(routineId).observe(getViewLifecycleOwner(), r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if (r.getData() != null && r.getData().getContent() != null) {
                    AtomicInteger counter = new AtomicInteger();

                    List<Cycle> cycles = r.getData().getContent();
                    counter.addAndGet(cycles.size());
                    cycles.forEach(cycle -> {
                        ViewModelProvider.Factory exerciseViewModelFactory = new RepositoryViewModelFactory<>(ExerciseRepository.class, app.getExerciseRepository());
                        ExerciseViewModel exerciseViewModel = new ViewModelProvider(this, exerciseViewModelFactory).get(ExerciseViewModel.class);

                        app.getExerciseRepository().getCycleExercises(cycle.getId()).observe(getViewLifecycleOwner(), res -> {
                            if (res.getStatus() == Status.SUCCESS) {
                                if (res.getData() != null && res.getData().getContent() != null) {
                                    counter.decrementAndGet();
                                    cycle.setCycleExercises(res.getData().getContent());

                                    if (canExecute && res.getData().getContent().size() == 0) {
                                        canExecute = false;
                                    }

                                    if (counter.get() == 0) {
                                        routineCyclesAdapter = new RoutineCyclesCustomAdapter(cycles);
                                        routineCyclesRecyclerView.setAdapter(routineCyclesAdapter);
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

        fragmentRoutineDetailBinding.ratingButton.setOnClickListener(v -> {
            RatingBar ratingBar = (RatingBar) fragmentRoutineDetailBinding.ratingBar;
            float rating = ratingBar.getRating(); //este es el rating local .
            app.getRoutineRepository().postReview(routineId, (int) rating).observe(getViewLifecycleOwner(), r -> {
            });
            Snackbar.make(v, getResources().getString(R.string.rated), Snackbar.LENGTH_LONG).show();
        });

        fragmentRoutineDetailBinding.shareButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.profit.com/routineDetail/" + routineId);

            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);

            v.getContext().startActivity(shareIntent);
        });

        fragmentRoutineDetailBinding.favoriteButton.setOnClickListener(v -> {
            if (fragmentRoutineDetailBinding.favoriteButton.isChecked()) {
                app.getRoutineRepository().addFavorite(routineId).observe(getViewLifecycleOwner(), r -> {
                });
                Snackbar.make(v, getResources().getString(R.string.fav_added), Snackbar.LENGTH_LONG).show();
            } else {
                app.getRoutineRepository().deleteFavorite(routineId).observe(getViewLifecycleOwner(), r -> {
                });
                Snackbar.make(v, getResources().getString(R.string.fav_removed), Snackbar.LENGTH_LONG).show();
            }
        });

        fragmentRoutineDetailBinding.playRoutineFab.setOnClickListener(v -> {
            if (canExecute) {
                switch (app.getPreferences().getRoutineDisplayMode()) {
                    case 0:
                        Log.d("TAG", "Ejecutar vista simple");
                        NavHostFragment.findNavController(this).navigate(R.id.action_routineDetailFragment_to_routineExecutionSimplifiedFragment);
                        break;
                    case 1:
                        Log.d("TAG", "Ejecutar vista detallada");
                        NavHostFragment.findNavController(this).navigate(R.id.action_routineDetailFragment_to_routineExecutionDetailedFragment);
                        break;
                }
            } else {
                Snackbar.make(v, getResources().getString(R.string.cannot_execute), Snackbar.LENGTH_LONG).show(); //cambiar por un String nuestro
            }
        });
    }
}
