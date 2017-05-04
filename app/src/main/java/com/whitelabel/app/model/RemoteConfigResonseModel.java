package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2017/4/5.
 */

public class RemoteConfigResonseModel implements Serializable {

    /**
     * status : 1
     * message :
     * data : {"uiStyle":{"themeColor":"","navBarBackgroudColor":"","navBarTextIconColor":"","sideMenuColor":"","sldeMenuTextIconColor":"","sideMenuTextIconSelectedColor":""},"baseUrl":{"serviceBaseUrl":""},"version":100001}
     */

    private int code;
    private String message;
    private RetomeConfig data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RetomeConfig getData() {
        return data;
    }

    public void setData(RetomeConfig data) {
        this.data = data;
    }

    public static class RetomeConfig  implements Serializable{
        /**
         * uiStyle : {"themeColor":"","navBarBackgroudColor":"","navBarTextIconColor":"","sideMenuColor":"","sldeMenuTextIconColor":"","sideMenuTextIconSelectedColor":""}
         * baseUrl : {"serviceBaseUrl":""}
         * version : 100001
         */

        private ThemeConfigModel uiStyle;
        private LayoutStyleModel layoutStyle;
        private String version;
        private ThirdPartyConfig thirdParty;

        public LayoutStyleModel getLayoutStyle() {
            return layoutStyle;
        }

        public void setLayoutStyle(LayoutStyleModel layoutStyle) {
            this.layoutStyle = layoutStyle;
        }

        public ThirdPartyConfig getThirdParty() {
            return thirdParty;
        }
        public void setThirdParty(ThirdPartyConfig thirdParty) {
            this.thirdParty = thirdParty;
        }
        public ThemeConfigModel getUiStyle() {
            return uiStyle;
        }
        public void setUiStyle(ThemeConfigModel uiStyle) {
            this.uiStyle = uiStyle;
        }
        public String getVersion() {
            return version;
        }
        public void setVersion(String version) {
            this.version = version;
        }
        public static class UiStyleBean {
            /**
             * themeColor :
             * navBarBackgroudColor :
             * navBarTextIconColor :
             * sideMenuColor :
             * sldeMenuTextIconColor :
             * sideMenuTextIconSelectedColor :
             */
            private String buttonPressColor;
            private String themeColor;
            private String navBarBackgroudColor;
            private String navBarTextIconColor;
            private String sideMenuColor;
            private String sldeMenuTextIconColor;
            private String sideMenuTextIconSelectedColor;

            public String getThemeColor() {
                return themeColor;
            }

            public String getButtonPressColor() {
                return buttonPressColor;
            }

            public void setButtonPressColor(String buttonPressColor) {
                this.buttonPressColor = buttonPressColor;
            }

            public void setThemeColor(String themeColor) {
                this.themeColor = themeColor;
            }

            public String getNavBarBackgroudColor() {
                return navBarBackgroudColor;
            }

            public void setNavBarBackgroudColor(String navBarBackgroudColor) {
                this.navBarBackgroudColor = navBarBackgroudColor;
            }

            public String getNavBarTextIconColor() {
                return navBarTextIconColor;
            }

            public void setNavBarTextIconColor(String navBarTextIconColor) {
                this.navBarTextIconColor = navBarTextIconColor;
            }

            public String getSideMenuColor() {
                return sideMenuColor;
            }

            public void setSideMenuColor(String sideMenuColor) {
                this.sideMenuColor = sideMenuColor;
            }

            public String getSldeMenuTextIconColor() {
                return sldeMenuTextIconColor;
            }

            public void setSldeMenuTextIconColor(String sldeMenuTextIconColor) {
                this.sldeMenuTextIconColor = sldeMenuTextIconColor;
            }

            public String getSideMenuTextIconSelectedColor() {
                return sideMenuTextIconSelectedColor;
            }

            public void setSideMenuTextIconSelectedColor(String sideMenuTextIconSelectedColor) {
                this.sideMenuTextIconSelectedColor = sideMenuTextIconSelectedColor;
            }
        }

        public static class BaseUrlBean {
            /**
             * serviceBaseUrl :
             */

            private String serviceBaseUrl;

            public String getServiceBaseUrl() {
                return serviceBaseUrl;
            }

            public void setServiceBaseUrl(String serviceBaseUrl) {
                this.serviceBaseUrl = serviceBaseUrl;
            }
        }
    }
}
