package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/9.
 */
public class SVRAppserviceNotificationListReturnEntity extends SVRReturnEntity {

    private int code;
    private int page;
    private NotificationListEntity  data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public NotificationListEntity getData() {
        return data;
    }

    public void setData(NotificationListEntity data) {
        this.data = data;
    }
}
