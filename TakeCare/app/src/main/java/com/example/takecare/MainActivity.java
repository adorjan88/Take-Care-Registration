package com.example.takecare;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import Classes.FallDetector;

public class MainActivity extends AppCompatActivity {

    private FallDetector sensorHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FallDetector fallDetector = new FallDetector(this);
        //fallDetector.start();


        EditText email = findViewById(R.id.inputUsername);
        EditText password = findViewById(R.id.inputPassword);
        ImageButton imageButton = findViewById(R.id.imageButton2);

        TextView createAccount = findViewById(R.id.createAccount);

        createAccount.setPaintFlags(createAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("userLogin",Context.MODE_PRIVATE);

        String usernameCheck = sharedPreferences.getString("Email", "");
        String passwordCheck = sharedPreferences.getString("Password", "");



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                 sharedPreferences.edit().remove("Email").apply();
                 sharedPreferences.edit().remove("Password").apply();
                 */


                if (!usernameCheck.isEmpty() && !passwordCheck.isEmpty()) {

                    Toast.makeText(MainActivity.this, "Saved preferences", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("Email", email.getText().toString());
                                        editor.putString("Password", password.getText().toString());
                                        editor.apply();

                                        Toast.makeText(v.getContext(), "Successful login!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        try {

                                            throw Objects.requireNonNull(task.getException());

                                        } catch (FirebaseAuthInvalidCredentialsException firebaseAuthInvalidCredentialsException) {

                                            Toast.makeText(v.getContext(), "Invalid login credentials!", Toast.LENGTH_SHORT).show();

                                        } catch (FirebaseNetworkException firebaseNetworkException) {

                                            Toast.makeText(v.getContext(), "You are offline!", Toast.LENGTH_SHORT).show();

                                        } catch (Throwable t) {

                                            Toast.makeText(v.getContext(), "Unknown exception!", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                });
                    }
                    catch (IllegalArgumentException illegalArgumentException){

                        Toast.makeText(v.getContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });





    }



}