package com.example.events3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SuccessfulCreateGroupDialogFragment extends DialogFragment implements View.OnClickListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.successful_create_group_dialog_fragment, container, false);

        Button buttonOkDialogFragment = v.findViewById(R.id.id_buttonOkSuccessfulCreateGroupDialogFragment);
        buttonOkDialogFragment.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case (R.id.id_buttonOkSuccessfulCreateGroupDialogFragment): {

                dismiss();
                break;
            }
        }
    }
}