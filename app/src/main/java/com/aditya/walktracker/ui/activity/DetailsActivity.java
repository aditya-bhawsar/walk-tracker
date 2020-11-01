package com.aditya.walktracker.ui.activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aditya.walktracker.viewModel.DetailsViewModel;
import com.aditya.walktracker.viewModel.DetailsViewModelFactory;
import com.aditya.walktracker.widget.LastWalkWidgetProvider;
import com.aditya.walktracker.R;
import com.aditya.walktracker.database.WalkEntity;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    DetailsViewModel detailsViewModel;
    String endTime, startTime;
    float weight;

    Toolbar mToolbar;
    ImageView walkIv;
    EditText etStart, etEnd, etDate, etDistance, etCalorieBurn;
    Button saveOrDeleteBtn;

    SharedPreferences sharedPreferences;
    WalkEntity walk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(R.string.walk_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weightString =sharedPreferences.getString(getString(R.string.weight), "0");
        weight = Float.parseFloat(weightString);

        DetailsViewModelFactory factory = new DetailsViewModelFactory(getApplication(),getIntent().getBooleanExtra(getString(R.string.toSave),false));
        detailsViewModel = ViewModelProviders.of(this,factory).get(DetailsViewModel.class);

        walkIv = findViewById(R.id.walk_iv);
        saveOrDeleteBtn = findViewById(R.id.save_or_delete_btn);
        etCalorieBurn = findViewById(R.id.calorie_burn_et);
        etStart = findViewById(R.id.start_time_et);
        etEnd = findViewById(R.id.end_time_et);
        etDate = findViewById(R.id.date_et);
        etDistance = findViewById(R.id.distance_et);

        etDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String distance = s.toString();
                if(distance!=null && !distance.trim().isEmpty())
                    etCalorieBurn.setText(String.valueOf(detailsViewModel.calorieBurn(distance,startTime,endTime,weight)));
                else
                    etCalorieBurn.setText("");
            }
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        if(getIntent().getIntExtra(getString(R.string.idWalk),0)!=0){
            etDistance.setFocusable(false);
            etDistance.setFocusableInTouchMode(false);
            etDistance.setClickable(false);

            detailsViewModel.getWalkData(getIntent().getIntExtra(getString(R.string.idWalk),0)).observe(this, new Observer<WalkEntity>() {
                @Override
                public void onChanged(@Nullable WalkEntity walkEntity) {
                    if(walkEntity!=null){
                        walk = walkEntity;
                        startTime = walkEntity.getStartTime();
                        endTime = walkEntity.getEndTime();
                        etStart.setText(startTime);
                        etEnd.setText(endTime);
                        etDate.setText(walkEntity.getDate());
                        etDistance.setText(walkEntity.getDistance());
                        Picasso.get().load(walkEntity.getWalkType()).error(R.drawable.ic_walk_med).into(walkIv);
                        saveOrDeleteBtn.setText(R.string.delete);
                    }
                }
            });
        }
        else{
            startTime = getIntent().getStringExtra(getString(R.string.startKey));
            endTime = getIntent().getStringExtra(getString(R.string.endKey));
            etDate.setText(getIntent().getStringExtra(getString(R.string.dateKey)));
            etStart.setText(startTime);
            etEnd.setText(endTime);
            System.out.println("Shit 1"+ getIntent().getStringExtra(getString(R.string.startKey)));
            System.out.println("Shit 2"+ getIntent().getStringExtra(getString(R.string.endKey)));
            System.out.println("Shit 3"+ getIntent().getStringExtra(getString(R.string.dateKey)));

        }

        saveOrDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(DetailsActivity.this);
                int[] appWidgetsId = appWidgetManager.getAppWidgetIds(new ComponentName(DetailsActivity.this, LastWalkWidgetProvider.class));
                if(saveOrDeleteBtn.getText().equals(getString(R.string.save))){
                    if(etDistance.getText()==null || etDistance.getText().toString().trim().isEmpty() || etDistance.getText().toString().trim().equals("0")){
                        Toast.makeText(DetailsActivity.this, R.string.valid_distance,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    walk = new WalkEntity(startTime,endTime,etDate.getText().toString().trim(),etDistance.getText().toString().trim());
                    detailsViewModel.saveClicked(walk);
                    Toast.makeText(DetailsActivity.this, R.string.walk_saved, Toast.LENGTH_SHORT).show();
                    LastWalkWidgetProvider.onUpdateManual(DetailsActivity.this,appWidgetManager,appWidgetsId);
                    finish();
                }
                else {
                    detailsViewModel.deleteClicked(walk);
                    Toast.makeText(DetailsActivity.this, R.string.walk_deleted, Toast.LENGTH_SHORT).show();
                    LastWalkWidgetProvider.onUpdateManual(DetailsActivity.this,appWidgetManager,appWidgetsId);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.setting_menu) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        } else if(item.getItemId() == R.id.share_menu){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setAction(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT, "Hey there i found a really cool app named as Walk Tracker you should try it");
            i.setType("text/plain");
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { askOrFinish(); }

    public void askOrFinish(){
        if(detailsViewModel.getToSave()){
            AlertDialog.Builder builder= new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setIcon(R.drawable.ic_walk_med)
                    .setMessage(getString(R.string.save_first_dialog))
                    .setPositiveButton(R.string.ok, null)
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false);
            builder.create().show();
            return;
        }
        finish();
    }
}