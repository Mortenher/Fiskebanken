package com.example.morten.fiskebanken.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.morten.fiskebanken.utility.FishListAdapter;
import com.example.morten.fiskebanken.utility.Fisk;
import com.example.morten.fiskebanken.R;
import com.example.morten.fiskebanken.database.FishDataSource;
import com.example.morten.fiskebanken.utility.Fiskeart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowfishActivity extends AppCompatActivity {

    private FishDataSource fishDataSource;
    private FishListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfish);

        //Database-connection
        fishDataSource = new FishDataSource(this);
        try {
            fishDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) findViewById(R.id.list);

        List<Fisk> fisker = fishDataSource.getAllFisk();

        //Fyller listen med fiskeobjekter
        adapter = new FishListAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        adapter.addAll(fisker);

        //Onclick fra listen som tar deg til Infoaktivitet om den valgte fisken
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visFisk(position);
            }
        });
    }
    //Metoden som blir kalt i onclick på liste
    private void visFisk(int position){
        Fisk f = adapter.getItem(position);
        Intent intent = new Intent(ShowfishActivity.this, FishinfoActivity.class);
        intent.putExtra("Id",f.getId());
        startActivity(intent);


    }


}
