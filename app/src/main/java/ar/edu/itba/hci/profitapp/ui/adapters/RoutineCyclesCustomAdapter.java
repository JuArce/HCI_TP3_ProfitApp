package ar.edu.itba.hci.profitapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.databinding.ItemCycleBinding;

public class RoutineCyclesCustomAdapter extends RecyclerView.Adapter<RoutineCyclesCustomAdapter.ViewHolder> {
    private List<Cycle> cycleList;
    private List<ExercisesCustomAdapter> exercisesCustomAdapters = new ArrayList<>();

    public RoutineCyclesCustomAdapter(List<Cycle> cycleList) {
        this.cycleList = cycleList;
    }

    public void addCycles(List<Cycle> toAdd) {
        cycleList.addAll(toAdd);
    }

    public void addCycle(Cycle toAdd) {
        cycleList.add(toAdd);
    }

    public List<ExercisesCustomAdapter> getExercisesCustomAdapters() {
        return exercisesCustomAdapters;
    }

    @NonNull
    @NotNull
    @Override
    public RoutineCyclesCustomAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCycleBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_cycle, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RoutineCyclesCustomAdapter.ViewHolder holder, int position) {
        Cycle cycle = cycleList.get(position);
        ItemCycleBinding itemCycleBinding = holder.getItemCycleBinding();

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemCycleBinding.exercisesRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        ExercisesCustomAdapter exercisesCustomAdapter = new ExercisesCustomAdapter(cycle.getCycleExercises());
        exercisesCustomAdapters.add(exercisesCustomAdapter);
//        layoutManager.setInitialPrefetchItemCount(cycle.getCycleExercises().size());
        holder.itemCycleBinding.exercisesRecyclerView.setLayoutManager(layoutManager);
        holder.itemCycleBinding.exercisesRecyclerView.setAdapter(exercisesCustomAdapter);

        itemCycleBinding.setCycle(cycle);
    }

    @Override
    public int getItemCount() {
        return cycleList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCycleBinding itemCycleBinding;

        public ViewHolder(@NonNull @NotNull ItemCycleBinding itemView) {
            super(itemView.getRoot());
            View view = itemView.getRoot();

            this.itemCycleBinding = itemView;
        }

        public ItemCycleBinding getItemCycleBinding() {
            return itemCycleBinding;
        }
    }
}
