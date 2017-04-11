package com.whitelabel.app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.ShoppingCartCampaignActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.ShoppingCartCampaignListEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/26.
 */
public class ShoppingCartCampaignAdapter extends BaseAdapter {

    private Context context;
    private ShoppingCartCampaignActivity activity;
    private ArrayList<ShoppingCartCampaignListEntity> list;
    private Dialog mDialog;
    private boolean flag = true;//escape resubmitting
    private ShoppingCarDao mShoppingCarDao;
    private final ImageLoader mImageLoader;

    public ShoppingCartCampaignAdapter(Context context, ArrayList<ShoppingCartCampaignListEntity> list, ImageLoader imageLoader) {
        this.context = context;
        this.activity = (ShoppingCartCampaignActivity) context;
        this.list = list;
        DataHandler dataHandler = new DataHandler(this, activity);
        mShoppingCarDao = new ShoppingCarDao("ShoppingCartCampaignAdapter", dataHandler);
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

        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_campaign_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ivCampaignImage);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.ctvCampaignProductName);
            viewHolder.by = (TextView) convertView.findViewById(R.id.ctvCampaignProductBy);
            viewHolder.oldPrice = (TextView) convertView.findViewById(R.id.ctvCampaignProductOldPrice);
            viewHolder.finalPrice = (TextView) convertView.findViewById(R.id.ctvCampaignProductFinalPrice);
            viewHolder.ctv_product_merchant = (TextView) convertView.findViewById(R.id.ctv_product_merchant);

            viewHolder.btnAddToCart = (TextView) convertView.findViewById(R.id.ctvCampaignProductAddToCart);
            viewHolder.imageViewSelected = (ImageView) convertView.findViewById(R.id.iv_campaign_item_selected);
            viewHolder.viewSelectedBg = (View) convertView.findViewById(R.id.iv_campaign_item_selected_bg);
            viewHolder.topSpace = (TextView) convertView.findViewById(R.id.campaign_cell_top_space);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ShoppingCartCampaignListEntity entity = list.get(position);

        viewHolder.productName.setText(entity.getName());
        viewHolder.by.setText(entity.getBrand());
        viewHolder.finalPrice.setText(context.getResources().getString(R.string.rm) + " " + entity.getFinalPrice());

        /**
         * we have set padding bottom space in the single item layout,
         * so we should deal with two special conditions position 0 and 1, just to show the top view space,
         */
        if (position == 0 || position == 1) {
            viewHolder.topSpace.setVisibility(View.VISIBLE);
        } else {
            viewHolder.topSpace.setVisibility(View.GONE);
        }

        /**
         * If final price == old price , then show final price only , else show two.
         */
        if (entity.getFinalPrice().equalsIgnoreCase(entity.getPrice())) {
            viewHolder.oldPrice.setText("A");
            viewHolder.oldPrice.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.oldPrice.setVisibility(View.VISIBLE);
            viewHolder.oldPrice.setText(context.getResources().getString(R.string.rm) + " " + entity.getPrice());
            viewHolder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }
        if (!TextUtils.isEmpty(entity.getVendorDisplayName())) {
            viewHolder.ctv_product_merchant.setText(viewHolder.ctv_product_merchant.getContext().getResources().getString(R.string.soldby) + " " + entity.getVendorDisplayName());
        } else {
            viewHolder.ctv_product_merchant.setText(" ");
        }

        //Get a square image
        JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, viewHolder.imageView, entity.getSmallImage(), JToolUtils.dip2px(context, 160), JToolUtils.dip2px(context, 160));

        /**
         * If user has selected a promotion product, make it highlight , else should deal with it as common.
         */
        if (entity.getProductId().equalsIgnoreCase(activity.getCampaignProductId())) {
            viewHolder.viewSelectedBg.setVisibility(View.VISIBLE);
            viewHolder.imageViewSelected.setVisibility(View.VISIBLE);
            viewHolder.btnAddToCart.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.viewSelectedBg.setVisibility(View.GONE);
            viewHolder.imageViewSelected.setVisibility(View.GONE);
            viewHolder.btnAddToCart.setVisibility(View.VISIBLE);
        }

        /**
         * If the product is out of stock,change add to cart button.
         */
        if (1 == entity.getInStock() && !"0".equals(entity.getAvailability())) {//In stock
            viewHolder.btnAddToCart.setText(context.getResources().getString(R.string.product_detail_addtocart));
            viewHolder.btnAddToCart.setBackground(WhiteLabelApplication.getInstance().getResources().getDrawable(R.drawable.big_button_style_config));
            viewHolder.btnAddToCart.setEnabled(true);
        } else {//Out of stock
            viewHolder.btnAddToCart.setText(context.getResources().getString(R.string.campaign_item_soldout));
            viewHolder.btnAddToCart.setBackground(WhiteLabelApplication.getInstance().getResources().getDrawable(R.drawable.big_button_style_b8));
            viewHolder.btnAddToCart.setEnabled(false);
        }

        /**
         * Add to cart
         */
        viewHolder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            private ShoppingCartCampaignListEntity p;

            public View.OnClickListener init(ShoppingCartCampaignListEntity p) {
                this.p = p;
                return this;
            }

            @Override
            public void onClick(View v) {
                if (flag) {
                    flag = false;
                    addToCart(p);
                }
            }
        }.init(entity));
        return convertView;
    }


    private static final class DataHandler extends Handler {
        private final WeakReference<ShoppingCartCampaignAdapter> mAdapter;
        private final WeakReference<ShoppingCartCampaignActivity> mActivity;

        public DataHandler(ShoppingCartCampaignAdapter adapter, ShoppingCartCampaignActivity activity) {
            mAdapter = new WeakReference<ShoppingCartCampaignAdapter>(adapter);
            mActivity = new WeakReference<ShoppingCartCampaignActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mAdapter.get() == null || mActivity == null) {
                return;
            }
            if (mAdapter.get().mDialog != null) {
                mAdapter.get().mDialog.cancel();
            }
            switch (msg.what) {
                case ShoppingCarDao.REQUEST_ADDCAMPAGINTOCART:

                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        mAdapter.get().flag = true;
//                        AddToCartEntity addToCartEntity = (AddToCartEntity) result;
//
//                        if (addToCartEntity.getStatus() == 1) {//success
//                            JLogUtils.i("Russell", "addToCartInCampaign -> onSuccess => resultCode=>" + resultCode);
//                        }
                        if (mAdapter.get().mDialog != null) {
                            mAdapter.get().mDialog.cancel();
                        }
                        mAdapter.get().showToast(mActivity.get());

//                Intent intent = new Intent();
//                intent.setClass(activity, ShoppingCartActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                activity.startActivity(intent);
                        mActivity.get().setResult(1000);
                        mActivity.get().finish();
                        mActivity.get().overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
                    } else {

                        mAdapter.get().flag = true;
                        String errorStr = (String) msg.obj;
                        if (!JToolUtils.expireHandler(mActivity.get(), errorStr, 1000)) {
                            Toast.makeText(mActivity.get(), errorStr, Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case ShoppingCarDao.REQUEST_ERROR:
                    mAdapter.get().flag = true;
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;

            }
            super.handleMessage(msg);
        }
    }


    /**
     * Only after user login, they can take part in promotion.
     *
     * @param entity
     */
    private void addToCart(ShoppingCartCampaignListEntity entity) {
        mDialog = JViewUtils.showProgressDialog(context);
        mShoppingCarDao.addCampaignProductToCart(WhiteLabelApplication.getAppConfiguration().getUserInfo(context).getSessionKey(), entity.getProductId());

//        SVRParameters parameters = new SVRParameters();
//        parameters.put("session_key", WhiteLabelApplication.getAppConfiguration().getUserInfo(context).getSessionKey());
//        parameters.put("product_id", entity.getProductId());
//
//        SVRAddToCartPromotion addToCartHandler = new SVRAddToCartPromotion(context, parameters);
//        addToCartHandler.loadDatasFromServer(new SVRCallback() {
//            private final int REQUESTCODE_LOGIN = 1000;
//            private final String SESSION_EXPIRED = "session expired,login again please";
//
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//
//                flag = true;
//                AddToCartEntity addToCartEntity = (AddToCartEntity) result;
//
//                if (addToCartEntity.getStatus() == 1) {//success
//                    JLogUtils.i("Russell", "addToCartInCampaign -> onSuccess => resultCode=>" + resultCode);
//                }
//                if (mDialog != null) {
//                    mDialog.cancel();
//                }
//                showToast(context);
//
////                Intent intent = new Intent();
////                intent.setClass(activity, ShoppingCartActivity.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
////                activity.startActivity(intent);
//                activity.setResult(1000);
//                activity.finish();
//                activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
//            }
//
//            @Override
//            public void onFailure(int resultCode, String errorMsg) {
//                flag = true;
//                JLogUtils.i("Russell", "addToCartInCampaign -> onFailure => resultCode=>" + resultCode + "  errorMsg=>" + errorMsg);
//                if (mDialog != null) {
//                    mDialog.cancel();
//                }
//                if ((!JDataUtils.isEmpty(errorMsg)) && (errorMsg.contains(SESSION_EXPIRED))) {
//                    Intent intent = new Intent();
//                    intent.putExtra("expire", true);
//                    intent.setClass(context, LoginRegisterActivity.class);
//                    activity.startActivityForResult(intent, REQUESTCODE_LOGIN);
//                    return;
//                }
//                if (!JDataUtils.errorMsgHandler(context, errorMsg)) {
//                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    private void showToast(Context context) {
        if (context == null) {
            return;
        }

        Toast toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        if (WhiteLabelApplication.getPhoneConfiguration() != null && WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() != 0) {
            toast.setGravity(Gravity.BOTTOM, 0, (int) (WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() * 0.25));
        }

        LinearLayout toastView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_prompt_productdetail_addtocart, null);
        ImageView ivPrompt = (ImageView) toastView.findViewById(R.id.ivPrompt);
        CustomTextView ctvPrompt = (CustomTextView) toastView.findViewById(R.id.ctvPrompt);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivPrompt.setImageDrawable(activity.getResources().getDrawable(R.mipmap.success, null));
        } else {
            ivPrompt.setImageDrawable(activity.getResources().getDrawable(R.mipmap.success));
        }
        ctvPrompt.setText(R.string.product_detail_prompy_addtocart_success);

        toast.setView(toastView);
        toast.show();
    }

    private class ViewHolder {
        private ImageView imageView, imageViewSelected;
        private TextView productName, by, oldPrice, finalPrice, btnAddToCart, topSpace, ctv_product_merchant;
        private View viewSelectedBg;
    }
}
