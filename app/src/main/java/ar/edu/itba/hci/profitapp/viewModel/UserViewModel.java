package ar.edu.itba.hci.profitapp.viewModel;

import androidx.lifecycle.LiveData;

import ar.edu.itba.hci.profitapp.api.model.User;
import ar.edu.itba.hci.profitapp.repository.Resource;
import ar.edu.itba.hci.profitapp.repository.UserRepository;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModel;

public class UserViewModel extends RepositoryViewModel<UserRepository> {

//    private MutableLiveData<User> user = new MutableLiveData<>();
    private LiveData<Resource<User>> currentUser;

    public UserViewModel(UserRepository repository) {
        super(repository);
    }

    public LiveData<Resource<User>> getCurrentUser() {
        currentUser = repository.getCurrentUser();
        return currentUser;
    }

    public LiveData<Resource<Void>> logOut(){
        return repository.logout();
    }
}
