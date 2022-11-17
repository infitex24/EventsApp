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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EventsFragment extends Fragment implements View.OnClickListener {

    private View v;
    private TextView textViewButtonLogout, textViewSortBy;
    private Button buttonBelongingToEvent;
    private ImageView imageViewFilters;

    //Kolekcja przechowująca wydarzenia, która wyświetla się w ekranie widoku "Events"
    public static List<Event> listEventsRV;

    private RecyclerView myRecycleView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.events_fragment, container, false);

        myRecycleView = (RecyclerView) v.findViewById(R.id.id_eventList_recycleView);
        RecycleViewAdapterEvents recycleAdapter = new RecycleViewAdapterEvents(getContext(), EventsFragment.listEventsRV);
        myRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecycleView.setAdapter(recycleAdapter);

        buttonBelongingToEvent = v.findViewById(R.id.id_button_eventsFragment_selectBelongingToEvent);
        buttonBelongingToEvent.setOnClickListener(this);

        textViewButtonLogout = v.findViewById(R.id.textViewButtonLogoutEventsFragment);
        textViewButtonLogout.setOnClickListener(this);

        imageViewFilters = v.findViewById(R.id.id_imageview_eventsFragment_filters);
        imageViewFilters.setOnClickListener(this);

        textViewSortBy = v.findViewById(R.id.id_textView_eventsFragment_sortBy);

        listEventsRV.clear();

        //Inicializacja minutesToStart za każdym razem kiedy otwierany jest fragment "Events"
        initializeMinutesToStart(AccountMainActivity.collectionEventICreated);
        initializeMinutesToStart(AccountMainActivity.collectionEventRegistered);
        initializeMinutesToStart(AccountMainActivity.collectionEventUnregistered);

        //Inicializacja dateAddedAgo za każdym razem kiedy otwierany jest fragment "Events"
        initializeDateAddedAgo(AccountMainActivity.collectionEventICreated);
        initializeDateAddedAgo(AccountMainActivity.collectionEventRegistered);
        initializeDateAddedAgo(AccountMainActivity.collectionEventUnregistered);

        //Inicializacja distanceToUser za każdym razem kiedy otwierany jest fragment "Events"
        initializeDistanceToUser(AccountMainActivity.collectionEventICreated);
        initializeDistanceToUser(AccountMainActivity.collectionEventRegistered);
        initializeDistanceToUser(AccountMainActivity.collectionEventRegistered);

        switch (AccountMainActivity.belongingEventEventsFragment) {
            case("all"): {
                buttonBelongingToEvent.setText("all");

                if(AccountMainActivity.filtersMap.get("All")) {

                    for (Event event : AccountMainActivity.collectionEventUnregistered){
                        if(event.getMinutesToStart() >= 0 ){
                            listEventsRV.add(event);
                        }
                    }

                    for (Event event : AccountMainActivity.collectionEventRegistered){
                        if(event.getMinutesToStart() >= 0 ){
                            listEventsRV.add(event);
                        }
                    }

                    for (Event event : AccountMainActivity.collectionEventICreated){
                        if(event.getMinutesToStart() >= 0 ){
                            listEventsRV.add(event);
                        }
                    }

                }
                else{
                    filterEventsByType(AccountMainActivity.collectionEventICreated);
                    filterEventsByType(AccountMainActivity.collectionEventRegistered);
                    filterEventsByType(AccountMainActivity.collectionEventUnregistered);
                }
                break;
            }
            case("unregistered"): {
                buttonBelongingToEvent.setText("unregistered");

                if(AccountMainActivity.filtersMap.get("All")) {

                    for (Event event : AccountMainActivity.collectionEventUnregistered) {

                        if(event.getMinutesToStart() >= 0) {
                            listEventsRV.add(event);
                        }
                    }

                } else {
                    filterEventsByType(AccountMainActivity.collectionEventUnregistered);
                }
                break;
            }
            case("registered"): {
                buttonBelongingToEvent.setText("registered");

                if(AccountMainActivity.filtersMap.get("All")) {

                    for (Event event : AccountMainActivity.collectionEventRegistered) {
                        if(event.getMinutesToStart() >= 0) {
                            listEventsRV.add(event);
                        }
                    }

            } else {
                    filterEventsByType(AccountMainActivity.collectionEventRegistered);
            }
                break;
            }
            case("icreated"):{
                buttonBelongingToEvent.setText("i created");
                if(AccountMainActivity.filtersMap.get("All")) {
                    for (Event event : AccountMainActivity.collectionEventICreated) {
                        if(event.getMinutesToStart() >= 0) {
                            listEventsRV.add(event);
                        }
                    }
                }
                else{
                    filterEventsByType(AccountMainActivity.collectionEventICreated);
                }
                break;
            }
        }

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listEventsRV = new ArrayList<>();

    }

    @Override
    public void onStart() {
        super.onStart();

        if(AccountMainActivity.filtersMap.get("Incoming")) {
            textViewSortBy.setText("Incoming");
        }
        else if(AccountMainActivity.filtersMap.get("DateAdded")){
            textViewSortBy.setText("Date Added");
        }
        else{
            textViewSortBy.setText("Distance");
        }

        if(AccountMainActivity.filtersMap.get("Incoming").equals(true)){
            sortEventsByMinutesToStart(listEventsRV);
        }
        else if(AccountMainActivity.filtersMap.get("DateAdded").equals(true)){
            sortEventsByDateAdded(listEventsRV);
        }
        else{
            sortEventsByDistanceToUser(listEventsRV);
        }

        if(EventsFiltersActivity.isApplyFilters){
            AccountMainActivity.fragmentSelected = new EventsFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, AccountMainActivity.fragmentSelected).commit();

            EventsFiltersActivity.isApplyFilters = false;
        }

        if(MoreInfoEventActivity.buttonClicked){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, new EventsFragment()).commit();
            MoreInfoEventActivity.buttonClicked = false;
        }

    }


    //Funkcja, zwracająca liczbę minut jaka pozostała do rozpoczęcia wydarzenia
    public long findDifferenceInMinutes(String start_date, String end_date) {

        long diffrence=0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Calendar calendar = Calendar.getInstance();
        TimeZone currentTimezone = calendar.getTimeZone();
        SimpleDateFormat sdfTZ = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdfTZ.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        try {
            Date dateCurrentTimezone = sdfTZ.parse(end_date);
            sdfTZ.setTimeZone(TimeZone.getTimeZone(currentTimezone.getID()));
            String strDateEndCurrentTimezone = sdfTZ.format(dateCurrentTimezone);

            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(strDateEndCurrentTimezone);

            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            diffrence = (difference_In_Time / 1000 / 60);
        }

        catch (ParseException e) {

            e.printStackTrace();
        }
        return diffrence;
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case(R.id.id_button_eventsFragment_selectBelongingToEvent): {
                SelectBelongingToEventDialogFragment selectBelongingToEventDialogFragment = new SelectBelongingToEventDialogFragment();
                selectBelongingToEventDialogFragment.show(getActivity().getSupportFragmentManager(), "select belonging to event dialog fragment");

                break;
            }

            case (R.id.textViewButtonLogoutEventsFragment): {
                LogoutConfirmFragment logoutConfirmFragment = new LogoutConfirmFragment();
                logoutConfirmFragment.show(getActivity().getSupportFragmentManager(), "logout confirm dialog fragment");
                break;

            }

            case (R.id.id_imageview_eventsFragment_filters): {

                openEventsFiltersActivity();
                break;

            }

        }

    }

    //Funkcja, która inicializuje liczbę minut do rozpoczęcia wydarzenia dla każdego obiektu Event
    private void initializeMinutesToStart(List<Event> eventCollection) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatTZ = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String strDateCurrentTimezone1 = simpleDateFormatTZ.format(calendar.getTime());

        for (Event event : eventCollection) {
            long tempMinutesToStart = findDifferenceInMinutes(strDateCurrentTimezone1, event.getDateStartDefaultTimezone());

            event.setMinutesToStart(tempMinutesToStart);

        }
    }

    //Funkcja, która inicializuje dystans, który dzieli użytkownika do wydarzenia dla każdego obiektu Event
    private void initializeDistanceToUser(List<Event> eventCollection)
    {
        for (Event event : eventCollection) {
            event.setDistanceToUser(calculateDistanceToUser(AccountMainActivity.userLocationLatitude, AccountMainActivity.userLocationLongitude, event.getLatitude(), event.getLongitude()));
        }
    }

    //Funkcja zwracająca dystans, który dzieli użytkownika od wydarzenia
    private float calculateDistanceToUser(Double userLatitude, Double userLongitude, String markerLatitude, String markerLongitude) {
        Location locationUser = new Location("");
        locationUser.setLatitude(userLatitude);
        locationUser.setLongitude(userLongitude);

        Location locationMarker = new Location("");
        locationMarker.setLatitude(Double.valueOf(markerLatitude));
        locationMarker.setLongitude(Double.valueOf(markerLongitude));

        return locationUser.distanceTo(locationMarker);
    }

    //Funkcja, która inicializuje liczbę minut od momentu stworzenia wydarzenia
    private void initializeDateAddedAgo(List<Event> eventCollection) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatTZ = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String strDateCurrentTimezone1 = simpleDateFormatTZ.format(calendar.getTime());

        for (Event event : eventCollection) {
            event.setMinutesAgoAdded(findDifferenceInMinutes(event.getDateStartDefaultTimezone() ,strDateCurrentTimezone1));
        }
    }

    private void openEventsFiltersActivity() {
        Intent intent = new Intent(getActivity(), EventsFiltersActivity.class);
        startActivity(intent);
    }

    //Funckja sortująca rosnąco wydarzenia na podstawie dystansu pomiędzy wydarzeniem a użytkownikiem
    private void sortEventsByDistanceToUser(List<Event> eventList) {
        Collections.sort(eventList, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return Float.compare(o1.getDistanceToUser(), o2.getDistanceToUser());
            }
        });
    }

    //Funkcja sortująca rosnąco wydarzenia na podstawie liczby minut która została do rozpoczęcia wydarzenia
    private void sortEventsByMinutesToStart(List<Event> eventList) {
        Collections.sort(eventList, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return Float.compare(o1.getMinutesToStart(), o2.getMinutesToStart());
            }
        });
    }

    //Funkcja sortująca rosnąco wydarzenia na podstawie liczby minut istnienia wydarzenia
    private void sortEventsByDateAdded(List<Event> eventList) {
        Collections.sort(eventList, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return Float.compare(o1.getMinutesAgoAdded(), o2.getMinutesAgoAdded());
            }
        });
    }

    //Funkcja, która dodaje do wyświetlanej listy wydarzeń wydarzenia określonego typu które zostały zaznaczone w filtrach
    private void filterEventsByType(List<Event> eventCollection){
        for(Event event : eventCollection){
            if(AccountMainActivity.filtersMap.get(event.getType())){

                if( event.getMinutesToStart() >= 0){
                    listEventsRV.add(event);
                }

            }
        }
    }
}
