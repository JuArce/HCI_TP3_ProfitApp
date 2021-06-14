package ar.edu.itba.hci.profitapp.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "Routine", indices = {@Index("id")}, primaryKeys = {"id"})
public class RoutineEntity {
    @NonNull
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "detail")
    public String detail;
    @ColumnInfo(name = "date")
    public long date;
    @ColumnInfo(name = "averageRating")
    public double averageRating;
    @ColumnInfo(name = "isPublic")
    public boolean isPublic;
    @ColumnInfo(name = "difficulty")
    public String difficulty;
    @ColumnInfo(name = "category")
    public int categoryId;
    @ColumnInfo(name = "user")
    public int userId;


    public RoutineEntity(int id, String name, String detail, long date, double averageRating, boolean isPublic, String difficulty, int categoryId, int userId) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.date = date;
        this.averageRating = averageRating;
        this.isPublic = isPublic;
        this.difficulty = difficulty;
        this.categoryId = categoryId;
        this.userId = userId;
    }
}
