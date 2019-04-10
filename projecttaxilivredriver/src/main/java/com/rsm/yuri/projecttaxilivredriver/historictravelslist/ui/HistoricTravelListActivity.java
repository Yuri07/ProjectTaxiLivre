package com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.historictravel.ui.HistoricTravelActivity;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.HistoricTravelsListPresenter;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.entities.HistoricTravelItem;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.adapters.HistoricTravelsListAdapter;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoricTravelListActivity extends AppCompatActivity implements HistoricTravelListView, OnHistoricTravelItemClickListener {

    @BindView(R.id.container_historic_travels)
    CoordinatorLayout container;
    @BindView(R.id.recyclerViewTravels)
    RecyclerView recyclerView;

    @Inject
    HistoricTravelsListAdapter adapter;
    @Inject
    HistoricTravelsListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_travel_list);
        ButterKnife.bind(this);

        setupInjection();

        presenter.onCreate();

        setupRecyclerView();

    }

    private void setupInjection() {
        TaxiLivreDriverApp app = (TaxiLivreDriverApp) getApplication();
        app.getHistoricTravelsListComponent(this, this, this).inject(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onHistoricTravelAdded(HistoricTravelItem travelItem) {
        adapter.add(travelItem);
    }

    @Override
    public void onHistoricTravelChanged(Travel travel) {

    }

    @Override
    public void onHistoricTravelRemoved(Travel travel) {

    }

    @Override
    public void onHistoricTravelError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onUrlPhotoMapTravelRetrived(Travel travel) {

    }

    @Override
    public void onItemClick(HistoricTravelItem travelItem) {
        Intent i = new Intent(this, HistoricTravelActivity.class);
        /*i.putExtra(HistoricTravelActivity.EMAIL_KEY, travel.getRequesterEmail());
        i.putExtra(HistoricTravelActivity.STATUS_KEY, travel.getTravelId());
        i.putExtra(HistoricTravelActivity.URL_KEY, travel.getPlaceOriginAddress());*/

        startActivity(i);

    }

    @Override
    public void onItemLongClick(HistoricTravelItem travelItem) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

}
