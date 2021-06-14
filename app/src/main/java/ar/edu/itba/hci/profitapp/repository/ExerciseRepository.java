package ar.edu.itba.hci.profitapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.api.ApiClient;
import ar.edu.itba.hci.profitapp.api.ApiExerciseService;
import ar.edu.itba.hci.profitapp.api.ApiResponse;
import ar.edu.itba.hci.profitapp.api.model.CycleExercise;
import ar.edu.itba.hci.profitapp.api.model.ExerciseMedia;
import ar.edu.itba.hci.profitapp.api.model.PagedList;

public class ExerciseRepository {
    private final ApiExerciseService apiService;

    public ExerciseRepository(App application) {
        this.apiService = ApiClient.create(application, ApiExerciseService.class);
    }

    public LiveData<Resource<PagedList<CycleExercise>>> getCycleExercises(int cycleId) {
        return new NetworkBoundResource<PagedList<CycleExercise>, PagedList<CycleExercise>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<CycleExercise>>> createCall() {
                return apiService.getCycleExercises(cycleId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<CycleExercise>> getCycleExercise(int cycleId, int exerciseId) {
        return new NetworkBoundResource<CycleExercise, CycleExercise>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<CycleExercise>> createCall() {
                return apiService.getCycleExercise(cycleId, exerciseId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<PagedList<ExerciseMedia>>> getExerciseImages(int exerciseId) {
        return new NetworkBoundResource<PagedList<ExerciseMedia>, PagedList<ExerciseMedia>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<ExerciseMedia>>> createCall() {
                return apiService.getExerciseImages(exerciseId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<PagedList<ExerciseMedia>>> getExerciseVideos(int exerciseId) {
        return new NetworkBoundResource<PagedList<ExerciseMedia>, PagedList<ExerciseMedia>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<ExerciseMedia>>> createCall() {
                return apiService.getExerciseVideos(exerciseId);
            }
        }.asLiveData();
    }
}
