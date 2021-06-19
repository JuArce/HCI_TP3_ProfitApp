package ar.edu.itba.hci.profitapp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.repository.Resource;
import ar.edu.itba.hci.profitapp.repository.RoutineRepository;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModel;

public class RoutineCycleViewModel extends RepositoryViewModel<RoutineRepository> {

    private final static int PAGE_SIZE = 10;
    private final static int CYCLE_PAGE = 0;

    private String orderBy = "id";
    private String direction = "asc";

    private final PagedList<Cycle> allRoutineCycles = new PagedList<>();
    private final MediatorLiveData<Resource<PagedList<Cycle>>> routineCycles = new MediatorLiveData<>();

    public RoutineCycleViewModel(RoutineRepository repository) {
        super(repository);
    }

    //usamos default de pedir cosas a la api
    public LiveData<Resource<PagedList<Cycle>>> getRoutineCycles(int routineId) {
        routineCycles.addSource(repository.getRoutineCycles(routineId), resource -> {
            if (resource.getStatus() == Status.SUCCESS) {
                allRoutineCycles.setContent(resource.getData().getContent());
                routineCycles.setValue(Resource.success(allRoutineCycles));
            } else if (resource.getStatus() == Status.LOADING) {
                routineCycles.setValue(resource);
            }
        });
        return routineCycles;
    }

}
