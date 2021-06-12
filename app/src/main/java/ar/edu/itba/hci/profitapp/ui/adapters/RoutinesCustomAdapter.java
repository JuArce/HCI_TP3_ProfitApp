package ar.edu.itba.hci.profitapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Routine;

public class RoutinesCustomAdapter extends RecyclerView.Adapter<RoutinesCustomAdapter.ViewHolder> {

    private List<Routine> routineList;

    public RoutinesCustomAdapter(List<Routine> routineList) {
        this.routineList = routineList;
    }

    public void addRoutines(List<Routine> toAdd) {
        routineList.addAll(toAdd);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.getRoutineName().setText(routineList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView routineName;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.routineName = itemView.findViewById(R.id.routine_name);
        }

        public TextView getRoutineName() {
            return routineName;
        }
    }
}
