package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/9.
 */
public class SVRAppserviceNotificationListReturnEntity extends SVRReturnEntity {

    private int status;
    private int page;
    private NotificationCell [] notification_items;

    private int notification_unread_count;

    public int getStatus() {
        return status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public NotificationCell[] getNotification_items() {
        return notification_items;
    }

    public void setNotification_items(NotificationCell[] notification_items) {
        this.notification_items = notification_items;
    }

    public int getNotification_unread_count() {
        return notification_unread_count;
    }

    public void setNotification_unread_count(int notification_unread_count) {
        this.notification_unread_count = notification_unread_count;
    }
}
