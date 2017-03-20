package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppServiceCustomerMonthlyIncom extends SVRReturnEntity {
    private ArrayList<MonthlyIncomList> incomeList;
    private int status;

    public int getStatus() {return status;}

    public void setStatus(int status) {this.status = status;}
    public ArrayList<MonthlyIncomList> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(ArrayList<MonthlyIncomList> incomeList) {
        this.incomeList = incomeList;
    }
}
