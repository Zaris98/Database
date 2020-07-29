package com.example.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE =1;
    private int IMAGE_CAPTURE_CODE = 2;
    private EditText Email,Password, Username, Age;
    private Spinner  Gender;
    private Button regButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ImageView UserProfilePic;
    private String Name;
    private String UserEmail;
    private String UserAge;
    private String UserGender;
    private static int PICK_IMAGE = 123;
    private static int TAKE_IMAGE = 0;
    Uri imagePath;
    private StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Pick image from local storage
        assert data != null;
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null ){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                UserProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Email = findViewById(R.id.Email);
        Age = findViewById(R.id.ageupdate);
        Gender = findViewById(R.id.genderupdate);
        Password = findViewById(R.id.etUserPass);
        regButton = findViewById(R.id.btnUpdate);

        Username = findViewById(R.id.etUnameUpdate);
        UserProfilePic = findViewById(R.id.imPP2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //This is a list for the gender selection category
        List<String> categories = new ArrayList<>();
        categories.add(0, "Select Gender");
        categories.add("Male");
        categories.add("Female");
        categories.add("Transgender");

        //Style and Populate spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        //Dropdown Layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        Gender.setAdapter(dataAdapter);

        Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Gender Options")) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO Auto-generated
            }
        });

        UserProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    intent.setType("image/*"); //application/* audio/*
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select image"), PICK_IMAGE);

                }else{
                    requestStoragePermissions();
                }

            }
        });


        //When the register button is pressed
        regButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Checks if the email and password matches
                if(validate()){
                    String User_Email = Email.getText().toString().trim();
                    String User_Password = Password.getText().toString().trim();

                    //Creates the users Email and Password
                    firebaseAuth.createUserWithEmailAndPassword(User_Email,User_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if(task.isSuccessful()){
                                sendEmailVerification();
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "Regeistration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private Boolean validate(){
        boolean result = false;
        Name = Username.getText().toString();
        UserEmail = Email.getText().toString();
        String userpassword = Password.getText().toString();
        UserGender = Gender.getSelectedItem().toString();
        UserAge = Age.getText().toString();

        //If the fields are empty
        if(Name.isEmpty() || UserEmail.isEmpty() || userpassword.isEmpty()){
            Toast.makeText(this, "Please Enter All The Details", Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return result;
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegistrationActivity.this, "Successfully Registered, Verification Email has been sent!", Toast.LENGTH_SHORT).show();
                        //Signs the user out of the app
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(RegistrationActivity.this,"Verification Email hasn't been sent!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //This is used to ask the user permissions when you click the image icon
    private void requestStoragePermissions(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Dialog_Alert))
                .setTitle("Permission needed")
                .setMessage("Permission needed for Storage")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(RegistrationActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Used to send the data onto the database
    private void sendUserData(){
        FirebaseDatabase firebasedatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebasedatabase.getReference(firebaseAuth.getUid());
        if (imagePath != null) {
            StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic"); // User Id/Images/profile_pic
            UploadTask uploadTask = imageReference.putFile(imagePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegistrationActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(RegistrationActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            UserProfile UP = new UserProfile(UserEmail, Name, UserAge, UserGender);
            myRef.setValue(UP);
        }else{
            UserProfile UP = new UserProfile(UserEmail, Name, UserAge, UserGender);
            myRef.setValue(UP);
        }
    }


}
