package com.rsm.yuri.projecttaxilivre.chat;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.rsm.yuri.projecttaxilivre.chat.entities.ChatMessage;
import com.rsm.yuri.projecttaxilivre.chat.events.ChatEvent;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseEventListenerCallback;
import com.rsm.yuri.projecttaxilivre.historicchatslist.events.HistoricChatsListEvent;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;

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
        ChatMessage chatMessage = new ChatMessage(keySender, msg, false);
        String newChatMessageId = firebase.createChatMessageId(receiver);
        chatMessage.setId(newChatMessageId);
        DatabaseReference chatsReference = firebase.getChatsReference(receiver);
        //chatsReference.push().setValue(chatMessage);
        chatsReference.child(chatMessage.getId()).setValue(chatMessage);
    }

    @Override
    public void setReceiver(String receiver) {
        this.receiver = receiver;
        //firebase.setChatReceiver(receiver);
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

                if(!chatMessage.isRead() && !chatMessage.isSentByMe())
                    firebase.setMessageChatRead(receiver, chatMessage.getId());

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
    public void subscribeForStatusReceiverUpdate() {
        firebase.subscribeForStatusReceiverUpdate(receiver, new FirebaseEventListenerCallback(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                /*long status = (long) dataSnapshot.getValue();
                Log.d("d", "subscribeForStatusReceiverUpdates onchildAdded status((long) dataSnapshot.getValue()) = " + status);

                post(ChatEvent.READ_STATUS_RECEIVER_EVENT, status);*/
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {
                long status = (long) dataSnapshot.getValue();
                Log.d("d", "subscribeForStatusReceiverUpdates onchildChanged status((long) dataSnapshot.getValue()) = " + status);

                post(ChatEvent.READ_STATUS_RECEIVER_EVENT, status);
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
    public void unSubscribeForStatusReceiverUpdate() {
        firebase.unSubscribeForStatusReceiverUpdate(receiver);
    }

    @Override
    public void changeUserConnectionStatus(long status) {
        firebase.changeUserConnectionStatus(status);
    }

    private void post(int type, ChatMessage message){
        post(type, message, 0, null);
    }

    private void post(int type, long status){
        post(type, null, status, null);
    }

    private void post(int type, String error){
        post(type, null, 0,error);
    }

    private void post(int type, ChatMessage message, long status, String error){
        ChatEvent chatEvent = new ChatEvent();
        chatEvent.setMsg(message);
        chatEvent.setStatusReceiver(status);
        chatEvent.setEventType(type);
        chatEvent.setError(error);
        eventBus.post(chatEvent);
    }

}
