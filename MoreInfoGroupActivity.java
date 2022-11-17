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

public class MoreInfoGroupActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    final DatabaseReference dbRefGroups = FirebaseDatabase.getInstance().getReference().child("Groups");

    public static boolean buttonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info_group);

        getSupportActionBar().hide();

        Button buttonJoinTheGroup = findViewById(R.id.id_button_moreInfoGroupActivity_joinTheGroup);
        Button buttonLeaveTheGroup = findViewById(R.id.id_button_moreInfoGroupActivity_leaveTheGroup);
        Button buttonDeleteTheGroup = findViewById(R.id.id_button_moreInfoGroupActivity_deleteTheGroup);
        Button buttonCreateEvent = findViewById(R.id.id_button_moreInfoGroupActivity_createEvent);
        TextView textViewLogout = findViewById(R.id.id_textview_moreInfoGroupActivity_logout);

        buttonJoinTheGroup.setOnClickListener(this);
        buttonLeaveTheGroup.setOnClickListener(this);
        buttonDeleteTheGroup.setOnClickListener(this);
        buttonCreateEvent.setOnClickListener(this);
        textViewLogout.setOnTouchListener(this);

        //Create View depends of due to group
        if(RecycleViewAdapterGroups.selectedGroup.getAdminKey().equals(MainActivity.emailKeyCurrentUser)){
            buttonJoinTheGroup.setVisibility(View.GONE);
            buttonLeaveTheGroup.setVisibility(View.GONE);
            buttonDeleteTheGroup.setVisibility(View.VISIBLE);
            buttonCreateEvent.setVisibility(View.VISIBLE);
        }
        else if(RecycleViewAdapterGroups.selectedGroup.getMemberList().containsKey(MainActivity.emailKeyCurrentUser)){
            buttonJoinTheGroup.setVisibility(View.GONE);
            buttonDeleteTheGroup.setVisibility(View.GONE);
            buttonLeaveTheGroup.setVisibility(View.VISIBLE);
            buttonCreateEvent.setVisibility(View.VISIBLE);
        }
        else{
            buttonDeleteTheGroup.setVisibility(View.GONE);
            buttonLeaveTheGroup.setVisibility(View.GONE);
            buttonJoinTheGroup.setVisibility(View.VISIBLE);
            buttonCreateEvent.setVisibility(View.GONE);
        }


        TextView textViewNameDB = findViewById(R.id.id_textview_moreInfoGroupActivity_name_DB);
        TextView textViewDescriptionDB = findViewById(R.id.id_textview_moreInfoGroupActivity_description_DB);
        TextView textViewNumberOfMembersDB = findViewById(R.id.id_textview_moreInfoGroupActivity_numberOfMembers_DB);
        TextView textViewDateOfCreationDB = findViewById(R.id.id_textview_moreInfoGroupActivity_creationDate_DB);
        TextView textViewAdminNameDB = findViewById(R.id.id_textview_moreInfoGroupActivity_adminName_DB);

        textViewNameDB.setText(RecycleViewAdapterGroups.selectedGroup.getName());
        textViewDescriptionDB.setText(RecycleViewAdapterGroups.selectedGroup.getDescription());
        textViewNumberOfMembersDB.setText(String.valueOf(RecycleViewAdapterGroups.selectedGroup.getMemberList().size()));
        textViewDateOfCreationDB.setText(RecycleViewAdapterGroups.selectedGroup.getDateOfCreationDefaultTimezone());
        textViewAdminNameDB.setText(RecycleViewAdapterGroups.selectedGroup.getAdminName());

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AccountMainActivity.showDialogSuccessfulCreateEvent) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case(R.id.id_button_moreInfoGroupActivity_joinTheGroup): {
                Map<String, String> updatedMemberListMap = new HashMap<>();
                updatedMemberListMap = RecycleViewAdapterGroups.selectedGroup.getMemberList();
                updatedMemberListMap.put(MainActivity.emailKeyCurrentUser, "true");
                dbRefGroups.child(RecycleViewAdapterGroups.selectedGroup.getKey()).child("memberList").setValue(updatedMemberListMap);
                AccountMainActivity.collectionGroupIBelong.add(RecycleViewAdapterGroups.selectedGroup);
                AccountMainActivity.collectionGroupICanJoin.removeIf(obj -> obj.getKey().equals(RecycleViewAdapterGroups.selectedGroup.getKey()));
                buttonClicked = true;
                finish();
                break;
            }

            case(R.id.id_button_moreInfoGroupActivity_leaveTheGroup): {
                Map<String, String> updatedMemberListMap = new HashMap<>();
                updatedMemberListMap = RecycleViewAdapterGroups.selectedGroup.getMemberList();
                updatedMemberListMap.remove(MainActivity.emailKeyCurrentUser);
                dbRefGroups.child(RecycleViewAdapterGroups.selectedGroup.getKey()).child("memberList").setValue(updatedMemberListMap);
                AccountMainActivity.collectionGroupICanJoin.add(RecycleViewAdapterGroups.selectedGroup);
                AccountMainActivity.collectionGroupIBelong.removeIf(obj -> obj.getKey().equals(RecycleViewAdapterGroups.selectedGroup.getKey()));
                buttonClicked = true;
                finish();

                break;
            }

            case(R.id.id_button_moreInfoGroupActivity_deleteTheGroup): {
                    dbRefGroups.child(RecycleViewAdapterGroups.selectedGroup.getKey()).removeValue();
                    buttonClicked = true;
                    finish();
                break;
            }

            case(R.id.id_button_moreInfoGroupActivity_createEvent): {
                    openCreateEventActivity();
                    break;
                }
        }

    }

    private void openCreateEventActivity() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(v.getId()) {

            case(R.id.id_textview_moreInfoGroupActivity_logout): {
                LogoutConfirmFragment logoutConfirmFragment = new LogoutConfirmFragment();
                logoutConfirmFragment.show(getSupportFragmentManager(), "logout confirm dialog fragment");
            }
        }

        return false;
    }
}