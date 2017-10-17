package com.jayzh7.tma.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayzh7.tma.Activities.AddEventActivity;
import com.jayzh7.tma.Database.DatabaseHelper;
import com.jayzh7.tma.Models.TravelEvent;
import com.jayzh7.tma.R;
import com.jayzh7.tma.Utils.DateTimeConverter;

import java.util.ArrayList;

/**
 * Adapters provide a binding from an app-specific data set to views
 * that are displayed within a RecyclerView.
 * @author Jay
 * @version 1.0
 * @since 10/14/2017
 */
public class TravelEventAdapter extends RecyclerView.Adapter<TravelEventAdapter.ViewHolder> {

    public static final String START_TIME = "startTime";
    public static final String TRAVEL_EVENT = "travelEvent";
    private ArrayList<TravelEvent> mTravelEvent;
    private Context mContext;

    /**
     * Constructor for TravelEventAdapter
     *
     * @param eventList The data set to be displayed in Recycler view.
     */
    public TravelEventAdapter(ArrayList<TravelEvent> eventList) {
        mTravelEvent = eventList;
    }

    /**
     * Constructor for TravelEventAdapter
     * @param context Context from the activity that displays recycler view.
     * @param eventList The data set to be displayed in Recycler view.
     */
    public TravelEventAdapter(Context context, ArrayList<TravelEvent> eventList) {
        mTravelEvent = eventList;
        mContext = context;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     * @param parent  The ViewGroup into which the new View will be added after
     *                it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @Override
    public TravelEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item,
                parent, false);
        return new TravelEventAdapter.ViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the item at the given position.
     * @param holder The ViewHolder which should be updated to represent the contents of the item
     *               at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final TravelEventAdapter.ViewHolder holder, final int position) {
        final TravelEvent event = mTravelEvent.get(position);

        holder.toPlace.setText(event.getToPlace());
        holder.fromPlace.setText(event.getFromPlace());
        holder.fromTime.setText(new DateTimeConverter(event.getStartT()).getConvertedTime());
        holder.toTime.setText(new DateTimeConverter(event.getEndT()).getConvertedTime());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send start time of the event which can be used to identify am event
                // to new activity
                Intent intent = new Intent(mContext, AddEventActivity.class);
                intent.putExtra(START_TIME, event.getStartT());
                intent.putExtra(TRAVEL_EVENT, event);
                mContext.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete the event and refresh
                DatabaseHelper.getInstance(mContext).deleteEvent(event.getStartT());
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, (int) DatabaseHelper.getInstance(mContext).getItemNumberForEvents());
                holder.itemView.setVisibility(View.GONE);
            }
        });

        // Event name can be empty
        if (event.getEventName() != null) {
            holder.eventName.setText(event.getEventName());
        }

        // Display specific icons for each item
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

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fromPlace, toPlace;
        public TextView fromTime, toTime;
        public TextView eventName;
        public ImageView eventType;
        public Button edit, delete;

        /**
         * Find views of the item.
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventNameTV);
            fromPlace = itemView.findViewById(R.id.fromPlace);
            toPlace = itemView.findViewById(R.id.toPlace);
            fromTime = itemView.findViewById(R.id.item_start_time);
            toTime = itemView.findViewById(R.id.item_end_time);
            eventType = itemView.findViewById(R.id.item_image);
            edit = itemView.findViewById(R.id.itemEdit);
            delete = itemView.findViewById(R.id.itemDelete);
        }
    }
}
