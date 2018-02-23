package com.rsm.yuri.projecttaxilivre.chat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rsm.yuri.projecttaxilivre.R;

public class ChatActivity extends AppCompatActivity implements ChatView{

    public final static String EMAIL_KEY = "email";
    public final static String STATUS_KEY = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
