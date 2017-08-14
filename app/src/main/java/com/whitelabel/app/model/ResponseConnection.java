package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

public class ResponseConnection implements Serializable {


    /**
     * response : {"request":{"date":"Fri, 11 Aug 2017 10:04:59 +0200","resource":"/users.json?platformId=2&versionNumber=1.0.2&serviceVersion=1.0.5","status":{"flag":"success","code":200,"info":"Your request has been processed successfully"}},"result":{"data":{"user":{"user_token":"1bb01c10-a63d-4d27-a51f-b66291ab2fb4","publish_token":{"key":"c260cde3-fa08-4dbe-be1f-d1a7f23ec1b0","date_creation":"Mon, 10 Apr 2017 09:57:27 +0200","date_expiration":"Thu, 09 Nov 2017 09:04:59 +0100"},"identity":{"identity_token":"84d5cba7-f560-4602-bb54-d648f09a4e79","date_creation":"Mon, 10 Apr 2017 09:57:27 +0200","date_last_update":"Fri, 11 Aug 2017 10:04:59 +0200","provider":"facebook","provider_identity_uid":"PIU2624DE4FBA17438155D596480248F698","source":{"name":"Facebook","key":"facebook","access_token":{"key":"EAASSzzmImkABAA77a9QnxQoPREXaYVAzdFZB87I6QvZAxyXQHuIrS2ZCL9MixZClZAPIjXsW1StZB4YLf1an1NSAkxPeGXtQZAjzrfOZC2KyjgHfZCNH05HIaeFgLeubDEZAZBYE3S8rze4rkI5ZBQQLMgm3klDsQPHhVvO4ZAkCgmSgMXqGcnBZC8UJhmCMaxhBybIHC4MGCzFJwASRXyyExVVRyb","date_expiration":"05/20/2065 16:34:23"}},"id":"http://www.facebook.com/profile.php?id=1953844871516245","displayName":"Jiang Ray","name":{"formatted":"Jiang Ray","givenName":"Jiang","familyName":"Ray"},"preferredUsername":"jiang.ray","profileUrl":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","thumbnailUrl":"https://graph.facebook.com/1953844871516245/picture?type=square","pictureUrl":"https://graph.facebook.com/1953844871516245/picture?type=large","gender":"male","utcOffset":"8:00","emails":[{"value":"176721325@qq.com","is_verified":true}],"urls":[{"value":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","type":"profile"}],"accounts":[{"domain":"facebook.com","userid":"1953844871516245"}],"photos":[{"value":"https://graph.facebook.com/1953844871516245/picture?type=square","size":"2:XS"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=small","size":"3:S"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=normal","size":"4:M"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=large","size":"5:L"}],"browser":{"agent":"okhttp/3.8.1","type":"okhttp","version":{"major":"3","full":"3.8.1"}}},"identities":[{"identity_token":"84d5cba7-f560-4602-bb54-d648f09a4e79","provider":"facebook"}]}}}}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * request : {"date":"Fri, 11 Aug 2017 10:04:59 +0200","resource":"/users.json?platformId=2&versionNumber=1.0.2&serviceVersion=1.0.5","status":{"flag":"success","code":200,"info":"Your request has been processed successfully"}}
         * result : {"data":{"user":{"user_token":"1bb01c10-a63d-4d27-a51f-b66291ab2fb4","publish_token":{"key":"c260cde3-fa08-4dbe-be1f-d1a7f23ec1b0","date_creation":"Mon, 10 Apr 2017 09:57:27 +0200","date_expiration":"Thu, 09 Nov 2017 09:04:59 +0100"},"identity":{"identity_token":"84d5cba7-f560-4602-bb54-d648f09a4e79","date_creation":"Mon, 10 Apr 2017 09:57:27 +0200","date_last_update":"Fri, 11 Aug 2017 10:04:59 +0200","provider":"facebook","provider_identity_uid":"PIU2624DE4FBA17438155D596480248F698","source":{"name":"Facebook","key":"facebook","access_token":{"key":"EAASSzzmImkABAA77a9QnxQoPREXaYVAzdFZB87I6QvZAxyXQHuIrS2ZCL9MixZClZAPIjXsW1StZB4YLf1an1NSAkxPeGXtQZAjzrfOZC2KyjgHfZCNH05HIaeFgLeubDEZAZBYE3S8rze4rkI5ZBQQLMgm3klDsQPHhVvO4ZAkCgmSgMXqGcnBZC8UJhmCMaxhBybIHC4MGCzFJwASRXyyExVVRyb","date_expiration":"05/20/2065 16:34:23"}},"id":"http://www.facebook.com/profile.php?id=1953844871516245","displayName":"Jiang Ray","name":{"formatted":"Jiang Ray","givenName":"Jiang","familyName":"Ray"},"preferredUsername":"jiang.ray","profileUrl":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","thumbnailUrl":"https://graph.facebook.com/1953844871516245/picture?type=square","pictureUrl":"https://graph.facebook.com/1953844871516245/picture?type=large","gender":"male","utcOffset":"8:00","emails":[{"value":"176721325@qq.com","is_verified":true}],"urls":[{"value":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","type":"profile"}],"accounts":[{"domain":"facebook.com","userid":"1953844871516245"}],"photos":[{"value":"https://graph.facebook.com/1953844871516245/picture?type=square","size":"2:XS"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=small","size":"3:S"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=normal","size":"4:M"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=large","size":"5:L"}],"browser":{"agent":"okhttp/3.8.1","type":"okhttp","version":{"major":"3","full":"3.8.1"}}},"identities":[{"identity_token":"84d5cba7-f560-4602-bb54-d648f09a4e79","provider":"facebook"}]}}}
         */

        private RequestBean request;
        private ResultBean result;

        public RequestBean getRequest() {
            return request;
        }

        public void setRequest(RequestBean request) {
            this.request = request;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class RequestBean {
            /**
             * date : Fri, 11 Aug 2017 10:04:59 +0200
             * resource : /users.json?platformId=2&versionNumber=1.0.2&serviceVersion=1.0.5
             * status : {"flag":"success","code":200,"info":"Your request has been processed successfully"}
             */

            private String date;
            private String resource;
            private StatusBean status;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getResource() {
                return resource;
            }

            public void setResource(String resource) {
                this.resource = resource;
            }

            public StatusBean getStatus() {
                return status;
            }

            public void setStatus(StatusBean status) {
                this.status = status;
            }

            public static class StatusBean {
                /**
                 * flag : success
                 * code : 200
                 * info : Your request has been processed successfully
                 */

                private String flag;
                private int code;
                private String info;

                public String getFlag() {
                    return flag;
                }

                public void setFlag(String flag) {
                    this.flag = flag;
                }

                public int getCode() {
                    return code;
                }

                public void setCode(int code) {
                    this.code = code;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }
            }
        }

        public static class ResultBean {
            /**
             * data : {"user":{"user_token":"1bb01c10-a63d-4d27-a51f-b66291ab2fb4","publish_token":{"key":"c260cde3-fa08-4dbe-be1f-d1a7f23ec1b0","date_creation":"Mon, 10 Apr 2017 09:57:27 +0200","date_expiration":"Thu, 09 Nov 2017 09:04:59 +0100"},"identity":{"identity_token":"84d5cba7-f560-4602-bb54-d648f09a4e79","date_creation":"Mon, 10 Apr 2017 09:57:27 +0200","date_last_update":"Fri, 11 Aug 2017 10:04:59 +0200","provider":"facebook","provider_identity_uid":"PIU2624DE4FBA17438155D596480248F698","source":{"name":"Facebook","key":"facebook","access_token":{"key":"EAASSzzmImkABAA77a9QnxQoPREXaYVAzdFZB87I6QvZAxyXQHuIrS2ZCL9MixZClZAPIjXsW1StZB4YLf1an1NSAkxPeGXtQZAjzrfOZC2KyjgHfZCNH05HIaeFgLeubDEZAZBYE3S8rze4rkI5ZBQQLMgm3klDsQPHhVvO4ZAkCgmSgMXqGcnBZC8UJhmCMaxhBybIHC4MGCzFJwASRXyyExVVRyb","date_expiration":"05/20/2065 16:34:23"}},"id":"http://www.facebook.com/profile.php?id=1953844871516245","displayName":"Jiang Ray","name":{"formatted":"Jiang Ray","givenName":"Jiang","familyName":"Ray"},"preferredUsername":"jiang.ray","profileUrl":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","thumbnailUrl":"https://graph.facebook.com/1953844871516245/picture?type=square","pictureUrl":"https://graph.facebook.com/1953844871516245/picture?type=large","gender":"male","utcOffset":"8:00","emails":[{"value":"176721325@qq.com","is_verified":true}],"urls":[{"value":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","type":"profile"}],"accounts":[{"domain":"facebook.com","userid":"1953844871516245"}],"photos":[{"value":"https://graph.facebook.com/1953844871516245/picture?type=square","size":"2:XS"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=small","size":"3:S"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=normal","size":"4:M"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=large","size":"5:L"}],"browser":{"agent":"okhttp/3.8.1","type":"okhttp","version":{"major":"3","full":"3.8.1"}}},"identities":[{"identity_token":"84d5cba7-f560-4602-bb54-d648f09a4e79","provider":"facebook"}]}}
             */

            private DataBean data;

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public static class DataBean {
                /**
                 * user : {"user_token":"1bb01c10-a63d-4d27-a51f-b66291ab2fb4","publish_token":{"key":"c260cde3-fa08-4dbe-be1f-d1a7f23ec1b0","date_creation":"Mon, 10 Apr 2017 09:57:27 +0200","date_expiration":"Thu, 09 Nov 2017 09:04:59 +0100"},"identity":{"identity_token":"84d5cba7-f560-4602-bb54-d648f09a4e79","date_creation":"Mon, 10 Apr 2017 09:57:27 +0200","date_last_update":"Fri, 11 Aug 2017 10:04:59 +0200","provider":"facebook","provider_identity_uid":"PIU2624DE4FBA17438155D596480248F698","source":{"name":"Facebook","key":"facebook","access_token":{"key":"EAASSzzmImkABAA77a9QnxQoPREXaYVAzdFZB87I6QvZAxyXQHuIrS2ZCL9MixZClZAPIjXsW1StZB4YLf1an1NSAkxPeGXtQZAjzrfOZC2KyjgHfZCNH05HIaeFgLeubDEZAZBYE3S8rze4rkI5ZBQQLMgm3klDsQPHhVvO4ZAkCgmSgMXqGcnBZC8UJhmCMaxhBybIHC4MGCzFJwASRXyyExVVRyb","date_expiration":"05/20/2065 16:34:23"}},"id":"http://www.facebook.com/profile.php?id=1953844871516245","displayName":"Jiang Ray","name":{"formatted":"Jiang Ray","givenName":"Jiang","familyName":"Ray"},"preferredUsername":"jiang.ray","profileUrl":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","thumbnailUrl":"https://graph.facebook.com/1953844871516245/picture?type=square","pictureUrl":"https://graph.facebook.com/1953844871516245/picture?type=large","gender":"male","utcOffset":"8:00","emails":[{"value":"176721325@qq.com","is_verified":true}],"urls":[{"value":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","type":"profile"}],"accounts":[{"domain":"facebook.com","userid":"1953844871516245"}],"photos":[{"value":"https://graph.facebook.com/1953844871516245/picture?type=square","size":"2:XS"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=small","size":"3:S"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=normal","size":"4:M"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=large","size":"5:L"}],"browser":{"agent":"okhttp/3.8.1","type":"okhttp","version":{"major":"3","full":"3.8.1"}}},"identities":[{"identity_token":"84d5cba7-f560-4602-bb54-d648f09a4e79","provider":"facebook"}]}
                 */

                private UserBean user;

                public UserBean getUser() {
                    return user;
                }

                public void setUser(UserBean user) {
                    this.user = user;
                }

                public static class UserBean {
                    /**
                     * user_token : 1bb01c10-a63d-4d27-a51f-b66291ab2fb4
                     * publish_token : {"key":"c260cde3-fa08-4dbe-be1f-d1a7f23ec1b0","date_creation":"Mon, 10 Apr 2017 09:57:27 +0200","date_expiration":"Thu, 09 Nov 2017 09:04:59 +0100"}
                     * identity : {"identity_token":"84d5cba7-f560-4602-bb54-d648f09a4e79","date_creation":"Mon, 10 Apr 2017 09:57:27 +0200","date_last_update":"Fri, 11 Aug 2017 10:04:59 +0200","provider":"facebook","provider_identity_uid":"PIU2624DE4FBA17438155D596480248F698","source":{"name":"Facebook","key":"facebook","access_token":{"key":"EAASSzzmImkABAA77a9QnxQoPREXaYVAzdFZB87I6QvZAxyXQHuIrS2ZCL9MixZClZAPIjXsW1StZB4YLf1an1NSAkxPeGXtQZAjzrfOZC2KyjgHfZCNH05HIaeFgLeubDEZAZBYE3S8rze4rkI5ZBQQLMgm3klDsQPHhVvO4ZAkCgmSgMXqGcnBZC8UJhmCMaxhBybIHC4MGCzFJwASRXyyExVVRyb","date_expiration":"05/20/2065 16:34:23"}},"id":"http://www.facebook.com/profile.php?id=1953844871516245","displayName":"Jiang Ray","name":{"formatted":"Jiang Ray","givenName":"Jiang","familyName":"Ray"},"preferredUsername":"jiang.ray","profileUrl":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","thumbnailUrl":"https://graph.facebook.com/1953844871516245/picture?type=square","pictureUrl":"https://graph.facebook.com/1953844871516245/picture?type=large","gender":"male","utcOffset":"8:00","emails":[{"value":"176721325@qq.com","is_verified":true}],"urls":[{"value":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","type":"profile"}],"accounts":[{"domain":"facebook.com","userid":"1953844871516245"}],"photos":[{"value":"https://graph.facebook.com/1953844871516245/picture?type=square","size":"2:XS"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=small","size":"3:S"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=normal","size":"4:M"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=large","size":"5:L"}],"browser":{"agent":"okhttp/3.8.1","type":"okhttp","version":{"major":"3","full":"3.8.1"}}}
                     * identities : [{"identity_token":"84d5cba7-f560-4602-bb54-d648f09a4e79","provider":"facebook"}]
                     */

                    private String user_token;
                    private PublishTokenBean publish_token;
                    private IdentityBean identity;
                    private List<IdentitiesBean> identities;

                    public String getUser_token() {
                        return user_token;
                    }

                    public void setUser_token(String user_token) {
                        this.user_token = user_token;
                    }

                    public PublishTokenBean getPublish_token() {
                        return publish_token;
                    }

                    public void setPublish_token(PublishTokenBean publish_token) {
                        this.publish_token = publish_token;
                    }

                    public IdentityBean getIdentity() {
                        return identity;
                    }

                    public void setIdentity(IdentityBean identity) {
                        this.identity = identity;
                    }

                    public List<IdentitiesBean> getIdentities() {
                        return identities;
                    }

                    public void setIdentities(List<IdentitiesBean> identities) {
                        this.identities = identities;
                    }

                    public static class PublishTokenBean {
                        /**
                         * key : c260cde3-fa08-4dbe-be1f-d1a7f23ec1b0
                         * date_creation : Mon, 10 Apr 2017 09:57:27 +0200
                         * date_expiration : Thu, 09 Nov 2017 09:04:59 +0100
                         */

                        private String key;
                        private String date_creation;
                        private String date_expiration;

                        public String getKey() {
                            return key;
                        }

                        public void setKey(String key) {
                            this.key = key;
                        }

                        public String getDate_creation() {
                            return date_creation;
                        }

                        public void setDate_creation(String date_creation) {
                            this.date_creation = date_creation;
                        }

                        public String getDate_expiration() {
                            return date_expiration;
                        }

                        public void setDate_expiration(String date_expiration) {
                            this.date_expiration = date_expiration;
                        }
                    }

                    public static class IdentityBean {
                        /**
                         * identity_token : 84d5cba7-f560-4602-bb54-d648f09a4e79
                         * date_creation : Mon, 10 Apr 2017 09:57:27 +0200
                         * date_last_update : Fri, 11 Aug 2017 10:04:59 +0200
                         * provider : facebook
                         * provider_identity_uid : PIU2624DE4FBA17438155D596480248F698
                         * source : {"name":"Facebook","key":"facebook","access_token":{"key":"EAASSzzmImkABAA77a9QnxQoPREXaYVAzdFZB87I6QvZAxyXQHuIrS2ZCL9MixZClZAPIjXsW1StZB4YLf1an1NSAkxPeGXtQZAjzrfOZC2KyjgHfZCNH05HIaeFgLeubDEZAZBYE3S8rze4rkI5ZBQQLMgm3klDsQPHhVvO4ZAkCgmSgMXqGcnBZC8UJhmCMaxhBybIHC4MGCzFJwASRXyyExVVRyb","date_expiration":"05/20/2065 16:34:23"}}
                         * id : http://www.facebook.com/profile.php?id=1953844871516245
                         * displayName : Jiang Ray
                         * name : {"formatted":"Jiang Ray","givenName":"Jiang","familyName":"Ray"}
                         * preferredUsername : jiang.ray
                         * profileUrl : https://www.facebook.com/app_scoped_user_id/1953844871516245/
                         * thumbnailUrl : https://graph.facebook.com/1953844871516245/picture?type=square
                         * pictureUrl : https://graph.facebook.com/1953844871516245/picture?type=large
                         * gender : male
                         * utcOffset : 8:00
                         * emails : [{"value":"176721325@qq.com","is_verified":true}]
                         * urls : [{"value":"https://www.facebook.com/app_scoped_user_id/1953844871516245/","type":"profile"}]
                         * accounts : [{"domain":"facebook.com","userid":"1953844871516245"}]
                         * photos : [{"value":"https://graph.facebook.com/1953844871516245/picture?type=square","size":"2:XS"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=small","size":"3:S"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=normal","size":"4:M"},{"value":"https://graph.facebook.com/1953844871516245/picture?type=large","size":"5:L"}]
                         * browser : {"agent":"okhttp/3.8.1","type":"okhttp","version":{"major":"3","full":"3.8.1"}}
                         */

                        private String identity_token;
                        private String date_creation;
                        private String date_last_update;
                        private String provider;
                        private String provider_identity_uid;
                        private SourceBean source;
                        private String id;
                        private String displayName;
                        private NameBean name;
                        private String preferredUsername;
                        private String profileUrl;
                        private String thumbnailUrl;
                        private String pictureUrl;
                        private String gender;
                        private String utcOffset;
                        private BrowserBean browser;
                        private List<EmailsBean> emails;
                        private List<UrlsBean> urls;
                        private List<AccountsBean> accounts;
                        private List<PhotosBean> photos;

                        public String getIdentity_token() {
                            return identity_token;
                        }

                        public void setIdentity_token(String identity_token) {
                            this.identity_token = identity_token;
                        }

                        public String getDate_creation() {
                            return date_creation;
                        }

                        public void setDate_creation(String date_creation) {
                            this.date_creation = date_creation;
                        }

                        public String getDate_last_update() {
                            return date_last_update;
                        }

                        public void setDate_last_update(String date_last_update) {
                            this.date_last_update = date_last_update;
                        }

                        public String getProvider() {
                            return provider;
                        }

                        public void setProvider(String provider) {
                            this.provider = provider;
                        }

                        public String getProvider_identity_uid() {
                            return provider_identity_uid;
                        }

                        public void setProvider_identity_uid(String provider_identity_uid) {
                            this.provider_identity_uid = provider_identity_uid;
                        }

                        public SourceBean getSource() {
                            return source;
                        }

                        public void setSource(SourceBean source) {
                            this.source = source;
                        }

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
                        }

                        public String getDisplayName() {
                            return displayName;
                        }

                        public void setDisplayName(String displayName) {
                            this.displayName = displayName;
                        }

                        public NameBean getName() {
                            return name;
                        }

                        public void setName(NameBean name) {
                            this.name = name;
                        }

                        public String getPreferredUsername() {
                            return preferredUsername;
                        }

                        public void setPreferredUsername(String preferredUsername) {
                            this.preferredUsername = preferredUsername;
                        }

                        public String getProfileUrl() {
                            return profileUrl;
                        }

                        public void setProfileUrl(String profileUrl) {
                            this.profileUrl = profileUrl;
                        }

                        public String getThumbnailUrl() {
                            return thumbnailUrl;
                        }

                        public void setThumbnailUrl(String thumbnailUrl) {
                            this.thumbnailUrl = thumbnailUrl;
                        }

                        public String getPictureUrl() {
                            return pictureUrl;
                        }

                        public void setPictureUrl(String pictureUrl) {
                            this.pictureUrl = pictureUrl;
                        }

                        public String getGender() {
                            return gender;
                        }

                        public void setGender(String gender) {
                            this.gender = gender;
                        }

                        public String getUtcOffset() {
                            return utcOffset;
                        }

                        public void setUtcOffset(String utcOffset) {
                            this.utcOffset = utcOffset;
                        }

                        public BrowserBean getBrowser() {
                            return browser;
                        }

                        public void setBrowser(BrowserBean browser) {
                            this.browser = browser;
                        }

                        public List<EmailsBean> getEmails() {
                            return emails;
                        }

                        public void setEmails(List<EmailsBean> emails) {
                            this.emails = emails;
                        }

                        public List<UrlsBean> getUrls() {
                            return urls;
                        }

                        public void setUrls(List<UrlsBean> urls) {
                            this.urls = urls;
                        }

                        public List<AccountsBean> getAccounts() {
                            return accounts;
                        }

                        public void setAccounts(List<AccountsBean> accounts) {
                            this.accounts = accounts;
                        }

                        public List<PhotosBean> getPhotos() {
                            return photos;
                        }

                        public void setPhotos(List<PhotosBean> photos) {
                            this.photos = photos;
                        }

                        public static class SourceBean {
                            /**
                             * name : Facebook
                             * key : facebook
                             * access_token : {"key":"EAASSzzmImkABAA77a9QnxQoPREXaYVAzdFZB87I6QvZAxyXQHuIrS2ZCL9MixZClZAPIjXsW1StZB4YLf1an1NSAkxPeGXtQZAjzrfOZC2KyjgHfZCNH05HIaeFgLeubDEZAZBYE3S8rze4rkI5ZBQQLMgm3klDsQPHhVvO4ZAkCgmSgMXqGcnBZC8UJhmCMaxhBybIHC4MGCzFJwASRXyyExVVRyb","date_expiration":"05/20/2065 16:34:23"}
                             */

                            private String name;
                            private String key;
                            private AccessTokenBean access_token;

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
                            }

                            public String getKey() {
                                return key;
                            }

                            public void setKey(String key) {
                                this.key = key;
                            }

                            public AccessTokenBean getAccess_token() {
                                return access_token;
                            }

                            public void setAccess_token(AccessTokenBean access_token) {
                                this.access_token = access_token;
                            }

                            public static class AccessTokenBean {
                                /**
                                 * key : EAASSzzmImkABAA77a9QnxQoPREXaYVAzdFZB87I6QvZAxyXQHuIrS2ZCL9MixZClZAPIjXsW1StZB4YLf1an1NSAkxPeGXtQZAjzrfOZC2KyjgHfZCNH05HIaeFgLeubDEZAZBYE3S8rze4rkI5ZBQQLMgm3klDsQPHhVvO4ZAkCgmSgMXqGcnBZC8UJhmCMaxhBybIHC4MGCzFJwASRXyyExVVRyb
                                 * date_expiration : 05/20/2065 16:34:23
                                 */

                                private String key;
                                private String date_expiration;

                                public String getKey() {
                                    return key;
                                }

                                public void setKey(String key) {
                                    this.key = key;
                                }

                                public String getDate_expiration() {
                                    return date_expiration;
                                }

                                public void setDate_expiration(String date_expiration) {
                                    this.date_expiration = date_expiration;
                                }
                            }
                        }

                        public static class NameBean {
                            /**
                             * formatted : Jiang Ray
                             * givenName : Jiang
                             * familyName : Ray
                             */

                            private String formatted;
                            private String givenName;
                            private String familyName;

                            public String getFormatted() {
                                return formatted;
                            }

                            public void setFormatted(String formatted) {
                                this.formatted = formatted;
                            }

                            public String getGivenName() {
                                return givenName;
                            }

                            public void setGivenName(String givenName) {
                                this.givenName = givenName;
                            }

                            public String getFamilyName() {
                                return familyName;
                            }

                            public void setFamilyName(String familyName) {
                                this.familyName = familyName;
                            }
                        }

                        public static class BrowserBean {
                            /**
                             * agent : okhttp/3.8.1
                             * type : okhttp
                             * version : {"major":"3","full":"3.8.1"}
                             */

                            private String agent;
                            private String type;
                            private VersionBean version;

                            public String getAgent() {
                                return agent;
                            }

                            public void setAgent(String agent) {
                                this.agent = agent;
                            }

                            public String getType() {
                                return type;
                            }

                            public void setType(String type) {
                                this.type = type;
                            }

                            public VersionBean getVersion() {
                                return version;
                            }

                            public void setVersion(VersionBean version) {
                                this.version = version;
                            }

                            public static class VersionBean {
                                /**
                                 * major : 3
                                 * full : 3.8.1
                                 */

                                private String major;
                                private String full;

                                public String getMajor() {
                                    return major;
                                }

                                public void setMajor(String major) {
                                    this.major = major;
                                }

                                public String getFull() {
                                    return full;
                                }

                                public void setFull(String full) {
                                    this.full = full;
                                }
                            }
                        }

                        public static class EmailsBean {
                            /**
                             * value : 176721325@qq.com
                             * is_verified : true
                             */

                            private String value;
                            private boolean is_verified;

                            public String getValue() {
                                return value;
                            }

                            public void setValue(String value) {
                                this.value = value;
                            }

                            public boolean isIs_verified() {
                                return is_verified;
                            }

                            public void setIs_verified(boolean is_verified) {
                                this.is_verified = is_verified;
                            }
                        }

                        public static class UrlsBean {
                            /**
                             * value : https://www.facebook.com/app_scoped_user_id/1953844871516245/
                             * type : profile
                             */

                            private String value;
                            private String type;

                            public String getValue() {
                                return value;
                            }

                            public void setValue(String value) {
                                this.value = value;
                            }

                            public String getType() {
                                return type;
                            }

                            public void setType(String type) {
                                this.type = type;
                            }
                        }

                        public static class AccountsBean {
                            /**
                             * domain : facebook.com
                             * userid : 1953844871516245
                             */

                            private String domain;
                            private String userid;

                            public String getDomain() {
                                return domain;
                            }

                            public void setDomain(String domain) {
                                this.domain = domain;
                            }

                            public String getUserid() {
                                return userid;
                            }

                            public void setUserid(String userid) {
                                this.userid = userid;
                            }
                        }

                        public static class PhotosBean {
                            /**
                             * value : https://graph.facebook.com/1953844871516245/picture?type=square
                             * size : 2:XS
                             */

                            private String value;
                            private String size;

                            public String getValue() {
                                return value;
                            }

                            public void setValue(String value) {
                                this.value = value;
                            }

                            public String getSize() {
                                return size;
                            }

                            public void setSize(String size) {
                                this.size = size;
                            }
                        }
                    }

                    public static class IdentitiesBean {
                        /**
                         * identity_token : 84d5cba7-f560-4602-bb54-d648f09a4e79
                         * provider : facebook
                         */

                        private String identity_token;
                        private String provider;

                        public String getIdentity_token() {
                            return identity_token;
                        }

                        public void setIdentity_token(String identity_token) {
                            this.identity_token = identity_token;
                        }

                        public String getProvider() {
                            return provider;
                        }

                        public void setProvider(String provider) {
                            this.provider = provider;
                        }
                    }
                }
            }
        }
    }
}
