package ar.edu.itba.hci.profitapp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import ar.edu.itba.hci.profitapp.repository.Resource;
import ar.edu.itba.hci.profitapp.repository.RoutineRepository;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModel;

public class RoutineViewModel extends RepositoryViewModel<RoutineRepository> {

    private final static int PAGE_SIZE = 10;

    private int routinePage = 0;
    private boolean isLastRoutinePage = false;
    private final PagedList<Routine> allRoutines = new PagedList<>();
    private final MediatorLiveData<Resource<PagedList<Routine>>> routines = new MediatorLiveData<>();
    private final MutableLiveData<Integer> routineId = new MutableLiveData<>();
    private final LiveData<Resource<Routine>> routine;
    private final MediatorLiveData<Resource<Routine>> addRoutine = new MediatorLiveData<>();

    public RoutineViewModel(RoutineRepository repository) {
        super(repository);

        routine = Transformations.switchMap(routineId, routineId -> {
            if(routineId == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getRoutine(routineId);
            }
        });
    }

    public LiveData<Resource<PagedList<Routine>>> getRoutines() {
//        getMoreRoutines();
        routines.addSource(repository.getRoutines(), resource -> {
            if(resource.getStatus() == Status.SUCCESS) {
                if ((resource.getData().getSize() == 0) || (resource.getData().getSize() < PAGE_SIZE)) {
                    isLastRoutinePage = true;
                }

                routinePage++;

                allRoutines.setContent(resource.getData().getContent());
                routines.setValue(Resource.success(allRoutines));
            } else if(resource.getStatus() == Status.LOADING) {
                routines.setValue(resource);
            }
        });
        return routines;
    }

//    public void getMoreRoutines() {
//        if(isLastRoutinePage) {
//            return;
//        }
//
//        routines.addSource(repository.getRoutines());
//    }

    public LiveData<Resource<Routine>> getRoutine() {
        return routine;
    }

    public void setRoutineId(int routineId) {
        if((this.routineId.getValue() != null) && (routineId == this.routineId.getValue())) {
            return;
        }
        this.routineId.setValue(routineId);
    }

}
