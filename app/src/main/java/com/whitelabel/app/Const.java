package com.whitelabel.app;

/**
 * Created by img on 2017/11/23.
 */

public class Const {
    //recyclerview diff data type
    public final static int HEADER = 1;
    public final static int TITLE = 2;
    public final static int ITEM = 3;
    public final static int FOOTER = 4;

    //banner 3:8
    public static final double BANNER_PIC_HEIGHT_THAN_WIDTH = 180.0/480;
    public static final int NORMAL_BANNER_DELAY_TIME=3000;

    public interface GA{
        public final static String HOME_SCREEN = " Category Landing Screen";
        public final static String CATEGORY_ALL_LIST_SCREEN = "Category list";
        public final static String ADD_ADDRESS_SCREEN = "Add Address Screen";
        public final static String EDIT_ADDRESS_SCREEN = "Edit Address Screen";
        public final static String ACCOUNT_WISHLIST_SCREEN = "My Wishlist Screen";
        public final static String ADDRESS_BOOK_SCREEN = "My Address Book Screen";
        public final static String HOME_BANNER_EVENT = "banner";

    }

}
