package com.example.project.danyaapp.Clasess.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.project.danyaapp.Clasess.User;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private String[] days = {
            "Понедельник",
            "Вторник",
            "Среда",
            "Четверг",
            "Пятницы",
            "Суббота"
    };

    private String[] times = {
            "08:30-10:00",
            "10:10-11:40",
            "12:10-13:40",
            "13:50-15:20",
            "15:30-17:00",
            "17:10-18:40"
    };

    private String[] weeks = {
            "Чётная",
            "Нечётная"
    };

    SQLiteDatabase sqLiteDatabase;

    static SQLiteDatabase  sqLiteDatabase1 = null;

    private String tableYou = "you";

    private String tableDays = "days";

    private String tableTime = "time";

    private String tableWeek = "week";

    private String tableSchedule = "schedule";

    public DBHelper(Context context){
        super(context, "db", null, 1);
        if(DBHelper.sqLiteDatabase1 == null){
            DBHelper.sqLiteDatabase1 = getWritableDatabase();
        }
        sqLiteDatabase = DBHelper.sqLiteDatabase1;
    }

    public boolean isYouGroup(){
        return sqLiteDatabase.query(this.tableYou, null, null, null, null, null, null).getCount() == 0 ? false : true;
    }

    public boolean isScheduleGroup(){
        boolean b = sqLiteDatabase.query(this.tableSchedule, null, null, null, null, null, null).getCount() == 0 ? false : true;
        return b;
    }

    public String getYouGroup(){
        String selectQuery = "SELECT group_name FROM you";
        Cursor c = sqLiteDatabase.rawQuery(selectQuery, null);
        String s = "";
        if (c.moveToFirst()) {
            s = c.getString(c.getColumnIndex("group_name"));
        }
        return s;
    }

    public void insertG(ContentValues contentValues){
        sqLiteDatabase.insert(this.tableSchedule, null, contentValues);
    }

    public String[] getP(int day){
        String selectQuery = "SELECT * FROM schedule WHERE day_id = " + day + " AND week_id = " + User.TYPE_W;
        Cursor c = sqLiteDatabase.rawQuery(selectQuery, null);
        ArrayList<String> arrayList = new ArrayList<String>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String time = c.getString(c.getColumnIndex("time_id"));
                    String str = "";
                    switch (time){
                        case "1":
                            str = "08:30-10:00";
                            break;
                        case "2":
                            str = "10:10-11:40";
                            break;
                        case "3":
                            str = "12:10-13:40";
                            break;
                        case "4":
                            str = "13:50-15:20";
                            break;
                        case "5":
                            str = "15:30-17:00";
                            break;
                        case "6":
                            str = "17:10-18:40";
                            break;
                    }
                    arrayList.add(time + " Пара ("+str+")\n" + c.getString(c.getColumnIndex("name")));
                } while (c.moveToNext());
            }
        }
        String [] arr = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++){
            arr[i] = arrayList.get(i);
        }
        return arr;
    }

    public void G(){
        sqLiteDatabase.execSQL("delete from " + tableSchedule);
    }

    public void insertYouGroup(String nameGroup){
        try{
            sqLiteDatabase.execSQL("delete from " + tableYou);
            G();
            ContentValues cv = new ContentValues();
            cv.put("group_name", nameGroup);
            sqLiteDatabase.insert(tableYou, null, cv);
        }catch (Exception e){

        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Ты
        sqLiteDatabase.execSQL("create table " + this.tableYou + " ("
                + "user_id integer primary key autoincrement,"
                + "group_name text" + ");");
        //Пн-Сб
        sqLiteDatabase.execSQL("create table " + this.tableDays + " ("
                + "id integer primary key autoincrement,"
                + "day text" + ");");
        //Время
        sqLiteDatabase.execSQL("create table " + this.tableTime + " ("
                + "id integer primary key autoincrement,"
                + "time text" + ");");
        //Неделя
        sqLiteDatabase.execSQL("create table " + this.tableWeek + " ("
                + "id integer primary key autoincrement,"
                + "name text" + ");");
        //Расписание
        sqLiteDatabase.execSQL("create table " + this.tableSchedule + " ("
                + "id integer primary key autoincrement,"
                + "time_id integer,"
                + "day_id integer," +
                "week_id integer," +
                "name text"+
                ");");
        addDefaultData(sqLiteDatabase);
    }

    private void addDefaultData(SQLiteDatabase sqLiteDatabase){
        ContentValues cvDay = new ContentValues();
        ContentValues cvTime = new ContentValues();
        ContentValues cvWeek = new ContentValues();
        for (String day : this.days) {
            cvDay.put("day", day);
        }
        for(String time : this.times){
            cvTime.put("time", time);
        }
        for (String week : this.weeks){
            cvWeek.put("name", week);
        }
        try {
            sqLiteDatabase.insert(this.tableDays, null, cvDay);
            sqLiteDatabase.insert(this.tableTime, null, cvTime);
            sqLiteDatabase.insert(this.tableWeek, null, cvWeek);
        } catch (Exception e){
            Log.d("Error db transaction",  e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
