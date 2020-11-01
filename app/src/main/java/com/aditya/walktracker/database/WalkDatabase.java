package com.aditya.walktracker.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {WalkEntity.class}, version = 1,exportSchema = false)
public abstract class WalkDatabase extends RoomDatabase {

    private static WalkDatabase sInstance;
    private static final String DATABASE_NAME = "walkDb";
    private static final Object LOCK= new Object();

    public static WalkDatabase getInstance(Context context){
        if(sInstance==null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(), WalkDatabase.class, WalkDatabase.DATABASE_NAME).allowMainThreadQueries().build();
            }
        }
        return sInstance;
    }

    public abstract WalkDao walkDao();

}
