package com.example.events3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

public class SelectBelongingToGroupDialogFragment extends DialogFragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.select_belonging_to_group_dialog_fragment, container, false);

        Button buttonAll = v.findViewById(R.id.id_button_selectBelongingToGroupDialogFragment_allGroups);
        buttonAll.setOnClickListener(this);
        Button buttonICanJoin = v.findViewById(R.id.id_button_selectBelongingToGroupDialogFragment_unknownGroups);
        buttonICanJoin.setOnClickListener(this);
        Button buttonIBelongTo = v.findViewById(R.id.id_button_selectBelongingToGroupDialogFragment_groupsIBelongTo);
        buttonIBelongTo.setOnClickListener(this);
        Button buttonICreated = v.findViewById(R.id.id_button_selectBelongingToGroupDialogFragment_iCreated);
        buttonICreated.setOnClickListener(this);

        if(AccountMainActivity.belongingGroupGroupsFragment.equals("all")) {
            buttonAll.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
            buttonICanJoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonIBelongTo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonICreated.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        if(AccountMainActivity.belongingGroupGroupsFragment.equals("unknown")) {
            buttonAll.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonICanJoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
            buttonIBelongTo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonICreated.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        else if(AccountMainActivity.belongingGroupGroupsFragment.equals("ibelongto")) {
            buttonAll.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonICanJoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonIBelongTo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
            buttonICreated.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        else if(AccountMainActivity.belongingGroupGroupsFragment.equals("icreated")) {
            buttonAll.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonICanJoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonIBelongTo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            buttonICreated.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case (R.id.id_button_selectBelongingToGroupDialogFragment_allGroups): {

                AccountMainActivity.belongingGroupGroupsFragment ="all";
                AccountMainActivity.fragmentSelected = new GroupsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, AccountMainActivity.fragmentSelected).commit();

                dismiss();
                break;
            }

            case (R.id.id_button_selectBelongingToGroupDialogFragment_unknownGroups): {

                AccountMainActivity.belongingGroupGroupsFragment ="i can join";
                AccountMainActivity.fragmentSelected = new GroupsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, AccountMainActivity.fragmentSelected).commit();

                dismiss();
                break;
            }

            case (R.id.id_button_selectBelongingToGroupDialogFragment_groupsIBelongTo): {

                AccountMainActivity.belongingGroupGroupsFragment ="ibelongto";
                AccountMainActivity.fragmentSelected = new GroupsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, AccountMainActivity.fragmentSelected).commit();

                dismiss();
                break;
            }

            case (R.id.id_button_selectBelongingToGroupDialogFragment_iCreated): {

                AccountMainActivity.belongingGroupGroupsFragment ="icreated";
                AccountMainActivity.fragmentSelected = new GroupsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, AccountMainActivity.fragmentSelected).commit();

                dismiss();
                break;
            }
        }
    }

}