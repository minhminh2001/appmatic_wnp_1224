package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/10/23.
 */
public class GetAnimCodeEntity extends SVRReturnEntity {
    private int status;
    private String message;
    private String won_type;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWon_type() {
        return won_type;
    }

    public void setWon_type(String won_type) {
        this.won_type = won_type;
    }
}
