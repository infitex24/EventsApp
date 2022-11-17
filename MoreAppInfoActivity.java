package com.example.events3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MoreAppInfoActivity extends AppCompatActivity implements View.OnTouchListener {

    TextView textViewLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_app_info);
        getSupportActionBar().hide();

        textViewLogout = findViewById(R.id.textViewButtonLogoutMoreAppInfoActivity);
        textViewLogout.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(v.getId()) {
            case(R.id.textViewButtonLogoutMoreAppInfoActivity): {
                LogoutConfirmFragment logoutConfirmFragment = new LogoutConfirmFragment();
                logoutConfirmFragment.show(getSupportFragmentManager(), "logout confirm dialog fragment");
            }
        }
        return false;
    }
}