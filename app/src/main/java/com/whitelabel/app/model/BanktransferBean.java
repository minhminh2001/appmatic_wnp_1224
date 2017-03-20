package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ray on 2015/10/16.
 */
public class BanktransferBean implements Serializable {
    private String transferId;
    private String title;
    private String code;
    private String content;
    private String bankFrom;
    private String email;
    private String orderNo;
    private String transferee;
    private String transferred;
    private String transferDate;
    private String proofFile;
    private int canSubmit;

    public int getCanSubmit() {
        return canSubmit;
    }

    public void setCanSubmit(int canSubmit) {
        this.canSubmit = canSubmit;
    }

    private List<KeyValueBean> bankFromList;

    public List<KeyValueBean> getBankFromList() {
        return bankFromList;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public void setBankFromList(List<KeyValueBean> bankFromList) {
        this.bankFromList = bankFromList;
    }

    public String getBankFrom() {
        return bankFrom;
    }

    public void setBankFrom(String bankFrom) {
        this.bankFrom = bankFrom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTransferee() {
        return transferee;
    }

    public void setTransferee(String transferee) {
        this.transferee = transferee;
    }

    public String getTransferred() {
        return transferred;
    }

    public void setTransferred(String transferred) {
        this.transferred = transferred;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getProofFile() {
        return proofFile;
    }

    public void setProofFile(String proofFile) {
        this.proofFile = proofFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
