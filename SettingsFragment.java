package com.example.events3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment implements View.OnTouchListener{

    View v;
    TextView textViewButtonLogout, textViewMoreInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.settings_fragment, container, false);

        textViewButtonLogout = v.findViewById(R.id.textViewButtonLogoutSettingsFragment);
        textViewMoreInfo = v.findViewById(R.id.id_textViewAboutAppSettingsFragment);
        textViewMoreInfo.setOnTouchListener(this);
        textViewButtonLogout.setOnTouchListener(this);

        return v;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(v.getId()) {

            case(R.id.textViewButtonLogoutSettingsFragment):
            {
                LogoutConfirmFragment logoutConfirmFragment = new LogoutConfirmFragment();
                logoutConfirmFragment.show(getActivity().getSupportFragmentManager(), "logout confirm dialog fragment");
                break;
            }
            case(R.id.id_textViewAboutAppSettingsFragment):{

                openMoreAppInfoActivity();
            }
        }

        return false;
    }

    public void openMoreAppInfoActivity() {
        Intent intent = new Intent(getActivity(), MoreAppInfoActivity.class);
        startActivity(intent);
    }
}
