package com.whitelabel.app.ui.checkout.model;

import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CheckoutDefaultShippingAddress;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */

public class CheckoutDefaultAddressResponse implements Serializable {
    private AddressBook primaryBilling;
    private AddressBook  primaryShipping;
    private List<ShippingMethodBean> shippingMethod;
    private PickUpAddress pickupAddress;
    public AddressBook getPrimaryBilling() {
        return primaryBilling;
    }
    public void setPrimaryBilling(AddressBook primaryBilling) {
        this.primaryBilling = primaryBilling;
    }
    public AddressBook getPrimaryShipping() {
        return primaryShipping;
    }
    public void setPrimaryShipping(AddressBook primaryShipping) {
        this.primaryShipping = primaryShipping;
    }

    public List<ShippingMethodBean> getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(List<ShippingMethodBean> shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public static class ShippingMethodBean {
        /**
         * title : Pick up in store
         * code : 2
         * order : 1
         * checked : 0
         */

        private String title;
        private int code;
        private int order;
        private int checked;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getChecked() {
            return checked;
        }

        public void setChecked(int checked) {
            this.checked = checked;
        }
    }
    private static class PickUpAddress{
        private String title;
        private String address;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
