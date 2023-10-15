package com.example.house_rentalapplication;

import static android.app.Activity.RESULT_OK;

import static com.google.common.io.Files.getFileExtension;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView houseimg,bathroomimg,kitchenimg,hallimg;
    Button renthousebtn;
    FirebaseAuth mAuth;
    FirebaseUser user;
    int count=0;
     static final int IMAGE_REQUEST = 2;
    public Uri imageUri,houseimguri,bathroomimguri,kitchenimguri,hallimguri;
    public ImageView clickedImageView;


    String uid;
    EditText nameofhouse,houselocation,contactno,noofbhk,housecity,priceofhouse,abouthouse;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        houseimg=view.findViewById(R.id.houseimg);
        bathroomimg=view.findViewById(R.id.bathroomimg);
        kitchenimg=view.findViewById(R.id.kitchenimg);
        hallimg=view.findViewById(R.id.hallimg);
        nameofhouse=view.findViewById(R.id.nameofhouse);
        houselocation=view.findViewById(R.id.houselocation);
        contactno=view.findViewById(R.id.contactno);
        noofbhk=view.findViewById(R.id.noofbhk);
        housecity=view.findViewById(R.id.housecity);
        priceofhouse=view.findViewById(R.id.priceofhouse);
        abouthouse=view.findViewById(R.id.abouthouse);
        renthousebtn=view.findViewById(R.id.renthousebtn);
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();




            renthousebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String housename,cityofhouse,locationhouse,contactdetail,bhk,houseprice,housedescription;
                    housename = nameofhouse.getText().toString();
                    cityofhouse = housecity.getText().toString();
                    locationhouse = houselocation.getText().toString();
                    contactdetail = contactno.getText().toString();
                    bhk = noofbhk.getText().toString();
                    houseprice = priceofhouse.getText().toString();
                    housedescription = abouthouse.getText().toString();

                    if (housename.isEmpty() || cityofhouse.isEmpty() || locationhouse.isEmpty() || contactdetail.isEmpty() ||
                            bhk.isEmpty() || houseprice.isEmpty() || housedescription.isEmpty()) {
                        Toast.makeText(getContext(), "Enter full details", Toast.LENGTH_SHORT).show();
                    } else if (count == 0 || count == 1 || count == 2 || count == 3) {
                        Toast.makeText(getContext(), "Upload all images", Toast.LENGTH_SHORT).show();
                    } else if (!statename(cityofhouse)) {
                        Toast.makeText(getContext(), "Enter valid state", Toast.LENGTH_SHORT).show();
                    } else {

                        uid = user.getEmail();

                        Map<String, Object> data = new HashMap<>();
                        data.put("Name", housename);
                        data.put("City", cityofhouse);
                        data.put("Location", locationhouse);
                        data.put("Contact", contactdetail);
                        data.put("No_of_bhk", bhk);
                        data.put("Price", houseprice);
                        data.put("Discription", housedescription);
                        data.put("House_image", houseimguri);
                        data.put("Bathroom_image", bathroomimguri);
                        data.put("Kitchen_image", kitchenimguri);
                        data.put("Hall_image", hallimguri);
                        FirebaseFirestore.getInstance().collection(uid + "House").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseFirestore.getInstance().collection("Houses").document().set(data);
                                    Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });



        houseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage(houseimg);
            }
        });

        bathroomimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage(bathroomimg);
            }
        });

        kitchenimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage(kitchenimg);
            }
        });

        hallimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage(hallimg);
            }
        });



        return view;
    }

    public static boolean statename(String str) {
        String regex = "^[a-zA-Z\\s]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    private void openImage(ImageView imageView) {
        clickedImageView=imageView;
        Intent intent=new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK){
            imageUri=data.getData();
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        count++;
        pd.show();

        if(imageUri!=null){
            StorageReference fileRef= FirebaseStorage.getInstance().getReference().child("uploads").child(System.currentTimeMillis()+"."+getFileExtension(String.valueOf(imageUri)));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (clickedImageView == houseimg) {
                                houseimg.setImageURI(imageUri);
                                houseimguri=uri;
                            } else if (clickedImageView == bathroomimg) {
                                bathroomimg.setImageURI(imageUri);
                                bathroomimguri=uri;
                            } else if (clickedImageView == kitchenimg) {
                                kitchenimg.setImageURI(imageUri);
                                kitchenimguri=uri;
                            } else if (clickedImageView == hallimg) {
                                hallimg.setImageURI(imageUri);
                                hallimguri=uri;
                            }

                            pd.dismiss();
                        }
                    });
                }
            });
        }
    }
}
