package ar.edu.itba.hci.profitapp.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName ="Category", indices = {@Index("id")}, primaryKeys = {"id"})
public class CategoryEntity {
    @NonNull
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "detail")
    public String detail;

    public CategoryEntity(int id, String name, String detail) {
        this.id = id;
        this.name = name;
        this.detail = detail;
    }
}
