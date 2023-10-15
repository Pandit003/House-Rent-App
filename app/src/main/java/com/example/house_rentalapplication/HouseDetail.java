package com.example.house_rentalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class HouseDetail extends AppCompatActivity {
    ImageView imageofhouse,imageofbathroom,imageofkitchen,imageofhall;
    TextView housenametextview,locationtextview,contacttextview,discriptiontextview,pricetextview;
    Button housebookbutton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);
        imageofhouse=findViewById(R.id.imageofhouse);
        imageofbathroom=findViewById(R.id.imageofbathroom);
        imageofkitchen=findViewById(R.id.imageofkitchen);
        imageofhall=findViewById(R.id.imageofhall);
        housenametextview=findViewById(R.id.housenametextView);
        locationtextview=findViewById(R.id.locationtextview);
        contacttextview=findViewById(R.id.contacttextview);
        discriptiontextview=findViewById(R.id.discriptiontextview);
        pricetextview=findViewById(R.id.pricetextview);
        housebookbutton=findViewById(R.id.housebookbutton);
        Intent intent=getIntent();
        String docid=intent.getStringExtra(HouseAdapter.extraname);
        FirebaseFirestore.getInstance().collection("Houses").document(docid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String hname=task.getResult().getString("Name");
                String hloc=task.getResult().getString("Location");
                String hcon=task.getResult().getString("Contact");
                String hdis=task.getResult().getString("Discription");
                String hpri=task.getResult().getString("Price");
                String himg=task.getResult().getString("House_image");
                String kimag=task.getResult().getString("Kitchen_image");
                String bimg=task.getResult().getString("Bathroom_image");
                String hllimg=task.getResult().getString("Hall_image");
                housenametextview.setText("House :    "+hname);
                locationtextview.setText("Location : \n    "+hloc);
                contacttextview.setText("Contact : \n    "+hcon);
                discriptiontextview.setText("About : \n    "+hdis);
                pricetextview.setText("Rs  "+hpri);
                Picasso.get()
                        .load(himg).fit()
                        .into(imageofhouse);
                Picasso.get()
                        .load(kimag).fit()
                        .into(imageofkitchen);
                Picasso.get()
                        .load(bimg).fit()
                        .into(imageofbathroom);
                Picasso.get()
                        .load(hllimg).fit()
                        .into(imageofhall);
            }
        });
    }
}