package ar.edu.itba.hci.profitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import ar.edu.itba.hci.profitapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mainBinding.getRoot());

        NavHostFragment mainNavHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_host_fragment);

        assert mainNavHostFragment != null;
        NavController mainNavController = mainNavHostFragment.getNavController();

//        if(savedInstanceState == null) {
//            mainNavController.navigate(R.id.action_favoritesFragment_to_homeFragment);
//        }

        NavigationUI.setupWithNavController(mainBinding.mainBottomNav, mainNavController);

//        mainBinding.mainBottomNav.setOnNavigationItemSelectedListener(item -> {
//            if(item.getItemId() == R.id.homeIconFragment) {
//                mainNavController.navigate();
//
//            } else if(item.getItemId() == R.id.favoritesIconFragment) {
//                mainNavController.navigate(BlankFragment2Directions.actionBlankFragment2ToBlankFragment1());
//
//            } else if(item.getItemId() == R.id.routinesIconFragment) {
//
//            }
//            return true;
//        });
    }
}