package ar.edu.itba.hci.profitapp.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CycleExercise {

    @SerializedName("order")
    @Expose
    private int order;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("repetitions")
    @Expose
    private int repetitions;
    @SerializedName("exercise")
    @Expose
    private Exercise exercise;

    public CycleExercise() {
    }

    public CycleExercise(int order, int duration, int repetitions, Exercise exercise) {
        super();
        this.order = order;
        this.duration = duration;
        this.repetitions = repetitions;
        this.exercise = exercise;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
