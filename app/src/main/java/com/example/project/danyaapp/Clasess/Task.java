package com.example.project.danyaapp.Clasess;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.project.danyaapp.Clasess.DB.DBHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class Task {

    public static class ParseGroupJson extends AsyncTask<Void, Void, ArrayList<String>> {

        private Event event;

        public ParseGroupJson(Event event){
            this.event = event;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> arrayList = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(User.URL).get();
                Elements elements = doc.select("tr");
                String str;
                for (int i = 1; i < elements.size(); i++){
                    for (Element element1 : elements.get(i).select("td")){
                        str = String.valueOf(element1.getElementsByTag("font").text());
                        if(!str.equals("")){
                            arrayList.add(str);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> arrayList) {
            super.onPostExecute(arrayList);
            event.event(arrayList);
        }
    }

    public static class getSchedule extends AsyncTask<Void, Void, Void>{

        private Event event;

        private DBHelper dbHelper;

        public getSchedule(DBHelper dbHelper, Event event){
            this.dbHelper = dbHelper;
            this.event = event;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(User.URL).get();
                Elements elements = doc.select("tr");
                String nameGroup =  dbHelper.getYouGroup();
                String nameGroupS = "";
                dbHelper.G();
                String scheduleGroupUrl = "";
                Main:for (int i = 1; i < elements.size(); i++){
                    for (Element element : elements.get(i).select("td")){
                        nameGroupS = element.getElementsByTag("font").text();
                        if(nameGroup.equals(nameGroupS)){
                            scheduleGroupUrl = User.URL + element.getElementsByTag("a").attr("href");
                            break Main;
                        }
                    }
                }
                doc = Jsoup.connect(scheduleGroupUrl).get();
                elements = doc.select("tr");
                for (int i = 2; i < elements.size(); i++){
                    Elements elements1 = elements.get(i).select("td");
                    for (int j = 1; j < elements1.size(); j++){
                        ContentValues contentValues = new ContentValues();
                        int y = i - 1;
                        int week_id = 1;
                        if(y >= 7){
                            y = y - 6;
                            week_id = 2;
                        }
                        contentValues.put("time_id", j);
                        contentValues.put("day_id", y);
                        contentValues.put("week_id", week_id);
                        contentValues.put("name", elements1.get(j).getElementsByTag("font").text());
                        dbHelper.insertG(contentValues);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            event.event(aVoid);
        }
    }

}
