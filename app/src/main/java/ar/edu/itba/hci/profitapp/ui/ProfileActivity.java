package ar.edu.itba.hci.profitapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.databinding.ActivityProfileBinding;
import ar.edu.itba.hci.profitapp.repository.AchievementsRepository;
import ar.edu.itba.hci.profitapp.repository.Status;
import ar.edu.itba.hci.profitapp.repository.UserRepository;
import ar.edu.itba.hci.profitapp.viewModel.AchievementsViewModel;
import ar.edu.itba.hci.profitapp.viewModel.UserViewModel;
import ar.edu.itba.hci.profitapp.viewModel.repositoryVM.RepositoryViewModelFactory;

public class ProfileActivity extends AppCompatActivity {
    private App app;
    private ActivityProfileBinding profileBinding;
    private UserViewModel userViewModel;
    private AchievementsViewModel achievementsViewModel;

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
                    if(!r.getData().getAvatarUrl().equals("")) {
                        Glide.with(profileBinding.getRoot()).load(r.getData().getAvatarUrl()).into(profileBinding.profilePicture);
                    }
                }
            } else {
//                defaultResourceHandler(r);
            }
        });
//        LOG OUT
        profileBinding.logoutButton.setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(getResources().getString(R.string.log_out));
            builder.setMessage(getResources().getString(R.string.log_out_msg));
            builder.setPositiveButton(getResources().getString(R.string.confirm), (dialog, which) -> {
                userViewModel.logOut().observe(this, r -> {
                    if (r.getStatus() == Status.SUCCESS) {
                        app.getPreferences().setAuthToken(null);
                        Intent outIntent = new Intent(this, LoginActivity.class);
                        startActivity(outIntent);
                    } else {
//                defaultResourceHandler(r);
                    }
                });
            });
            builder.setNegativeButton(getResources().getString(R.string.deny), null);
            AlertDialog dialog = builder.create();
            dialog.show();

        });
        ViewModelProvider.Factory viewModelFactoryAch = new RepositoryViewModelFactory<>(AchievementsRepository.class, app.getAchievementsRepository());
        achievementsViewModel = new ViewModelProvider(this, viewModelFactoryAch).get(AchievementsViewModel.class);
        achievementsViewModel.getAchievements().observe(this, r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if (r.getData() != null && r.getData().getContent() != null) {
                    Log.d("TAG", r.getData().getContent().toString());
                }
            }
        });

    }
}
