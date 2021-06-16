package ar.edu.itba.hci.profitapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Cycle;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import ar.edu.itba.hci.profitapp.databinding.ItemCycleBinding;
import ar.edu.itba.hci.profitapp.databinding.ItemRoutineBinding;

public class RoutineCyclesCustomAdapter extends RecyclerView.Adapter<RoutineCyclesCustomAdapter.ViewHolder> {
    private List<Cycle> cycleList;

    public RoutineCyclesCustomAdapter(List<Cycle> cycleList) {
        this.cycleList = cycleList;
    }

    public void addCycles(List<Cycle> toAdd) {
        cycleList.addAll(toAdd);
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
        holder.getItemCycleBinding().setCycle(cycleList.get(position));
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
