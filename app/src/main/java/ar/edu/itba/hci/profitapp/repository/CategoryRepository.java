package ar.edu.itba.hci.profitapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.api.ApiCategoryService;
import ar.edu.itba.hci.profitapp.api.ApiClient;
import ar.edu.itba.hci.profitapp.api.ApiResponse;
import ar.edu.itba.hci.profitapp.api.ApiRoutineService;
import ar.edu.itba.hci.profitapp.api.model.Category;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.api.model.Routine;

public class CategoryRepository {
    private final ApiCategoryService apiService;

    public CategoryRepository(App application) {
        this.apiService = ApiClient.create(application, ApiCategoryService.class);
    }

    public LiveData<Resource<PagedList<Category>>> getCategories() {
        return new NetworkBoundResource<PagedList<Category>, PagedList<Category>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<Category>>> createCall() {
                return apiService.getCategories();
            }
        }.asLiveData();
    }

    public LiveData<Resource<Category>> getCategory(int categoryId) {
        return new NetworkBoundResource<Category, Category>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<Category>> createCall() {
                return apiService.getCategory(categoryId);
            }
        }.asLiveData();
    }
}
