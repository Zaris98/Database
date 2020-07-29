package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rellay1, rellay2;
    EditText UEmail;
    EditText UPassword;
    Button UserLogin, UserReg, Forgottenpass;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    Handler handler = new Handler();
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        UserReg =  findViewById(R.id.btn2);
        UserLogin =  findViewById(R.id.Loginbtn);
        UEmail =  findViewById(R.id.etStudentEmail);
        UPassword =  findViewById(R.id.etPassword);
        Forgottenpass = findViewById(R.id.btn3);

        handler.postDelayed(runnable,2000);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){

            finish();
            startActivity(new Intent(MainActivity.this, MainPage.class));
        }

        Forgottenpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgottenPassword.class )
                );
            }
        });

        UserLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                validate(UEmail.getText().toString(), UPassword.getText().toString());
            }
        });

        UserReg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }

        });

        if(!isConnected()){
            new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Dialog_Alert))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Internet Connection Alert")
                .setMessage("Please turn on internet connection")
                .setPositiveButton("Close", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
        }
        else{
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        }

    }


    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public void validate(String userEmail, String userPassword){

        firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    checkEmailVerification();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        Boolean emailflag = firebaseUser.isEmailVerified();

        startActivity(new Intent(MainActivity.this, MainPage.class));
        /*if(emailflag){
            finish();

        }else{
            Toast.makeText(this,"Verify your email", Toast.LENGTH_SHORT);
            firebaseAuth.signOut();
        }*/
    }
}
