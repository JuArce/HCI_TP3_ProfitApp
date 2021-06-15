package ar.edu.itba.hci.profitapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.databinding.ActivityProfileBinding;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.repository.UserRepository;
import ar.edu.itba.hci.profitapp.viewModel.UserViewModel;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModelFactory;

public class ProfileActivity extends AppCompatActivity {
    private App app;
    private ActivityProfileBinding profileBinding;
    private UserViewModel userViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());

        setSupportActionBar(profileBinding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        app = ((App) getApplication());

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModelFactory<>(UserRepository.class, app.getUserRepository());
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);

        userViewModel.getCurrentUser().observe(this, r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if(r.getData() != null && r.getData() != null) {
                    profileBinding.setUser(r.getData());
                    if(r.getData().getAvatarUrl() != "") {
                        Glide.with(profileBinding.getRoot()).load(r.getData().getAvatarUrl()).into(profileBinding.profilePicture);
                    }
                }
            } else {
//                defaultResourceHandler(r);
            }
        });
    }
}
