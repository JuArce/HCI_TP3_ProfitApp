package ar.edu.itba.hci.profitapp.api;

import androidx.lifecycle.LiveData;

import ar.edu.itba.hci.profitapp.api.model.CycleExercise;
import ar.edu.itba.hci.profitapp.api.model.ExerciseMedia;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiExerciseService {

    @GET("cycles/{cycleId}/exercises")
    LiveData<ApiResponse<PagedList<CycleExercise>>> getCycleExercises(@Path("cycleId") int cycleId);

    @GET("cycles/{cycleId}/exercises/{exerciseId}")
    LiveData<ApiResponse<CycleExercise>> getCycleExercise(@Path("cycleId") int cycleId, @Path("exerciseId") int exerciseId);

    @GET("exercises/{exerciseId}/images")
    LiveData<ApiResponse<PagedList<ExerciseMedia>>> getExerciseImages(@Path("exerciseId") int exerciseId);

    @GET("exercises/{exerciseId}/videos")
    LiveData<ApiResponse<PagedList<ExerciseMedia>>> getExerciseVideos(@Path("exerciseId") int exerciseId);

}
