package com.example.events3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private EditText editTextTitleMarkerAppCreate, editTextDescriptionEventCreate, editTextPlaceEventCreate, editTextLimitUser;
    private Button buttonSetLocation, buttonDateStartPicker, buttonDateEndPicker, buttonTimeStartPicker, buttonTimeEndPicker;
    private TextView textViewGroupName;
    private Spinner spinner;

    private int tempTimeStartHour, tempTimeStartMinute, tempTimeEndHour, tempTimeEndMinute, tempDateStartYear, tempDateStartMonth, tempDateStartDay, tempDateEndYear, tempDateEndMonth, tempDateEndDay;
    private boolean isSelectedTimeStart, isSelectedDateStart;


    //Zmienne String zawierające dane o wydarzeniu
    public static String sstrTitle, sstrDesc, sstrTag, sstrPlace, sstrDateStartDefaultTimezone, sstrDateEndDefaultTimezone, sstrLimitUser;
    public static boolean isUnlimited;

    public static boolean isCreateEventSetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getSupportActionBar().hide();

        isUnlimited = false;
        isSelectedDateStart=true;
        isSelectedTimeStart=true;

        buttonSetLocation = findViewById(R.id.id_buttonSetLocationCreateMarker);
        buttonSetLocation.setOnClickListener(this);
        buttonDateStartPicker = findViewById(R.id.id_buttonDatePickerStartAddMarkerCreate);
        buttonDateStartPicker.setOnClickListener(this);
        buttonDateEndPicker = findViewById(R.id.id_buttonDatePickerEndAddMarkerCreate);
        buttonDateEndPicker.setOnClickListener(this);
        buttonTimeStartPicker = findViewById(R.id.id_buttonTimePickerStartAddMarkerCreate);
        buttonTimeStartPicker.setOnClickListener(this);
        buttonTimeEndPicker = findViewById(R.id.id_buttonTimePickerEndAddMarkerCreate);
        buttonTimeEndPicker.setOnClickListener(this);

        textViewGroupName = findViewById(R.id.id_textview_createEventActivity_groupNameDB);
        textViewGroupName.setText(RecycleViewAdapterGroups.selectedGroup.getName());

        editTextDescriptionEventCreate = findViewById(R.id.id_editTextDescCreateMarker);
        editTextPlaceEventCreate = findViewById(R.id.id_editTextPlaceCreateMarker);
        editTextTitleMarkerAppCreate = findViewById(R.id.id_editTextTitleCreateMarker);
        editTextLimitUser = findViewById(R.id.id_editTextLimitUsersAddMarkerCreate);
        spinner = findViewById(R.id.id_spinnerAddMarkerCreate);

        Switch switchUnlimited = findViewById(R.id.id_switchUnlimitedAddMarkerCreate);

        switchUnlimited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    editTextLimitUser.setEnabled(false);
                    editTextLimitUser.setInputType(InputType.TYPE_NULL);
                    isUnlimited=true;
                }
                else {
                    editTextLimitUser.setEnabled(true);
                    editTextLimitUser.setInputType(InputType.TYPE_CLASS_NUMBER);
                    isUnlimited=false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case(R.id.id_buttonDatePickerStartAddMarkerCreate): {
                isSelectedDateStart = true;
                DialogFragment dateStartPicker = new DateStartPickerFragment();
                dateStartPicker.show(getSupportFragmentManager(), "date start picker");

                break;
            }

            case(R.id.id_buttonDatePickerEndAddMarkerCreate): {
                isSelectedDateStart = false;
                DialogFragment dateEndPicker = new DateEndPickerFragment();
                dateEndPicker.show(getSupportFragmentManager(), "date end picker");

                break;
            }

            case(R.id.id_buttonTimePickerStartAddMarkerCreate): {
                isSelectedTimeStart = true;
                DialogFragment timeStartPicker = new TimeStartPickerFragment();
                timeStartPicker.show(getSupportFragmentManager(), "time start picker");

                break;
            }

            case(R.id.id_buttonTimePickerEndAddMarkerCreate): {
                isSelectedTimeStart = false;
                DialogFragment timeEndPicker = new TimeEndPickerFragment();
                timeEndPicker.show(getSupportFragmentManager(), "time end picker");

                break;
            }

            case (R.id.id_buttonSetLocationCreateMarker): {
                sstrTitle = editTextTitleMarkerAppCreate.getText().toString().trim();
                sstrDesc = editTextDescriptionEventCreate.getText().toString().trim();
                sstrPlace = editTextPlaceEventCreate.getText().toString().trim();
                sstrTag = spinner.getSelectedItem().toString().trim();

                if(isUnlimited) {
                    sstrLimitUser="unlimited";
                }
                else {
                    sstrLimitUser = editTextLimitUser.getText().toString().trim();
                }

                if(sstrTitle.isEmpty()) {
                    editTextTitleMarkerAppCreate.setError("Title cannot be empty!");
                    editTextTitleMarkerAppCreate.requestFocus();
                    return;
                }
                if(sstrTitle.length() < 3) {
                    editTextTitleMarkerAppCreate.setError("Title can not be shorter than 3 characters");
                    editTextTitleMarkerAppCreate.requestFocus();
                    return;
                }

                if(sstrTitle.length() > 35) {
                    editTextTitleMarkerAppCreate.setError("Title can not be longer than 35 characters");
                    editTextTitleMarkerAppCreate.requestFocus();
                    return;
                }
                if(sstrDesc.isEmpty()) {
                    editTextDescriptionEventCreate.setError("Description cannot be empty");
                    editTextDescriptionEventCreate.requestFocus();
                    return;
                }

                if(sstrDesc.length() < 10) {
                    editTextDescriptionEventCreate.setError("Description cannot shorter than 10 characters");
                    editTextDescriptionEventCreate.requestFocus();
                    return;
                }

                if(sstrDesc.length() > 128) {
                    editTextDescriptionEventCreate.setError("Description cannot be longer than 120 characters");
                    editTextDescriptionEventCreate.requestFocus();
                    return;
                }
                if(sstrLimitUser.isEmpty() && isUnlimited == false) {
                    editTextLimitUser.setError("Limit user cannot be empty");
                    editTextLimitUser.requestFocus();
                    return;
                }

                sstrDateStartDefaultTimezone = calculateDefaultTimeZoneDateToString(tempDateStartYear, tempDateStartMonth, tempDateStartDay, tempTimeStartHour, tempTimeStartMinute);
                sstrDateEndDefaultTimezone = calculateDefaultTimeZoneDateToString(tempDateEndYear, tempDateEndMonth, tempDateEndDay, tempTimeEndHour, tempTimeEndMinute);

                MapActivity.bool_AddMarkerCreateEvent =true;
                isCreateEventSetLocation = true;

                finish();
                openMapActivity();

                break;
            }
        }
    }

    //Funkcja, konwertująca datę do typu String w formacie domyślnej (Europe/London) strefy czasowej
    private String calculateDefaultTimeZoneDateToString(int dateYear, int dateMonth, int dateDay, int dateHour, int dateMinute){
        //Utworzenie obiektu klasy Calendar.
        Calendar calendar = Calendar.getInstance();
        //Ustawienie daty kalendarza podana przez uzytkownika
        calendar.set(Calendar.YEAR, dateYear);
        calendar.set(Calendar.MONTH, dateMonth);
        calendar.set(Calendar.DAY_OF_MONTH, dateDay);
        calendar.set(Calendar.HOUR_OF_DAY, dateHour);
        calendar.set(Calendar.MINUTE, dateMinute);
        calendar.set(Calendar.SECOND, 0);

        //Konwersja poprzez utworzenie obiektu klasy Date
        //i ustawienie tej samej daty co obiekt calendar.
        Date date = calendar.getTime();
        //Utworzenie obiektu klasy SimpleDateFormat
        //ktory pozwala na formatowanie daty do roznych stref czasowych
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //Ustawienie "domyślnej" strefy czasowej wykorzystujac obiekt klasy TimeZone
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        //Konwersja obiektu date przy uzyciu obiektu simpleDateFormat do String
        String strDate = simpleDateFormat.format(date);

        return strDate;
    }

    //Funkcja, która zapisuje godzinę, wprowadzoną przez użytkownika przy użyciu TimePicker
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(isSelectedTimeStart) {
            tempTimeStartHour = hourOfDay;
            tempTimeStartMinute = minute;

            if(minute<10) {
                String tempTimeStart = String.valueOf(hourOfDay) + ":0" + String.valueOf(minute);
                buttonTimeStartPicker.setText(tempTimeStart);
            }

            else {
                String tempTimeStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                buttonTimeStartPicker.setText(tempTimeStart);
                System.out.println(tempTimeStart);
            }

        }else {
            tempTimeEndHour = hourOfDay;
            tempTimeEndMinute = minute;

            if(minute<10) {
                String tempTimeEnd = String.valueOf(hourOfDay) + ":0" + String.valueOf(minute);
                buttonTimeEndPicker.setText(tempTimeEnd);
            }
            else {
                String tempTimeEnd = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                buttonTimeEndPicker.setText(tempTimeEnd);
            }
        }
    }

    //Funkcja, która zapisuje datę, wprowadzoną przez użytkownika
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(isSelectedDateStart){
            tempDateStartYear = year;
            tempDateStartMonth = month;
            tempDateStartDay = dayOfMonth;
            String tempDateStart = String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
            buttonDateStartPicker.setText(tempDateStart);
        }
        else{
            tempDateEndYear = year;
            tempDateEndMonth = month;
            tempDateEndDay = dayOfMonth;
            String tempDateEnd = String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
            buttonDateEndPicker.setText(tempDateEnd);
        }
    }

    public void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}