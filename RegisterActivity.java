package com.example.events3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.util.Hex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth fAuthRegister;

    //Pola tekstowe do wprowadzania danych przez użytkownika
    private EditText editTextNameRegister, editTextEmailRegister, editTextPasswordRegister, editTextPasswordConfirmRegister;

    private ProgressBar progressBarRegister;

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

         editTextPasswordRegister = findViewById(R.id.id_editTextPasswordRegister);
         editTextPasswordConfirmRegister = findViewById(R.id.id_editTextPasswordConfirmRegister);
         editTextNameRegister = findViewById(R.id.id_editTextNameRegister);
         editTextEmailRegister = findViewById(R.id.id_editTextEmailRegister);

         fAuthRegister = FirebaseAuth.getInstance();
         Button buttonRegister = findViewById(R.id.id_buttonRegisterRegister);
         buttonRegister.setOnClickListener(this);

         progressBarRegister = findViewById(R.id.id_progressBarRegister);
    }

     @Override
     public void onClick(View v) {
        switch (v.getId()) {
           case R.id.id_buttonRegisterRegister: {
                     Register();
           }
        }
     }

     //Funkcja sprawdzająca poprawność wprowadzonych danych i podejmująca próbę rejestracji użytkownika
     public void Register() {

        String password_fbRegister= editTextPasswordRegister.getText().toString().trim();
        String passwordConfirm_fbRegister= editTextPasswordConfirmRegister.getText().toString().trim();
        String fullNameRegister= editTextNameRegister.getText().toString().trim();
        String emailRegister= editTextEmailRegister.getText().toString().trim();

        if(fullNameRegister.isEmpty()) {
            editTextNameRegister.setError("Enter your fullname");
            editTextNameRegister.requestFocus();
            return;
        }
        if(emailRegister.isEmpty()) {
            editTextEmailRegister.setError("Enter your email");
            editTextEmailRegister.requestFocus();
            return;
        }
        if(password_fbRegister.isEmpty()) {
            editTextPasswordRegister.setError("Enter your password");
            editTextPasswordRegister.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailRegister).matches()) {
            editTextEmailRegister.setError("Email is valid");
            editTextEmailRegister.requestFocus();
            return;
        }
        if(!password_fbRegister.equals(passwordConfirm_fbRegister)) {
            editTextPasswordConfirmRegister.setError("Passwords are diffrent");
            editTextPasswordConfirmRegister.requestFocus();
            return;
         }

         progressBarRegister.setVisibility(View.VISIBLE);

         byte[] salt = createSalt();
         String hashPass;
         hashPass="admin";
         String str_salt = Hex.bytesToStringLowercase(salt);

         try {
            hashPass = MainActivity.generateHash(password_fbRegister, "MD5", salt);
         } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
         }

         fAuthRegister.createUserWithEmailAndPassword(emailRegister,hashPass).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

               if(task.isSuccessful()) {
                   String emailKey = emailRegister.replace(".", "");

                   // Utworzenie  obiektu  klasy  User.
                   User user = new User(fullNameRegister, str_salt);

                   // Utworzenie  obiektu  klasy  FirebaseDatabase.
                   DatabaseReference usersFirebaseDatabase;

                   // Odwolanie  do  instancji  bazy  danych i ustawienie odniesienia do wezla Users
                   usersFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");

                   //Zapis informacji do bazy poprzez przekazanie obiektu klasy User
                   //do podwezla ktorego kluczem jest adres e-mail
                   usersFirebaseDatabase.child(emailKey).setValue(user);


                   MainActivity.isSuccessfulCreateAccount = true;
                   progressBarRegister.setVisibility(View.GONE);
                   finish();
               }
               else{
                  progressBarRegister.setVisibility(View.GONE);

                  editTextEmailRegister.setError("Email is already used");
                  editTextEmailRegister.requestFocus();

                  return;
               }
            }
         });
     }

     //Stworzenie nowej soli
     public static byte[] createSalt() {
         byte[] bytes = new byte[20];
         SecureRandom random = new SecureRandom();
         random.nextBytes(bytes);
         return bytes;
     }

}