package com.rsm.yuri.projecttaxilivre.adddialog.AddDialogEvent;

/**
 * Created by yuri_ on 10/11/2017.
 */

public class AddDialogEvent {

    String key;
    String value;
    String errorMsg;
    boolean error = false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
