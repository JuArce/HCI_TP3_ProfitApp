package ar.edu.itba.hci.profitapp.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.api.ApiClient;
import ar.edu.itba.hci.profitapp.api.ApiFavoriteService;
import ar.edu.itba.hci.profitapp.api.ApiResponse;
import ar.edu.itba.hci.profitapp.api.ApiReviewService;
import ar.edu.itba.hci.profitapp.api.ApiRoutineService;
import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.api.model.Review;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import retrofit2.http.Query;

public class RoutineRepository {
    private final ApiRoutineService apiService;
    private final ApiFavoriteService apiFavoriteService;
    private final ApiReviewService apiReviewService;

    public RoutineRepository(App application) {
        this.apiService = ApiClient.create(application, ApiRoutineService.class);
        this.apiFavoriteService = ApiClient.create(application, ApiFavoriteService.class);
        this.apiReviewService = ApiClient.create(application, ApiReviewService.class);
    }

    public LiveData<Resource<PagedList<Routine>>> getRoutines(int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<PagedList<Routine>, PagedList<Routine>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<Routine>>> createCall() {
                //TODO refactor
                return apiService.getRoutines(page, size, orderBy, direction);
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

    public LiveData<Resource<PagedList<Routine>>> getFavorites(int page, int size) {
        return new NetworkBoundResource<PagedList<Routine>, PagedList<Routine>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<Routine>>> createCall() {
                return apiFavoriteService.getFavorites(page, size);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Routine>> addFavorite(int routineId) {
        Log.d("TAG", "add1");

        return new NetworkBoundResource<Routine, Routine>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Routine>> createCall() {
                return apiFavoriteService.addFavorite(routineId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> deleteFavorite(int routineId) {
        Log.d("TAG", "remove1");
        return new NetworkBoundResource<Void, Void>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiFavoriteService.deleteFavorite(routineId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> postReview(int routineId, int score) {
        return new NetworkBoundResource<Void, Void>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiReviewService.postReview(routineId, new Review(score));
            }
        }.asLiveData();
    }

    public LiveData<Resource<PagedList<Routine>>> getCurrentUserRoutines() {
        return new NetworkBoundResource<PagedList<Routine>, PagedList<Routine>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<Routine>>> createCall() {
                //TODO refactor
                return apiService.getCurrentUserRoutines();
            }
        }.asLiveData();
    }
}
