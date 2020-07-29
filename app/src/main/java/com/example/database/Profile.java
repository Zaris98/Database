package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    ImageView ProfilePic;
    TextView ProfileName, ProfileEmail, ProfileAge, ProfileGender;
    Button edit;
    FirebaseAuth firebaseauth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");
        ProfilePic = findViewById(R.id.idPP);
        ProfileName = findViewById(R.id.tvProfileName);
        ProfileEmail = findViewById(R.id.tvProfileEmail);
        ProfileAge = findViewById(R.id.tvAgeProfile);
        ProfileGender = findViewById(R.id.tvGenderProfile);
        edit = findViewById(R.id.btnedit);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, UpdateProfile.class));
            }
        });

        firebaseauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseauth.getUid());

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseauth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ProfilePic);
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                //Gets the data from the database and uses it to set the text
                ProfileName.setText("Name: " + userProfile.getName());
                ProfileEmail.setText("E-Mail: " + userProfile.getEmail());
                ProfileAge.setText("Age: " + userProfile.getAge());
                ProfileGender.setText("Gender: " + userProfile.getGender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logoutmenu: {
                Logout();
            }
        }

        switch(item.getItemId()) {
            case R.id.Refreshmenu: {
                Refresh();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void Refresh(){
        startActivity(new Intent(Profile.this, Profile.class));
    }
    public void Logout(){
        firebaseauth.signOut();
        finish();
        startActivity(new Intent(Profile.this, MainActivity.class));
    }

}
