package com.whitelabel.app.model;

import java.util.List;

/**
 * Created by imaginato on 2015/7/7.
 */
public class AddresslistReslut extends SVRReturnEntity {
    private int success;
    private int status;
    private List<AddressBook> address;
    public int getSuccess() {
        return success;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSuccess(int success) {
        this.success = success;
    }


    public List<AddressBook> getAddress() {
        return address;
    }

    public void setAddress(List<AddressBook> address) {
        this.address = address;
    }
}
