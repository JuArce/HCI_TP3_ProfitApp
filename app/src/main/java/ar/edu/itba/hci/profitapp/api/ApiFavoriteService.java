package ar.edu.itba.hci.profitapp.api;

import androidx.lifecycle.LiveData;

import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiFavoriteService {
    @GET("favourites")
    LiveData<ApiResponse<PagedList<Routine>>> getFavorites(@Query("page") int page, @Query("size") int size);

    @POST("favourites/{routineId}/")
    LiveData<ApiResponse<Routine>> addFavorite(@Path("routineId") int routineId);

    @DELETE("favourites/{routineId}/")
    LiveData<ApiResponse<Void>> deleteFavorite(@Path("routineId") int routineId);
}
