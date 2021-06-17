package ar.edu.itba.hci.profitapp.ui.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ar.edu.itba.hci.profitapp.R;
import ar.edu.itba.hci.profitapp.api.model.Routine;
import ar.edu.itba.hci.profitapp.databinding.ItemRoutineBinding;
import ar.edu.itba.hci.profitapp.ui.RoutineActivity;

public class FavoritesCustomAdapter extends RecyclerView.Adapter<FavoritesCustomAdapter.ViewHolder> {
    private List<Routine> routineList;
    public final static String EXTRA_MESSAGE = "ar.edu.itba.hci.profitapp.message";
    public final View.OnClickListener favoriteClickListener;

    public FavoritesCustomAdapter(List<Routine> routineList, View.OnClickListener favoriteClickListener) {
        this.routineList = routineList;
        this.favoriteClickListener = favoriteClickListener;
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
            View view = itemView.getRoot();

            itemView.favoriteButton.setOnClickListener(v -> {
                v.setTag(routineList.get(getAdapterPosition()));
                favoriteClickListener.onClick(v);
            });

            itemView.shareButton.setOnClickListener(v -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.profit.com/routineDetail/" + routineList.get(getAdapterPosition()).getId());

                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);

                v.getContext().startActivity(shareIntent);
            });

            view.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), RoutineActivity.class);
                intent.putExtra(EXTRA_MESSAGE, routineList.get(getAdapterPosition()).getId());
                v.getContext().startActivity(intent);
            });

            this.itemRoutineBinding = itemView;
        }

        public ItemRoutineBinding getItemRoutineBinding() {
            return itemRoutineBinding;
        }
    }

}
