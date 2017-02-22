package com.example.project.danyaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.project.danyaapp.Clasess.DB.DBHelper;
import com.example.project.danyaapp.Clasess.Event;
import com.example.project.danyaapp.Clasess.Task;
import com.example.project.danyaapp.Clasess.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SelectGroupActivity extends AppCompatActivity implements View.OnClickListener, Event{

    private ProgressDialog myDialog;

    private RelativeLayout relativeLayout;

    private DBHelper dbHelper;

    private String groupName = "";

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        dbHelper = new DBHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        relativeLayout = (RelativeLayout) findViewById(R.id.rel);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        if(dbHelper.isYouGroup()){
            groupName = dbHelper.getYouGroup();
            flag = true;
        }
        getGroups();
    }

    public void getGroups(){
        myDialog = new ProgressDialog(this);
        myDialog.setTitle("Загрузка групп");
        myDialog.setMessage("Пожалуйста подождите");
        myDialog.show();
        new Task.ParseGroupJson(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(SelectGroupActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_re_group:
                getGroups();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void event(Object obj) {
        ArrayList<String> arrayList = (ArrayList) obj;
        RadioButton rd;
        RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        relativeLayout.removeAllViews();
        relativeLayout.addView(radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                Log.d("id", String.valueOf(id));
                if(id != -1){
                    String name = String.valueOf(((RadioButton) radioGroup.findViewById(id)).getText());
                    Toast.makeText(getApplicationContext(),
                            "Вы выбрали " + name,
                            Toast.LENGTH_SHORT).show();
                    dbHelper.insertYouGroup(name);
                }
            }
        });
        Random r = new Random(System.currentTimeMillis());
        for (String s : arrayList){
            rd = new RadioButton(getApplication());
            rd.setId(r.nextInt(Integer.MAX_VALUE));
            rd.setText(s);
            rd.setTextSize(22);
            rd.setTextColor(Color.BLACK);
            radioGroup.addView(rd);
            if (flag){
                if(groupName.equals(s)){
                    radioGroup.check(rd.getId());
                }
            }
        }
        myDialog.dismiss();
    }
}
