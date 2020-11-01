package com.aditya.walktracker.ui.activity;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;

import com.aditya.walktracker.ui.adapter.WalkAdapter;
import com.aditya.walktracker.viewModel.MainViewModel;
import com.aditya.walktracker.R;
import com.aditya.walktracker.database.WalkEntity;
import com.aditya.walktracker.service.WalkingService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements WalkAdapter.ClickListener {

    MainViewModel mainViewModel;

    RecyclerView mRvWalks;
    FloatingActionButton addWalkFab;
    Toolbar mToolbar;
    WalkAdapter mWalkAdapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mRvWalks = findViewById(R.id.walk_rv);
        addWalkFab = findViewById(R.id.add_walk_fab);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("  "+ getString(R.string.name_of_app));
        getSupportActionBar().setIcon(R.drawable.ic_walk_40dp);


        mWalkAdapter = new WalkAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvWalks.setLayoutManager(linearLayoutManager);
        mRvWalks.setAdapter(mWalkAdapter);

        mainViewModel.getWalks().observe(this, new Observer<List<WalkEntity>>() {@Override public void onChanged(@Nullable List<WalkEntity> walkEntityList) { mWalkAdapter.refresh(walkEntityList); }});

        addWalkFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WalkingService.class);
                startService(i);
                addWalkFab.hide();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String s = sharedPreferences.getString(getString(R.string.weight),"");
        try {
            float w= Float.parseFloat(s);
            if(w==0 || w<1 || w>=200){ askWeight(); }
        }
        catch (Exception e){
            askWeight();
            e.printStackTrace();
        }

        if(mainViewModel.walkingNow(this, WalkingService.class)){ addWalkFab.hide(); }
        else { addWalkFab.show(); }
    }

    public void askWeight(){
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(input)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast error = Toast.makeText(MainActivity.this,R.string.weight_msg, Toast.LENGTH_LONG);
                        String s = input.getText().toString().trim();
                        try {
                            float w = Float.parseFloat(s);
                            if(w==0 || w<1 || w>=200){
                                error.show();
                                askWeight();
                            }
                            else { sharedPreferences.edit().putString(getString(R.string.weight),s).apply(); }
                        }catch (Exception e){
                            e.printStackTrace();
                            askWeight();
                        }
                    }
                })
                .setTitle(getString(R.string.user_weight));
        builder.show();
    }

    @Override
    public void walkClicked(int pos) {
        Intent i = new Intent(this, DetailsActivity.class);
        i.putExtra(getString(R.string.toSave),false);
        i.putExtra(getString(R.string.idWalk), pos);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting_menu) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        } else if(item.getItemId() == R.id.share_menu){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setAction(Intent.ACTION_SEND);
            System.out.println("Bakchodi hui kya");
            i.putExtra(Intent.EXTRA_TEXT, "Hey there i found a really cool app named as Walk Tracker you should try it");
            i.setType("text/plain");
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}