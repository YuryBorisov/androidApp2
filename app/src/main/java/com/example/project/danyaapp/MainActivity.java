package com.example.project.danyaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.project.danyaapp.Clasess.DB.DBHelper;
import com.example.project.danyaapp.Clasess.Event;
import com.example.project.danyaapp.Clasess.Task;
import com.example.project.danyaapp.Clasess.User;

public class MainActivity extends AppCompatActivity implements Event{

    private DBHelper dbHelper;

    private ProgressDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        if(!dbHelper.isYouGroup()){
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }else{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle(dbHelper.getYouGroup());
            if(this.dbHelper.getWritableDatabase().query("you", null, null, null, null, null, null).getCount() != 0){
                if(!this.dbHelper.isScheduleGroup()){
                    myDialog = new ProgressDialog(this);
                    myDialog.setTitle("Загрузка расписания");
                    myDialog.setMessage("Пожалуйста подождите");
                    myDialog.show();
                    new Task.getSchedule(dbHelper, this).execute();
                }
            }
            if(User.TYPE_W == 1){
                toolbar.setSubtitle("Чётная неделя");
            }else{
                toolbar.setSubtitle("Нечётная неделя");
            }
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setSubtitleTextColor(Color.WHITE);
            Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            if (toolbar2 != null) {
                setSupportActionBar(toolbar2);
            }
            viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_type_day:
                if(User.TYPE_W == 1){
                    User.TYPE_W = 2;
                }else{
                    User.TYPE_W = 1;
                }
                finish();
                startActivity(new Intent(this, this.getClass()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void event(Object obj) {
        myDialog.dismiss();
        finish();
        startActivity(new Intent(this , this.getClass()));
    }
}
