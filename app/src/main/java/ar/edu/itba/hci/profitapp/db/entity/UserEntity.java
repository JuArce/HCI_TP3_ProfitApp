package ar.edu.itba.hci.profitapp.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

import java.util.Date;

@Entity(tableName = "User", indices = {@Index("id")}, primaryKeys = {"id"})
public class UserEntity {
    @NonNull
    @ColumnInfo(name = "id")
    public Integer id;
    @ColumnInfo(name = "username")
    public String username;
    @ColumnInfo(name = "firstName")
    public String firstName;
    @ColumnInfo(name = "lastName")
    public String lastName;
    @ColumnInfo(name = "gender")
    public String gender;
    @ColumnInfo(name = "birthdate")
    public long birthdate;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "avatarUrl")
    public String avatarUrl;
    @ColumnInfo(name = "date")
    public long date;
    @ColumnInfo(name = "lastActivity")
    public long lastActivity;
    //@ColumnInfo(name = "verified") //me parece que este no va
    //private Boolean verified;


    public UserEntity(@NonNull Integer id, String username, String firstName, String lastName, String gender, long birthdate, String email, String phone, String avatarUrl, long date, long lastActivity) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthdate = birthdate;
        this.email = email;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.date = date;
        this.lastActivity = lastActivity;
    }
}
