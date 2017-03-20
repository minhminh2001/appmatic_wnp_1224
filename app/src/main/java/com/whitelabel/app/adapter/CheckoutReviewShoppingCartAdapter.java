package com.whitelabel.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.ShoppingCartListEntityCell;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/6/11.
 */
public class CheckoutReviewShoppingCartAdapter extends BaseAdapter {
    private String currTag = "CheckoutReviewShoppingCartAdapter";
    public ArrayList<ShoppingCartListEntityCell> list;
    private Context context;
    private final ImageLoader mImageLoader;

    public CheckoutReviewShoppingCartAdapter(Context context, ImageLoader imageLoader) {
        this.list = new ArrayList<ShoppingCartListEntityCell>();
        this.context = context;
        mImageLoader = imageLoader;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_checkout_review_shopping_cart_cell, null);
        }

        ShoppingCartListEntityCell sc = (ShoppingCartListEntityCell) getItem(position);
        TextView ctv_revieworder_merchant = (TextView) convertView.findViewById(R.id.ctv_revieworder_merchant);
        TextView tvBrand = (TextView) convertView.findViewById(R.id.tv_checkout_review_shoppingcart_cell_brand);
        TextView tvProductName = (TextView) convertView.findViewById(R.id.tv_checkout_review_shoppingcart_cell_productname);
        TextView tvColorsAndSize = (TextView) convertView.findViewById(R.id.tv_checkout_review_shoppingcart_cell_colorandsize);
        TextView tvQuantity = (TextView) convertView.findViewById(R.id.tv_checkout_review_shoppingcart_cell_quantity);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_checkout_review_shoppingcart_cell_price);
        ImageView image = (ImageView) convertView.findViewById(R.id.image_checkout_review_shoppingcart_cell);

        tvBrand.setText(sc.getBrand() == null ? "BRAND" : sc.getBrand().toUpperCase());
        tvProductName.setText(sc.getName());
        tvQuantity.setText(sc.getQty());
        tvPrice.setText(sc.getPrice());
        if (!TextUtils.isEmpty(sc.getVendorDisplayName())) {
            ctv_revieworder_merchant.setText(context.getResources().getString(R.string.soldby) + " " + sc.getVendorDisplayName());
        } else {
            ctv_revieworder_merchant.setText("");
        }
        //colors and size
        ArrayList<HashMap<String, String>> colorAndSizes = sc.getOptions();
        if (colorAndSizes != null && colorAndSizes.size() > 0) {
            String attributeStr = "";
            if (colorAndSizes != null && colorAndSizes.size() > 0) {
                for (int z = 0; z < colorAndSizes.size(); z++) {
                    for (String key : colorAndSizes.get(z).keySet()) {
                        attributeStr += colorAndSizes.get(z).get(key) + " | ";
                    }
                }
                attributeStr = attributeStr.substring(0, attributeStr.length() - 2);
            }
            tvColorsAndSize.setVisibility(View.VISIBLE);
            tvColorsAndSize.setText(attributeStr);
        } else {
            tvColorsAndSize.setVisibility(View.INVISIBLE);
        }
        JLogUtils.i(currTag, "sc.getImage():" + sc.getImage());
        JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, image, sc.getImage(), JToolUtils.dip2px(context, 200), JToolUtils.dip2px(context, 200));

        return convertView;
    }
}

