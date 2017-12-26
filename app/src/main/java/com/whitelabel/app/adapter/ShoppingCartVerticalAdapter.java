package com.whitelabel.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.callback.ShoppingCartAdapterCallback;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.KeyValueBean;
import com.whitelabel.app.model.ShoppingCartDeleteCellEntity;
import com.whitelabel.app.model.ShoppingCartListBase;
import com.whitelabel.app.model.ShoppingCartListEntityBody;
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
import com.whitelabel.app.widget.swipe.SwipeLayout;
import com.whitelabel.app.widget.swipe.SwipeLayoutCallBack;
import com.whitelabel.app.widget.swipe.SwipeableAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/1/27.
 */
public class ShoppingCartVerticalAdapter extends SwipeableAdapter {
    private static final int TYPE_CELL = 1;
    public static final int TYPE_BODY = 2;
    private ArrayList<ShoppingCartListBase> list;
    private final Context context;
    private Dialog mDialog;
    private final ShoppingCartAdapterCallback callback;
    private final ShoppingCarDao mShoppingCartDao;
    private final static String TAG = "ShoppingCartVerticalAdapter";
    private final ImageLoader mImageLoader;

    private AdapterView.OnItemClickListener itemOnClickListener;

    public void setItemOnClickListener(AdapterView.OnItemClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
    public ShoppingCartVerticalAdapter(Context context, ArrayList<ShoppingCartListBase> products, ImageLoader imageLoader, ShoppingCartAdapterCallback callback) {
        super();
        JLogUtils.d(TAG, "init");
        this.list = new ArrayList<>();
        this.context = context;
        this.callback = callback;
        this.list = products;
        DataHandler dataHandler = new DataHandler(context, this);
        mShoppingCartDao = new ShoppingCarDao("ShoppingCartAdapterV2", dataHandler);
        mImageLoader = imageLoader;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            //TYPE_CELL=1 为 product item
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.activity_shopping_cart_vertical_cell, null);
            return new ViewHolder(view);
        } else {
            //TYPE_BODY=2 为 subtotal， voucher等
            View view = callback.getInfoView();
            return new InfoViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //TYPE_CELL=1 为 product item
        //TYPE_BODY=2 为 subtotal， voucher等
        if (list.get(position) instanceof ShoppingCartListEntityBody) {
            return TYPE_BODY;
        }
        return TYPE_CELL;
    }

    public void setData(ArrayList<ShoppingCartListBase> list) {
        this.list = list;
    }

    private ShoppingCartListEntityCell getItem(int position) {
        return (ShoppingCartListEntityCell) list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof InfoViewHolder) {
            //如果不是 product item,则直接return,因其view已在fragment里加载完毕了。
            return;
        }
        final ShoppingCartListEntityCell sc = getItem(position);

        final ViewHolder viewHolder = (ViewHolder) holder;

        //if ("1".equals(sc.getInStock())&&!"0".equals(sc.getAvailability())) {
        if ("1".equals(sc.getInStock())) {
            viewHolder.tvOutOfStock.setVisibility(View.INVISIBLE);
            viewHolder.tvAllBarrier.setVisibility(View.INVISIBLE);
            viewHolder.tvAllBarrier2.setVisibility(View.INVISIBLE);
            if ("1".equals(sc.getHasError())){
                //have stock & have error message show error message
                viewHolder.llShoppingCartError.setVisibility(View.VISIBLE);
                viewHolder.ivShoppingCartIcon.setBackgroundResource(R.mipmap.ic_checkout_error_mark_item);
                ((ViewHolder) holder).tvShoppingCartErrorMsg.setTextColor(ContextCompat.getColor(context,R.color.red_cart_shopping_error));
                ((ViewHolder) holder).tvShoppingCartErrorMsg.setText(sc.getErrorMessage());
            }
        } else {
            viewHolder.tvOutOfStock.setVisibility(View.VISIBLE);
            viewHolder.llShoppingCartError.setVisibility(View.GONE);
        }
        viewHolder.tvProductBland.setText(sc.getBrand());
        viewHolder.tvProductBland.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startBrandStoreActivity((Activity) view.getContext(),sc.getBrand(),sc.getBrandId());
            }
        });
        //plus or minus Qty
        //SubString price ,delete "RM " three letter and calculate singlePrice
        viewHolder.swipeShoppingCart.setDragEdge(SwipeLayout.DragEdge.Right);
        viewHolder.swipeShoppingCart.setShowMode(SwipeLayout.ShowMode.PullOut);
        //用于管理所有item开闭状态的
        viewHolder.swipeShoppingCart.setCallBack(this);
        viewHolder.swipeShoppingCart.close();

        viewHolder.rlShoppingcartCountSub.setOnClickListener(subtractListener);
        viewHolder.rlShoppingcartCountSub.setTag(position);
        viewHolder.rlShoppingcartCountPlus.setOnClickListener(addListener);
        viewHolder.rlShoppingcartCountPlus.setTag(position);

        viewHolder.ivShoppingCartDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (callback.getSwipeRefreshStatus()) {
                    return;
                }
                viewHolder.swipeShoppingCart.close();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(viewHolder.itemView.getWindowToken(), 0);
                ShoppingCartListEntityCell bean = (ShoppingCartListEntityCell) list.get(position);
                if (!WhiteLabelApplication.getAppConfiguration().isSignIn(context)) {
                    list.remove(position);
                    calculationToatalPriceAndNum(list);
                    ShoppingCartVerticalAdapter.this.notifyDataSetChanged();
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
                viewHolder.unavailable.setVisibility(View.INVISIBLE);
                viewHolder.rlShoppingcartCountSub.setEnabled(true);
                viewHolder.rlShoppingcartCountPlus.setEnabled(true);
            } else {
                viewHolder.unavailable.setVisibility(View.VISIBLE);
                viewHolder.tvOutOfStock.setVisibility(View.INVISIBLE);
                viewHolder.rlShoppingcartCountSub.setEnabled(false);
                viewHolder.rlShoppingcartCountPlus.setEnabled(false);

            }
        } else {
            viewHolder.unavailable.setVisibility(View.INVISIBLE);
        }

        if (viewHolder.tvOutOfStock.getVisibility() == View.VISIBLE || viewHolder.unavailable.getVisibility() == View.VISIBLE) {

            //因相对布局中代码前后位置的原因，如果是unavailable， tvAllBarrier3是覆盖在加减按钮上的，
            //如果是oos,tvAllBarrier2只是覆盖在文字的
            if (viewHolder.unavailable.getVisibility() == View.VISIBLE) {
                viewHolder.tvAllBarrier2.setVisibility(View.VISIBLE);
                viewHolder.tvAllBarrier.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.tvAllBarrier.setVisibility(View.VISIBLE);
                viewHolder.tvAllBarrier2.setVisibility(View.INVISIBLE);
            }
        } else {
            viewHolder.tvAllBarrier.setVisibility(View.INVISIBLE);
            viewHolder.tvAllBarrier2.setVisibility(View.INVISIBLE);
        }
        try {
            if (Double.parseDouble(sc.getPrice()) == Double.parseDouble(sc.getFinalPrice())) {
                viewHolder.tvPrice.setText("");
                viewHolder.tvPrice.setVisibility(View.GONE);
            } else {
                viewHolder.tvPrice.setVisibility(View.VISIBLE);
                viewHolder.tvPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + JDataUtils.formatDouble(sc.getPrice()));
            }
            viewHolder.tvFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + JDataUtils.formatDouble(sc.getFinalPrice()));
        } catch (Exception ex) {
            ex.getStackTrace();
        }

        if (!TextUtils.isEmpty(sc.getVendorDisplayName())) {

            String soldBy = viewHolder.tvCheckMername.getContext().getResources().getString(R.string.soldby);
//            if (!TextUtils.isEmpty(sc.getVendor_id())) {
//                viewHolder.tvCheckMername.setTextColor(context.getResources().getColor(R.color.purple92018d));
//                SpannableStringBuilder ss = new SpannableStringBuilder(soldBy + " " + sc.getVendorDisplayName());
//                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.greyB8B8B8)), 0, soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                viewHolder.tvCheckMername.setText(ss);
//                if (!"0".equals(sc.getVendor_id())) {
//                    viewHolder.tvCheckMername.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, MerchantStoreFrontActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, sc.getVendor_id());
//                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, sc.getVendorDisplayName());
//                            intent.putExtras(bundle);
//                            context.startActivity(intent);
//                            ((Activity) context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//                        }
//                    });
//                } else {
//                    viewHolder.tvCheckMername.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(context, HomeActivity.class);
//                            context.startActivity(i);
//                            ((Activity) context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//                        }
//                    });
//                   }
//            } else {
                viewHolder.tvCheckMername.setText(soldBy + " " + sc.getVendorDisplayName());
                viewHolder.tvCheckMername.setTextColor(context.getResources().getColor(R.color.black));
//            }
        } else {
            viewHolder.tvCheckMername.setText("");
        }
        viewHolder.tvProductName.setText(sc.getName());
        viewHolder.tvCount.setText(sc.getQty());
        //colors and size
        ArrayList<HashMap<String, String>> productAttributes = sc.getOptions();

        viewHolder.tvColorAndSize.setText("");
        if (productAttributes != null && productAttributes.size() > 0) {
            viewHolder.tvColorAndSize.setVisibility(View.VISIBLE);
            StringBuilder attributeStr = new StringBuilder();
            if (productAttributes.size() > 0) {
                for (int z = 0; z < productAttributes.size(); z++) {
                    for (String key : productAttributes.get(z).keySet()) {
                        attributeStr.append(productAttributes.get(z).get(key)).append(" | ");
                    }
                }
                attributeStr = new StringBuilder(attributeStr.substring(0, attributeStr.length() - 2));
                viewHolder.tvColorAndSize.setText(attributeStr.toString());
            }
        } else {
            viewHolder.tvColorAndSize.setVisibility(View.GONE);
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
            viewHolder.rlShoppingCartCellContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // llShoppingcartCellPoint  tag 里记载  points 的状态，0为未展开，1为展开
                    String swipeOpenStatus = "1";
                    if (!swipeOpenStatus.equals(viewHolder.llShoppingcartCellPoint.getTag())) {
                        itemOnClickListener.onItemClick(null, null, position, 0);
                    } else {
                        viewHolder.swipeShoppingCart.toggle();
                    }
                }
            });
        }
        viewHolder.swipeShoppingCart.setStatuCallBack(new SwipeLayoutCallBack() {
            @Override
            public void swipeCallback(SwipeLayout v, int type) {
                boolean drakColor = false;
                if (type == SwipeLayoutCallBack.type_add) {
                    //改变 three points的颜色,是否用深颜色？
                    drakColor = false;
                } else if (type == SwipeLayoutCallBack.type_re) {
                    drakColor = true;
                }
                setPonitColor(viewHolder, drakColor);
            }
        });

        if ("1".equals(viewHolder.llShoppingcartCellPoint.getTag())) {
            setDotSelected(viewHolder);
        } else {
            setDotDefault(viewHolder);
        }


        viewHolder.llShoppingcartCellPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.swipeShoppingCart.toggle();
            }
        });

        if (list.size() == 2 ||list.size() == 3) {
            if(getInitBlankView()) {
                //预计算布局高度//  android api <=17无效  view.measure(...)有源码级bug.
                int itemHeight = getViewHeight(viewHolder.itemView);
                int infoHeight = getViewHeight(callback.getInfoView());
                //how much item
                callback.setItemHeightByView(itemHeight*(list.size()-1) + infoHeight);
                setInitBlankView(false);
            }
            //底部是否需要贴底
            ViewTreeObserver vto = viewHolder.itemView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    viewHolder.itemView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    callback.setItemHeight(viewHolder.itemView.getHeight());
                }
            });
        }
    }
    private void setPonitColor(ViewHolder viewHolder, boolean darkColor) {
        //是否使用深颜色
        if (!darkColor) {
            setDotSelected(viewHolder);
        } else {
            setDotDefault(viewHolder);
        }
    }

    private  void setDotSelected(ViewHolder viewHolder){
        viewHolder.llShoppingcartCellPoint.setTag("1");
        viewHolder.tvShoppingcartCellPoint1.setBackgroundResource(R.drawable.button_oval_grey);
        viewHolder.tvShoppingcartCellPoint2.setBackgroundResource(R.drawable.button_oval_grey);
        viewHolder.tvShoppingcartCellPoint3.setBackgroundResource(R.drawable.button_oval_grey);
    }

    private void setDotDefault(ViewHolder viewHolder){
        viewHolder.llShoppingcartCellPoint.setTag("0");
        viewHolder.tvShoppingcartCellPoint1.setBackground(JImageUtils.getThemeCircle(context));
        viewHolder.tvShoppingcartCellPoint2.setBackground(JImageUtils.getThemeCircle(context));
        viewHolder.tvShoppingcartCellPoint3.setBackground(JImageUtils.getThemeCircle(context));
    }
    private boolean initBlankView;
    public void setInitBlankView(boolean init){
        initBlankView=init;
    }
    private boolean getInitBlankView(){
        return initBlankView;
    }

    private int getViewHeight(View view){
        if(view instanceof LinearLayout){
            view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }else{
            view.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        return view.getMeasuredHeight();
    }
    public static class InfoViewHolder extends RecyclerView.ViewHolder {
        public InfoViewHolder(View view) {
            super(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView tvOutOfStock;
        final TextView tvShoppingcartSplit;
        final TextView unavailable;
        final TextView tvProductName;
        final TextView tvProductBland;
        final TextView tvColorAndSize;
        final TextView tvPrice;
        final TextView tvFinalPrice;
        final TextView tvShoppingcartCellPoint1;
        final TextView tvShoppingcartCellPoint2;
        final TextView tvShoppingcartCellPoint3;
        final ImageView imageView;
        final ImageView ivShoppingCartDelete;
        final TextView tvCount;
        final TextView tvCheckMername;
        final TextView tvAllBarrier;
        final TextView tvAllBarrier2;
        final LinearLayout llShoppingCartError;
        final ImageView ivShoppingCartIcon;
        final TextView tvShoppingCartErrorMsg;
        final SwipeLayout swipeShoppingCart;
        final RelativeLayout rlShoppingCartCellContent;
        final RelativeLayout rlShoppingcartCountSub;
        final RelativeLayout rlShoppingcartCountPlus;
        final LinearLayout llShoppingcartCellPoint;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvOutOfStock = (TextView) view.findViewById(R.id.tv_shoppingcart_cell_outofstock);
            tvProductBland = (TextView) view.findViewById(R.id.tv_sc_cell_product_bland);
            tvAllBarrier = (TextView) view.findViewById(R.id.tv_all_barrier);
            tvAllBarrier2 = (TextView) view.findViewById(R.id.tv_all_barrier2);
            unavailable = (TextView) view.findViewById(R.id.unavailable);
            tvProductName = (TextView) view.findViewById(R.id.tv_sc_cell_productname);
            tvColorAndSize = (TextView) view.findViewById(R.id.tv_sc_cell_color_size);
            tvPrice = (TextView) view.findViewById(R.id.tv_sc_cell_price);
            tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvFinalPrice = (TextView) view.findViewById(R.id.tv_sc_cell_finalprice);
            imageView = (ImageView) view.findViewById(R.id.iv_shopping_cart_cell);
            tvCount = (TextView) view.findViewById(R.id.tv_shoppingcart_cell_count);
            tvCheckMername = (TextView) view.findViewById(R.id.tv_check_mername);
            swipeShoppingCart = (SwipeLayout) view.findViewById(R.id.swipe_shoppingcart_cell);
            rlShoppingCartCellContent = (RelativeLayout) view.findViewById(R.id.rl_shopping_cart_cell_content);
            ivShoppingCartDelete = (ImageView) view.findViewById(R.id.iv_shopping_cart_delete);
            llShoppingcartCellPoint = (LinearLayout) view.findViewById(R.id.ll_shoppingcart_cell_point);
            tvShoppingcartCellPoint1 = (TextView) view.findViewById(R.id.tv_shoppingcart_cell_point1);
            tvShoppingcartCellPoint2 = (TextView) view.findViewById(R.id.tv_shoppingcart_cell_point2);
            tvShoppingcartCellPoint3 = (TextView) view.findViewById(R.id.tv_shoppingcart_cell_point3);
            tvShoppingcartSplit = (TextView) view.findViewById(R.id.tv_shoppingcart_split);
            rlShoppingcartCountSub = (RelativeLayout) view.findViewById(R.id.rl_shoppingcart_count_sub);
            rlShoppingcartCountPlus = (RelativeLayout) view.findViewById(R.id.rl_shoppingcart_count_plus);
            llShoppingCartError = (LinearLayout) view.findViewById(R.id.ll_shopping_cart_error);
            ivShoppingCartIcon = (ImageView) view.findViewById(R.id.iv_shopping_cart_icon);
            tvShoppingCartErrorMsg = (TextView) view.findViewById(R.id.tv_shopping_cart_error_msg);
        }
    }

    private void gaTrackerDeleteFromCart(String name, String productId) {
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

    private void gaTrackerIncresaseQuantity(String name, String productId) {
        try {
            GaTrackHelper.getInstance().googleAnalyticsEvent("Cart Action",
                    "Incresase Quantity From Cart",
                    name,
                    Long.valueOf(productId));
            //TODO joyson may be use
//            GaTrackHelper.getInstance().googleAnalyticsAddCart( productId, name);
            //TODO joyson may be use
//            GaTrackHelper.getInstance().googleAnalyticsDeleteCart(context, productId, name);
            JLogUtils.i("googleGA", "Remove Item From Cart");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<TMPLocalCartRepositoryProductEntity> shoppingCarToTMPLocal(ArrayList<ShoppingCartListBase> list) {
        List<TMPLocalCartRepositoryProductEntity> beans = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ShoppingCartListEntityBody) {
                continue;
            }
            TMPLocalCartRepositoryProductEntity bean = new TMPLocalCartRepositoryProductEntity();
            ShoppingCartListEntityCell cell = (ShoppingCartListEntityCell) list.get(i);
            ArrayList<TMPLocalCartRepositoryProductOptionEntity> options = new ArrayList<>();
            bean.setProductId(cell.getProductId());
            bean.setPrice(cell.getPrice());
            bean.setBrandId(cell.getBrandId());
            bean.setName(cell.getName());
            try {
                bean.setInStock(Integer.parseInt(cell.getInStock()));
            } catch (Exception ex) {
                ex.getStackTrace();
            }

            List<HashMap<String, String>> hashs = cell.getLocalOptions();
            if (hashs != null) {
                for (int j = 0; j < hashs.size(); j++) {
                    TMPLocalCartRepositoryProductOptionEntity option = new TMPLocalCartRepositoryProductOptionEntity();
                    option.setId(hashs.get(j).get("attributeId"));
                    option.setLabel(hashs.get(j).get("attributeLabel"));
                    option.setSuperAttribute(hashs.get(j).get("attributeKey"));
                    options.add(option);
                }
//                HashMap<String, String> map1 = hashs.get(0);
//                HashMap<String, String> map2 = hashs.get(1);
//                option.setId(map1.get("ColorId"));
//                option.setLabel(map1.get("Color"));
//                option.setSuperAttribute(map1.get("ColorKey"));
//                option1.setId(map2.get("SizeId"));
//                option1.setLabel(map2.get("Size"));
//                option1.setSuperAttribute(map2.get("SizeKey"));
            }
            // 0 -> Color, 1 -> Size
            bean.setImage(cell.getImage());
            bean.setFinalPrice(cell.getFinalPrice());
            bean.setBrand(cell.getBrand());
            bean.setCategory(cell.getCategory());
            bean.setVendorDisplayName(cell.getVendorDisplayName());
            bean.setVendor_id(cell.getVendor_id());
            bean.setVisibility(cell.getVisibility());
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

    private final View.OnClickListener addListener = new View.OnClickListener() {
        private ShoppingCartVerticalAdapter mShoppingCartVerticalAdapter;

        public View.OnClickListener init(ShoppingCartVerticalAdapter adapter) {
            this.mShoppingCartVerticalAdapter = adapter;
            return this;
        }

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            if (callback.getSwipeRefreshStatus()) {
                return;
            }
            if (list.get(position) instanceof ShoppingCartListEntityBody) {
                return;
            }
            ShoppingCartListEntityCell shoppingCart = (ShoppingCartListEntityCell) list.get(position);
            if (!WhiteLabelApplication.getAppConfiguration().isSignIn(context)) {
                if (Integer.parseInt(shoppingCart.getQty()) + 1 <= Integer.parseInt(shoppingCart.getMaxQty())) {
                    if (!TextUtils.isEmpty(shoppingCart.getMaxSaleQty())) {
                        String maxSaleQty = shoppingCart.getMaxSaleQty();
                        JLogUtils.d(TAG, "maxSaleQty=" + Integer.parseInt(maxSaleQty));
                        if (Integer.parseInt(shoppingCart.getQty()) + 1 <= Integer.parseInt(maxSaleQty)) {
                            final int newCount = Integer.parseInt(shoppingCart.getQty()) + 1;
                            shoppingCart.setQty(newCount + "");
                            calculationToatalPriceAndNum(list);
                            JStorageUtils.savaProductListToLocalCartRepository(context, shoppingCarToTMPLocal(list));
                            gaTrackerIncresaseQuantity(shoppingCart.getName(), shoppingCart.getProductId());
                        } else {
                            String message = context.getResources().getString(R.string.over_maxSale);
                            message = message.replace("x", Integer.parseInt(maxSaleQty) + "");
                            JViewUtils.showSingleToast(context, message);
                        }
                    } else {
                        final int newCount = Integer.parseInt(shoppingCart.getQty()) + 1;
                        shoppingCart.setQty(newCount + "");
                        calculationToatalPriceAndNum(list);
                        JStorageUtils.savaProductListToLocalCartRepository(context, shoppingCarToTMPLocal(list));
                        gaTrackerIncresaseQuantity(shoppingCart.getName(), shoppingCart.getProductId());
                    }

                } else {
                    JViewUtils.showMaterialDialog(context, null, context.getString(R.string.insufficient_stock), null);
                }
                ShoppingCartVerticalAdapter.this.notifyDataSetChanged();
            } else {
                if ("1".equals(shoppingCart.getInStock())) {
                    gaTrackerIncresaseQuantity(shoppingCart.getName(), shoppingCart.getProductId());
                    final int newCount = Integer.parseInt(shoppingCart.getQty()) + 1;
                    int stockQty = shoppingCart.getStockQty();
                    int mCurrentStockQty = Integer.parseInt(shoppingCart.getCurrStockQty());
                    JLogUtils.d(TAG, "shoppingCart--newCount=" + newCount + "--------------curretnQty=" + mCurrentStockQty + "-------------stockQty=" + stockQty);
                    if (newCount <= mCurrentStockQty) {
                        shoppingCart.setQty(newCount + "");
                        sendRequestToChangeCount(position, shoppingCart.getId(), newCount, "opt-plus");
                    } else {
                        JViewUtils.showMaterialDialog(context, null, context.getResources().getString(R.string.insufficient_stock), null);
                    }
                } else {
                    JViewUtils.showMaterialDialog(context, null, context.getResources().getString(R.string.insufficient_stock), null);
                }
            }
        }
    }.init(this);
    private final View.OnClickListener subtractListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            if (callback.getSwipeRefreshStatus()) {
                return;
            }
            if (list.get(position) instanceof ShoppingCartListEntityBody) {
                return;
            }
            ShoppingCartListEntityCell shoppingCart = (ShoppingCartListEntityCell) list.get(position);
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
                    ShoppingCartVerticalAdapter.this.notifyDataSetChanged();
                } else {
                    sendRequestToChangeCount(position, shoppingCart.getId(), newCount, "opt-sub");
                }
            }
        }
    };

    private void calculationToatalPriceAndNum(ArrayList<ShoppingCartListBase> beans) {
        double total = 0;
        int qty = 0;
        for (int i = 0; i < beans.size(); i++) {
            if (beans.get(i) instanceof ShoppingCartListEntityBody) {
                continue;
            }
            ShoppingCartListEntityCell bean = (ShoppingCartListEntityCell) beans.get(i);
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

    private void closeDialog() {
        if (mDialog != null) {
            mDialog.cancel();
        }
    }

    private static final class DataHandler extends Handler {
        private final WeakReference<ShoppingCartVerticalAdapter> mAdapter;
        private final WeakReference<Context> mContext;

        public DataHandler(Context context, ShoppingCartVerticalAdapter shoppingCartAdapterCallback) {
            mAdapter = new WeakReference<>(shoppingCartAdapterCallback);
            mContext = new WeakReference<>(context);
        }


        @Override
        public void handleMessage(Message msg) {
            if (mAdapter.get() == null || mContext.get() == null) {
                return;
            }
            switch (msg.what) {
                case ShoppingCarDao.REQUEST_CHANGESHOPPINGCARCOUNT:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        final ShoppingCartDeleteCellEntity shoppingCartUpdateCellCount = (ShoppingCartDeleteCellEntity) msg.obj;
                        //success
                        if (shoppingCartUpdateCellCount != null && shoppingCartUpdateCellCount.getStatus() == 1) {
                            int position = Integer.parseInt(shoppingCartUpdateCellCount.getParams().getKey());
                            try {
                                ShoppingCartListEntityCell shoppingCart = (ShoppingCartListEntityCell) mAdapter.get().list.get(position);
                                if ("1".equals(shoppingCartUpdateCellCount.getInStock())) {
                                    shoppingCart.setInStock("1");
                                } else {
                                    int oldQty = Integer.parseInt(shoppingCart.getOldQty());
                                    int nowQty = Integer.parseInt(shoppingCart.getQty());
                                    if (oldQty < nowQty) {
                                        shoppingCart.setInStock("0");
                                        JViewUtils.showMaterialDialog(mContext.get(), null, mContext.get().getString(R.string.insufficient_stock), null);
                                    }
                                }
                                mAdapter.get().callback.updateShoppingData(shoppingCartUpdateCellCount);
                                mAdapter.get().notifyDataSetChanged();
                                mAdapter.get().closeDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mAdapter.get().closeDialog();
                        ErrorMsgBean errorMsg = (ErrorMsgBean) msg.obj;
                        if (!TextUtils.isEmpty(errorMsg.getErrorMessage())) {
                            JViewUtils.showSingleToast(mContext.get(), errorMsg.getErrorMessage());
                        }
                        KeyValueBean keyValueBean = (KeyValueBean) errorMsg.getParams();
                        int position = Integer.parseInt(keyValueBean.getKey());
                        String option = keyValueBean.getValue();
                        if (!JToolUtils.expireHandler((Activity) mContext.get(), errorMsg.getErrorMessage(), 2000)) {
                            try {
                                ShoppingCartListEntityCell shoppingCart = (ShoppingCartListEntityCell) mAdapter.get().list.get(position);
                                if (shoppingCart == null) {
                                    return;
                                }
                                int qty = Integer.parseInt(shoppingCart.getQty());
                                if ("opt-plus".equals(option)) {
                                    shoppingCart.setQty((qty - 1) + "");
                                } else {
                                    shoppingCart.setQty((qty + 1) + "");
                                }
                                mAdapter.get().notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case ShoppingCarDao.REQUEST_ERROR:
                    mAdapter.get().closeDialog();
                    if (msg.arg1 == ShoppingCarDao.REQUEST_CHANGESHOPPINGCARCOUNT) {
                        KeyValueBean keyValueBean = (KeyValueBean) msg.obj;
                        int position = Integer.parseInt(keyValueBean.getKey());
                        String option = keyValueBean.getValue();
                        ShoppingCartListEntityCell shoppingCart = (ShoppingCartListEntityCell) mAdapter.get().list.get(position);
                        int qty = Integer.parseInt(shoppingCart.getQty());
                        if ("opt-plus".equals(option)) {
                            shoppingCart.setQty((qty - 1) + "");
                        } else {
                            shoppingCart.setQty((qty + 1) + "");
                        }
                        mAdapter.get().notifyDataSetChanged();
                    }
                    RequestErrorHelper requestErrorHelper = new RequestErrorHelper(mContext.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
                case ShoppingCarDao.REQUEST_DELETEFROMSHOPPINGCART:
                    mAdapter.get().closeDialog();
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        final ShoppingCartDeleteCellEntity shoppingCartDeleteCell = (ShoppingCartDeleteCellEntity) msg.obj;
                        int position = Integer.parseInt((String) shoppingCartDeleteCell.getParam());
                        if (shoppingCartDeleteCell.getStatus() == 1) {//success
                            ShoppingCartListEntityCell cell = null;
                            try {

                                cell = (ShoppingCartListEntityCell) mAdapter.get().list.get(position);
                                //TODO joyson may be use
//                                GaTrackHelper.getInstance().googleAnalyticsDeleteCart(mContext.get(), cell.getProductId(), cell.getName());
                            } catch (Exception ex) {
                                ex.getStackTrace();
                            }
                            try {
                                assert cell != null;
                                if (cell.getIsCampaignProduct() == 1) {
                                    shoppingCartDeleteCell.setIsCampaignProduct(true);
                                }
                                mAdapter.get().list.remove(position);
                                mAdapter.get().callback.deleteShoppingData(shoppingCartDeleteCell,position);
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
//    private void startBrandStoreActivity(Activity activity,String brandName,String brandId){
//        if(!"0".equals(brandId)) {
//            Intent brandStoreIntent = new Intent(activity, BrandStoreFontActivity.class);
//            brandStoreIntent.putExtra(BrandStoreFontActivity.EXTRA_BRAND_ID, brandId);
//            brandStoreIntent.putExtra(BrandStoreFontActivity.EXTRA_BRAND_NAME, brandName);
//            activity.startActivity(brandStoreIntent);
//            activity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//        }else{
//            Intent intent=new Intent(activity, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            activity.startActivity(intent);
//            activity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//        }
//    }
    private void sendRequestToDeteleteCell(final int position, String itemId) {
        mDialog = JViewUtils.showProgressDialog(context);
        mShoppingCartDao.deleteProductFromShoppingCart(WhiteLabelApplication.getAppConfiguration().getUserInfo(context).getSessionKey(), itemId, position + "");
    }

}
