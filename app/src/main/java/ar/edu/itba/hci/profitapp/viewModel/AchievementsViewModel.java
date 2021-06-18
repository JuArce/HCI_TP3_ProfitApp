package ar.edu.itba.hci.profitapp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import ar.edu.itba.hci.profitapp.api.model.Achievement;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.repository.AchievementsRepository;
import ar.edu.itba.hci.profitapp.repository.Resource;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModel;



public class AchievementsViewModel extends RepositoryViewModel<AchievementsRepository> {

    private final static int PAGE_SIZE = 10;

    private int achievementPage = 0;
    private String orderBy = "date";
    private String direction = "asc";
    private boolean isLastAchievementPage = false;

    private final PagedList<Achievement> allAchievements = new PagedList<>();
    private final MediatorLiveData<Resource<PagedList<Achievement>>> achievements = new MediatorLiveData<>();
    private final MutableLiveData<Integer> achievementId = new MutableLiveData<>();
    private LiveData<Resource<Achievement>> achievement;
    
    public AchievementsViewModel(AchievementsRepository repository) {
        super(repository);
    }

    public LiveData<Resource<PagedList<Achievement>>> getAchievements() {
        if(!isLastAchievementPage) {
            achievements.addSource(repository.getAchievements(achievementPage, PAGE_SIZE, orderBy, direction), resource -> {
                if (resource.getStatus() == Status.SUCCESS) {
                    assert resource.getData() != null;
                    isLastAchievementPage = resource.getData().getIsLastPage();

                    allAchievements.setContent(resource.getData().getContent());
                    achievements.setValue(Resource.success(allAchievements));
                    if (!isLastAchievementPage) {
                        achievementPage++;
                    }
                } else if (resource.getStatus() == Status.LOADING) {
                    achievements.setValue(resource);
                }
            });
        }
        return achievements;
    }

    public LiveData<Resource<Achievement>> getAchievement(int achievementId) {
        achievement = repository.getAchievement(achievementId);
        return achievement;
    }

    public LiveData<Resource<Achievement>> addAchievement(double weight){
        return repository.addAchievement(new Achievement(weight));
    }

}
