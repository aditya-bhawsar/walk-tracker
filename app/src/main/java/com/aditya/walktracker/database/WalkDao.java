package com.aditya.walktracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WalkDao {

    @Query("SELECT * FROM walk ORDER BY id")
    LiveData<List<WalkEntity>> loadAllWalks();

    @Insert
    void insertWalk(WalkEntity walkEntity);

    @Delete
    void deleteWalk(WalkEntity walkEntity);

    @Query("SELECT * FROM walk WHERE id = :id")
    LiveData<WalkEntity> loadWalkByID(int id);

    @Query("SELECT * FROM walk WHERE id = :id")
    WalkEntity loadWalkByIDWidget(int id);

    @Query("SELECT MAX(id) FROM walk ")
    int getLastWalk();
}
