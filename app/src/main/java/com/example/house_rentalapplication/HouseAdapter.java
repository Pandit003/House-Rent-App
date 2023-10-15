package com.example.house_rentalapplication;

import static androidx.core.content.ContextCompat.createDeviceProtectedStorageContext;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {

    private static List<Map<String, Object>> houseDataList;
    String docid;
    public static final String extraname="com.example.house_rentalapplication";


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView housename;
        private final TextView houseaddress;
        private final TextView houseprice;
        private  ImageView imageView;
        Button buy;

        public ViewHolder(View view) {
            super(view);
            housename = view.findViewById(R.id.housename);
            houseaddress = view.findViewById(R.id.houseaddress);
            houseprice = view.findViewById(R.id.houseprice);
            imageView = view.findViewById(R.id.imageView2);
            buy = view.findViewById(R.id.buy);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openhousedetail(v.getContext());
                }
            });
            housename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openhousedetail(v.getContext());
                }
            });
            houseaddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openhousedetail(v.getContext());
                }
            });
            houseprice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openhousedetail(v.getContext());
                }
            });
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openhousedetail(v.getContext());
                }
            });


        }
        void openhousedetail(Context context){
            Map<String, Object> houseData = houseDataList.get(getAdapterPosition());
            String docid1 = (String) houseData.get("Docid");
            Intent intent= new Intent(context, HouseDetail.class);
            intent.putExtra(extraname,docid1);
            context.startActivity(intent);
        }
    }
    public void housedetailpage(){

    }

    public HouseAdapter(List<Map<String, Object>> houseDataList) {
        this.houseDataList = houseDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.house_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> houseData = houseDataList.get(position);

        String imageUriOfHouse = (String) houseData.get("House_image");
         docid = (String) houseData.get("Docid");
        holder.housename.setText("  " + houseData.get("Name"));
        holder.houseaddress.setText("  Location: " + houseData.get("Location"));
        holder.houseprice.setText("  Price: " + houseData.get("Price"));

        // Load image into ImageView using Picasso or any other image loading library
        Picasso.get()
                .load(imageUriOfHouse).fit()
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return houseDataList.size();
    }
}
