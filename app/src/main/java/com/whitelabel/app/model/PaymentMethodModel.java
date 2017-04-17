package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2017/4/17.
 */

public class PaymentMethodModel implements Serializable {
    private String  label;
    private String code;
    private String content;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public void setContent(String content) {
        this.content = content;
    }
}
