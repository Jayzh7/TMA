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

import com.jayzh7.tma.Adapter.TravelEventAdapter;
import com.jayzh7.tma.Database.DatabaseHelper;
import com.jayzh7.tma.R;

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

    /**
     * Inflate views and notify activity that option menu will be set
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container          If non-null, this is the parent view that the fragment's UI should be
     *                           attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_event_list, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    /**
     * Does final initialization including setting up recycler view and its adapter
     * @param savedInstanceState not in use
     */
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

    /**
     * Refresh recycler view upon resume
     */
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


    /**
     * Inflate option menu for this fragment
     * @param menu the option menu
     * @param inflater Menu infalter
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This is called whenever an item in options menu is selected.
     * @param item The menu item that was selected
     * @return super
     */
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

