package ar.edu.itba.hci.profitapp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.CycleExercise;
import ar.edu.itba.hci.profitapp.api.model.PagedList;
import ar.edu.itba.hci.profitapp.repository.ExerciseRepository;
import ar.edu.itba.hci.profitapp.repository.Resource;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModel;

public class ExerciseViewModel extends RepositoryViewModel<ExerciseRepository> {
    private final static int PAGE_SIZE = 10;
    private final static int CYCLE_PAGE = 0;

    private String orderBy = "order";
    private String direction = "asc";

    private final PagedList<CycleExercise> allCycleExercises = new PagedList<>();
    private final MediatorLiveData<Resource<PagedList<CycleExercise>>> cycleExercises = new MediatorLiveData<>();

    public ExerciseViewModel(ExerciseRepository repository) {
        super(repository);
    }

    //TODO usamos default de pedir cosas a la api
    public LiveData<Resource<PagedList<CycleExercise>>> getCycleExercises(int cycleId) {
        cycleExercises.addSource(repository.getCycleExercises(cycleId), resource -> {
            if (resource.getStatus() == Status.SUCCESS) {
                allCycleExercises.setContent(resource.getData().getContent());
                cycleExercises.setValue(Resource.success(allCycleExercises));
            } else if (resource.getStatus() == Status.LOADING) {
                cycleExercises.setValue(resource);
            }
        });
        return cycleExercises;
    }
}
