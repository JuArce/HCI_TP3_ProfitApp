package ar.edu.itba.hci.profitapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.profitapp.db.entity.UserEntity;

@Dao
public abstract class UserDao {
    @Query("SELECT * FROM User")
    public abstract LiveData<List<UserEntity>> findAll();

//    @Query("SELECT * FROM User LIMIT :limit OFFSET :offset")
//    public abstract LiveData<List<UserEntity>> findAll(int limit, int offset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(UserEntity... user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<UserEntity> users);

    @Update
    public abstract void update(UserEntity user);

    @Delete
    public abstract void delete(UserEntity user);

    @Query("DELETE FROM User WHERE id = :id")
    public abstract void delete(int id);

//    @Query("DELETE FROM User WHERE id IN (SELECT id FROM User LIMIT :limit OFFSET :offset)")
//    public abstract void delete(int limit, int offset);

    @Query("DELETE FROM User")
    public abstract void deleteAll();

    @Query("SELECT * FROM User WHERE id = :id")
    public abstract LiveData<UserEntity> findById(int id);

}
