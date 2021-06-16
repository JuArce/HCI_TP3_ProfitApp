package ar.edu.itba.hci.profitapp.api;

import androidx.lifecycle.LiveData;

import ar.edu.itba.hci.profitapp.api.model.Achievement;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiAchievementsService {
    @GET("achievements")
    LiveData<ApiResponse<PagedList<Achievement>>> getAchievements(@Query("page") int page, @Query("size") int size, @Query("orderBy") String orderBy, @Query("direction") String direction);

    @POST("achievement")
    LiveData<ApiResponse<Achievement>> addAchievement(@Body Achievement achievement);

    @GET("achievement/{achievementId}")
    LiveData<ApiResponse<Achievement>> getAchievement(@Path("achievementId") int achievementId);

    @DELETE("achievement/{achievementId}/")
    LiveData<ApiResponse<Void>> deleteAchievement(@Path("achievementId") int achievementId);

    @PUT("achievement/{achievementId}/")
    LiveData<ApiResponse<Achievement>> modifyAchievement(@Path("achievementId") int achievementId, @Body Achievement achievement);
}
