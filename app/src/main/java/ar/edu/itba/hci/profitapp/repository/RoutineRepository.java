package ar.edu.itba.hci.profitapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.api.ApiClient;
import ar.edu.itba.hci.profitapp.api.ApiResponse;
import ar.edu.itba.hci.profitapp.api.ApiRoutineService;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.api.model.Routine;

public class RoutineRepository {
    private final ApiRoutineService apiService;

    public RoutineRepository(App application) {
        this.apiService = ApiClient.create(application, ApiRoutineService.class);
    }

    public LiveData<Resource<PagedList<Routine>>> getRoutines() {
        return new NetworkBoundResource<PagedList<Routine>, PagedList<Routine>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<Routine>>> createCall() {
                return apiService.getRoutines();
            }
        }.asLiveData();
    }

    public LiveData<Resource<Routine>> getRoutine(int routineId) {
        return new NetworkBoundResource<Routine, Routine>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<Routine>> createCall() {
                return apiService.getRoutine(routineId);
            }
        }.asLiveData();
    }
}
