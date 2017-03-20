package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/10.
 */
public class SVRAppServiceCustomerMyAccountUpdate extends SVRReturnEntity {
    private int status;
    private CustomerList customer;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CustomerList getCustomer() {return customer;}

    public void setCustomer(CustomerList customer) {this.customer = customer;}
}
