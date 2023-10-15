package com.example.house_rentalapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity {
    String genderofaperson="null",uid,usermail,updateimguri,uri1;
    EditText nameofperson,ageofaperson,locationofaperson,aadharno,stateofaperson;
    Button editdetailsbtn;
    FirebaseAuth auth;
    RadioButton r1,r2,r3;
    Uri imageUri;
    FirebaseUser user;
    ImageView profileimg,clickedImageView;
    static final int IMAGE_REQUEST = 2;


    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        nameofperson=findViewById(R.id.personemail);
        ageofaperson=findViewById(R.id.personpass);
        locationofaperson=findViewById(R.id.emailofaperson);
        stateofaperson=findViewById(R.id.stateofaperson);
        aadharno=findViewById(R.id.personpassword);
        editdetailsbtn=findViewById(R.id.editdetailsbutton);
        profileimg=findViewById(R.id.profileimg);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        usermail=user.getEmail();
        uid= user.getUid();
        r1=findViewById(R.id.r1);
        r2=findViewById(R.id.r2);
        r3=findViewById(R.id.r3);





        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection(usermail+"Person").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()) {
                    String imageuri;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                         imageuri = doc.getString("Profile_uri");
                        nameofperson.setText(doc.getString("Person_Name"));
                        ageofaperson.setText(doc.getString("Age"));
                        locationofaperson.setText(doc.getString("Location"));
                        stateofaperson.setText(doc.getString("State"));
                        aadharno.setText(doc.getString("Aadhar"));
                        stateofaperson.setText(doc.getString("State"));
                        String gender1=doc.getString("Gender");
                        if(gender1.equals("Male")){
                            r1.setChecked(true);}
                        else if (gender1.equals("Female")){
                            r2.setChecked(true);}
                        else if(gender1.equals("Other")){
                            r3.setChecked(true);}
                        if(uri1==null){
                         uri1 = doc.getString("Profile_uri");
                         }
                        updateimguri=imageuri;
                        Picasso.get()
                                .load(imageuri).fit()
                                .into(profileimg);
                    }
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                // Check which radio button was selected
                if (checkedId == R.id.r1) {
                    // Handle option 1 selection
                    genderofaperson="Male";
                } else if (checkedId == R.id.r2) {
                    // Handle option 2 selection
                    genderofaperson="Female";
                } else if (checkedId==R.id.r3) {
                    genderofaperson="Other";
                }
                else
                    Toast.makeText(EditProfile.this, "Select gender", Toast.LENGTH_SHORT).show();
            }
        });

        editdetailsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender=genderofaperson;
                String personName = nameofperson.getText().toString();
                String personAge = ageofaperson.getText().toString();
                String personLocation = locationofaperson.getText().toString();
                String aadharNumber = aadharno.getText().toString();
                String state= stateofaperson.getText().toString();
                if(personName.isEmpty() || personAge.isEmpty() || personLocation.isEmpty() || aadharNumber.isEmpty() || gender.equals("null") || state.isEmpty()){
                    Toast.makeText(EditProfile.this, "Enter full details", Toast.LENGTH_SHORT).show();
                } else if (!personnamedetect(personName)) {
                    Toast.makeText(EditProfile.this, "Enter your valid name", Toast.LENGTH_SHORT).show();
                }
                else {
                    Map<String, Object> data = new HashMap<>();
                    data.put("Person_Name", personName);
                    data.put("Location", personLocation);
                    data.put("State", state);
                    data.put("Age", personAge);
                    data.put("Gender", gender);
                    data.put("Aadhar", aadharNumber);
                    data.put("Profile_uri", uri1);
                    FirebaseFirestore.getInstance().collection(usermail + "Person").document(uid).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage(profileimg);
            }
        });

    }

    public static boolean personnamedetect(String str) {
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
    private String getFileExtension (Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage() {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri!=null){
            StorageReference fileRef= FirebaseStorage.getInstance().getReference().child(usermail+"profile_img").child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pd.dismiss();
                            uri1=uri.toString();
                            profileimg.setImageURI(imageUri);
                            Toast.makeText(EditProfile.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}