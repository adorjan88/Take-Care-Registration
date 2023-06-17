package com.example.takecare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.regex.Pattern;

import Classes.User;
import Exceptions.RegexPatternException;

public class RegistrationActivity extends AppCompatActivity {

    public void storeUser(FirebaseFirestore db, User user){
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "User added to Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding user to Firestore", e);
                    }
                });
    }

    private static final Pattern passwordRegex = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9]).{8,16}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        EditText emailAddress = findViewById(R.id.email);
        EditText password = findViewById(R.id.editTextTextPassword);
        ImageButton sign_up = findViewById(R.id.imageButton2);

        RadioButton setPatient = findViewById(R.id.patientButton);
        RadioButton setCaregiver = findViewById(R.id.caregiverButton);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        //user must be either patient or caregiver
        setPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPatient.setChecked(true);
                setCaregiver.setChecked(false);
            }
        });
        setCaregiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCaregiver.setChecked(true);
                setPatient.setChecked(false);
            }
        });


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseAuth firebaseUser = FirebaseAuth.getInstance();
                    firebaseUser.createUserWithEmailAndPassword(emailAddress.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser1 = firebaseUser.getCurrentUser();
                                    if (firebaseUser1 != null){
                                        firebaseUser1.sendEmailVerification();
                                        if(setCaregiver.isChecked()){
                                            User user = new User(emailAddress.getText().toString(),"Caregiver");
                                            storeUser(db, user);
                                        }
                                        else{
                                            User user = new User(emailAddress.getText().toString(),"Patient");
                                            storeUser(db, user);
                                        }
                                    }

                                    Toast.makeText(v.getContext(), "Registration was successful!", Toast.LENGTH_SHORT).show();
                                } else { //exception handling
                                    try {
                                        if (!Pattern.compile(passwordRegex.toString()).matcher(password.getText().toString()).matches()) {
                                            throw new RegexPatternException("");
                                        }
                                    } catch (RegexPatternException regexPatternException) {
                                        Toast.makeText(v.getContext(), "Your password doesn't match the requirements!", Toast.LENGTH_SHORT).show();
                                    } catch (Throwable t) {
                                        Toast.makeText(RegistrationActivity.this, "Unknown exception!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }catch (IllegalArgumentException illegalArgumentException){
                    Toast.makeText(RegistrationActivity.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                }catch (Throwable t){
                    Toast.makeText(RegistrationActivity.this, "Unknown exception!", Toast.LENGTH_SHORT).show();
                }
            }

        });





    }
}
