package com.example.project.danyaapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.danyaapp.Clasess.DB.DBHelper;

public class ShActivity extends Fragment {

    private int day_id = -1;

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_sh, null);
        listView = (ListView) v.findViewById(R.id.ls);
        DBHelper db = new DBHelper(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, db.getP(this.day_id));
        listView.setAdapter(adapter);
        return v;
    }

    public void setDayID(int day_id){
        this.day_id = day_id;
    }

}
