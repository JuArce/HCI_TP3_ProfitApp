package ar.edu.itba.hci.profitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import ar.edu.itba.hci.profitapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mainBinding.getRoot());

        NavHostFragment mainNavHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_host_fragment);

        NavController mainNavController = mainNavHostFragment.getNavController();

        if(savedInstanceState == null) {
            mainNavController.navigate(R.id.action_blankFragment1_to_blankFragment2);
        }

        mainBinding.toFragment1.setOnClickListener(v -> {
            mainBinding.toFragment1.setEnabled(false);
            mainBinding.toFragment2.setEnabled(true);

            mainNavController.navigate(BlankFragment2Directions.actionBlankFragment2ToBlankFragment1());
        });

        mainBinding.toFragment2.setOnClickListener(v -> {
            mainBinding.toFragment2.setEnabled(false);
            mainBinding.toFragment1.setEnabled(true);

            mainNavController.navigate(BlankFragment1Directions.actionBlankFragment1ToBlankFragment2());
        });
    }
}