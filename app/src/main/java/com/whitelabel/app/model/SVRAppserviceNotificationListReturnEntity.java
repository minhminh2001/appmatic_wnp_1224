package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/9.
 */
public class SVRAppserviceNotificationListReturnEntity extends SVRReturnEntity {

    private int code;
    private int page;
    private NotificationCell [] data;

    private int notification_unread_count;

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


    public NotificationCell[] getData() {
        return data;
    }

    public void setData(NotificationCell[] data) {
        this.data = data;
    }

    public int getNotification_unread_count() {
        return notification_unread_count;
    }

    public void setNotification_unread_count(int notification_unread_count) {
        this.notification_unread_count = notification_unread_count;
    }
}
