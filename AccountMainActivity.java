package com.example.events3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountMainActivity extends AppCompatActivity implements View.OnClickListener {


    private int LOCATION_REQUEST_CODE = 10001;
    private final DatabaseReference dbRefGroups = FirebaseDatabase.getInstance().getReference().child("Groups");
    private long backPressedTime;
    private Toast backToast;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public static Fragment fragmentSelected = null;

    //Pelne imię aktualnie zalogowanego użytkownika
    public static String fullNameCurrentUser;
    //Współrzędne geografizne aktualnie zalogowanego użytkownika
    public static Double userLocationLatitude, userLocationLongitude;

    public static String belongingGroupGroupsFragment;
    public static String belongingEventEventsFragment;
    public static Map<String, Boolean> filtersMap;

    public static boolean showDialogSuccessfulCreateEvent = false;
    public static boolean showDialogSuccessfulCreateGroup = false;

    //Kolekcje przechowujące wydarzenia i grupy
    public static List<Event> collectionEventUnregistered, collectionEventRegistered, collectionEventICreated;
    public static List<Group> collectionGroupICanJoin, collectionGroupIBelong, collectionGroupICreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_main);
        getSupportActionBar().hide();

        MainActivity.fAuthMain = FirebaseAuth.getInstance();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        FloatingActionButton floatingActionButtonMapAccount = findViewById(R.id.id_floating_action_button_map);
        floatingActionButtonMapAccount.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.id_bottom_navigation_view);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListiner);

        getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, new HomeFragment()).commit();

        collectionEventUnregistered = new ArrayList<>();
        collectionEventRegistered = new ArrayList<>();
        collectionEventICreated = new ArrayList<>();
        collectionGroupICanJoin = new ArrayList<>();
        collectionGroupIBelong = new ArrayList<>();
        collectionGroupICreated = new ArrayList<>();

        belongingGroupGroupsFragment = "all";
        belongingEventEventsFragment = "all";

        filtersMap = new HashMap<>();
        filtersMap.put("Incoming", true);
        filtersMap.put("DateAdded", false);
        filtersMap.put("Distance", false);
        filtersMap.put("All", true);
        filtersMap.put("Sport", false);
        filtersMap.put("Food", false);
        filtersMap.put("Shopping", false);
        filtersMap.put("Walking", false);
        filtersMap.put("Other", false);

        readOnceCurrentUserFirebase();
        GroupsAndEventsListinerFirebase();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        getLastKnownLocation();

        if (AccountMainActivity.showDialogSuccessfulCreateEvent) {
            CreateEventSucessfullDialogFragment createEventSucessfullDialogFragment = new CreateEventSucessfullDialogFragment();
            createEventSucessfullDialogFragment.show(getSupportFragmentManager(), "create event successful dialog fragment");
            AccountMainActivity.showDialogSuccessfulCreateEvent = false;
        }
    }


    //Przełączanie między fragmentami w ekranie głównym korzystając z dolnego paska nawigacji po aplikacji
    private BottomNavigationView.OnNavigationItemSelectedListener navListiner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case (R.id.id_bottom_navigation_home_item): {
                    fragmentSelected = new HomeFragment();
                    break;
                }

                case (R.id.id_bottom_navigation_events_item): {
                    fragmentSelected = new EventsFragment();
                    break;
                }

                case (R.id.id_bottom_navigation_groups_item): {
                    fragmentSelected = new GroupsFragment();
                    break;
                }

                case (R.id.id_bottom_navigation_settings_item): {
                    fragmentSelected = new SettingsFragment();
                    break;
                }
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, fragmentSelected).commit();

            return true;
        }
    };

    //Nadpisanie funkcji, która wykonuje się po wciśnięciu przycisku powrotu, która ma za zadanie zminimalizować aplikację jeśli użytkownik próbuje cofnąć się do ekranu logowania
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            minimizeApp();
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    //Funkcja uruchamiana przy naciśnięciu dowolnego przycisku.
    @Override
    public void onClick(View v) {
        //Switch ustala, który przycisk został wciśnięty
        switch (v.getId()) {
            case (R.id.id_floating_action_button_map): {
                openMapActivity();
                break;
            }
        }
    }

    //Funkcja, która ma za zadanie pobierać informację z bazy danych dzięki nasłuchiwaniu biężacych zmian w bazie danych
    private void GroupsAndEventsListinerFirebase() {
        dbRefGroups.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {

                Group group = snapshot.getValue(Group.class);
                group.setKey(snapshot.getKey());

                if (group.getMemberList().containsKey(MainActivity.emailKeyCurrentUser)) {

                    for (DataSnapshot eventSnapshot : snapshot.child("Events").getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        event.setKey(eventSnapshot.getKey());

                        if (event.getAdminKey().equals(MainActivity.emailKeyCurrentUser)) {
                            if (!containsEvent(collectionEventICreated, event.getKey())) {
                                collectionEventICreated.add(event);
                            }
                        } else if (event.getUsersRegistered().containsKey(MainActivity.emailKeyCurrentUser)) {
                            if (!containsEvent(collectionEventRegistered, event.getKey())) {
                                collectionEventRegistered.add(event);
                            }
                        } else {
                            if (!containsEvent(collectionEventUnregistered, event.getKey())) {
                                collectionEventUnregistered.add(event);
                            }
                        }
                    }
                }

                if (group.getAdminKey().equals(MainActivity.emailKeyCurrentUser)) {
                    if (!containsGroup(collectionGroupICreated, group.getKey())) {
                        collectionGroupICreated.add(group);
                    }
                } else if (group.getMemberList().containsKey(MainActivity.emailKeyCurrentUser)) {
                    if (!containsGroup(collectionGroupIBelong, group.getKey())) {
                        collectionGroupIBelong.add(group);
                    }
                } else {
                    if (!containsGroup(collectionGroupICanJoin, group.getKey())) {
                        collectionGroupICanJoin.add(group);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                Map<String, String> updatedMemberList;
                updatedMemberList = (Map) snapshot.child("memberList").getValue();

                updateGroupMemberList(AccountMainActivity.collectionGroupICanJoin, snapshot.getKey(), updatedMemberList);
                updateGroupMemberList(AccountMainActivity.collectionGroupIBelong, snapshot.getKey(), updatedMemberList);
                updateGroupMemberList(AccountMainActivity.collectionGroupICreated, snapshot.getKey(), updatedMemberList);

                dbRefGroups.child(snapshot.getKey()).child("Events").addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                        Event event = snapshot.getValue(Event.class);
                        event.setKey(snapshot.getKey());

                        if (event.getAdminKey().equals(MainActivity.emailKeyCurrentUser)) {
                            if (!containsEvent(collectionEventICreated, event.getKey())) {
                                collectionEventICreated.add(event);
                            }
                        } else if (event.getUsersRegistered().containsKey(MainActivity.emailKeyCurrentUser)) {
                            if (!containsEvent(collectionEventRegistered, event.getKey())) {
                                collectionEventRegistered.add(event);
                            }
                        } else {
                            if (!containsEvent(collectionEventUnregistered, event.getKey())) {
                                collectionEventUnregistered.add(event);
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                        Map<String, String> updatedUsersRegistered;
                        updatedUsersRegistered = (Map) snapshot.child("usersRegistered").getValue();

                        updateEventUserRegistered(AccountMainActivity.collectionEventUnregistered, snapshot.getKey(), updatedUsersRegistered);
                        updateEventUserRegistered(AccountMainActivity.collectionEventRegistered, snapshot.getKey(), updatedUsersRegistered);
                        updateEventUserRegistered(AccountMainActivity.collectionEventICreated, snapshot.getKey(), updatedUsersRegistered);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot snapshot) {
                        System.out.println("REMOVED EVENT");
                        collectionEventICreated.removeIf(obj -> obj.getKey().equals(snapshot.getKey()));
                        collectionEventRegistered.removeIf(obj -> obj.getKey().equals(snapshot.getKey()));
                        collectionEventUnregistered.removeIf(obj -> obj.getKey().equals(snapshot.getKey()));
                    }

                    @Override
                    public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

                System.out.println("REMOVED GROUP");
                collectionGroupICreated.removeIf(obj -> obj.getKey().equals(snapshot.getKey()));
                collectionGroupIBelong.removeIf(obj -> obj.getKey().equals(snapshot.getKey()));
                collectionGroupICanJoin.removeIf(obj -> obj.getKey().equals(snapshot.getKey()));

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    //Funkcja, która zczytuje informacje o aktualnie zalogowanym użytkowniku z Firebase
    private void readOnceCurrentUserFirebase() {
        DatabaseReference dbRefUserName = FirebaseDatabase.getInstance().getReference().child("Users").child(MainActivity.emailKeyCurrentUser);

        dbRefUserName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                fullNameCurrentUser = snapshot.child("fullName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    //Funkcja sprawdzająca czy na liście grup istnieje grupa o zadanym kluczu
    public boolean containsGroup(final List<Group> list, final String key) {
        return list.stream().filter(o -> o.getKey().equals(key)).findFirst().isPresent();
    }

    //Funkcja sprawdzająca czy na liście wydarzeń istnieje wydarzenie o zadanym kluczu
    public boolean containsEvent(final List<Event> list, final String key) {
        return list.stream().filter(o -> o.getKey().equals(key)).findFirst().isPresent();
    }


    private void getLastKnownLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    userLocationLatitude = location.getLatitude();
                    userLocationLongitude = location.getLongitude();
                }
            }
        });
    }

    //Funkcja, która aktualizuje listę członków grupy
    public void updateGroupMemberList(final List<Group> list, final String key, Map<String, String> updatedMemberList){
        list.stream().filter(o -> o.getKey().equals(key)).forEach(
                o -> {
                    o.setMemberList(updatedMemberList);
                }
        );
    }

    //Funkcja, która aktualizuje listę zarejestrowanych użytkowników do wydarzenia
    public void updateEventUserRegistered(final List<Event> list, final String key, Map<String, String> updatedUsersRegistered){
        list.stream().filter(o -> o.getKey().equals(key)).forEach(
                o -> {
                    o.setUsersRegistered(updatedUsersRegistered);
                }
        );
    }

    //Funkcja, która minimalizuje aplikacje
    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
