package com.example.events3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

public class SelectBelongingToEventDialogFragment extends DialogFragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.select_belonging_to_event_dialog_fragment, container, false);

        Button buttonAll = v.findViewById(R.id.id_button_selectBelongingToEventDialogFragment_all);
        buttonAll.setOnClickListener(this);
        Button buttonUnregistered = v.findViewById(R.id.id_button_selectBelongingToEventDialogFragment_unregistered);
        buttonUnregistered.setOnClickListener(this);
        Button buttonRegistered = v.findViewById(R.id.id_button_selectBelongingToEventDialogFragment_registered);
        buttonRegistered.setOnClickListener(this);
        Button buttonICreated = v.findViewById(R.id.id_button_selectBelongingToEventDialogFragment_iCreated);
        buttonICreated.setOnClickListener(this);

        if(AccountMainActivity.belongingEventEventsFragment.equals("all")) {
            buttonAll.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
            buttonUnregistered.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonRegistered.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonICreated.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        if(AccountMainActivity.belongingEventEventsFragment.equals("unregistered")) {
            buttonAll.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonUnregistered.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
            buttonRegistered.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonICreated.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        else if(AccountMainActivity.belongingEventEventsFragment.equals("registered")) {
            buttonAll.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonUnregistered.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonRegistered.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
            buttonICreated.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        else if(AccountMainActivity.belongingEventEventsFragment.equals("icreated")) {
            buttonAll.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonUnregistered.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonRegistered.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonICreated.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
        }
        return v;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case (R.id.id_button_selectBelongingToEventDialogFragment_all): {

                AccountMainActivity.belongingEventEventsFragment ="all";
                AccountMainActivity.fragmentSelected = new EventsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, AccountMainActivity.fragmentSelected).commit();
                dismiss();
                break;
            }
            case (R.id.id_button_selectBelongingToEventDialogFragment_unregistered): {

                AccountMainActivity.belongingEventEventsFragment ="unregistered";
                AccountMainActivity.fragmentSelected = new EventsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, AccountMainActivity.fragmentSelected).commit();
                dismiss();
                break;
            }

            case (R.id.id_button_selectBelongingToEventDialogFragment_registered): {

                AccountMainActivity.belongingEventEventsFragment ="registered";
                AccountMainActivity.fragmentSelected = new EventsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, AccountMainActivity.fragmentSelected).commit();
                dismiss();
                break;
            }

            case (R.id.id_button_selectBelongingToEventDialogFragment_iCreated): {

                AccountMainActivity.belongingEventEventsFragment ="icreated";
                AccountMainActivity.fragmentSelected = new EventsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, AccountMainActivity.fragmentSelected).commit();

                dismiss();
                break;
            }


        }

    }
}