package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/16.
 */
public class MolpayResponseBean implements Serializable {



    private String status_code;
    private String amount;
    private String chksum;
    private String pInstruction;
    private String msgType;
    private String paydate;
    private String order_id;
    private String err_desc;
    private String channel;
    private String app_code;
    private String txn_ID;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChksum() {
        return chksum;
    }

    public void setChksum(String chksum) {
        this.chksum = chksum;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getpInstruction() {

        return pInstruction;
    }

    public void setpInstruction(String pInstruction) {
        this.pInstruction = pInstruction;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }



    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getErr_desc() {
        return err_desc;
    }

    public void setErr_desc(String err_desc) {
        this.err_desc = err_desc;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public String getTxn_ID() {
        return txn_ID;
    }

    public void setTxn_ID(String txn_ID) {
        this.txn_ID = txn_ID;
    }
}
