package ar.edu.itba.hci.profitapp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import ar.edu.itba.hci.profitapp.repository.Resource;
import ar.edu.itba.hci.profitapp.repository.RoutineRepository;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModel;

public class FavoritesViewModel extends RepositoryViewModel<RoutineRepository> {
    private final static int PAGE_SIZE = 10;

    private int routinePage = 0;
    private boolean isLastRoutinePage = false;

    private final PagedList<Routine> allFavorites = new PagedList<>();
    private final MediatorLiveData<Resource<PagedList<Routine>>> favorites = new MediatorLiveData<>();
    private final MutableLiveData<Integer> routineId = new MutableLiveData<>();
    private final LiveData<Resource<Routine>> favorite;

    public FavoritesViewModel(RoutineRepository repository) {
        super(repository);

        favorite = Transformations.switchMap(routineId, routineId -> {
            if(routineId == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getRoutine(routineId);
            }
        });
    }

    public LiveData<Resource<PagedList<Routine>>> getFavorites() {
        if(!isLastRoutinePage) {
            favorites.addSource(repository.getFavorites(routinePage, PAGE_SIZE), resource -> {
                if (resource.getStatus() == Status.SUCCESS) {
//                if ((resource.getData().getSize() == 0) || (resource.getData().getSize() < PAGE_SIZE)) {
//                    isLastRoutinePage = true;
//                }
                    assert resource.getData() != null;
                    isLastRoutinePage = resource.getData().getIsLastPage();

                    allFavorites.setContent(resource.getData().getContent());
                    favorites.setValue(Resource.success(allFavorites));
                    if (!isLastRoutinePage) {
                        routinePage++;
                    }
                } else if (resource.getStatus() == Status.LOADING) {
                    favorites.setValue(resource);
                }
            });
        }
        return favorites;
    }

    public LiveData<Resource<Routine>> getRoutine() {
        return favorite;
    }

    public void setRoutineId(int routineId) {
        if((this.routineId.getValue() != null) && (routineId == this.routineId.getValue())) {
            return;
        }
        this.routineId.setValue(routineId);
    }
}
