package com.example.morten.fiskebanken;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.morten.fiskebanken.database.FishDataSource;
import com.example.morten.fiskebanken.RegisterActivity;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class ShowfishActivity extends AppCompatActivity {

    private FishDataSource fishDataSource;
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

        FishListAdapter adapter = new FishListAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        adapter.addAll(fisker);
    }
}
