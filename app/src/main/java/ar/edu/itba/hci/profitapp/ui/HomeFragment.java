package ar.edu.itba.hci.profitapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private App app;
    private FragmentHomeBinding fragmentHomeBinding;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        fragmentHomeBinding = FragmentHomeBinding.inflate(getLayoutInflater());

        return inflater.inflate(R.layout.fragment_home, container, false);

    }
}