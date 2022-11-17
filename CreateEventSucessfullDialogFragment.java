package com.example.events3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

public class CreateEventSucessfullDialogFragment extends DialogFragment implements View.OnClickListener {

    private View v;
    private Button buttonOK;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        v = inflater.inflate(R.layout.create_event_successful_dialog_fragment,container,false);

        buttonOK = v.findViewById(R.id.id_buttonOKCreateEventSuccessful);
        buttonOK.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case(R.id.id_buttonOKCreateEventSuccessful): {
                dismiss();
                break;
            }
        }
    }

}
