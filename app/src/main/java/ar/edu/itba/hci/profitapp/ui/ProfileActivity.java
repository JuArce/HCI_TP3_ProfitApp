package ar.edu.itba.hci.profitapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Achievement;
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
        LineChart achievementsChart = (LineChart) findViewById(R.id.lineChart);
        List<Achievement> achievementList = new ArrayList<>();
        ViewModelProvider.Factory viewModelFactoryAch = new RepositoryViewModelFactory<>(AchievementsRepository.class, app.getAchievementsRepository());
        achievementsViewModel = new ViewModelProvider(this, viewModelFactoryAch).get(AchievementsViewModel.class);
        achievementsViewModel.getAchievements().observe(this, r -> {
            if (r.getStatus() == Status.SUCCESS) {
                if (r.getData() != null && r.getData().getContent() != null) {
                    achievementList.addAll(r.getData().getContent());
                    Log.d("TAG", r.getData().getContent().toString());
                    ArrayList<Entry> valueSet = new ArrayList<>();
                    List<Double> weightList = new ArrayList<>();
                    List<Long> dateList = new ArrayList<>();
                    achievementList.forEach(v -> {
                        weightList.add(v.getWeight());
                        dateList.add(v.getDate());
                        valueSet.add(new BarEntry(v.getDate(), (float) v.getWeight()));
                    });
                    LineDataSet dataSet = new LineDataSet(valueSet, "");
                    LineData data = new LineData(dataSet);
                    achievementsChart.setData(data);
                    achievementsChart.animateXY(2000,2000); //o solo achievementsChart.animateX(2000);
                    achievementsChart.invalidate();
                    achievementsChart.getDescription().setEnabled(false);
                    YAxis yAxis = achievementsChart.getAxisLeft();
                    achievementsChart.getAxisRight().setEnabled(false);
                    yAxis.enableGridDashedLine(10f,10f,0f);
                    yAxis.setAxisMaximum((float) (Collections.max(weightList) + 1));
                    yAxis.setAxisMinimum((float) (Collections.min(weightList) - 1));
                    XAxis xAxis = achievementsChart.getXAxis();
                    xAxis.setAxisMinimum(Collections.min(dateList));
                    xAxis.setAxisMaximum(Collections.max(dateList));
                }
            }
        });


    }
}
