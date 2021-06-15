package ar.edu.itba.hci.profitapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.api.ApiClient;
import ar.edu.itba.hci.profitapp.api.ApiFavoriteService;
import ar.edu.itba.hci.profitapp.api.ApiResponse;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import ar.edu.itba.hci.profitapp.api.model.Sport;

@Deprecated
public class FavoriteRepository {
    private final ApiFavoriteService apiService;

    public FavoriteRepository(App application) {
        this.apiService = ApiClient.create(application, ApiFavoriteService.class);
    }

    public LiveData<Resource<PagedList<Routine>>> getFavorites(int page, int size) {
        return new NetworkBoundResource<PagedList<Routine>, PagedList<Routine>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<Routine>>> createCall() {
                LiveData<ApiResponse<PagedList<Routine>>> aux = apiService.getFavorites(page, size);
                aux.getValue().getData().getContent().forEach(routine -> routine.setFavorite(true));
                return aux;
            }
        }.asLiveData();
    }

    public LiveData<Resource<Routine>> addFavorite(int routineId) {
        return new NetworkBoundResource<Routine, Routine>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Routine>> createCall() {
                return apiService.addFavorite(routineId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> deleteFavorite(int routineId) {
        return new NetworkBoundResource<Void, Void>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiService.deleteFavorite(routineId);
            }
        }.asLiveData();
    }
}
