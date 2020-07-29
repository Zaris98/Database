package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainPage extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ListView lv;
    FirebaseListAdapter adapter;
    FloatingActionButton addstudent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = findViewById(R.id.listview);
        firebaseAuth = FirebaseAuth.getInstance();
        addstudent = findViewById(R.id.Add);

        Query query = FirebaseDatabase.getInstance().getReference().child("Applicant");
            FirebaseListOptions<StudentList> options = new FirebaseListOptions.Builder<StudentList>()
            .setLayout(R.layout.cardview)
            .setQuery(query, StudentList.class)
            .build();
            adapter = new FirebaseListAdapter(options) {
                @Override
                protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                    TextView Name = v.findViewById(R.id.post_name);
                    TextView Role = v.findViewById(R.id.post_role);
                    TextView Email = v.findViewById(R.id.post_email);
                    TextView Address = v.findViewById(R.id.post_address);
                    TextView Number = v.findViewById(R.id.post_number);

                    StudentList std = (StudentList) model;
                    Name.setText(std.getName().toUpperCase());
                    Role.setText(std.getRole());
                    Email.setText(std.getEmail());
                    Address.setText(std.getAddress());
                    Number.setText(std.getPhone());
                }
            };
            lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent updateDelete = new Intent(MainPage.this, UpdateDelete.class);
                StudentList s = (StudentList) adapterView.getItemAtPosition(i);
                updateDelete.putExtra("Name", s.getName());
                updateDelete.putExtra("Role", s.getRole());
                updateDelete.putExtra("Email", s.getEmail());
                updateDelete.putExtra("Address", s.getAddress());
                updateDelete.putExtra("Number", s.getPhone());
                startActivity(updateDelete);
            }
        });

            addstudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainPage.this, add.class));
                }
            });

        }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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

        switch(item.getItemId()) {
            case R.id.Profile: {
                Profile();
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void Profile(){
        startActivity(new Intent(MainPage.this, Profile.class));
    }
    private void Refresh(){
        startActivity(new Intent(MainPage.this, MainPage.class));
    }
    public void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(MainPage.this, MainActivity.class));
    }

}
