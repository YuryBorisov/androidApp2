package com.example.project.danyaapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.project.danyaapp.Clasess.DB.DBHelper;
import com.example.project.danyaapp.Clasess.Event;
import com.example.project.danyaapp.Clasess.Task;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, Event {

    private DBHelper dbHelper;

    private Button selectButtonGroup, buttonUpdate;

    private TextView textViewGroup;

    private ProgressDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        selectButtonGroup = (Button) findViewById(R.id.settings_button_select_group);
        textViewGroup = (TextView) findViewById(R.id.settings_textView_group);
        buttonUpdate = (Button) findViewById(R.id.settings_button_update);
        buttonUpdate.setOnClickListener(this);
        selectButtonGroup.setOnClickListener(this);
        dbHelper = new DBHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        if(this.dbHelper.getWritableDatabase().query("you", null, null, null, null, null, null).getCount() != 0){
            textViewGroup.setText("Ваша группа: " + this.dbHelper.getYouGroup());
            if(this.dbHelper.isScheduleGroup()){
            }else{
                dialog();
                new Task.getSchedule(dbHelper, this).execute();
            }
        }else{
            buttonUpdate.setEnabled(false);
        }
    }

    public void dialog(){
        myDialog = new ProgressDialog(this);
        myDialog.setTitle("Загрузка расписания");
        myDialog.setMessage("Пожалуйста подождите");
        myDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(this.dbHelper.isYouGroup()){
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Пожалуйста выберете группу!",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settings_button_select_group:
                startActivity(new Intent(SettingsActivity.this, SelectGroupActivity.class));
                break;
            case R.id.settings_button_update:
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setMessage("Обновить расписание?");
                ad.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog();
                        new Task.getSchedule(dbHelper, SettingsActivity.this).execute();
                    }
                });
                ad.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                    }
                });
                ad.show();
                break;
        }
    }

    @Override
    public void event(Object obj) {
        myDialog.dismiss();
    }

}
