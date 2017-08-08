package com.whitelabel.app.model;

public class NativeLoginRequest {
    public class Request {
        public class User {
            public class Identity {
                public class Source {
                    public class AccessToken {
                        public String key;
                        public String secret;

                        public AccessToken(String accessToken, String secret) {
                            this.key = accessToken;
                            this.secret = secret;
                        }
                    }
                    public String key;
                    public AccessToken access_token;

                    public Source(String provider, String accessToken, String secret) {
                        this.key = provider;
                        this.access_token = new AccessToken(accessToken, secret);
                    }
                }
                public Source source;

                public Identity(String provider, String accessToken, String secret) {
                    this.source = new Source(provider, accessToken, secret);
                }
            }
            public String action = "import_from_access_token";
            public Identity identity;

            public User(String provider, String accessToken, String secret) {
                this.identity = new Identity(provider, accessToken, secret);
            }
        }
        public User user;

        public Request(String provider, String accessToken, String secret) {
            this.user = new User(provider, accessToken, secret);
        }
    }
    public Request request;

    public NativeLoginRequest(String provider, String accessToken, String secret) {
        this.request = new Request(provider, accessToken, secret);
    }
}