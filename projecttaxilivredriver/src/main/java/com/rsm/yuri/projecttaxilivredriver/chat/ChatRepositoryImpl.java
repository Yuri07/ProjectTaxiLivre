package com.rsm.yuri.projecttaxilivredriver.chat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.rsm.yuri.projecttaxilivredriver.chat.entities.ChatMessage;
import com.rsm.yuri.projecttaxilivredriver.chat.events.ChatEvent;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseEventListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class ChatRepositoryImpl implements ChatRepository {

    private FirebaseAPI firebase;
    private EventBus eventBus;
    private String receiver;

    public ChatRepositoryImpl(FirebaseAPI firebase, EventBus eventBus) {
        this.firebase = firebase;
        this.eventBus = eventBus;
    }

    @Override
    public void sendMessage(String msg) {
        String keySender = firebase.getAuthUserEmail().replace(".","_");
        ChatMessage chatMessage = new ChatMessage(keySender, msg);
        DatabaseReference chatsReference = firebase.getChatsReference(receiver);
        chatsReference.push().setValue(chatMessage);
    }

    @Override
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public void destroyChatListener() {
        firebase.destroyChatListener();
    }

    @Override
    public void subscribeForChatUpates() {
        //Log.d("d", "subscriveForChatupdates receiver = " + receiver);
        firebase.subscribeForChatUpdates(receiver, new FirebaseEventListenerCallback(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                String msgSender = chatMessage.getSender();
                msgSender = msgSender.replace("_",".");

                String currentUserEmail = firebase.getAuthUserEmail();
                chatMessage.setSentByMe(msgSender.equals(currentUserEmail));

                post(ChatEvent.READ_EVENT, chatMessage);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(ChatEvent.ERROR_EVENT, error.getMessage());
            }
        });
    }

    @Override
    public void unSubscribeForChatUpates() {
        firebase.unSubscribeForChatUpdates(receiver);
    }

    @Override
    public void changeUserConnectionStatus(long status) {
        firebase.changeUserConnectionStatus(status);
    }

    private void post(int type, ChatMessage message){
        post(type, message, null);
    }

    private void post(int type, String error){
        post(type, null, error);
    }

    private void post(int type, ChatMessage message, String error){
        ChatEvent chatEvent = new ChatEvent();
        chatEvent.setMsg(message);
        chatEvent.setEventType(type);
        chatEvent.setError(error);
        eventBus.post(chatEvent);
    }

}
