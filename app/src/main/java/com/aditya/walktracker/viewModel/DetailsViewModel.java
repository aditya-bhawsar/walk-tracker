package com.aditya.walktracker.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.aditya.walktracker.database.WalkDatabase;
import com.aditya.walktracker.database.WalkEntity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsViewModel extends AndroidViewModel {

    private boolean toSave;
    LiveData<WalkEntity> walkData;
    private WalkDatabase mDb;

    public DetailsViewModel(@NonNull Application application,boolean toSave) {
        super(application);
        mDb = WalkDatabase.getInstance(this.getApplication());
        this.toSave = toSave;
    }

    public void deleteClicked(WalkEntity walkEntity){
        mDb.walkDao().deleteWalk(walkEntity);
    }

    public LiveData<WalkEntity> getWalkData(int id) {
        walkData = mDb.walkDao().loadWalkByID(id);
        return walkData;
    }

    public void saveClicked(WalkEntity walkEntity){
        mDb.walkDao().insertWalk(walkEntity);
        toSave = false;
    }

    public boolean getToSave(){ return toSave; }


    public double getTime(String startTime, String endTime){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        double diffSec= 0;
        try {
            Date d1;
            Date d2;
            d1 = format.parse(startTime);
            d2 = format.parse(endTime);
            double diff = d2.getTime() - d1.getTime();
            diffSec = diff/ (1000*60*60);
        }
        catch (Exception e){e.printStackTrace();}
        return diffSec;
    }

    public double getVelocity(String distance, String startTime, String endTime){
        double vel = getTime(startTime,endTime);
        System.out.println(vel);
        return (Float.parseFloat(distance)/1000)/vel;
    }

    public String calorieBurn(String distance, String startTime, String endTime, float weight){
        //[0.0215 x KPH3 - 0.1765 x KPH2 + 0.8710 x KPH + 1.4577] x WKG x T (0% grade)

        double vel = getVelocity(distance,startTime,endTime);
        System.out.println(vel);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((0.0215 * Math.pow(vel,3)- 0.1765 * Math.pow(vel,2) + 0.8710 * Math.pow(vel,1) + 1.4577 ) * weight * getTime(startTime, endTime));
    }
}
