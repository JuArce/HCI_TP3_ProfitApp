package ar.edu.itba.hci.profitapp.api;

import androidx.lifecycle.LiveData;

import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRoutineService {
    @GET("routines")
    LiveData<ApiResponse<PagedList<Routine>>> getRoutines(@Query("page") int page, @Query("size") int size, @Query("orderBy") String orderBy, @Query("direction") String direction);

    @GET("routines/{routineId}")
    LiveData<ApiResponse<Routine>> getRoutine(@Path("routineId") int routineId);

    @GET("routines/{routineId}/cycles")
    LiveData<ApiResponse<PagedList<Cycle>>> getRoutineCycles(@Path("routineId") int routineId);

    @GET("routines/{routineId}/cycles/{cycleId}")
    LiveData<ApiResponse<Cycle>> getRoutineCycle(@Path("routineId") int routineId, @Path("cycleId") int cycleId);
}
