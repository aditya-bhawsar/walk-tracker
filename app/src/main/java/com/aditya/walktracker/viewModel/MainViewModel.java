package com.aditya.walktracker.viewModel;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.aditya.walktracker.database.WalkDatabase;
import com.aditya.walktracker.database.WalkEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<WalkEntity>> walks;

    public MainViewModel(@NonNull Application application) {
        super(application);
        WalkDatabase mDb = WalkDatabase.getInstance(this.getApplication());
        walks = mDb.walkDao().loadAllWalks();
    }

    public LiveData<List<WalkEntity>> getWalks() {
        return walks;
    }

    public boolean walkingNow(Context context, Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
