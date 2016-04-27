package com.example.morten.fiskebanken.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.morten.fiskebanken.R;
import com.example.morten.fiskebanken.database.FishDataSource;
import com.example.morten.fiskebanken.utility.Fisk;
import com.example.morten.fiskebanken.utility.Fiskeart;

import java.sql.SQLException;
import java.util.ArrayList;

public class FishinfoActivity extends AppCompatActivity {

    TextView mTextView;
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;
    TextView mTextView4;
    Fisk denneFisken;
    Fiskeart denneArten;
    ArrayList<Fisk> fisker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishinfo);

        Intent i = getIntent();
        int fishId = i.getIntExtra("Id",0);

        mTextView = (TextView)findViewById(R.id.fishinfo);
        mTextView1 = (TextView)findViewById(R.id.fishinfo1);
        mTextView2 = (TextView)findViewById(R.id.fishinfo2);
        mTextView3 = (TextView)findViewById(R.id.fishinfo3);
        mTextView4 = (TextView)findViewById(R.id.fishinfo4);




        FishDataSource fishDataSource = new FishDataSource(this);

        try {
            fishDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fisker = (ArrayList<Fisk>) fishDataSource.getAllFisk();
        for(Fisk f : fisker){
            if(f.getId() == fishId){
                denneFisken = f;
                break;
            }
        }
        findArtInfo();
        mTextView1.setText(denneArten.name);
        mTextView2.setText(denneArten.getMeanWeight() + "");
        mTextView3.setText(denneArten.getMeanLenght()+"");



    }

    private void findArtInfo(){
        System.out.println(fisker.size());
        ArrayList<Fiskeart> arter = new ArrayList<Fiskeart>();
        outer: for(Fisk f : fisker){
            for(Fiskeart a: arter){
                //arten ligger i listen
                if(a.name.contentEquals(f.getType())){
                    System.out.println("Finnes i lista, legger til verdier: " + f.getType());
                    a.length += f.getLengde();
                    if(a.longest < f.getLengde()){
                        a.longest = f.getLengde();
                    }
                    a.weight += f.getVekt();
                    if(a.heaviest < f.getVekt()){
                        a.heaviest = f.getVekt();
                    }
                    a.antall++;
                    continue outer;
                }
            }
            System.out.println("Legger til ny art i lista");
            Fiskeart art = new Fiskeart();
            art.name = f.getType();
            art.weight = f.getVekt();
            art.heaviest = f.getVekt();
            art.length = f.getLengde();
            art.longest = f.getLengde();
            art.antall++;
            arter.add(art);
        }

        for(Fiskeart a: arter){
            if(a.name.contentEquals(denneFisken.getType())){
                denneArten = a;
                return;
            }
        }
    }
}
