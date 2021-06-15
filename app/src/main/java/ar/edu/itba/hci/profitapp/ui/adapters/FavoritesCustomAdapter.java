package ar.edu.itba.hci.profitapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import ar.edu.itba.hci.profitapp.databinding.ItemRoutineBinding;

public class FavoritesCustomAdapter extends RecyclerView.Adapter<FavoritesCustomAdapter.ViewHolder> {
    private List<Routine> routineList;

    public FavoritesCustomAdapter(List<Routine> routineList) {
        this.routineList = routineList;
    }

    public void addRoutines(List<Routine> toAdd) {
        routineList.addAll(toAdd);
    }

    @NonNull
    @NotNull
    @Override
    public FavoritesCustomAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRoutineBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_routine, parent, false);
        return new FavoritesCustomAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavoritesCustomAdapter.ViewHolder holder, int position) {
        holder.getItemRoutineBinding().setRoutine(routineList.get(position));
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemRoutineBinding itemRoutineBinding;

        public ViewHolder(@NonNull @NotNull ItemRoutineBinding itemView) {
            super(itemView.getRoot());
            this.itemRoutineBinding = itemView;
        }

        public ItemRoutineBinding getItemRoutineBinding() {
            return itemRoutineBinding;
        }
    }

}
