package com.example.events3;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupsFragment extends Fragment implements  View.OnClickListener{

    public static List<Group> listGroupRV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listGroupRV = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(MoreInfoGroupActivity.buttonClicked){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, new GroupsFragment()).commit();
            MoreInfoGroupActivity.buttonClicked = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.groups_fragment, container, false);

        RecyclerView myRecycleView = (RecyclerView) v.findViewById(R.id.id_groupList_recycleView);
        RecycleViewAdapterGroups recycleAdapter = new RecycleViewAdapterGroups(getContext(), GroupsFragment.listGroupRV);
        myRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecycleView.setAdapter(recycleAdapter);

        Button buttonBelongingToGroup = v.findViewById(R.id.id_button_groupsFragment_pickBelongingToGroup);
        buttonBelongingToGroup.setOnClickListener(this);

        TextView textViewButtonLogout = v.findViewById(R.id.textViewButtonLogoutGroupsFragment);
        textViewButtonLogout.setOnClickListener(this);
        TextView textViewCreateGroup = v.findViewById(R.id.id_textViewCreateGroupGroupsFragment);
        textViewCreateGroup.setOnClickListener(this);

        ImageView imageViewAddGroupIcon = v.findViewById(R.id.id_imageViewAddGroupIconGroupsFragment);
        imageViewAddGroupIcon.setOnClickListener(this);

        //Inicializacja distanceToUser za każdym razem kiedy otwierany jest fragment "Groups"
        initializeDistanceToUser(AccountMainActivity.collectionGroupICreated);
        initializeDistanceToUser(AccountMainActivity.collectionGroupIBelong);
        initializeDistanceToUser(AccountMainActivity.collectionGroupICanJoin);

        switch (AccountMainActivity.belongingGroupGroupsFragment) {
            case("all"): {
                buttonBelongingToGroup.setText("all");

                listGroupRV.addAll(AccountMainActivity.collectionGroupICanJoin);
                listGroupRV.addAll(AccountMainActivity.collectionGroupIBelong);
                listGroupRV.addAll(AccountMainActivity.collectionGroupICreated);
                break;
            }

            case("i can join"): {
                buttonBelongingToGroup.setText("i can join");

                listGroupRV.addAll(AccountMainActivity.collectionGroupICanJoin);
                break;
            }

            case("ibelongto"): {
                buttonBelongingToGroup.setText("i belong to");

                listGroupRV.addAll(AccountMainActivity.collectionGroupIBelong);
                break;
            }

            case("icreated"): {
                buttonBelongingToGroup.setText("i created");

                listGroupRV.addAll(AccountMainActivity.collectionGroupICreated);
                break;
            }
        }
        sortGroupsByDistanceToUser(listGroupRV);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(AccountMainActivity.showDialogSuccessfulCreateGroup) {
            AccountMainActivity.fragmentSelected = new HomeFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, AccountMainActivity.fragmentSelected).commit();

            SuccessfulCreateGroupDialogFragment successfulCreateGroupDialogFragment = new SuccessfulCreateGroupDialogFragment();
            successfulCreateGroupDialogFragment.show(getActivity().getSupportFragmentManager(), "successful create group dialog fragment");

            AccountMainActivity.showDialogSuccessfulCreateGroup = false;
        }
    }

    public void openCreateGroupActivity() {
        Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case(R.id.id_button_groupsFragment_pickBelongingToGroup): {
                    SelectBelongingToGroupDialogFragment selectBelongingToGroupDialogFragment = new SelectBelongingToGroupDialogFragment();
                    selectBelongingToGroupDialogFragment.show(getActivity().getSupportFragmentManager(), "select belonging to group dialog fragment");
                    break;
                }

            case (R.id.textViewButtonLogoutGroupsFragment): {
                LogoutConfirmFragment logoutConfirmFragment = new LogoutConfirmFragment();
                logoutConfirmFragment.show(getActivity().getSupportFragmentManager(), "logout confirm dialog fragment");
                break;

            }

            case (R.id.id_textViewCreateGroupGroupsFragment):

            case (R.id.id_imageViewAddGroupIconGroupsFragment): {
                openCreateGroupActivity();
                break;
            }

        }
    }

    //Funkcja zwracająca dystans, który dzieli użytkownika dd grupy
    private float calculateDistanceToUser(Double userLatitude, Double userLongitude, String markerLatitude, String markerLongitude) {
        Location locationUser = new Location("");
        locationUser.setLatitude(userLatitude);
        locationUser.setLongitude(userLongitude);

        Location locationMarker = new Location("");
        locationMarker.setLatitude(Double.valueOf(markerLatitude));
        locationMarker.setLongitude(Double.valueOf(markerLongitude));

        return locationUser.distanceTo(locationMarker);
    }

    //Funkcja, która inicializuje dystans, który dzieli użytkownika od grupy dla każdego obiektu Group
    private void initializeDistanceToUser(List<Group> groupCollection) {
        for (Group group : groupCollection) {
            group.setDistanceToUser(calculateDistanceToUser(AccountMainActivity.userLocationLatitude, AccountMainActivity.userLocationLongitude, group.getLatitude(), group.getLongitude()));
        }
    }

    //Funckja sortująca rosnąco wydarzenia na podstawie dystansu pomiędzy grupą a użytkownikiem
    private void sortGroupsByDistanceToUser(List<Group> groupList) {
        Collections.sort(groupList, new Comparator<Group>() {
            @Override
            public int compare(Group o1, Group o2) {
                return Float.compare(o1.getDistanceToUser(), o2.getDistanceToUser());
            }
        });
    }
}
