package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/10.
 */
public class SVRAppServiceCustomerPhoneNumber  extends SVRReturnEntity {
    private ArrayList<PhoneNumberList> telephoneCodeList;
    private int status;

    public ArrayList<PhoneNumberList> getTelephoneCodeList() {
        return telephoneCodeList;
    }

    public void setTelephoneCodeList(ArrayList<PhoneNumberList> telephoneCodeList) {
        this.telephoneCodeList = telephoneCodeList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
