package com.whitelabel.app.widget.daimajia_swipe.interfaces;



import com.whitelabel.app.widget.daimajia_swipe.SwipeLayout;
import com.whitelabel.app.widget.daimajia_swipe.util.Attributes;

import java.util.List;

public interface SwipeItemMangerInterface {

    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(SwipeLayout layout);
    
    void closeAllItems();

    List<Integer> getOpenItems();

    List<SwipeLayout> getOpenLayouts();

    void removeShownLayouts(SwipeLayout layout);

    boolean isOpen(int position);

    Attributes.Mode getMode();

    void setMode(Attributes.Mode mode);
}
