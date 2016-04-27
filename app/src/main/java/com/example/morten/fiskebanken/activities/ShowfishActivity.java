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
    RegisterActivity registerActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfish);

        fishDataSource = new FishDataSource(this);
        try {
            fishDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) findViewById(R.id.list);

        List<Fisk> fisker = fishDataSource.getAllFisk();



    /*
        ImageView imageView = (ImageView) findViewById(R.id.fiskebilde);
        Bitmap bitmap = BitmapFactory.decodeFile(registerActivity.getmCurrentPhotoPath());
        imageView.setImageBitmap(bitmap);
    */

        adapter = new FishListAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        adapter.addAll(fisker);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visFisk(position);
            }
        });
    }

    private void visFisk(int position){
        Fisk f = adapter.getItem(position);
        Intent intent = new Intent(ShowfishActivity.this, FishinfoActivity.class);
        intent.putExtra("Id",f.getId());
        startActivity(intent);


    }


}
