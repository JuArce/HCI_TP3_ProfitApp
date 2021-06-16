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
import ar.edu.itba.hci.profitapp.api.model.CycleExercise;
import ar.edu.itba.hci.profitapp.api.model.Exercise;
import ar.edu.itba.hci.profitapp.databinding.ItemExerciseBinding;

public class ExercisesCustomAdapter extends RecyclerView.Adapter<ExercisesCustomAdapter.ViewHolder> {
    private List<CycleExercise> exerciseList;

    public ExercisesCustomAdapter(List<CycleExercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public void addRoutines(List<CycleExercise> toAdd) {
        exerciseList.addAll(toAdd);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemExerciseBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_exercise, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //holder.getItemRoutineBinding().setRoutine(routineList.get(position));
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //private final ItemRoutineBinding itemRoutineBinding;

        public ViewHolder(@NonNull @NotNull ItemExerciseBinding itemView) {
            super(itemView.getRoot());
            View view = itemView.getRoot();

            //this.itemRoutineBinding = itemView;

        }

                /*
                public ItemRoutineBinding getItemRoutineBinding() {
                    return itemRoutineBinding;
                }
                */
    }

}
