package com.whitelabel.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whitelabel.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by img on 2017/12/21.
 */

public class CustomerCareAdapter extends BaseQuickAdapter<String,BaseViewHolder> {


    public CustomerCareAdapter(List<String> datas) {
        super(R.layout.layout_customer_help_item,datas);
    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_customer_care_item,item);
    }
}
