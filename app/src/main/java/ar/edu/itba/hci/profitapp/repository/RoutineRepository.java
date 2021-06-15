package ar.edu.itba.hci.profitapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.api.ApiClient;
import ar.edu.itba.hci.profitapp.api.ApiFavoriteService;
import ar.edu.itba.hci.profitapp.api.ApiResponse;
import ar.edu.itba.hci.profitapp.api.ApiRoutineService;
import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import retrofit2.http.Query;

public class RoutineRepository {
    private final ApiRoutineService apiService;
    private final ApiFavoriteService apiFavoriteService;

    public RoutineRepository(App application) {
        this.apiService = ApiClient.create(application, ApiRoutineService.class);
        this.apiFavoriteService = ApiClient.create(application, ApiFavoriteService.class);
    }

    public LiveData<Resource<PagedList<Routine>>> getRoutines(int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<PagedList<Routine>, PagedList<Routine>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<Routine>>> createCall() {
                LiveData<ApiResponse<PagedList<Routine>>> aux = apiService.getRoutines(page, size, orderBy, direction);
                LiveData<ApiResponse<PagedList<Routine>>> favorites = apiFavoriteService.getFavorites(0, 10);
                
//                aux.getValue().getData().getContent().forEach(routine -> {
//                    if(favorites.getValue().getData().getContent().stream().map(Routine::getId).collect(Collectors.toList()).contains(routine.getId())) {
//                        routine.setFavorite(true);
//                    }
//                });
                
                return aux;
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

    public LiveData<Resource<PagedList<Cycle>>> getRoutineCycles(int routineId) {
        return new NetworkBoundResource<PagedList<Cycle>, PagedList<Cycle>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<Cycle>>> createCall() {
                return apiService.getRoutineCycles(routineId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Cycle>> getRoutineCycle(int routineId, int cycleId) {
        return new NetworkBoundResource<Cycle, Cycle>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<Cycle>> createCall() {
                return apiService.getRoutineCycle(routineId, cycleId);
            }
        }.asLiveData();
    }
}
