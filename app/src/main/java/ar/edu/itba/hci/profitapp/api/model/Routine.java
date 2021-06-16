package ar.edu.itba.hci.profitapp.api.model;

import androidx.room.util.StringUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Routine {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("date")
    @Expose
    private long date;
    @SerializedName("averageRating")
    @Expose
    private double averageRating;
    @SerializedName("isPublic")
    @Expose
    private boolean isPublic;
    @SerializedName("difficulty")
    @Expose
    private String difficulty;
    @SerializedName("metadata")
    @Expose
    private Object metadata;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("user")
    @Expose
    private User user;

    private boolean isFavorite;

    public Routine() {
        this.isFavorite = false;
    }

    public Routine(int id, String name, String detail, long date, double averageRating, boolean isPublic, String difficulty, Object metadata, Category category, User user) {
        super();
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.date = date;
        this.averageRating = averageRating;
        this.isPublic = isPublic;
        this.difficulty = difficulty;
        this.metadata = metadata;
        this.category = category;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        Date fullDate = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strDate= formatter.format(fullDate);
        return strDate;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getDifficulty() {
        return difficulty.substring(0,1).toUpperCase() + difficulty.substring(1).toLowerCase();
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

}