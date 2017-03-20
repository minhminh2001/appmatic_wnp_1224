package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/11/2.
 */
public class GetServiceListEntity extends SVRReturnEntity {
    private String id;
    private String serverDomain;
    private String serverName;
    private String serverType;
    private String serverLoginName;
    private String serverLoginPwd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerDomain() {
        return serverDomain;
    }

    public void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getServerLoginName() {
        return serverLoginName;
    }

    public void setServerLoginName(String serverLoginName) {
        this.serverLoginName = serverLoginName;
    }

    public String getServerLoginPwd() {
        return serverLoginPwd;
    }

    public void setServerLoginPwd(String serverLoginPwd) {
        this.serverLoginPwd = serverLoginPwd;
    }
}
