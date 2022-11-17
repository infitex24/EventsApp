package com.example.events3;

        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import androidx.fragment.app.FragmentActivity;

        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.TimeZone;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, View.OnTouchListener{

    private final DatabaseReference dbRefGroups = FirebaseDatabase.getInstance().getReference().child("Groups");
    private Button buttonConfirmEventLocationMap, buttonConfirmGroupLocationMap, buttonMoreEventInfo;
    private MarkerOptions createEventMarkerOptions, createGroupMarkerOptions;

    private GoogleMap mMap;
    public static final int REQUEST_LOCATION_PERMISSION = 1;

    public static boolean bool_AddMarkerCreateEvent = false;
    public static boolean bool_AddMarkerCreateGroup = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonConfirmEventLocationMap = findViewById(R.id.id_buttonConfirmEventLocationMap);
        buttonConfirmGroupLocationMap = findViewById(R.id.id_buttonConfirmGroupLocationMap);
        buttonMoreEventInfo = findViewById(R.id.id_button_mapActivity_moreEventInfo);
        TextView textviewLogout = findViewById(R.id.textViewButtonLogoutMapActivity);

        buttonConfirmEventLocationMap.setOnClickListener(this);
        buttonConfirmGroupLocationMap.setOnClickListener(this);
        buttonMoreEventInfo.setOnClickListener(this);
        textviewLogout.setOnTouchListener(this);


        //Inicializacja minutesToStart za każdym razem kiedy otwierana jest mapa
        initializeMinutesToStart(AccountMainActivity.collectionEventICreated);
        initializeMinutesToStart(AccountMainActivity.collectionEventRegistered);
        initializeMinutesToStart(AccountMainActivity.collectionEventUnregistered);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
        enableMyLocation();

        drawMarkersOnMap(AccountMainActivity.collectionEventICreated);
        drawMarkersOnMap(AccountMainActivity.collectionEventRegistered);
        drawMarkersOnMap(AccountMainActivity.collectionEventUnregistered);

        if(MoreInfoEventActivity.animateToMarker) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(RecycleViewAdapterEvents.selectedEvent.getLatitude()), Double.valueOf(RecycleViewAdapterEvents.selectedEvent.getLongitude())), 16.0f));
            MoreInfoEventActivity.animateToMarker = false;
        }
        else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(AccountMainActivity.userLocationLatitude, AccountMainActivity.userLocationLongitude), 13.0f));
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                buttonMoreEventInfo.setVisibility(View.GONE);

                if(bool_AddMarkerCreateEvent) {

                    buttonConfirmEventLocationMap.setVisibility(View.VISIBLE);

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.SECOND, 0);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String currentDateTime = simpleDateFormat.format(c.getTime());
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));

                    MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title(CreateEventActivity.sstrTitle).snippet("Address: " + CreateEventActivity.sstrDesc).draggable(true);
                    createEventMarkerOptions = marker;

                    if(findDifferenceInMinutes(currentDateTime, CreateEventActivity.sstrDateStartDefaultTimezone) >= 1440) {
                        createEventMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(250.5f));
                        mMap.addMarker(createEventMarkerOptions);


                    }
                    else if(findDifferenceInMinutes(currentDateTime, CreateEventActivity.sstrDateStartDefaultTimezone) < 1440 && findDifferenceInMinutes(currentDateTime, CreateEventActivity.sstrDateStartDefaultTimezone) >= 60) {
                        createEventMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(50));
                        mMap.addMarker(createEventMarkerOptions);

                    }
                    else if(findDifferenceInMinutes(currentDateTime, CreateEventActivity.sstrDateStartDefaultTimezone) < 60 && findDifferenceInMinutes(currentDateTime, CreateEventActivity.sstrDateStartDefaultTimezone) >= 0 ) {

                        createEventMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(20));
                        mMap.addMarker(createEventMarkerOptions);

                    }
                    else if(findDifferenceInMinutes(currentDateTime, CreateEventActivity.sstrDateStartDefaultTimezone) < 0 ) {
                        createEventMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        mMap.addMarker(createEventMarkerOptions);

                    }

                    bool_AddMarkerCreateEvent =false;
                }

                if(bool_AddMarkerCreateGroup) {
                    bool_AddMarkerCreateGroup = false;
                    buttonConfirmGroupLocationMap.setVisibility(View.VISIBLE);

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.SECOND, 0);

                    MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title(CreateEventActivity.sstrTitle).draggable(true);
                    createGroupMarkerOptions = marker;

                        createGroupMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(300));
                        mMap.addMarker(createGroupMarkerOptions);

                }
            }
        });
    }

    //Funkcja, która zapisuje informacje o nowo dodanym wydarzeniu do bazy danych
    public void PutEventInFirebase(MarkerOptions marker, String title, String desc, String place, String tag, String dateStartDefaultTimezone, String dateEndDefaultTimezone, String dateOfCreationDefaultTimezone, String limitUsers){

        LatLng latlngtemp = marker.getPosition();
        Double dlatitude = latlngtemp.latitude;
        Double dlongitude = latlngtemp.longitude;
        String str_latitude = dlatitude.toString();
        String str_longitude = dlongitude.toString();

        Map<String, String> memberList = new HashMap<>();
        memberList.put(MainActivity.emailKeyCurrentUser, "true");

        Event event = new Event(MainActivity.emailKeyCurrentUser, AccountMainActivity.fullNameCurrentUser, title, desc, str_longitude, str_latitude, tag, place, RecycleViewAdapterGroups.selectedGroup.getKey(), RecycleViewAdapterGroups.selectedGroup.getName(), dateStartDefaultTimezone,  dateEndDefaultTimezone, dateOfCreationDefaultTimezone, limitUsers, memberList);

        dbRefGroups.child(RecycleViewAdapterGroups.selectedGroup.getKey()).child("Events").push().setValue(event);
    }

    //Funkcja, która zapisuje informacje o nowo dodanej grupie do bazy danych
    public void PutGroupInFirebase(MarkerOptions marker, String title, String desc, String dateOfCreation) {
        LatLng latlngtemp = marker.getPosition();
        Double dlatitude = latlngtemp.latitude;
        Double dlongitude = latlngtemp.longitude;
        String str_latitude = dlatitude.toString();
        String str_longitude = dlongitude.toString();

        Map<String, String> memberList = new HashMap<>();
        memberList.put(MainActivity.emailKeyCurrentUser, "true");

        Group group = new Group(MainActivity.emailKeyCurrentUser, AccountMainActivity.fullNameCurrentUser, title, desc, dateOfCreation, str_longitude, str_latitude, memberList);
        dbRefGroups.push().setValue(group);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case (R.id.id_button_mapActivity_moreEventInfo): {
                openMoreInfoEventActivity();
                break;
            }

            case (R.id.id_buttonConfirmEventLocationMap): {

                Calendar c = Calendar.getInstance();
                c.set(Calendar.SECOND, 0);

                Date date = c.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));

                String dateAddedDefaultTimezone = simpleDateFormat.format(date);

                createEventMarkerOptions.draggable(false);
                 PutEventInFirebase(createEventMarkerOptions,CreateEventActivity.sstrTitle,CreateEventActivity.sstrDesc, CreateEventActivity.sstrPlace, CreateEventActivity.sstrTag , CreateEventActivity.sstrDateStartDefaultTimezone, CreateEventActivity.sstrDateEndDefaultTimezone, dateAddedDefaultTimezone, CreateEventActivity.sstrLimitUser);
                buttonConfirmEventLocationMap.setVisibility(View.GONE);
                AccountMainActivity.showDialogSuccessfulCreateEvent = true;
                finish();


                 break;
            }
            case (R.id.id_buttonConfirmGroupLocationMap): {

                Calendar c = Calendar.getInstance();
                c.set(Calendar.SECOND, 0);

                Date date = c.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));

                String dateOfCreation = simpleDateFormat.format(date);

                PutGroupInFirebase(createGroupMarkerOptions,CreateGroupActivity.sstrGroupName,CreateGroupActivity.sstrGroupDesc, dateOfCreation);

                createGroupMarkerOptions.draggable(false);
                buttonConfirmGroupLocationMap.setVisibility(View.GONE);
                AccountMainActivity.showDialogSuccessfulCreateGroup = true;
                finish();
                break;
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        buttonMoreEventInfo.setVisibility(View.VISIBLE);

        LatLng tempLatLng = marker.getPosition();
        Double dlatitude = tempLatLng.latitude;
        Double dlongitude = tempLatLng.longitude;
        String str_latitude = dlatitude.toString();
        String str_longitude = dlongitude.toString();

        getEventInfoByMarkerClick(AccountMainActivity.collectionEventICreated, str_latitude, str_longitude);
        getEventInfoByMarkerClick(AccountMainActivity.collectionEventRegistered, str_latitude, str_longitude);
        getEventInfoByMarkerClick(AccountMainActivity.collectionEventUnregistered, str_latitude, str_longitude);

        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng currentLatLng = marker.getPosition();

        if(bool_AddMarkerCreateEvent) {
            createEventMarkerOptions.position(currentLatLng);
        }
        if(bool_AddMarkerCreateGroup){
            createGroupMarkerOptions.position(currentLatLng);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }

    //Funkcja, która pozwala na dostęp do lokalizacji
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    //Funkcja, zwracająca liczbę minut jaka pozostała do rozpoczęcia wydarzenia
    private long findDifferenceInMinutes(String start_date, String end_date) {

        long diffrence=0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        //CONVERTING end_date to current Timezone date
        Calendar calendar = Calendar.getInstance();
        TimeZone currentTimezone = calendar.getTimeZone();
        SimpleDateFormat sdfTZ = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdfTZ.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        // Try Block
        try {

            Date dateCurrentTimezone = sdfTZ.parse(end_date);
            sdfTZ.setTimeZone(TimeZone.getTimeZone(currentTimezone.getID()));
            String strDateEndCurrentTimezone = sdfTZ.format(dateCurrentTimezone);

            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(strDateEndCurrentTimezone);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();


            diffrence = (difference_In_Time / 1000) / 60;

        }

        // Catch the Exception
        catch (ParseException e) {

        }

        return diffrence;
    }

    //Funkcja, która inicializuje liczbę minut do rozpoczęcia wydarzenia dla każdego obiektu Event
    private void initializeMinutesToStart(List<Event> eventCollection) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatTZ = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String strDateCurrentTimezone1 = simpleDateFormatTZ.format(calendar.getTime());

        for (Event event : eventCollection)
        {
            event.setMinutesToStart(findDifferenceInMinutes(strDateCurrentTimezone1, event.getDateStartDefaultTimezone()));
        }
    }

    private void drawMarkersOnMap(List<Event> eventCollection) {

        for (Event event: eventCollection) {
            Double doubLocationX = Double.valueOf(event.getLatitude());
            Double doubLocationY = Double.valueOf(event.getLongitude());
            LatLng latLngtemp = new LatLng(doubLocationX,doubLocationY);

            //SETTINGS COLOR MARKERS BY USING TIME AND TIME FROM DATABASE FIREBASE
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String currentDateTime = simpleDateFormat.format(calendar.getTime());


            //CREATING MARKER
            MarkerOptions options = new MarkerOptions().position(latLngtemp).title(event.getTitle()).snippet("Address: " + event.getPlace());

            if(event.getMinutesToStart() >= 1440)
            {
                options.icon(BitmapDescriptorFactory.defaultMarker(250.5f));
                mMap.addMarker(options);
                continue;

            }
            if(event.getMinutesToStart() < 1440 && event.getMinutesToStart() >= 60)
            {
                options.icon(BitmapDescriptorFactory.defaultMarker(50));
                mMap.addMarker(options);
                continue;

            }
            if(event.getMinutesToStart() < 60 && event.getMinutesToStart() >= 0 )
            {

                options.icon(BitmapDescriptorFactory.defaultMarker(20));
                mMap.addMarker(options);
                continue;

            }

        }
    }

    //Funkcja, która znajduje obiekt Event po kliknięciu w znacznik (wydarzenie) na mapie
    public void getEventInfoByMarkerClick(final List<Event> list, final String lat, final String lon){
        list.stream().filter(o -> o.getLatitude().equals(lat) && o.getLongitude().equals(lon)).forEach(
                o -> {
                    RecycleViewAdapterEvents.selectedEvent = o;
                }
        );
    }

    private void openMoreInfoEventActivity() {
        Intent intent = new Intent(this, MoreInfoEventActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(v.getId()) {

            case(R.id.textViewButtonLogoutMapActivity):
            {
                LogoutConfirmFragment logoutConfirmFragment = new LogoutConfirmFragment();
                logoutConfirmFragment.show(getSupportFragmentManager(), "logout confirm dialog fragment");

            }
        }
        return false;
    }

}


