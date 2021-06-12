package ar.edu.itba.hci.profitapp.api;

import androidx.lifecycle.LiveData;

import ar.edu.itba.hci.profitapp.api.model.Category;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiCategoryService {
    @GET("categories")
    LiveData<ApiResponse<PagedList<Category>>> getCategories();

    @GET("category/{categoryId}")
    LiveData<ApiResponse<Category>> getCategory(@Path("categoryId") int categoryId);
}
