package ar.edu.itba.hci.profitapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.edu.itba.hci.profitapp.App;
import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.databinding.FragmentFavoritesBinding;
import ar.edu.itba.hci.profitapp.databinding.FragmentHomeBinding;
import ar.edu.itba.hci.profitapp.ui.adapters.FavoritesCustomAdapter;
import ar.edu.itba.hci.profitapp.ui.adapters.RoutinesCustomAdapter;
import ar.edu.itba.hci.profitapp.viewModel.FavoritesViewModel;
import ar.edu.itba.hci.profitapp.viewModel.RoutineViewModel;

public class FavoritesFragment extends Fragment {

    private App app;
    private FragmentFavoritesBinding fragmentFavoritesBinding;
    private FavoritesCustomAdapter favoritesAdapter;
    private RecyclerView recyclerView;
    private FavoritesViewModel favoritesViewModel;

    public FavoritesFragment() {

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favorites, container, false);
//    }
}