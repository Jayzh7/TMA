package com.jayzh7.tma.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jayzh7.tma.Database.DatabaseHelper;
import com.jayzh7.tma.R;
import com.jayzh7.tma.Utils.TravelEventAdapter;

public class EventsListFragment extends Fragment {


    public static final int sREQUEST_CODE = 1;

    private RecyclerView mRecyclerView;
    private TravelEventAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_event_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = getView().findViewById(R.id.recyclerView);
        mAdapter = new TravelEventAdapter(getActivity(), DatabaseHelper.getInstance(getActivity()).readForEventList());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        mAdapter = new TravelEventAdapter(getActivity(), DatabaseHelper.getInstance(getActivity()).readForEventList());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up butto n, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), NaviActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_clear_all) {
            DatabaseHelper.getInstance(getActivity()).clearAll();
        }
        return super.onOptionsItemSelected(item);
    }
}

