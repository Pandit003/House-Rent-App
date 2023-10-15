package com.example.house_rentalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.house_rentalapplication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SearchView searchView;
    ImageView houseimage,proimg;
    FirebaseUser user;
    FirebaseAuth auth;
    int count;
    TextView housenametext,houseaddresstext,housepricetext,nearby,houseforyou;
    RecyclerView recyclerView;
//    String housename,cityofhouse,locationhouse,houseprice,houseimguri;
    List<Map<String, Object>> houseDataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        houseimage=findViewById(R.id.imageView2);
        housenametext=findViewById(R.id.housename);
        houseaddresstext=findViewById(R.id.houseaddress);
        housepricetext=findViewById(R.id.houseprice);
        proimg=findViewById(R.id.proimg);
        searchView=findViewById(R.id.search);
        nearby=findViewById(R.id.nearby);

        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=user.getEmail();
                count=1;
                FirebaseFirestore.getInstance().collection(email+"Person").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                String yourlocation = doc.getString("Location");
                                filterHouses(yourlocation);
                            }
                        }
                    }
                });
            }
        });


        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser()==null){
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
            finish();
        }
        FirebaseFirestore.getInstance().collection("Houses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String housename,cityofhouse,locationhouse,houseprice,houseimguri,docid;
                                housename = doc.getString("Name");
                                locationhouse=doc.getString("Location");
                                cityofhouse=doc.getString("City");
                                houseprice=doc.getString("Price");
                                houseimguri=doc.getString("House_image");
                                docid=doc.getId();
                                String houseaddress=locationhouse+" , "+cityofhouse;

                                    Map<String, Object> housedata = new HashMap<>();
                                    housedata.put("Name", housename);
                                    housedata.put("Location", houseaddress);
                                    housedata.put("Price", houseprice);
                                    housedata.put("House_image", houseimguri);
                                    housedata.put("Docid",docid);

                                    houseDataList.add(housedata);

                                    recyclerView = findViewById(R.id.recyclerview);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                    HouseAdapter ad = new HouseAdapter(houseDataList);
                                    recyclerView.setAdapter(ad);
                                    recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
                                }
                            }

                        else
                            Toast.makeText(MainActivity.this, "not work", Toast.LENGTH_SHORT).show();
                    }
                });



        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int menuitemId= item.getItemId();
                if(menuitemId == R.id.home){
                    replaceFragment(new HomeFragment());
                    return true;
                }
                else if(menuitemId == R.id.renthome){
                    replaceFragment(new ProfileFragment());
                    return true;
                }
                else if(menuitemId == R.id.setting){
                    replaceFragment(new SettingFragment());
                    return true;
                }

                return true;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterHouses(newText);
                return false;
            }
        });

        user=auth.getCurrentUser();
        String usermail=user.getEmail();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection(usermail+"Person").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()) {

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String imageuri = doc.getString("Profile_uri");
                        Picasso.get()
                                .load(imageuri)
                                .into(proimg);
                    }
                }
            }
        });


    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
    private void filterHouses(String query) {
        houseDataList.clear();
        FirebaseFirestore.getInstance().collection("Houses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String query1=query;
                                String location = doc.getString("Location");
//                                String state = doc.getString("City");
//                                String location1=location+","+state;
                                String formattedLocation = location != null ? location.replaceAll("\\s", "").toLowerCase() : "";
                                String formattedQuery = query1.replaceAll("\\s", "").toLowerCase();
                                if (formattedLocation.contains(formattedQuery)){
                                    // The location contains the query substring, add it to your list
                                    String housename = doc.getString("Name");
                                    String cityofhouse = doc.getString("City");
                                    String houseprice = doc.getString("Price");
                                    String houseimguri = doc.getString("House_image");
                                    String houseaddress = location + " , " + cityofhouse;
                                    String docid2=doc.getId();
                                    Log.d("DEBUG", "Formatted Query: " + query1);

                                    Map<String, Object> housedata = new HashMap<>();
                                    housedata.put("Name", housename);
                                    housedata.put("Location", houseaddress);
                                    housedata.put("Price", houseprice);
                                    housedata.put("House_image", houseimguri);
                                    housedata.put("Docid",docid2);

                                    houseDataList.add(housedata);
                                }else{
                                    Log.d("DEBUG", "not work " );

                                }
                            }
                                // Update your RecyclerView adapter after filtering the houses
                                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                HouseAdapter ad = new HouseAdapter(houseDataList);
                                recyclerView.setAdapter(ad);
                                recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

                        } else {
                            Toast.makeText(MainActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

}
    public void onBackPressed() {
        if (count == 1) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Optional: Call finish() to close the current activity if you don't want to keep it in the back stack
        }
        else{
            moveTaskToBack(true);
        }
    }
}