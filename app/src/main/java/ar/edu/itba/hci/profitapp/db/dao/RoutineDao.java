package ar.edu.itba.hci.profitapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.profitapp.db.entity.RoutineEntity;

@Dao
public abstract class RoutineDao {

    @Query("SELECT * FROM Routine")
    public abstract LiveData<List<RoutineEntity>> findAll();

//    @Query("SELECT * FROM Routine LIMIT :limit OFFSET :offset")
//    public abstract LiveData<List<RoutineEntity>> findAll(int limit, int offset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(RoutineEntity... routine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<RoutineEntity> routines);

    @Update
    public abstract void update(RoutineEntity routine);

    @Delete
    public abstract void delete(RoutineEntity routine);

    @Query("DELETE FROM Routine WHERE id = :id")
    public abstract void delete(int id);

//    @Query("DELETE FROM Routine WHERE id IN (SELECT id FROM Routine LIMIT :limit OFFSET :offset)")
//    public abstract void delete(int limit, int offset);

    @Query("DELETE FROM Routine")
    public abstract void deleteAll();

    @Query("SELECT * FROM Routine WHERE id = :id")
    public abstract LiveData<RoutineEntity> findById(int id);

}
