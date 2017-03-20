package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ray on 2015/10/21.
 */
public class ErrorItemsBean implements Serializable {

    private List<String> oos_ids;
    private List<String> disabled_ids;
    private List<String> unapproved_ids;
    private List<String> insufficient_ids;

    public List<String> getDisabled_ids() {
        return disabled_ids;
    }

    public void setDisabled_ids(List<String> disabled_ids) {
        this.disabled_ids = disabled_ids;
    }

    public List<String> getUnapproved_ids() {
        return unapproved_ids;
    }

    public void setUnapproved_ids(List<String> unapproved_ids) {
        this.unapproved_ids = unapproved_ids;
    }

    public List<String> getInsufficient_ids() {
        return insufficient_ids;
    }

    public void setInsufficient_ids(List<String> insufficient_ids) {
        this.insufficient_ids = insufficient_ids;
    }

    public List<String> getOos_ids() {
        return oos_ids;
    }

    public void setOos_ids(List<String> oos_ids) {
        this.oos_ids = oos_ids;
    }
}
