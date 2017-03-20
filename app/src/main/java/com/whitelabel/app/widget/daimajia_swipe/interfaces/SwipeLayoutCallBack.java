package com.whitelabel.app.widget.daimajia_swipe.interfaces;

import com.whitelabel.app.widget.daimajia_swipe.SwipeLayout;

public interface SwipeLayoutCallBack {
        void swipeCallback(SwipeLayout v, int type);
        int type_add=0;
        int type_re=1;
        int type_reall=2;
}
