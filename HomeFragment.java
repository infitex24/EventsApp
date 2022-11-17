package com.example.events3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment implements View.OnTouchListener{

    private TextView textViewCurrentDayOfWeek;
    private TextView textViewCurrentDate;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);

        textViewCurrentDate = v.findViewById(R.id.id_currentDate_HomeFragment);
        textViewCurrentDayOfWeek = v.findViewById(R.id.id_textView_CurrentDayOfWeek_HomeFragment);

        TextView textViewButtonLogout = v.findViewById(R.id.textViewButtonLogoutHomeFragment);
        textViewButtonLogout.setOnTouchListener(this);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        int dayOfWeek;
        String strDayOfWeek = "";
        String currentDateTime = simpleDateFormat.format(cal.getTime());
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case(1): {
                    strDayOfWeek = "Sunday";
                    break;
                }

            case(2): {
                strDayOfWeek = "Monday";
                break;
            }

            case(3): {
                strDayOfWeek = "Tuesday";
                break;
            }

            case(4): {
                strDayOfWeek = "Wednesday";
                break;
            }

            case(5): {
                strDayOfWeek = "Thursday";
                break;
            }

            case(6): {
                strDayOfWeek = "Friday";
                break;
            }
            case(7): {
                strDayOfWeek = "Saturday";
            }

        }

        textViewCurrentDate.setText(currentDateTime);
        textViewCurrentDayOfWeek.setText(strDayOfWeek);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(v.getId()) {
            case(R.id.textViewButtonLogoutHomeFragment): {
                    LogoutConfirmFragment logoutConfirmFragment = new LogoutConfirmFragment();
                    logoutConfirmFragment.show(getActivity().getSupportFragmentManager(), "logout confirm dialog fragment");
                }
        }
        return false;
    }
}
