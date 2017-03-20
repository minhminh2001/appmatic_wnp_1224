package com.whitelabel.app.handler;

import com.whitelabel.app.model.SVRReturnEntity;

/**
 * Created by imaginato on 2015/7/6.
 */
public class SVRAppEverythingCms extends SVRReturnEntity {
    private String content;
    private int success;
    public int getSuccess() {
        return success;
    }
    public void setSuccess(int success) {
        this.success = success;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
