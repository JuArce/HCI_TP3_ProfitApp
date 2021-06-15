package ar.edu.itba.hci.profitapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import ar.edu.itba.hci.profitapp.R;
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

        NavigationUI.setupWithNavController(mainBinding.mainBottomNav, mainNavController);
        mainBinding.mainToolbar.setOnMenuItemClickListener(item -> {
            if ( item.getTitle().equals("Profile")) {
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
    }
}