package com.whitelabel.app.model;

import java.io.Serializable;

public class ResponseConnection implements Serializable {

    public class Status implements Serializable{
        public String flag;
        public Integer code;
        public String info;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public class Data implements Serializable {
        public class Connection implements Serializable {
            public String connectionToken;
            public String date;
            public String plugin;

            public String getConnectionToken() {
                return connectionToken;
            }

            public void setConnectionToken(String connectionToken) {
                this.connectionToken = connectionToken;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getPlugin() {
                return plugin;
            }

            public void setPlugin(String plugin) {
                this.plugin = plugin;
            }
        }

        public Connection connection;
        public User user;

        public Connection getConnection() {
            return connection;
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    public Status status;
    public Data data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
