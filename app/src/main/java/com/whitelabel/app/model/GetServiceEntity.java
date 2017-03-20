package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/11/2.
 */
public class GetServiceEntity extends SVRReturnEntity {
    private ArrayList<GetServiceListEntity> result;
    private int status;

    public ArrayList<GetServiceListEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<GetServiceListEntity> result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
