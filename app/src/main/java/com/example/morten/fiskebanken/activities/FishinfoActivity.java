package com.example.morten.fiskebanken.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.morten.fiskebanken.R;
import com.example.morten.fiskebanken.database.FishDataSource;
import com.example.morten.fiskebanken.utility.Fisk;
import com.example.morten.fiskebanken.utility.Fiskeart;
import com.google.android.gms.maps.model.BitmapDescriptor;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

public class FishinfoActivity extends AppCompatActivity {

    TextView mTextView;
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;
    TextView mTextView5;
    TextView mTextView6;
    ImageView mImageView;
    Fisk denneFisken;
    Fiskeart denneArten;
    ArrayList<Fisk> fisker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishinfo);

        //Henter id som blir sendt med fra enten Showfish eller maps-activity
        Intent i = getIntent();
        int fishId = i.getIntExtra("Id",0);

        mTextView = (TextView)findViewById(R.id.fishinfo);
        mTextView1 = (TextView)findViewById(R.id.fishinfo1);
        mTextView2 = (TextView)findViewById(R.id.fishinfo2);
        mTextView3 = (TextView)findViewById(R.id.fishinfo3);
        mImageView = (ImageView)findViewById(R.id.fishinfo5);
        mTextView5 = (TextView)findViewById(R.id.fishinfo6);
        mTextView6 = (TextView)findViewById(R.id.fishinfo7);

        //Database-tilkobling
        FishDataSource fishDataSource = new FishDataSource(this);

        try {
            fishDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Finner fisken aktiviteten skal vise informasjon om
        fisker = (ArrayList<Fisk>) fishDataSource.getAllFisk();
        for(Fisk f : fisker){
            if(f.getId() == fishId){
                denneFisken = f;
                break;
            }
        }
        //Gi data til hva som skal være i layout
        findArtInfo();
        mTextView1.setText(denneArten.name);
        mTextView2.setText(denneArten.getMeanWeight() + "" + " Kg");
        mTextView3.setText(denneArten.getMeanLenght()+"" + " Cm");
        mTextView5.setText(denneFisken.getVekt()+"" + " Kg");
        mTextView6.setText(denneFisken.getLengde() + "" + " Cm");
        File image = new File(denneFisken.getBilde());
        Bitmap b = BitmapFactory.decodeFile(image.getAbsolutePath());
        mImageView.setImageBitmap(b);


    }

    //Finn informasjon om en art
    private void findArtInfo(){
        ArrayList<Fiskeart> arter = new ArrayList<Fiskeart>();
        outer: for(Fisk f : fisker){
            for(Fiskeart a: arter){
                //arten ligger i listen
                if(a.name.contentEquals(f.getType())){
                    //Om den finnes i lista, legger til verdier
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
            //Legger til nye arter i lista
            Fiskeart art = new Fiskeart();
            art.name = f.getType();
            art.weight = f.getVekt();
            art.heaviest = f.getVekt();
            art.length = f.getLengde();
            art.longest = f.getLengde();
            art.antall++;
            arter.add(art);
        }
        //Sjekker om art og fisken som pekes til er lik
        for(Fiskeart a: arter){
            if(a.name.contentEquals(denneFisken.getType())){
                denneArten = a;
                return;
            }
        }
    }
}
