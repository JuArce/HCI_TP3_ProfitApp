package ar.edu.itba.hci.profitapp.api;

import androidx.lifecycle.LiveData;

import ar.edu.itba.hci.profitapp.api.model.Review;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiReviewService {
    @POST("reviews/{routineId}")
    LiveData<ApiResponse<Void>> postReview(@Path("routineId") int routineId, @Body Review review);
}
