package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class User implements Serializable {
    public class Identity implements Serializable{

        public class Name implements Serializable {
            public String formatted;

            public String getFormatted() {
                return formatted;
            }

            public void setFormatted(String formatted) {
                this.formatted = formatted;
            }
        }

        public class IdentityUrl implements Serializable {
            public String value;
            public String type;

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

        public class Account implements Serializable {
            public String domain;
            public String userId;
            public String username;

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }

        public class Photo implements Serializable {
            public String value;
            public String type;
            public String size;

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

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }
        }

        public class Email implements Serializable {
            public String value;
            public Boolean isVerified;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public Boolean getVerified() {
                return isVerified;
            }

            public void setVerified(Boolean verified) {
                isVerified = verified;
            }
        }

        public String identityToken;

        public String provider;

        public String id;


        public String displayName;

        public Name name;


        public String preferredUsername;

        public String thumbnailUrl;


        public String pictureUrl;


        public String profileUrl;

        public String gender;

        public String birthday;


        public String utcOffset;

        public List<Email> emails;

        public List<IdentityUrl> urls;

        public List<Account> accounts;

        public List<Photo> photos;

        public String getIdentityToken() {
            return identityToken;
        }

        public void setIdentityToken(String identityToken) {
            this.identityToken = identityToken;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
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

        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public String getPreferredUsername() {
            return preferredUsername;
        }

        public void setPreferredUsername(String preferredUsername) {
            this.preferredUsername = preferredUsername;
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

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getUtcOffset() {
            return utcOffset;
        }

        public void setUtcOffset(String utcOffset) {
            this.utcOffset = utcOffset;
        }

        public List<Email> getEmails() {
            return emails;
        }

        public void setEmails(List<Email> emails) {
            this.emails = emails;
        }

        public List<IdentityUrl> getUrls() {
            return urls;
        }

        public void setUrls(List<IdentityUrl> urls) {
            this.urls = urls;
        }

        public List<Account> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<Account> accounts) {
            this.accounts = accounts;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }
    }
    public class PublishToken implements Serializable {
        public String key;

        public String date_creation;

        public String date_expiration;

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
    public String uuid;
    public String userToken;
    public PublishToken publishToken;
    public Identity identity;
    public Collection<Identity> identies;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public PublishToken getPublishToken() {
        return publishToken;
    }

    public void setPublishToken(PublishToken publishToken) {
        this.publishToken = publishToken;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public Collection<Identity> getIdenties() {
        return identies;
    }

    public void setIdenties(Collection<Identity> identies) {
        this.identies = identies;
    }
}