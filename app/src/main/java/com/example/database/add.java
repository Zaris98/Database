package com.example.database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add extends AppCompatActivity {

    EditText name, studentemail, phonenumber, postaladdress, stuteach;
    Button addstudent;
    //Variable created for the Authentication
    FirebaseAuth firebaseAuth;
    //Variable created for the Database
    FirebaseDatabase firebaseDatabase;
    String studentname, email, phone, address, studentorteacher;
    DatabaseReference databaseReference;
    //Variables needed for notification
    private static final String TAG = "Add_Event";
    private static final String CHANNEL_ID = "Zaris1";
    private static final String CHANNEL_NAME = "Zaris";
    private static final String CHANNEL_DESC = "Zaris Notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.Name);
        studentemail = findViewById(R.id.etStudentEmail);
        phonenumber = findViewById(R.id.etPhone);
        postaladdress = findViewById(R.id.etPostal);
        stuteach = findViewById(R.id.StudentorTeacher);
        addstudent = findViewById(R.id.Add);
        //Gets instance from the FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        //Gets instance from the FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Gets Reference from the FirebaseDatabase
        databaseReference = firebaseDatabase.getReference();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        //Button which saves data onto the database
        addstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add.this, MainPage.class));
//                studentname = name.getText().toString();
//                email = studentemail.getText().toString();
//                phone = phonenumber.getText().toString();
//                address = postaladdress.getText().toString();
//                studentorteacher = stuteach.getText().toString();
//                if(studentname.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || studentorteacher.isEmpty()) {
//                    Toast.makeText(add.this, "Please Enter All The Details", Toast.LENGTH_SHORT).show();
//                }

                addNotification();
                addevent();
            }
        });


    }

    private void addevent() {
        studentname = name.getText().toString();
        email = studentemail.getText().toString();
        phone = phonenumber.getText().toString();
        address = postaladdress.getText().toString();
        studentorteacher = stuteach.getText().toString();

        if (studentname.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || studentorteacher.isEmpty()) {
            Toast.makeText(this, "Please Enter All The Details", Toast.LENGTH_LONG).show();
        } else {
            //Gets the current users ID from the database
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                //Targets the firebase database and creates children classess that store values
                databaseReference.child("Applicant").child(studentname).setValue(studentname);
                databaseReference.child("Applicant").child(studentname).child("Name").setValue(studentname);
                databaseReference.child("Applicant").child(studentname).child("Role").setValue(studentorteacher);
                databaseReference.child("Applicant").child(studentname).child("Email").setValue(email);
                databaseReference.child("Applicant").child(studentname).child("Phone").setValue(phone);
                databaseReference.child("Applicant").child(studentname).child("Address").setValue(address);
                Toast.makeText(this, "Data Added", Toast.LENGTH_LONG).show();
                startActivity(new Intent(add.this, MainPage.class));
            }
        }
    }

    private void addNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Database")
            .setContentText("Applicant Added")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, builder.build());


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
        startActivity(new Intent(add.this, add.class));
    }
    public void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(add.this, MainActivity.class));
    }
}
