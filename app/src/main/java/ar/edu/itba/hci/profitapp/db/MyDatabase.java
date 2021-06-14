package ar.edu.itba.hci.profitapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ar.edu.itba.hci.profitapp.db.dao.CategoryDao;
import ar.edu.itba.hci.profitapp.db.dao.RoutineDao;
import ar.edu.itba.hci.profitapp.db.dao.UserDao;
import ar.edu.itba.hci.profitapp.db.entity.CategoryEntity;
import ar.edu.itba.hci.profitapp.db.entity.RoutineEntity;
import ar.edu.itba.hci.profitapp.db.entity.UserEntity;


@Database(entities = {RoutineEntity.class, CategoryEntity.class, UserEntity.class}, exportSchema = false, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    abstract public RoutineDao routineDao();
    abstract public CategoryDao categoryDao();
    abstract public UserDao userDao();
}
