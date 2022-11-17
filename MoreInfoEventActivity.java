package com.example.events3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MoreInfoEventActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private final DatabaseReference dbRefEvents = FirebaseDatabase.getInstance().getReference().child("Groups");
    public static boolean animateToMarker = false;
    public static boolean buttonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info_event);

        getSupportActionBar().hide();

        Button buttonRegister = findViewById(R.id.id_button_moreInfoEventActivity_register);
        Button buttonUnregister = findViewById(R.id.id_button_moreInfoEventActivity_unregister);
        Button buttonDeleteTheEvent = findViewById(R.id.id_button_moreInfoEventActivity_deleteTheEvent);
        Button buttonSeeOnMap = findViewById(R.id.id_button_moreInfoEventActivity_seeOnMap);
        TextView textViewLogout = findViewById(R.id.id_textview_moreInfoEventActivity_logout);

        buttonRegister.setOnClickListener(this);
        buttonUnregister.setOnClickListener(this);
        buttonDeleteTheEvent.setOnClickListener(this);
        buttonSeeOnMap.setOnClickListener(this);
        textViewLogout.setOnTouchListener(this);

        //Create View depends of due to event
        if(RecycleViewAdapterEvents.selectedEvent.getAdminKey().equals(MainActivity.emailKeyCurrentUser)){
            buttonRegister.setVisibility(View.INVISIBLE);
            buttonUnregister.setVisibility(View.INVISIBLE);
            buttonDeleteTheEvent.setVisibility(View.VISIBLE);
        }
        else if(RecycleViewAdapterEvents.selectedEvent.getUsersRegistered().containsKey(MainActivity.emailKeyCurrentUser)){
            buttonRegister.setVisibility(View.INVISIBLE);
            buttonDeleteTheEvent.setVisibility(View.INVISIBLE);
            buttonUnregister.setVisibility(View.VISIBLE);
        }
        else {
            buttonDeleteTheEvent.setVisibility(View.INVISIBLE);
            buttonUnregister.setVisibility(View.INVISIBLE);
            buttonRegister.setVisibility(View.VISIBLE);
        }

        TextView textViewTitleFB = findViewById(R.id.id_textview_moreInfoEventActivity_title_DB);
        TextView textViewDescriptionFB = findViewById(R.id.id_textview_moreInfoEventActivity_description_DB);
        TextView textViewGroupNameFB = findViewById(R.id.id_textview_moreInfoEventActivity_groupName_DB);
        TextView textViewNumberOfUsersRegisteredFB = findViewById(R.id.id_textview_moreInfoEventActivity_numberOfRegistered_FB);
        TextView textViewDateOfCreationDB = findViewById(R.id.id_textview_moreInfoEventActivity_creationDate_DB);
        TextView textViewStartDateFB = findViewById(R.id.id_textview_moreInfoEventActivity_startDate_DB);
        TextView textViewEndDateFB = findViewById(R.id.id_textview_moreInfoEventActivity_endDate_DB);
        TextView textViewPlaceAndAddressFB = findViewById(R.id.id_textview_moreInfoEventActivity_placeAndAddress_FB);
        TextView textViewAdminNameFB = findViewById(R.id.id_textview_moreInfoEventActivity_adminName_DB);
        TextView textViewTypeFB = findViewById(R.id.id_textViewTypeDB);

        textViewTitleFB.setText(RecycleViewAdapterEvents.selectedEvent.getTitle());
        textViewDescriptionFB.setText(RecycleViewAdapterEvents.selectedEvent.getDescription());
        textViewGroupNameFB.setText(RecycleViewAdapterEvents.selectedEvent.getGroupName());
        textViewNumberOfUsersRegisteredFB.setText(String.valueOf(RecycleViewAdapterEvents.selectedEvent.getUsersRegistered().size()));
        textViewDateOfCreationDB.setText(RecycleViewAdapterEvents.selectedEvent.getDateOfCreationDefaultTimezone());
        textViewAdminNameFB.setText(RecycleViewAdapterEvents.selectedEvent.getAdminName());
        textViewStartDateFB.setText(RecycleViewAdapterEvents.selectedEvent.getDateStartDefaultTimezone());
        textViewEndDateFB.setText(RecycleViewAdapterEvents.selectedEvent.getDateEndDefaultTimezone());
        textViewPlaceAndAddressFB.setText(RecycleViewAdapterEvents.selectedEvent.getPlace());
        textViewTypeFB.setText(RecycleViewAdapterEvents.selectedEvent.getType());


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AccountMainActivity.showDialogSuccessfulCreateEvent)
        {
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case(R.id.id_button_moreInfoEventActivity_register): {
                Map<String, String> updatedUsersRegisteredMap = new HashMap<>();
                updatedUsersRegisteredMap = RecycleViewAdapterEvents.selectedEvent.getUsersRegistered();
                updatedUsersRegisteredMap.put(MainActivity.emailKeyCurrentUser, "true");
                dbRefEvents.child(RecycleViewAdapterEvents.selectedEvent.getGroupKey()).child("Events").child(RecycleViewAdapterEvents.selectedEvent.getKey()).child("usersRegistered").setValue(updatedUsersRegisteredMap);
                AccountMainActivity.collectionEventRegistered.add(RecycleViewAdapterEvents.selectedEvent);
                AccountMainActivity.collectionEventUnregistered.removeIf(obj -> obj.getKey().equals(RecycleViewAdapterEvents.selectedEvent.getKey()));
                buttonClicked = true;
                finish();
                break;
            }

            case(R.id.id_button_moreInfoEventActivity_unregister): {
                Map<String, String> updatedUsersRegisteredMap = new HashMap<>();
                updatedUsersRegisteredMap = RecycleViewAdapterEvents.selectedEvent.getUsersRegistered();
                updatedUsersRegisteredMap.remove(MainActivity.emailKeyCurrentUser);
                dbRefEvents.child(RecycleViewAdapterEvents.selectedEvent.getGroupKey()).child("Events").child(RecycleViewAdapterEvents.selectedEvent.getKey()).child("usersRegistered").setValue(updatedUsersRegisteredMap);
                AccountMainActivity.collectionEventUnregistered.add(RecycleViewAdapterEvents.selectedEvent);
                AccountMainActivity.collectionEventRegistered.removeIf(obj -> obj.getKey().equals(RecycleViewAdapterEvents.selectedEvent.getKey()));
                buttonClicked = true;
                finish();

                break;
            }

            case(R.id.id_button_moreInfoEventActivity_deleteTheEvent): {

                dbRefEvents.child(RecycleViewAdapterEvents.selectedEvent.getGroupKey()).child("Events").child(RecycleViewAdapterEvents.selectedEvent.getKey()).removeValue();
                buttonClicked = true;
                finish();
                break;
            }

            case(R.id.id_button_moreInfoEventActivity_seeOnMap): {

                animateToMarker = true;
                openMapActivity();
                }
        }

    }

    public void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(v.getId()) {

            case(R.id.id_textview_moreInfoEventActivity_logout): {
                LogoutConfirmFragment logoutConfirmFragment = new LogoutConfirmFragment();
                logoutConfirmFragment.show(getSupportFragmentManager(), "logout confirm dialog fragment");
            }
        }
        return false;
    }

}