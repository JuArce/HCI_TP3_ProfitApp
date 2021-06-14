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
    private Integer id;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "firstName")
    private String firstName;
    @ColumnInfo(name = "lastName")
    private String lastName;
    @ColumnInfo(name = "gender")
    private String gender;
    @ColumnInfo(name = "birthdate")
    private Date birthdate;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "avatarUrl")
    private String avatarUrl;
    @ColumnInfo(name = "date")
    private Date date;
    @ColumnInfo(name = "lastActivity")
    private Date lastActivity;
    //@ColumnInfo(name = "verified") //me parece que este no va
    //private Boolean verified;
}
