package com.whitelabel.app.callback;

/**
 * Created by Administrator on 2016/3/30.
 */
public interface ShoppingCartCallback {
    public static int TYPE_BACK=1;
    public static int TYPE_MENU=2;
    public void leftButtonClick();
    public int getLeftButtonImage();
    public boolean isLeftMenuOpen();
    public int getLeftMenuType();
}
