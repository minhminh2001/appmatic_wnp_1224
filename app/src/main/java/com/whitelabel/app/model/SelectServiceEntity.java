package com.whitelabel.app.model;

/**
 * Created by Administrator on 2016/3/30.
 */
public class SelectServiceEntity {
    private boolean current;
    private String serviceName;

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
