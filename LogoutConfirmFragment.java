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

public class LogoutConfirmFragment extends DialogFragment implements View.OnClickListener{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.logout_confirm_fragment, container, false);

        Button buttonYesLogoutConfirm = v.findViewById(R.id.id_buttonYesLogoutConfirm);
        Button buttonNoLogoutConfirm = v.findViewById(R.id.id_buttonNoLogoutConfirm);

        buttonYesLogoutConfirm.setOnClickListener(this);
        buttonNoLogoutConfirm.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case (R.id.id_buttonYesLogoutConfirm): {

                MainActivity.fAuthMain.signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                dismiss();
                startActivity(intent);

                break;
            }

            case (R.id.id_buttonNoLogoutConfirm): {

                dismiss();break;
            }
        }
    }

}