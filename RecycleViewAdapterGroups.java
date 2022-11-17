package com.example.events3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewAdapterGroups extends RecyclerView.Adapter<RecycleViewAdapterGroups.MyViewHolderGroups> {

    Context mContext;
    List<Group> mData;

    static Group selectedGroup;


    public RecycleViewAdapterGroups(Context mContext, List<Group> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolderGroups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_grouplist, parent, false);
        final MyViewHolderGroups vHolder = new MyViewHolderGroups(v);

        vHolder.linearLayoutItemGroupList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               selectedGroup = mData.get(vHolder.getAdapterPosition());

                openMoreInfoGroupActivity();
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderGroups holder, int position) {

        if(mData.get(position).getAdminKey().equals(MainActivity.emailKeyCurrentUser)){
            holder.linearLayoutItemGroupList.setBackgroundColor(Color.parseColor("#2fa120"));
            holder.tvBelongOrNo.setText("Your group");
        }
        else if(mData.get(position).getMemberList().containsKey(MainActivity.emailKeyCurrentUser)){
            holder.linearLayoutItemGroupList.setBackgroundColor(Color.parseColor("#1b8c59"));
            holder.tvBelongOrNo.setText("You belong");
        }
        else{
            holder.linearLayoutItemGroupList.setBackgroundColor(Color.parseColor("#23b372"));
            holder.tvBelongOrNo.setText("");
        }

        holder.tvGroupName.setText(mData.get(position).getName());
        holder.tvGroupDesc.setText(mData.get(position).getDescription());
        holder.tvGroupMembers.setText(String.valueOf(mData.get(position).getMemberList().size()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderGroups extends RecyclerView.ViewHolder {

        public LinearLayout linearLayoutItemGroupList;
        public TextView tvGroupName;
        public TextView tvGroupDesc;
        public TextView tvBelongOrNo;
        public TextView tvGroupMembers;

        public MyViewHolderGroups(@NonNull View itemView) {
            super(itemView);

            linearLayoutItemGroupList = (LinearLayout) itemView.findViewById(R.id.id_linearLayout_itemGroupList);
            tvGroupName = (TextView) itemView.findViewById(R.id.id_textview_itemGroupList_groupName_DB);
            tvGroupDesc = (TextView) itemView.findViewById(R.id.id_textview_itemGroupList_groupDesc_DB);
            tvBelongOrNo = (TextView) itemView.findViewById(R.id.id_textView_itemGroupList_belongOrNo);
            tvGroupMembers = (TextView) itemView.findViewById(R.id.id_textview_itemGroupList_groupMembers_DB);

        }
    }

    private void openMoreInfoGroupActivity()
    {
        Intent intent = new Intent(mContext, MoreInfoGroupActivity.class);
        mContext.startActivity(intent);
    }

}
