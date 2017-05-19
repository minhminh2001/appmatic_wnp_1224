package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ray on 2015/10/21.
 */
public class ErrorMsgBean implements Serializable {
    private int status;
    private String errorMessage;
    private String error_messages;
    private List<String> ids;
    private Object params;

    public String getError_messages() {
        return error_messages;
    }

    public void setError_messages(String error_messages) {
        this.error_messages = error_messages;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
