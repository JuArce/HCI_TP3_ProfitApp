package ar.edu.itba.hci.profitapp.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Achievement {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("date")
    @Expose
    private long date;

    @SerializedName("weight")
    @Expose
    private double weight;

    @SerializedName("height")
    @Expose
    private double height;

    @SerializedName("metadata")
    @Expose
    private Object metadata;


    public Achievement(double weight) {
        this(0, new Date().getTime(), weight, 0, null );
    }

    public Achievement(long date, double weight) {
        this.date = date;
        this.weight = weight;
    }

    public Achievement(int id, long date, double weight, double height, Object metadata) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.height = height;
        this.metadata = metadata;
    }

    public Integer getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }
}
/*Log.d("TAG","ARCHIEVEMENT LIST LENGTH" +Integer.toString(achievementList.size()));
        achievementList.forEach(v -> {
            double aux = v.getWeight();
            Log.d("TAG", "PESO: "+Double.toString(aux));
        });*/