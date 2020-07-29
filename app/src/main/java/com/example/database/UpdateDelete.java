package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class UpdateDelete extends AppCompatActivity {

    EditText   email, role, address, number;
    Button updata;
    TextView name;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.tvName);
        updata = findViewById(R.id.btnUpdate);
        email = findViewById(R.id.UpEmail);
        role = findViewById(R.id.UpRole);
        address = findViewById(R.id.Upaddress);
        number = findViewById(R.id.Upnumber);
        firebaseAuth = FirebaseAuth.getInstance();

        String key = getIntent().getExtras().get("Name").toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Applicant").child(key);

        name.setText(getIntent().getStringExtra("Name"));
        email.setText(getIntent().getStringExtra("Email"));
        role.setText(getIntent().getStringExtra("Role"));
        address.setText(getIntent().getStringExtra("Address"));
        number.setText(getIntent().getStringExtra("Number"));
    }

    public void btnUpdate_click(View view){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("Role").setValue(role.getText().toString());
                dataSnapshot.getRef().child("Email").setValue(email.getText().toString());
                dataSnapshot.getRef().child("Address").setValue(address.getText().toString());
                dataSnapshot.getRef().child("Phone").setValue(number.getText().toString());
                Toast.makeText(UpdateDelete.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateDelete.this, MainPage.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void btnDelete_click(View view){
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UpdateDelete.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateDelete.this, MainPage.class));
                }
            }
        });
    }
}
