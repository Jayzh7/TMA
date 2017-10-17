package com.jayzh7.tma.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jayzh7.tma.Database.DatabaseHelper;
import com.jayzh7.tma.R;
import com.jayzh7.tma.Adapter.TravelEventAdapter;

import static com.jayzh7.tma.Activities.ChartFragment.sREQUEST_CODE;

/**
 * Fragment that displays a recycler view of events
 *
 * @author Jay
 * @version 1.0
 * @since 10/14/2017
 */
public class EventsListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TravelEventAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_event_list, container, false);
        setHasOptionsMenu(true);
        return v;
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

    /**
     * Update data
     */
    private void refreshView() {
        mAdapter = new TravelEventAdapter(getActivity(), DatabaseHelper.getInstance(getActivity()).readForEventList());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.invalidate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up butto n, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.refreshBtn) {
            refreshView();
        }

        if (id == R.id.addBtn) {
            Intent addEventIntent = new Intent(getActivity(), AddEventActivity.class);
            startActivityForResult(addEventIntent, sREQUEST_CODE);
        }

        if (id == R.id.action_clear_all) {
            DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
            db.clearAll();
            refreshView();
        }

        return super.onOptionsItemSelected(item);
    }
}

