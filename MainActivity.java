package com.example.events3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmailMain, editTextPasswordMain;
    private Button buttonLoginMain;
    private ProgressBar progressBarMain;
    private final DatabaseReference dbRefLog = FirebaseDatabase.getInstance().getReference().child("Users");
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    //Klucz (adres e-mail) użytkownika.
    public static String emailKeyCurrentUser;

    public static boolean isSuccessfulCreateAccount = false;
    public static FirebaseAuth fAuthMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        fAuthMain = FirebaseAuth.getInstance();

        Button buttonSignUpMain = findViewById(R.id.id_buttonSignUpMain);
        buttonLoginMain = findViewById(R.id.id_buttonLoginMain);
        editTextPasswordMain = findViewById(R.id.id_editTextPasswordMain);
        editTextEmailMain = findViewById(R.id.id_editTextEmailMain);
        progressBarMain = findViewById(R.id.id_progressBarMain);

        buttonSignUpMain.setOnClickListener(this);
        buttonLoginMain.setOnClickListener(this);

        askForLocationPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        buttonLoginMain.setEnabled(true);

        if (isSuccessfulCreateAccount) {
            SuccessfulCreateAccountDialogFragment successfulCreateAccountDialogFragment = new SuccessfulCreateAccountDialogFragment();
            successfulCreateAccountDialogFragment.show(getSupportFragmentManager(), "successful create account dialog fragment");
            isSuccessfulCreateAccount = false;
        }
    }


    //Funkcja sprawdzająca poprawność wprowadzonych danych i podejmująca próbę zalogowania się do aplikacji
    public void Login() {
        buttonLoginMain.setEnabled(false);

        String password_fbMain = editTextPasswordMain.getText().toString().trim();
        String email_fbMain = editTextEmailMain.getText().toString().trim();
        String emailKey = email_fbMain.replace(".", "");

        if (email_fbMain.isEmpty()) {
            editTextEmailMain.setError("Enter your Email");
            editTextEmailMain.requestFocus();
            buttonLoginMain.setEnabled(true);
            return;
        }

        if (password_fbMain.isEmpty()) {
            editTextPasswordMain.setError("Enter your Password");
            editTextPasswordMain.requestFocus();
            buttonLoginMain.setEnabled(true);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_fbMain).matches()) {
            editTextEmailMain.setError("Email is valid");
            editTextEmailMain.requestFocus();
            buttonLoginMain.setEnabled(true);
            return;

        }
        progressBarMain.setVisibility(View.VISIBLE);

        //Sprawdzenie czy w bazie danych istnieje e-mail (klucz) wpisany przez użytkownika
        dbRefLog.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(emailKey)) {
                    dbRefLog.child(emailKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            String strsalt = snapshot.child("salt").getValue().toString();
                            byte[] salt = Hex.stringToBytes(strsalt);

                            String hashPass = "admin";
                            try {

                                hashPass = generateHash(password_fbMain, "MD5", salt);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }

                            fAuthMain.signInWithEmailAndPassword(email_fbMain, hashPass)
                                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                MainActivity.emailKeyCurrentUser = emailKey;
                                                progressBarMain.setVisibility(View.GONE);
                                                openAccountMainActivity();
                                            } else {
                                                Toast.makeText(MainActivity.this, "Incorrect email or password", Toast.LENGTH_LONG).show();
                                                buttonLoginMain.setEnabled(true);
                                                progressBarMain.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {
                    Toast.makeText(MainActivity.this, "Incorrect email or password", Toast.LENGTH_LONG).show();
                    buttonLoginMain.setEnabled(true);
                    progressBarMain.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_buttonLoginMain: {
                closeKeyboard();
                Login();
                break;
            }
            case R.id.id_buttonSignUpMain: {
                openRegisterActivity();
                break;
            }
        }
    }

    //Funkcja generujaca ciag znakow (hash) z polaczenia hasla uzytkownika i soli
    public static String generateHash(String data, String algorithm, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(data.getBytes());
        return bytesToStringHex(hash);
    }

    //Funkcja konwertująca byte do String
    public static String bytesToStringHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //Funkcja zamykająca klawiature ekranową
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Funkcja, która wyświetla zapytanie do użytkownika o pozwolenie na dostęp do lokalizacji
    private void askForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    //Funkcja otwierajaca ekran rejestracji konta
    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //Funkcja otwierająca główny interfejs użytkownika po zalogowaniu
    private void openAccountMainActivity() {
        Intent intent = new Intent(this, AccountMainActivity.class);
        startActivity(intent);
    }

}