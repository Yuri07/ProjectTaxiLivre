package com.rsm.yuri.projecttaxilivre.chat.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
/*import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;*/
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.chat.ChatPresenter;
import com.rsm.yuri.projecttaxilivre.chat.entities.ChatMessage;
import com.rsm.yuri.projecttaxilivre.chat.ui.adapter.ChatAdapter;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements ChatView {

    @BindView(R.id.imgAvatar)
    CircleImageView imgAvatar;
    @BindView(R.id.txtUser)
    TextView txtUser;
    @BindView(R.id.txtStatus)
    TextView txtStatus;
    @BindView(R.id.toolbar_act_chat)
    Toolbar toolbar;
    @BindView(R.id.messageRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.editTxtMessage)
    EditText inputMessage;

    @Inject
    ChatPresenter presenter;
    @Inject
    ChatAdapter adapter;
    @Inject
    ImageLoader imageLoader;

    public final static String EMAIL_KEY = "email";
    public final static String STATUS_KEY = "status";
    public final static String URL_KEY = "url_photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setupInjection();

        presenter.onCreate();

        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        setToolbarData(intent);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupInjection() {
        TaxiLivreApp app = (TaxiLivreApp) getApplication();
        app.getChatComponent(this, this).inject(this);
    }

    @Override
    public void onMessageReceived(ChatMessage msg) {
        adapter.add(msg);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onStatusChanged(long online){
        String status = online == 1 ? "online" : "offline";
        int color = online == 1 ? Color.GREEN : Color.RED;
        txtStatus.setText(status);
        txtStatus.setTextColor(color);
    }

    @OnClick(R.id.btnSendMessage)
    public void sendMessage() {
        presenter.sendMessage(inputMessage.getText().toString());
        inputMessage.setText("");
    }

    public void setToolbarData(Intent i) {
        String recipient = i.getStringExtra(EMAIL_KEY);
        presenter.setChatRecipient(recipient);

        long online = i.getLongExtra(STATUS_KEY, 0);
        Log.d("d", "ChatActivity.driver.getStatus(): "+online);
        String status = (online == Driver.OFFLINE || online==Driver.IN_TRAVEL)? "offline" : "online";
        int color = (online == Driver.OFFLINE || online==Driver.IN_TRAVEL) ? Color.RED : Color.GREEN;

        txtUser.setText(recipient);
        txtStatus.setText(status);
        txtStatus.setTextColor(color);

        String url = i.getStringExtra(URL_KEY);
        if(url!=null) {
            if (!url.equals("Default"))
                imageLoader.load(imgAvatar, url);
        }

        /*AndroidChatApplication app = (AndroidChatApplication)getApplication();
        ImageLoader imageLoader = app.getImageLoader();
        imageLoader.load(imgAvatar, AvatarHelper.getAvatarUrl(recipient));*/
    }
}
