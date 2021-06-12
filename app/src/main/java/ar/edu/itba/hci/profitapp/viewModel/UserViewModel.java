package ar.edu.itba.hci.profitapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import ar.edu.itba.hci.profitapp.api.model.User;

public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>();

    public UserViewModel(@NonNull @NotNull Application application) {
        super(application);
    }
}
