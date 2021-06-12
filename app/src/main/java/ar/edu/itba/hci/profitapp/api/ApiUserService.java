package ar.edu.itba.hci.profitapp.api;

import androidx.lifecycle.LiveData;

import ar.edu.itba.hci.profitapp.api.model.Credentials;
import ar.edu.itba.hci.profitapp.api.model.Token;
import ar.edu.itba.hci.profitapp.api.model.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiUserService {
    @POST("users/login")
    LiveData<ApiResponse<Token>> login(@Body Credentials credentials);

    @POST("users/logout")
    LiveData<ApiResponse<Void>> logout();

    @GET("users/current")
    LiveData<ApiResponse<User>> getCurrentUser();
}
