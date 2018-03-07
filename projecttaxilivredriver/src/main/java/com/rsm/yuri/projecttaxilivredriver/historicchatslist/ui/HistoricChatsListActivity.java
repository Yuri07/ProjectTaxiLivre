package com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.chat.ui.ChatActivity;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.HistoricChatsListPresenter;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.adapters.HistoricChatsListAdapter;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoricChatsListActivity extends AppCompatActivity implements HistoricChatsListView, OnItemClickListener {

    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.recyclerViewContacts)
    RecyclerView recyclerView;

    @Inject
    HistoricChatsListAdapter adapter;
    @Inject
    HistoricChatsListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_chats_list);
        ButterKnife.bind(this);

        setupInjection();

        presenter.onCreate();

        setupRecyclerView();

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

    private void setupInjection() {
        TaxiLivreDriverApp app = (TaxiLivreDriverApp) getApplication();
        app.getHistoricChatsListComponent(this, this, this).inject(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(User user) {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra(ChatActivity.EMAIL_KEY, user.getEmail());
        i.putExtra(ChatActivity.STATUS_KEY, user.getStatus());
        startActivity(i);
    }

    @Override
    public void onItemLongClick(User user) {
        presenter.removeHistoricChat(user.getEmail());
    }

    @Override
    public void onHistoricChatAdded(User user) {
        adapter.add(user);
    }

    @Override
    public void onHistoricChatChanged(User user) {
        adapter.update(user);
    }

    @Override
    public void onHistoricChatRemoved(User user) {
        adapter.remove(user);
    }

    @Override
    public void onHistoricChatError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }


}
