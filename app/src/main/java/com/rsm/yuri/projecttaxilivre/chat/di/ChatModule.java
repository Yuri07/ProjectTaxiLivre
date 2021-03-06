package com.rsm.yuri.projecttaxilivre.chat.di;

import android.content.Context;

import com.rsm.yuri.projecttaxilivre.chat.ChatInteractor;
import com.rsm.yuri.projecttaxilivre.chat.ChatInteractorImpl;
import com.rsm.yuri.projecttaxilivre.chat.ChatPresenter;
import com.rsm.yuri.projecttaxilivre.chat.ChatPresenterImpl;
import com.rsm.yuri.projecttaxilivre.chat.ChatRepository;
import com.rsm.yuri.projecttaxilivre.chat.ChatRepositoryImpl;
import com.rsm.yuri.projecttaxilivre.chat.ChatSessionInteractor;
import com.rsm.yuri.projecttaxilivre.chat.ChatSessionInteractorImpl;
import com.rsm.yuri.projecttaxilivre.chat.entities.ChatMessage;
import com.rsm.yuri.projecttaxilivre.chat.ui.ChatView;
import com.rsm.yuri.projecttaxilivre.chat.ui.adapter.ChatAdapter;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 13/01/2018.
 */
@Module
public class ChatModule {

    ChatView view;
    //Context context;usado no adapter(ja e passado no libsmodule)

    public ChatModule(ChatView view){//, Context context) {
        this.view = view;
        //this.context = context;
    }

    @Provides
    ChatView providesHistoricChatsListView(){
        return view;
    }

    @Provides
    @Singleton
    ChatPresenter providesChatPresenter(EventBus eventBus,
                                                     ChatView chatView,
                                                     ChatInteractor chatInteractor, ChatSessionInteractor sessionInteractor){
        return new ChatPresenterImpl(eventBus,
                chatView,
                chatInteractor,
                sessionInteractor);
    }

    @Provides
    @Singleton
    ChatInteractor providesChatInteractor(ChatRepository chatRepository){
        return new ChatInteractorImpl(chatRepository);
    }

    @Provides
    @Singleton
    ChatSessionInteractor providesChatSessionInteractor(ChatRepository chatRepository){
        return new ChatSessionInteractorImpl(chatRepository);
    }

    @Provides
    @Singleton
    ChatRepository providesChatRepository(FirebaseAPI firebase, EventBus eventBus){
        return new ChatRepositoryImpl(firebase, eventBus);
    }

    @Provides
    @Singleton
    ChatAdapter providesHistoricChatsListAdapter(Context context, List<ChatMessage> chatMessages){
        return new ChatAdapter(context, chatMessages);
    }

    @Provides
    @Singleton
    List<ChatMessage> providesChatMessages(){
        return new ArrayList<ChatMessage>();
    }

}
