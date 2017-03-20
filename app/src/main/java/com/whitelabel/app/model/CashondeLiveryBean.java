package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2015/11/24.
 */
public class CashondeLiveryBean implements Serializable {
    private String title;
    private String code;
    private String content;
    private int error_flag;
    private String content_error;

    public void setContent(String content) {
        this.content = content;
    }

    public int getError_flag() {
        return error_flag;
    }

    public void setError_flag(int error_flag) {
        this.error_flag = error_flag;
    }

    public String getContent_error() {
        return content_error;
    }

    public void setContent_error(String content_error) {
        this.content_error = content_error;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }


}
