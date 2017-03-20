package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/6.
 */
public class ShoppingCartListBase implements Serializable{


    public ShoppingCartListBase() {
        super();
    }
    public ShoppingCartListBase(int cellType) {
        this.cellType = cellType;
    }

    private int cellType=1;

    public int getCellType() {
        return cellType;
    }

    public void setCellType(int cellType) {
        this.cellType = cellType;
    }
}
