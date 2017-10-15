package com.jayzh7.tma.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayzh7.tma.Models.TravelEvent;
import com.jayzh7.tma.R;

import java.util.ArrayList;


/**
 * Created by Jay on 10/14/2017.
 */

/**
 *
 */
public class TravelEventAdapter extends RecyclerView.Adapter<TravelEventAdapter.ViewHolder> {

    private ArrayList<TravelEvent> mTravelEvent;
    private Context mContext;

    public TravelEventAdapter(ArrayList<TravelEvent> eventList) {
        mTravelEvent = eventList;
    }

    @Override
    public TravelEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new TravelEventAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TravelEventAdapter.ViewHolder holder, int position) {
        TravelEvent event = mTravelEvent.get(position);

        holder.toPlace.setText(event.getToPlace());
        holder.fromPlace.setText(event.getFromPlace());
        holder.fromTime.setText(new DateTimeConverter(event.getStartT()).getConvertedTime());
        holder.toTime.setText(new DateTimeConverter(event.getEndT()).getConvertedTime());
        if (event.getEventName() != null) {
            holder.eventName.setText(event.getEventName());
        }

        switch (event.getEventType()) {
            case SIGHTSEEING:
                holder.eventType.setImageResource(R.drawable.ic_sightseeing);
                break;
            case GUIDANCE:
                holder.eventType.setImageResource(R.drawable.ic_guidance);
                break;
            case SHOPPING:
                holder.eventType.setImageResource(R.drawable.ic_shopping);
                break;
            case MEAL:
                holder.eventType.setImageResource(R.drawable.ic_meal);
                break;
            case REST:
                holder.eventType.setImageResource(R.drawable.ic_rest);
                break;
            default:
                holder.eventType.setImageResource(R.drawable.ic_others);
        }
    }


    @Override
    public int getItemCount() {
        return mTravelEvent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fromPlace, toPlace;
        public TextView fromTime, toTime;
        public TextView eventName;
        public ImageView eventType;

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventNameTV);
            fromPlace = itemView.findViewById(R.id.fromPlace);
            toPlace = itemView.findViewById(R.id.toPlace);
            fromTime = itemView.findViewById(R.id.item_start_time);
            toTime = itemView.findViewById(R.id.item_end_time);
            eventType = itemView.findViewById(R.id.item_image);
        }
    }
}