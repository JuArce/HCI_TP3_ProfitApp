package ar.edu.itba.hci.profitapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.databinding.ActivityRoutineBinding;

import static ar.edu.itba.hci.profitapp.ui.adapters.RoutinesCustomAdapter.EXTRA_MESSAGE;

public class RoutineActivity extends AppCompatActivity {
    private ActivityRoutineBinding binding;
    private Integer routineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        routineId = intent.getIntExtra(EXTRA_MESSAGE, -1);

        NavHostFragment mainNavHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.routine_host_fragment);

    }

    public Integer getRoutineId() {
        return routineId;
    }
}
