package com.whitelabel.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.callback.ShoppingCartAdapterCallback;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.KeyValueBean;
import com.whitelabel.app.model.ShoppingCartDeleteCellEntity;
import com.whitelabel.app.model.ShoppingCartListEntityCell;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductOptionEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/27.
 */
public class ShoppingCartAdapterV2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public LinkedList<ShoppingCartListEntityCell> list;
    private Context context;
    private Dialog mDialog;
    private ShoppingCartAdapterCallback callback;
    private ShoppingCarDao mShoppingCartDao;
    private final static String TAG = "ShoppingCartAdapterV2";
    private final ImageLoader mImageLoader;

    private AdapterView.OnItemClickListener itemOnClickListener;

    public void setItemOnClickListener(AdapterView.OnItemClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public ShoppingCartAdapterV2(Context context, LinkedList<ShoppingCartListEntityCell> products, ImageLoader imageLoader, ShoppingCartAdapterCallback callback) {
        super();
        JLogUtils.d(TAG, "init");
        this.list = new LinkedList<>();
        this.context = context;
        this.callback = callback;
        this.list = products;
        DataHandler dataHandler = new DataHandler(context, this);
        mShoppingCartDao = new ShoppingCarDao("ShoppingCartAdapterV2", dataHandler);
        mImageLoader = imageLoader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_shopping_cart_cell, null);
        return new ViewHolder(view);
    }

    public void setData(LinkedList<ShoppingCartListEntityCell> list) {
        this.list = list;
    }

    public ShoppingCartListEntityCell getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(list.get(position).getId());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ShoppingCartListEntityCell sc = (ShoppingCartListEntityCell) getItem(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        if (position == 0) {
            viewHolder.leftSpace.setVisibility(View.VISIBLE);
        } else {
            viewHolder.leftSpace.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(sc.getBrand())) {
            viewHolder.tvBland.setText(sc.getBrand().toUpperCase());
        } else {
            viewHolder.tvBland.setText("");
        }
        //if ("1".equals(sc.getInStock())&&!"0".equals(sc.getAvailability())) {
        if ("1".equals(sc.getInStock())) {
            viewHolder.llOutOfStock.setVisibility(View.GONE);
            viewHolder.inStock.setVisibility(View.VISIBLE);
        } else {
            viewHolder.llOutOfStock.setVisibility(View.VISIBLE);
            viewHolder.inStock.setVisibility(View.GONE);
        }

        //plus or minus Qty
        //SubString price ,delete "RM " three letter and calculate singlePrice

        viewHolder.tvCountSub.setOnClickListener(subtractListener);
        viewHolder.tvCountSub.setTag(position);
        viewHolder.tvCountPlus.setOnClickListener(addListener);
        viewHolder.tvCountPlus.setTag(position);

        viewHolder.ivCancel.setTag(position);
        viewHolder.ivCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(viewHolder.ivCancel.getWindowToken(), 0);
                ShoppingCartListEntityCell bean = list.get(position);
                if (!WhiteLabelApplication.getAppConfiguration().isSignIn(context)) {
                    list.remove(position);
                    calculationToatalPriceAndNum(list);
                    ShoppingCartAdapterV2.this.notifyDataSetChanged();
                    JStorageUtils.savaProductListToLocalCartRepository(context, shoppingCarToTMPLocal(list));
                } else {
                    sendRequestToDeteleteCell(position, bean.getId());
                }
                gaTrackerDeleteFromCart(bean.getName(), bean.getProductId());
            }
        });

        // Availability
        if (!TextUtils.isEmpty(sc.getAvailability())) {
            if ("1".equals(sc.getAvailability())) {
                viewHolder.unavailable.setVisibility(View.GONE);
                viewHolder.shopCartTrans.setVisibility(View.GONE);
                viewHolder.tvCountSub.setClickable(true);
                viewHolder.tvCountPlus.setClickable(true);
            } else {
                viewHolder.unavailable.setVisibility(View.VISIBLE);
                viewHolder.shopCartTrans.setVisibility(View.VISIBLE);
                viewHolder.tvCountSub.setClickable(false);
                viewHolder.tvCountPlus.setClickable(false);

            }
        } else {
            viewHolder.unavailable.setVisibility(View.GONE);
            viewHolder.shopCartTrans.setVisibility(View.GONE);
        }

        try {
            if (Double.parseDouble(sc.getPrice()) == Double.parseDouble(sc.getFinalPrice())) {
                viewHolder.tvPrice.setText("");
            } else {
                viewHolder.tvPrice.setVisibility(View.VISIBLE);
                viewHolder.tvPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + JDataUtils.formatDouble(sc.getPrice()));
            }
            viewHolder.tvFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + JDataUtils.formatDouble(sc.getFinalPrice()));

        } catch (Exception ex) {
            ex.getStackTrace();
        }
        if (!TextUtils.isEmpty(sc.getVendorDisplayName())) {
            viewHolder.tvCheckMername.setText(viewHolder.tvCheckMername.getContext().getResources().getString(R.string.soldby) + " " + sc.getVendorDisplayName());
        } else {
            viewHolder.tvCheckMername.setText("");
        }

        viewHolder.tvProductName.setText(sc.getName());
        viewHolder.tvCategory.setText(sc.getCategory());
        viewHolder.tvCount.setText(sc.getQty());
        //colors and size
        ArrayList<HashMap<String, String>> colorAndSizes = sc.getOptions();
        viewHolder.tvColor.setText("");
        viewHolder.tvSize.setText("");
        if (colorAndSizes != null && colorAndSizes.size() > 0) {
            if (1 == colorAndSizes.size()) {
                HashMap<String, String> map = colorAndSizes.get(0);
                if (map.get("Color") != null) {
                    viewHolder.tvColor.setVisibility(View.VISIBLE);
                    viewHolder.tvSize.setVisibility(View.INVISIBLE);
                    viewHolder.tvColor.setText(map.get("Color"));
                } else {
                    viewHolder.tvSize.setVisibility(View.VISIBLE);
                    viewHolder.tvColor.setVisibility(View.INVISIBLE);
                    viewHolder.tvSize.setText(map.get("Size"));
                }
            } else {
                viewHolder.tvColor.setVisibility(View.VISIBLE);
                viewHolder.tvSize.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(colorAndSizes.get(0).get("Color"))) {
                    viewHolder.tvColor.setText(colorAndSizes.get(0).get("Color") + " | ");
                } else if (!TextUtils.isEmpty(colorAndSizes.get(1).get("Color"))) {
                    viewHolder.tvColor.setText(colorAndSizes.get(1).get("Color") + " | ");
                }
                if (!TextUtils.isEmpty(colorAndSizes.get(0).get("Size"))) {
                    viewHolder.tvSize.setText(colorAndSizes.get(0).get("Size"));
                } else if (!TextUtils.isEmpty(colorAndSizes.get(1).get("Size"))) {
                    viewHolder.tvSize.setText(colorAndSizes.get(1).get("Size"));
                }
            }
        } else {
            viewHolder.tvColor.setVisibility(View.INVISIBLE);
            viewHolder.tvSize.setVisibility(View.INVISIBLE);
        }
        // Image download
        try {
            if (!sc.getImage().equals(viewHolder.imageView.getTag())) {
                viewHolder.imageView.setTag(sc.getImage());
                JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, viewHolder.imageView, sc.getImage(), JToolUtils.dip2px(context, 150), JToolUtils.dip2px(context, 150));
            }
        } catch (Exception ex) {
            viewHolder.imageView.setImageBitmap(null);
        }
        if (itemOnClickListener != null) {
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickListener.onItemClick(null, null, position, 0);
                }
            });
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        View leftSpace, view;
        LinearLayout llOutOfStock;
        TextView tvOutOfStock;
        TextView inStock;
        TextView unavailable;
        TextView tvBland;
        TextView tvCategory;
        TextView tvProductName;
        TextView tvColor;
        TextView tvSize;
        TextView tvPrice;
        TextView tvFinalPrice;
        ImageView imageView;
        TextView tvCount;
        ImageView tvCountSub;
        ImageView tvCountPlus, ivCancel;
        TextView tvCheckMername;
        TextView shopCartTrans;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            leftSpace = view.findViewById(R.id.tv_shoppingcart_cell_left_placeholder);
            llOutOfStock = (LinearLayout) view.findViewById(R.id.ll_shoppingcart_cell_outofstock);
            tvOutOfStock = (TextView) view.findViewById(R.id.tv_shoppingcart_cell_outofstock);
            inStock = (TextView) view.findViewById(R.id.tv_shoppingcart_cell_instock);
            unavailable = (TextView) view.findViewById(R.id.unavailable);
            tvBland = (TextView) view.findViewById(R.id.tv_bland);
            tvCategory = (TextView) view.findViewById(R.id.tv_sc_cell_category);
            tvProductName = (TextView) view.findViewById(R.id.tv_sc_cell_productname);
            tvColor = (TextView) view.findViewById(R.id.tv_sc_cell_color);
            tvSize = (TextView) view.findViewById(R.id.tv_sc_cell_size);
            tvPrice = (TextView) view.findViewById(R.id.tv_sc_cell_price);
            tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvFinalPrice = (TextView) view.findViewById(R.id.tv_sc_cell_finalprice);
            imageView = (ImageView) view.findViewById(R.id.iv_shopping_cart_cell);
            tvCount = (TextView) view.findViewById(R.id.tv_shoppingcart_cell_count);
            tvCountSub = (ImageView) view.findViewById(R.id.tv_shoppingcart_count_sub);
            tvCountPlus = (ImageView) view.findViewById(R.id.tv_shoppingcart_count_plus);
            ivCancel = (ImageView) view.findViewById(R.id.iv_shopping_cart_cell_cancel);
            tvCheckMername = (TextView) view.findViewById(R.id.tv_check_mername);
            shopCartTrans = (TextView) view.findViewById(R.id.tv_shopping_trans);
        }
    }
//   //SwipeMenuListView调用删除
//    public void swipeMenuDelete(int position,View bodyView){
//        this.bodyView=bodyView;
//        ShoppingCartListEntityCell bean = list.get(position);
//        if (!WhiteLabelApplication.getAppConfiguration().isSignIn(context)) {
//            list.remove(position);
//            calculationToatalPriceAndNum(list);
//            ShoppingCartAdapterV2.this.notifyDataSetChanged();
//            JStorageUtils.savaProductListToLocalCartRepository(context, shoppingCarToTMPLocal(list));
//            if(list.size()==0){
//                bodyView.setVisibility(View.GONE);
//            }
//        }else{
//            sendRequestToDeteleteCell(position,bean.getId());
//        }
//        gaTrackerDeleteFromCart(bean.getName(),bean.getProductId());
//    }

    public void gaTrackerDeleteFromCart(String name, String productId) {
        try {
            GaTrackHelper.getInstance().googleAnalyticsEvent("Cart Action",
                    "Remove Item From Cart",
                    name,
                    Long.valueOf(productId));
            JLogUtils.i("googleGA", "Remove Item From Cart");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gaTrackerIncresaseQuantity(String name, String productId) {
        try {
            GaTrackHelper.getInstance().googleAnalyticsEvent("Cart Action",
                    "Remove Item From Cart",
                    name,
                    Long.valueOf(productId));
            GaTrackHelper.getInstance().googleAnalyticsAddCart( productId, name);
            GaTrackHelper.getInstance().googleAnalyticsDeleteCart(context, productId, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<TMPLocalCartRepositoryProductEntity> shoppingCarToTMPLocal(LinkedList<ShoppingCartListEntityCell> list) {
        List<TMPLocalCartRepositoryProductEntity> beans = new ArrayList<TMPLocalCartRepositoryProductEntity>();
        for (int i = 0; i < list.size(); i++) {
            TMPLocalCartRepositoryProductEntity bean = new TMPLocalCartRepositoryProductEntity();
            ShoppingCartListEntityCell cell = list.get(i);
            ArrayList<TMPLocalCartRepositoryProductOptionEntity> options = new ArrayList<TMPLocalCartRepositoryProductOptionEntity>();
            TMPLocalCartRepositoryProductOptionEntity option = new TMPLocalCartRepositoryProductOptionEntity();
            TMPLocalCartRepositoryProductOptionEntity option1 = new TMPLocalCartRepositoryProductOptionEntity();
            options.add(option);
            options.add(option1);
            bean.setProductId(cell.getProductId());
            bean.setPrice(cell.getPrice());
            bean.setName(cell.getName());
            try {
                bean.setInStock(Integer.parseInt(cell.getInStock()));
            } catch (Exception ex) {
                ex.getStackTrace();
            }
            List<HashMap<String, String>> hashs = cell.getOptions();
            HashMap<String, String> map1 = hashs.get(0);
            HashMap<String, String> map2 = hashs.get(1);
            option.setId(map1.get("ColorId"));
            option.setLabel(map1.get("Color"));
            option.setSuperAttribute(map1.get("ColorKey"));
            option1.setId(map2.get("SizeId"));
            option1.setLabel(map2.get("Size"));
            option1.setSuperAttribute(map2.get("SizeKey"));
            // 0 -> Color, 1 -> Size
            bean.setImage(cell.getImage());
            bean.setFinalPrice(cell.getFinalPrice());
            bean.setBrand(cell.getBrand());
            bean.setCategory(cell.getCategory());
            bean.setOptions(options);
            try {
                bean.setQty(Integer.parseInt(cell.getMaxQty()));
            } catch (Exception ex) {
                ex.getStackTrace();
            }
            try {
                bean.setSelectedQty(Integer.parseInt(cell.getQty()));
            } catch (Exception ex) {
                ex.getStackTrace();
            }
            beans.add(bean);
        }
        return beans;
    }

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            ShoppingCartListEntityCell shoppingCart = list.get(position);
            if (!WhiteLabelApplication.getAppConfiguration().isSignIn(context)) {
                if (Integer.parseInt(shoppingCart.getQty()) + 1 <= Integer.parseInt(shoppingCart.getMaxQty())) {
                    final int newCount = Integer.parseInt(shoppingCart.getQty()) + 1;
                    shoppingCart.setQty(newCount + "");
                    calculationToatalPriceAndNum(list);
                    JStorageUtils.savaProductListToLocalCartRepository(context, shoppingCarToTMPLocal(list));
                    gaTrackerIncresaseQuantity(shoppingCart.getName(), shoppingCart.getProductId());
                } else {
                    //JViewUtils.showWhiteToast(context,context.getString(R.string.insufficient_stock));
                    JViewUtils.showMaterialDialog(context, null, context.getResources().getString(R.string.insufficient_stock), null);
                }
                ShoppingCartAdapterV2.this.notifyDataSetChanged();
            } else {
                gaTrackerIncresaseQuantity(shoppingCart.getName(), shoppingCart.getProductId());
                final int newCount = Integer.parseInt(shoppingCart.getQty()) + 1;
                shoppingCart.setQty(newCount + "");
                sendRequestToChangeCount(position, shoppingCart.getId(), newCount, "opt-plus");
            }
        }
    };
    private View.OnClickListener subtractListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            ShoppingCartListEntityCell shoppingCart = list.get(position);
            int count = Integer.parseInt(shoppingCart.getQty());
            int newCount = count - 1;
            if (newCount <= 0) {
                return;
            } else {
                shoppingCart.setQty(newCount + "");
                if (!WhiteLabelApplication.getAppConfiguration().isSignIn(context)) {
                    if (newCount > Integer.parseInt(shoppingCart.getMaxQty())) {
                        shoppingCart.setInStock("0");
                    } else {
                        shoppingCart.setInStock("1");
                    }
                    JStorageUtils.savaProductListToLocalCartRepository(context, shoppingCarToTMPLocal(list));
                    calculationToatalPriceAndNum(list);
//                    if(list.size()==0){
//                        bodyView.setVisibility(View.GONE);
//                    }
                    ShoppingCartAdapterV2.this.notifyDataSetChanged();
                } else {
                    sendRequestToChangeCount(position, shoppingCart.getId(), newCount, "opt-sub");
                }
            }
        }
    };

    public void calculationToatalPriceAndNum(LinkedList<ShoppingCartListEntityCell> beans) {
        double total = 0;
        int qty = 0;
        for (int i = 0; i < beans.size(); i++) {
            ShoppingCartListEntityCell bean = beans.get(i);
            int num = Integer.parseInt(bean.getQty());
            qty += num;
            double price = Double.parseDouble(bean.getFinalPrice());
            total += (price * num);
        }
        callback.updateShoppingData(qty, total + "", total + "");
//            if (qty == 1) {
//                shoppingCartActivity.tvCheckout.setText(context.getResources().getString(R.string.check_out_num_product).replace("{num}", qty + ""));
//            } else {
//                shoppingCartActivity.tvCheckout.setText(context.getResources().getString(R.string.check_out_num_products).replace("{num}", qty + ""));
//            }
//
//            shoppingCartActivity.tvSubtotal.setText("RM " + JDataUtils.formatDoubleWithSpecifiedDecimals(total));
//            shoppingCartActivity.tvGrandTotal.setText("RM " + JDataUtils.formatDoubleWithSpecifiedDecimals(total));
    }

    public void closeDialog() {
        if (mDialog != null) {
            mDialog.cancel();
        }
    }
    private static final class DataHandler extends Handler {
        private final WeakReference<ShoppingCartAdapterV2> mAdapter;
        private final WeakReference<Context> mContext;

        public DataHandler(Context context, ShoppingCartAdapterV2 shoppingCartAdapterCallback) {
            mAdapter = new WeakReference<ShoppingCartAdapterV2>(shoppingCartAdapterCallback);
            mContext = new WeakReference<Context>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            if (mAdapter.get() == null || mContext.get() == null) {
                return;
            }
            switch (msg.what) {
                case ShoppingCarDao.REQUEST_CHANGESHOPPINGCARCOUNT:
                    mAdapter.get().closeDialog();
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        final ShoppingCartDeleteCellEntity shoppingCartUpdateCellCount = (ShoppingCartDeleteCellEntity) msg.obj;
                        //success
                        if (shoppingCartUpdateCellCount != null && shoppingCartUpdateCellCount.getStatus() == 1) {
                            int position = Integer.parseInt(shoppingCartUpdateCellCount.getParams().getKey());
                            ShoppingCartListEntityCell shoppingCart = mAdapter.get().list.get(position);
                            if ("1".equals(shoppingCartUpdateCellCount.getInStock())) {
                                shoppingCart.setInStock("1");
                            } else {
                                int oldQty = Integer.parseInt(shoppingCart.getOldQty());
                                if (oldQty < shoppingCartUpdateCellCount.getSummaryQty()) {
                                    shoppingCart.setInStock("0");
                                    JViewUtils.showMaterialDialog(mContext.get(), null, mContext.get().getString(R.string.insufficient_stock), null);
                                }
                            }
                            mAdapter.get().callback.updateShoppingData(shoppingCartUpdateCellCount);
                            mAdapter.get().notifyDataSetChanged();
                        }
                    } else {
                        ErrorMsgBean errorMsg = (ErrorMsgBean) msg.obj;
                        KeyValueBean keyValueBean = (KeyValueBean) errorMsg.getParams();
                        int position = Integer.parseInt(keyValueBean.getKey());
                        String option = keyValueBean.getValue();
                        if (!JToolUtils.expireHandler((Activity) mContext.get(), errorMsg.getErrorMessage(), 2000)) {
                            ShoppingCartListEntityCell shoppingCart = mAdapter.get().list.get(position);
                            int qty = Integer.parseInt(shoppingCart.getQty());
                            if ("opt-plus".equals(option)) {
                                shoppingCart.setQty((qty - 1) + "");
                            } else {
                                shoppingCart.setQty((qty + 1) + "");
                            }
                            mAdapter.get().notifyDataSetChanged();
                        }
                    }
                    break;
                case ShoppingCarDao.REQUEST_ERROR:
                    mAdapter.get().closeDialog();
                    if (msg.arg1 == ShoppingCarDao.REQUEST_CHANGESHOPPINGCARCOUNT) {
                        KeyValueBean keyValueBean = (KeyValueBean) msg.obj;
                        int position = Integer.parseInt(keyValueBean.getKey());
                        String option = keyValueBean.getValue();
                        ShoppingCartListEntityCell shoppingCart = mAdapter.get().list.get(position);
                        int qty = Integer.parseInt(shoppingCart.getQty());
                        if ("opt-plus".equals(option)) {
                            shoppingCart.setQty((qty - 1) + "");
                        } else {
                            shoppingCart.setQty((qty + 1) + "");
                        }
                        mAdapter.get().notifyDataSetChanged();
                    }
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mContext.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
                case ShoppingCarDao.REQUEST_DELETEFROMSHOPPINGCART:
                    mAdapter.get().closeDialog();
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        final ShoppingCartDeleteCellEntity shoppingCartDeleteCell = (ShoppingCartDeleteCellEntity) msg.obj;
                        int position = Integer.parseInt((String) shoppingCartDeleteCell.getParam());
                        if (shoppingCartDeleteCell != null && (shoppingCartDeleteCell.getStatus() == 1)) {//success
                            ShoppingCartListEntityCell cell = null;
                            try {
                                cell = mAdapter.get().list.get(position);
                                GaTrackHelper.getInstance().googleAnalyticsDeleteCart(mContext.get(), cell.getProductId(), cell.getName());
                            } catch (Exception ex) {
                                ex.getStackTrace();
                            }
                            try {
                                if (cell.getIsCampaignProduct() == 1) {
                                    shoppingCartDeleteCell.setIsCampaignProduct(true);
                                }
                                mAdapter.get().list.remove(position);
                                mAdapter.get().callback.updateShoppingData(shoppingCartDeleteCell);
                                mAdapter.get().notifyDataSetChanged();
//                                if (mAdapter.get().list.size() == 0) {
//                                    mAdapter.get().bodyView.setVisibility(View.GONE);
//                                }
                            } catch (Exception ex) {
                                ex.getStackTrace();
                            }
                        }
                    } else {
                        ErrorMsgBean msgBean = (ErrorMsgBean) msg.obj;
                        if (!JToolUtils.expireHandler((Activity) mContext.get(), msgBean.getErrorMessage(), 2000)) {
                            Toast.makeText(mContext.get(), msgBean.getErrorMessage() + "", Toast.LENGTH_LONG).show();
                        }
                    }

                    break;
            }
        }
    }

    /**
     * send Request To change count
     *
     * @param cellId
     * @param newCount
     */
    private void sendRequestToChangeCount(final int position, String cellId, final int newCount,
                                          final String option) {
        mDialog = JViewUtils.showProgressDialog(context);
        KeyValueBean bean = new KeyValueBean();
        bean.setKey(position + "");
        bean.setValue(option + "");
        mShoppingCartDao.requestChangeCount(WhiteLabelApplication.getAppConfiguration().getUserInfo(context).getSessionKey(), cellId, newCount + "", bean);

    }

    private void sendRequestToDeteleteCell(final int position, String itemId) {
        mDialog = JViewUtils.showProgressDialog(context);
        mShoppingCartDao.deleteProductFromShoppingCart(WhiteLabelApplication.getAppConfiguration().getUserInfo(context).getSessionKey(), itemId, position + "");
    }

}
