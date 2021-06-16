package ar.edu.itba.hci.profitapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.api.ApiAchievementsService;
import ar.edu.itba.hci.profitapp.api.ApiClient;
import ar.edu.itba.hci.profitapp.api.ApiResponse;
import ar.edu.itba.hci.profitapp.api.model.Achievement;
import ar.edu.itba.hci.profitapp.api.model.PagedList;

public class AchievementsRepository {
    private final ApiAchievementsService apiService;

    public AchievementsRepository(App application) {
        this.apiService = ApiClient.create(application, ApiAchievementsService.class);
    }

    public LiveData<Resource<PagedList<Achievement>>> getAchievements(int page, int size, String orderBy, String direction) {
        return new NetworkBoundResource<PagedList<Achievement>, PagedList<Achievement>>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<PagedList<Achievement>>> createCall() {
                return apiService.getAchievements(page, size, orderBy, direction);
            }
        }.asLiveData();
    }
    public LiveData<Resource<Achievement>> addAchievement(Achievement achievement) {
        return new NetworkBoundResource<Achievement, Achievement>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Achievement>> createCall() {
                return apiService.addAchievement(achievement);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Achievement>> getAchievement(int achievementId) {
        return new NetworkBoundResource<Achievement, Achievement>() {
            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<Achievement>> createCall() {
                return apiService.getAchievement(achievementId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Achievement>> modifyAchievement(Achievement achievement) {
        return new NetworkBoundResource<Achievement, Achievement>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Achievement>> createCall() {
                return apiService.modifyAchievement(achievement.getId(), achievement);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> deleteAchievement(int achievementId) {
        return new NetworkBoundResource<Void, Void>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<Void>> createCall() {
                return apiService.deleteAchievement(achievementId);
            }
        }.asLiveData();
    }
}
