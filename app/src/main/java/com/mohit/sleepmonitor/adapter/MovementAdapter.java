package com.mohit.sleepmonitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohit.sleepmonitor.R;
import com.mohit.sleepmonitor.Utilities.Utility;
import com.mohit.sleepmonitor.bean.MovementItem;

import java.util.ArrayList;

/**
 * Created by Mohit on 23-10-2016.
 */
public class MovementAdapter extends RecyclerView.Adapter<MovementAdapter.ViewHolder> {
    private final int VIEW_TYPE_HEADER = 0;
    private final int VIEW_TYPE_ROW = 1;
    private Context mContext;
    private ArrayList<MovementItem> mDataList;

    public MovementAdapter(Context context, ArrayList<MovementItem> list) {
        this.mContext = context;
        this.mDataList = list;
    }

    @Override
    public MovementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutID = -1;

        layoutID = R.layout.view_movement_item;

        View view = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovementAdapter.ViewHolder holder, int position) {
      //  if (position > 0){
            holder.startTimeView.setText(Utility.getFriedlyDate(mDataList.get(position).getStartTime()));
            holder.endTimeView.setText(Utility.getFriedlyDate(mDataList.get(position).getEndTime()));
            holder.durationView.setText(mDataList.get(position).getDuration());
      //  }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView startTimeView;
        TextView endTimeView;
        TextView durationView;

        public ViewHolder(View itemView) {
            super(itemView);
            startTimeView = (TextView) itemView.findViewById(R.id.textview_start_time);
            endTimeView = (TextView) itemView.findViewById(R.id.textview_end_time);
            durationView = (TextView) itemView.findViewById(R.id.textview_duration);
        }
    }
}
