package com.rsm.yuri.projecttaxilivre.lib.base;

/**
 * Created by yuri_ on 12/01/2018.
 */

public interface EventBus {

    void register(Object subscriber);
    void unregister(Object subscriber);
    void post(Object event);

}
