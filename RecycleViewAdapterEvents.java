package com.example.events3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RecycleViewAdapterEvents extends RecyclerView.Adapter<RecycleViewAdapterEvents.MyViewHolderEvents> {

    Context mContext;
    List<Event> mData;

    public static Event selectedEvent;


    public RecycleViewAdapterEvents(Context mContext, List<Event> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolderEvents onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_eventslist, parent, false);
        final MyViewHolderEvents vHolder = new MyViewHolderEvents(v);

        vHolder.item_eventslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedEvent = mData.get(vHolder.getAdapterPosition());
                openMoreInfoEventActivity();
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderEvents holder, int position) {

        String dateHourItem, dateItem, dayOfWeek = "MON", monthOfYear = "JAN";
        int photo = 0;

        Calendar calendar = Calendar.getInstance();
        TimeZone currentTimezone = calendar.getTimeZone();
        String strDateCurrentTimezone = "?date";
        SimpleDateFormat simpleDateFormatTZ = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        simpleDateFormatTZ.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        try {
            Date dateCurrentTimezone = simpleDateFormatTZ.parse(mData.get(position).getDateStartDefaultTimezone());

            simpleDateFormatTZ.setTimeZone(TimeZone.getTimeZone(currentTimezone.getID()));
            strDateCurrentTimezone = simpleDateFormatTZ.format(dateCurrentTimezone);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateItem = strDateCurrentTimezone.substring(0,10);
        dateHourItem = strDateCurrentTimezone.substring(10,16);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            System.out.println("DATEEEEEEEEEE:" + dateItem);
            date = sdf.parse(dateItem);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int intDayOfWeek = 1;
        int intMonthOfYear = 1;

        intDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        switch (intDayOfWeek) {

            case(1):
            {
                dayOfWeek = "SUN., ";
                break;
            }

            case(2):
            {
                dayOfWeek = "MON., ";
                break;
            }

            case(3):
            {
                dayOfWeek = "TUE., ";
                break;
            }

            case(4):
            {
                dayOfWeek = "WED., ";
                break;
            }

            case(5):
            {
                dayOfWeek = "THU., ";
                break;
            }

            case(6):
            {
                dayOfWeek = "FRI., ";
                break;
            }
            case(7):
            {
                dayOfWeek = "SAT., ";
            }

        }

        intMonthOfYear = cal.get(Calendar.MONTH);

        switch (intMonthOfYear) {

            case(0):
            {
                monthOfYear = "JAN ";
                break;
            }

            case(1):
            {
                monthOfYear = "FEB ";
                break;
            }

            case(2):
            {
                monthOfYear = "MAR ";
                break;
            }

            case(3):
            {
                monthOfYear = "APR ";
                break;
            }

            case(4):
            {
                monthOfYear = "MAY ";
                break;
            }

            case(5):
            {
                monthOfYear = "JUN ";
                break;
            }
            case(6):
            {
                monthOfYear = "JUL ";
                break;
            }

            case(7):
            {
                monthOfYear = "AUG ";
                break;
            }


            case(8):
            {
                monthOfYear = "SEP ";
                break;
            }

            case(9):
            {
                monthOfYear = "OCT ";
                break;
            }

            case(10):
            {
                monthOfYear = "NOV ";
                break;
            }

            case(11):
            {
                monthOfYear = "DEC ";
                break;
            }

        }

        if(mData.get(position).getAdminKey().equals(MainActivity.emailKeyCurrentUser)){
            holder.item_eventslist.setBackgroundColor(Color.parseColor("#2fa120"));
            holder.tv_registeredOrNo.setText("Your event");
        }
        else if(mData.get(position).getUsersRegistered().containsKey(MainActivity.emailKeyCurrentUser)){
            holder.item_eventslist.setBackgroundColor(Color.parseColor("#1b8c59"));
            holder.tv_registeredOrNo.setText("Registered");
        }
        else{
            holder.item_eventslist.setBackgroundColor(Color.parseColor("#23b372"));
            holder.tv_registeredOrNo.setText("");
        }

        holder.tv_evTitle.setText(mData.get(position).getTitle());
        holder.tv_evDescription.setText(mData.get(position).getDescription());
        holder.tv_evDayOfMonth.setText(dateItem.substring(0,2));
        holder.tv_evDateHour.setText(dateHourItem);
        holder.tv_evDayOfWeek.setText(dayOfWeek);
        holder.tv_evMonthOfYear.setText(monthOfYear);
        holder.tv_evGroupName.setText(mData.get(position).getGroupName());
        holder.tv_distanceToUser.setText(convertDistance(mData.get(position).getDistanceToUser()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderEvents extends RecyclerView.ViewHolder {

        public LinearLayout item_eventslist;
        public TextView tv_evTitle;
        public TextView tv_evDescription;
        public TextView tv_evDayOfMonth;
        public TextView tv_evDateHour;
        public TextView tv_evDayOfWeek;
        public TextView tv_evMonthOfYear;
        public TextView tv_evGroupName;
        public TextView tv_distanceToUser;
        public TextView tv_registeredOrNo;

        public MyViewHolderEvents(@NonNull View itemView) {
            super(itemView);

            item_eventslist = (LinearLayout) itemView.findViewById(R.id.id_eventList_item);
            tv_evTitle = (TextView) itemView.findViewById(R.id.id_event_title_itemEventsList);
            tv_evDescription = (TextView) itemView.findViewById(R.id.id_textview_itemEventList_eventDescription_FB);
            tv_evDayOfMonth = (TextView) itemView.findViewById(R.id.id_event_dayOfMonth_itemEventsList);
            tv_evDateHour = (TextView) itemView.findViewById(R.id.id_event_dateHour_itemEventsList);
            tv_evDayOfWeek = (TextView) itemView.findViewById(R.id.id_textView_dayOfWeek_itemEventsList);
            tv_evMonthOfYear = (TextView) itemView.findViewById(R.id.id_textView_monthOfYear_itemEventsList);
            tv_evGroupName = (TextView) itemView.findViewById(R.id.id_textview_itemEventList_groupName_FB);
            tv_distanceToUser = (TextView) itemView.findViewById(R.id.id_textview_itemEventList_distanceToUser_FB);
            tv_registeredOrNo = (TextView) itemView.findViewById(R.id.id_textview_itemEventList_registeredOrNo);
        }
    }

    private void openMoreInfoEventActivity() {
        Intent intent = new Intent(mContext, MoreInfoEventActivity.class);
        mContext.startActivity(intent);
    }

    private String convertDistance(float distance){

        String result = "";

        if(distance < 1000){

            DecimalFormat dfm = new DecimalFormat("#");
            result = String.valueOf(dfm.format(distance) + "m");
        }
        else{
            DecimalFormat dfkm = new DecimalFormat(".00");
            result = String.valueOf(dfkm.format(distance/1000) + "km");
        }
        return result;
    }

}
