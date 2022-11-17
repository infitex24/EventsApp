package com.example.events3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private EditText editTextNameGroup, editTextDescGroup;
    private Button buttonCreateGroup;
    private TextView textViewLogout;

    public static String sstrGroupName, sstrGroupDesc;

    static boolean isCreateGroupSetLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getSupportActionBar().hide();

        editTextNameGroup = findViewById(R.id.id_editTextNameGroupCreate);
        editTextDescGroup = findViewById(R.id.id_editTextDescGroupCreate);
        buttonCreateGroup = findViewById(R.id.id_buttonNextCreateGroup);
        textViewLogout = findViewById(R.id.textViewButtonLogoutCreateGroupActivity);
        buttonCreateGroup.setOnClickListener(this);
        textViewLogout.setOnTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AccountMainActivity.showDialogSuccessfulCreateGroup) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case (R.id.id_buttonNextCreateGroup): {
                sstrGroupName = editTextNameGroup.getText().toString().trim();
                sstrGroupDesc = editTextDescGroup.getText().toString().trim();

                isCreateGroupSetLocation = true;
                MapActivity.bool_AddMarkerCreateGroup = true;
                openMapActivity();
                break;
            }
        }
    }

    private  void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(v.getId()) {
            case(R.id.textViewButtonLogoutCreateGroupActivity): {
                LogoutConfirmFragment logoutConfirmFragment = new LogoutConfirmFragment();
                logoutConfirmFragment.show(getSupportFragmentManager(), "logout confirm dialog fragment");
            }
        }
        return false;

    }
}