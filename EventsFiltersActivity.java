package com.example.events3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EventsFiltersActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private RadioButton radioButtonIncoming, radioButtonDateAdded, radioButtonDistance;
    private CheckBox checkBoxSport, checkBoxFood, checkBoxShopping, checkBoxWalking, checkBoxOther;

    static boolean isApplyFilters = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_filters);
        getSupportActionBar().hide();

        Button buttonApply = findViewById(R.id.id_button_activityEventsFilters_apply);
        buttonApply.setOnClickListener(this);
        TextView textViewLogout = findViewById(R.id.textViewButtonLogoutFiltersActivity);
        textViewLogout.setOnTouchListener(this);

        radioButtonIncoming = findViewById(R.id.id_radioButton_activityEventsFilters_incoming);
        radioButtonDateAdded = findViewById(R.id.id_radioButton_activityEventsFilters_dateAdded);
        radioButtonDistance = findViewById(R.id.id_radioButton_activityEventsFilters_distance);

        checkBoxSport = findViewById(R.id.id_checkBox_activityEventsFilters_sport);
        checkBoxFood = findViewById(R.id.id_checkBox_activityEventsFilters_food);
        checkBoxShopping = findViewById(R.id.id_checkBox_activityEventsFilters_shopping);
        checkBoxWalking = findViewById(R.id.id_checkBox_activityEventsFilters_walking);
        checkBoxOther = findViewById(R.id.id_checkBox_activityEventsFilters_other);

        radioButtonIncoming.setChecked(AccountMainActivity.filtersMap.get("Incoming"));
        radioButtonDateAdded.setChecked(AccountMainActivity.filtersMap.get("DateAdded"));
        radioButtonDistance.setChecked(AccountMainActivity.filtersMap.get("Distance"));
        checkBoxSport.setChecked(AccountMainActivity.filtersMap.get("Sport"));
        checkBoxFood.setChecked(AccountMainActivity.filtersMap.get("Food"));
        checkBoxShopping.setChecked(AccountMainActivity.filtersMap.get("Shopping"));
        checkBoxWalking.setChecked(AccountMainActivity.filtersMap.get("Walking"));
        checkBoxOther.setChecked(AccountMainActivity.filtersMap.get("Other"));

        checkBoxSport.setOnClickListener(this);
        checkBoxFood.setOnClickListener(this);
        checkBoxShopping.setOnClickListener(this);
        checkBoxWalking.setOnClickListener(this);
        checkBoxOther.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case (R.id.id_radioButton_activityEventsFilters_incoming): {
                radioButtonIncoming.setChecked(true);
                break;
            }
            case (R.id.id_radioButton_activityEventsFilters_dateAdded): {
                radioButtonDateAdded.setChecked(true);
                break;
            }
            case (R.id.id_radioButton_activityEventsFilters_distance): {
                radioButtonDistance.setChecked(true);
                break;
            }
            case (R.id.id_button_activityEventsFilters_apply): {
                if(!checkBoxSport.isChecked() && !checkBoxFood.isChecked() && !checkBoxShopping.isChecked() && !checkBoxWalking.isChecked() && !checkBoxOther.isChecked()){
                    AccountMainActivity.filtersMap.put("Incoming", radioButtonIncoming.isChecked());
                    AccountMainActivity.filtersMap.put("DateAdded", radioButtonDateAdded.isChecked());
                    AccountMainActivity.filtersMap.put("Distance", radioButtonDistance.isChecked());
                    AccountMainActivity.filtersMap.put("All", true);
                    AccountMainActivity.filtersMap.put("Sport", checkBoxSport.isChecked());
                    AccountMainActivity.filtersMap.put("Food", checkBoxFood.isChecked());
                    AccountMainActivity.filtersMap.put("Shopping", checkBoxShopping.isChecked());
                    AccountMainActivity.filtersMap.put("Walking", checkBoxWalking.isChecked());
                    AccountMainActivity.filtersMap.put("Other", checkBoxOther.isChecked());
                }
                else {
                    AccountMainActivity.filtersMap.put("Incoming", radioButtonIncoming.isChecked());
                    AccountMainActivity.filtersMap.put("DateAdded", radioButtonDateAdded.isChecked());
                    AccountMainActivity.filtersMap.put("Distance", radioButtonDistance.isChecked());
                    AccountMainActivity.filtersMap.put("All", false);
                    AccountMainActivity.filtersMap.put("Sport", checkBoxSport.isChecked());
                    AccountMainActivity.filtersMap.put("Food", checkBoxFood.isChecked());
                    AccountMainActivity.filtersMap.put("Shopping", checkBoxShopping.isChecked());
                    AccountMainActivity.filtersMap.put("Walking", checkBoxWalking.isChecked());
                    AccountMainActivity.filtersMap.put("Other", checkBoxOther.isChecked());
                }
                isApplyFilters = true;

                finish();
                break;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(v.getId()) {
            case(R.id.textViewButtonLogoutFiltersActivity): {
                LogoutConfirmFragment logoutConfirmFragment = new LogoutConfirmFragment();
                logoutConfirmFragment.show(getSupportFragmentManager(), "logout confirm dialog fragment");
            }
        }
        return false;
    }

}
