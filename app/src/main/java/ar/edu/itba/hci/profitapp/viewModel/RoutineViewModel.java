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

public class RoutineViewModel extends RepositoryViewModel<RoutineRepository> {

    private final static int PAGE_SIZE = 50;

    private int routinePage = 0;
    private String orderBy = "date";
    private String direction = "asc";
    private boolean isLastRoutinePage = false;

    private PagedList<Routine> allRoutines = new PagedList<>();
    private MediatorLiveData<Resource<PagedList<Routine>>> routines = new MediatorLiveData<>();
    private final MutableLiveData<Integer> routineId = new MutableLiveData<>();
    private LiveData<Resource<Routine>> routine;

    private final PagedList<Routine> allFavorites = new PagedList<>();
    private final MediatorLiveData<Resource<PagedList<Routine>>> favorites = new MediatorLiveData<>();

    private final MediatorLiveData<Resource<Routine>> addRoutine = new MediatorLiveData<>();

    public RoutineViewModel(RoutineRepository repository) {
        super(repository);

        routine = Transformations.switchMap(routineId, routineId -> {
            if (routineId == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getRoutine(routineId);
            }
        });
    }

    public LiveData<Resource<PagedList<Routine>>> getRoutines() {
        if (!isLastRoutinePage) {
            routines.addSource(repository.getRoutines(routinePage, PAGE_SIZE, orderBy, direction), resource -> {
                if (resource.getStatus() == Status.SUCCESS) {
//                if ((resource.getData().getSize() == 0) || (resource.getData().getSize() < PAGE_SIZE)) {
//                    isLastRoutinePage = true;
//                }
                    assert resource.getData() != null;
                    isLastRoutinePage = resource.getData().getIsLastPage();

                    allRoutines.setContent(resource.getData().getContent());

                    routines.setValue(Resource.success(allRoutines));
                    if (!isLastRoutinePage) {
                        routinePage++;
                    }
                } else if (resource.getStatus() == Status.LOADING) {
                    routines.setValue(resource);
                }
            });
        }
        return routines;
    }

    public LiveData<Resource<Routine>> getRoutine(int routineId) {
        routine = repository.getRoutine(routineId);
        return routine;
    }

    public void setRoutineId(int routineId) {
        if ((this.routineId.getValue() != null) && (routineId == this.routineId.getValue())) {
            return;
        }
        this.routineId.setValue(routineId);
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        routines = new MediatorLiveData<>();
        allRoutines = new PagedList<>();
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
