package ar.edu.itba.hci.profitapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import ar.edu.itba.hci.profitapp.api.model.Routine;
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

    //TODO logout

    public LiveData<Resource<User>> getCurrentUser() {
        currentUser = repository.getCurrentUser();
        return currentUser;
    }
}
