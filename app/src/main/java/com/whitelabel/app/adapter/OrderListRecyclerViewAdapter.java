package com.whitelabel.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.bean.OrderBody;
import com.whitelabel.app.bean.OrderTip;
import com.whitelabel.app.model.MyAccountOrderInner;
import com.whitelabel.app.model.MyAccountOrderMiddle;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.model.MyAccountOrderTrackingInfo;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.CustomCheckBox;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.MaterialDialog;
import com.whitelabel.app.widget.RefreshLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/11.
 */
public class OrderListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 436874;
    private static final int TYPE_ITEM = 256478;
    private static final int TYPE_BODY_ITEM = 10000;
    private static final int TYPE_FOOTER = 9621147;
    private final Context context;
    private boolean loadMore;
    private final ArrayList dataList;
    private ArrayList transformDataList=new ArrayList();
    private final ImageLoader mImageLoader;
    //every item's position count
    private Map<Integer,OrderBody> itemCheckedMaps =new HashMap<>();
    //title opposite subItem position
    Map<Integer,ArrayList<Integer>> titleAndSubBody=new HashMap<>();
    //all title item positon
    ArrayList<Integer> titlePoss=new ArrayList<>();
    //if click btn no item checked ,then mark all items to this arraylist
    ArrayList<OrderBody> markAllCheckedItems=new ArrayList<>();
    private RecyclerView recyclerView;
    //add btn to this array
    private Map<Integer,CustomTextView> btnAddOrders=new HashMap<>();

    private View rootView;

    @SuppressWarnings("unchecked")
    public ArrayList getDataList(ArrayList dataList) {
        ArrayList arrayList = new ArrayList();
        ArrayList<String> availabilityLists=new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            MyAccountOrderOuter myAccountOrderOuter = (MyAccountOrderOuter) dataList.get(i);
            OrderTip orderTip = new OrderTip();
            orderTip.setOrderNumber(myAccountOrderOuter.getOrderSn());
            orderTip.setOrderDate(myAccountOrderOuter.getDate());
            orderTip.setOrderSttus(myAccountOrderOuter.getStatus());
            orderTip.setOrderId(myAccountOrderOuter.getOrderId());
            arrayList.add(orderTip);
            List<MyAccountOrderMiddle> orderlist = myAccountOrderOuter.getSuborders();
            availabilityLists.clear();
            for (int j = 0; j < orderlist.size(); j++) {
                MyAccountOrderTrackingInfo trackingInfo = null;
                if (orderlist.get(j).getTrackingInfo() != null) {
                  trackingInfo = orderlist.get(j).getTrackingInfo();
                }
                for (int k = 0; k < orderlist.get(j).getItems().size(); k++) {
                    MyAccountOrderInner orderInners = orderlist.get(j).getItems().get(k);
                    OrderBody orderBody = new OrderBody();
                    orderBody.setOrderNumber(myAccountOrderOuter.getOrderSn());
                    orderBody.setAvailability(orderInners.getAvailability());
                    if (orderInners.getAvailability().equals("0")){
                        availabilityLists.add("0");
                    }
                    orderBody.setOrderCs(setCS(orderInners));
                    orderBody.setOrderImage(orderInners.getImage());
                    orderBody.setIsRPayment(myAccountOrderOuter.getIsRPayment());
                    orderBody.setOrderName(orderInners.getName());
                    orderBody.setOrderPrice(orderInners.getPrice());
                    orderBody.setOrderQuantity(orderInners.getQty());
                    orderBody.setItemId(orderInners.getItemId());
                    orderBody.setOrderTextStatus(myAccountOrderOuter.getStatus());
                    orderBody.setOrderStatusCode(myAccountOrderOuter.getStatusCode());
                    orderBody.setMerchantName(orderInners.getVendorDisplayName());
                    orderBody.setVendor_id(orderInners.getVendor_id());
                    orderBody.setOrderId(myAccountOrderOuter.getOrderId());
                    orderBody.setStockQty(orderInners.getStockQty());
                    if(trackingInfo!=null){
                        orderBody.setTrickingTitle(trackingInfo.getTitle());
                        orderBody.setTrickingUrl(trackingInfo.getUrl());
                    }
                    if(j==orderlist.size()-1){
                        orderBody.setLast(true);
                    }
                    //if subOrders all inAvailability then btn no used
                    if (availabilityLists.size()==orderlist.size()){
                        orderBody.setBtnAddCartEnable(false);
                    }else {
                        orderBody.setBtnAddCartEnable(true);
                    }
                    arrayList.add(orderBody);
                }
            }
        }
        return arrayList;
    }


    public OrderListRecyclerViewAdapter(Context context, RecyclerView recyclerView,ArrayList dataList, boolean pullEnable, ImageLoader imageLoader,View rootView) {
        this.context = context;
        this.dataList = dataList;
        this.loadMore = pullEnable;
        mImageLoader = imageLoader;
        this.rootView=rootView;
        this.recyclerView=recyclerView;
    }

    public void updateDataChange(){
        transformDataList=getDataList(dataList);
        itemCheckedMaps.clear();
        initTitleAndSubItemPosition();
    }

    private OnOrderViewItemClickListener onOrderViewItemClickListener;

    public interface OnOrderViewItemClickListener {
        void onItemClick(View view, int position,ArrayList<OrderBody> orders);
    }

    public interface IOnErrorDialogConfirm{
        void onComfirm();
    }
    public void setOnOrderViewItemClickListener(OnOrderViewItemClickListener onOrderViewItemClickListener) {
        this.onOrderViewItemClickListener = onOrderViewItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.order_list_item, null);
            return new OrderListholder(view);
        } else if (viewType == TYPE_BODY_ITEM) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_myorder_list_new_item1, null);
            return new SubOrderHolder(view);
        }
//        else if (viewType == TYPE_HEADER) {
//            View headerView = new AlxRefreshLoadMoreRecyclerView.CustomDragHeaderView(parent.getContext());
//            return new VHHeader(headerView);
//        }
        else if (viewType == TYPE_FOOTER) {
            RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView footerView = new RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView(parent.getContext());
            return new VHFooter(footerView);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    public void setPullLoadMoreEnable(boolean enable) {
        this.loadMore = enable;
    }

    public boolean getPullLoadMoreEnable() {
        return loadMore;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {//相当于getView
        if (transformDataList==null && transformDataList.isEmpty()){
            return;
        }
        if (holder instanceof OrderListholder) {
            OrderTip orderTip = (OrderTip) transformDataList.get(position);
            final OrderListholder orderListholder = (OrderListholder) holder;
            orderListholder.orderNumber.setText("Order No. " + orderTip.getOrderNumber());
            orderListholder.orderDate.setText(orderTip.getOrderDate());
            orderListholder.orderStatus.setText(orderTip.getOrderSttus());
            orderListholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOrderViewItemClickListener.onItemClick(orderListholder.itemView, position,null);
                }
            });
        }  else if (holder instanceof VHFooter) {
//            if(!loadMore){
            ((VHFooter) holder).footerView.setState(RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView.STATE_READY);
//            }
        } else if (holder instanceof SubOrderHolder) {
            final OrderBody orderBody = (OrderBody) transformDataList.get(position);

            final SubOrderHolder subOrderHolder = (SubOrderHolder) holder;
            if (orderBody.getOrderImage() == null || !orderBody.getOrderImage().equals(String.valueOf(subOrderHolder.orderImage.getTag()))) {
                JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, subOrderHolder.orderImage, orderBody.getOrderImage(), JToolUtils.dip2px(context, 85), JToolUtils.dip2px(context, 85));
            }
            subOrderHolder.orderImage.setTag(orderBody.getOrderImage());
            subOrderHolder.rmTop.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+"");
            subOrderHolder.orderName.setText(orderBody.getOrderName());
            subOrderHolder.orderNum.setVisibility(View.GONE);

            //add to cart button
            if (orderBody.isLast()){
                subOrderHolder.btnOrderListItemAddtocart.setVisibility(View.VISIBLE);
                btnAddOrders.put(position,subOrderHolder.btnOrderListItemAddtocart);
//                GradientDrawable myGrad = (GradientDrawable)subOrderHolder.btnOrderListItemAddtocart.getBackground();
                //base current group product contain item all show : no Sell product-> to set btn enable
                if (orderBody.isBtnAddCartEnable()){
//                    myGrad.setColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
                    JViewUtils.setStrokeButtonGlobalStyle(context, subOrderHolder.btnOrderListItemAddtocart);
                    subOrderHolder.btnOrderListItemAddtocart.setEnabled(true);
                }else {
//                    myGrad.setColor(ContextCompat.getColor(context,R.color.greyDCDCDC));
                    JViewUtils.setStrokeButtonUnusableStyle(context,subOrderHolder.btnOrderListItemAddtocart);
                    subOrderHolder.btnOrderListItemAddtocart.setEnabled(false);
                }
            }else {
                subOrderHolder.btnOrderListItemAddtocart.setVisibility(View.GONE);
            }
            subOrderHolder.btnOrderListItemAddtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    ArrayList<OrderBody> allAddOrderToCart = getAllAddOrderToCart(position);
                    final ArrayList<OrderBody>[] noSellAndSellProductItem = getNoSellAndSellProductItem(allAddOrderToCart);
                    if (noSellAndSellProductItem[0].isEmpty()){
                        onOrderViewItemClickListener.onItemClick(v, position,noSellAndSellProductItem[1]);
                    }else {
                        showErrorProductDialog(noSellAndSellProductItem[0], new IOnErrorDialogConfirm() {
                            @Override
                            public void onComfirm() {
                                onOrderViewItemClickListener.onItemClick(v, position,noSellAndSellProductItem[1]);
                            }
                        });
                    }
                    gaBtnKind(((CustomTextView)v).getText().toString().trim(),orderBody.getOrderId());
                }
            });
            setDiffBtnShow(subOrderHolder.btnOrderListItemAddtocart);
            //checkBox
            subOrderHolder.cbReorderCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                    if(isChecked){
                        if (checkBox.getTag()!=null){
                            if (!itemCheckedMaps.containsKey((int) checkBox.getTag())){
                                Drawable pressIcon = JImageUtils.getThemeIcon(context, R.drawable.icon_order_checked);
                                checkBox.setBackground(pressIcon);
                                orderBody.setChecked(true);
                                itemCheckedMaps.put(position,orderBody);
                                transformDataList.set(position,orderBody);
                                //click item to let group btn 'add to cart ' set Tag
                                int[] preAndNextTitleIndex = getPreAndNextTitleIndex(position);
                                if (recyclerView.getScrollState()==RecyclerView.SCROLL_STATE_IDLE&&!recyclerView.isComputingLayout()){
                                    int currentBtnPos=preAndNextTitleIndex[1]-1;
                                    CustomTextView btnAddOrder = btnAddOrders.get(currentBtnPos);
                                    if (btnAddOrder!=null){
                                        btnAddOrder.setTag(currentBtnPos);
                                        setDiffBtnShow(btnAddOrder);
                                    }
                                }
                                gaChooseProduct(orderBody.getOrderName(),orderBody.getOrderId());
                            }
                        }
                    }else {
                        if (checkBox.getTag()!=null){
                            if (itemCheckedMaps.containsKey((int) checkBox.getTag())){
                                Drawable pressIcon = JImageUtils.getThemeIcon(context, R.drawable.icon_order_nocheck);
                                checkBox.setBackground(pressIcon);
                                orderBody.setChecked(false);
                                itemCheckedMaps.remove(position);
                                transformDataList.set(position,orderBody);
                                int[] preAndNextTitleIndex = getPreAndNextTitleIndex(position);
                                boolean isContainThisGroupCheckItem=false;
                                for (int startItemPos=preAndNextTitleIndex[0]+1;startItemPos<preAndNextTitleIndex[1];startItemPos++){
                                    if (itemCheckedMaps.containsKey(startItemPos)){
                                        isContainThisGroupCheckItem=true;
                                    }
                                }
                                int currentBtnPos=preAndNextTitleIndex[1]-1;
                                CustomTextView btnAddOrder = btnAddOrders.get(currentBtnPos);
                                //isContainThisGroupCheckItem=true means this group contain checkedItem
                                if (isContainThisGroupCheckItem && btnAddOrder!=null){
                                    btnAddOrder.setTag(currentBtnPos);
                                    setDiffBtnShow(btnAddOrder);
                                }else {
                                    btnAddOrder.setTag(null);
                                    setDiffBtnShow(btnAddOrder);
                                }
                            }
                        }
                    }
                }
            });
            if (!itemCheckedMaps.isEmpty()){
                subOrderHolder.cbReorderCheck.setChecked(itemCheckedMaps.containsKey(position)?true:false);
            }else {
                subOrderHolder.cbReorderCheck.setChecked(false);
            }
            subOrderHolder.cbReorderCheck.setTag(position);

            //sub quantity and plus
            subOrderHolder.tvShoppingcartCellCount.setText(orderBody.getOrderQuantity());
            subOrderHolder.rlShoppingcartCountSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int visibleOrderCount;
                    visibleOrderCount = Integer.valueOf(orderBody.getOrderQuantity());
                    if (visibleOrderCount>1){
                        visibleOrderCount--;
                    }
                    String itemQuantity=String.valueOf(visibleOrderCount);
                    orderBody.setOrderQuantity(itemQuantity);
                    transformDataList.set(position,orderBody);
                    subOrderHolder.tvShoppingcartCellCount.setText(itemQuantity);
                    gaModifyQty(orderBody.getOrderName(),false,itemQuantity,orderBody.getOrderId());
                }
            });
            subOrderHolder.rlShoppingcartCountPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int visibleOrderCount;
                    visibleOrderCount = Integer.valueOf(orderBody.getOrderQuantity());
                    if (Integer.valueOf(orderBody.getStockQty())>visibleOrderCount){
                        visibleOrderCount++;
                    }else {
                        JViewUtils.showPopUpWindw(context,rootView,context.getResources().getString(R.string.insufficient_stock));
                    }
                    String itemQuantity=String.valueOf(visibleOrderCount);
                    orderBody.setOrderQuantity(itemQuantity);
                    transformDataList.set(position,orderBody);
                    subOrderHolder.tvShoppingcartCellCount.setText(itemQuantity);
                    gaModifyQty(orderBody.getOrderName(),false,itemQuantity,orderBody.getOrderId());
                }
            });
            if (orderBody.getIsRPayment()==0||!orderBody.isLast()) {
                subOrderHolder.llOrderRePayment.setVisibility(View.GONE);
            } else {
                subOrderHolder.llOrderRePayment.setVisibility(View.VISIBLE);
                JViewUtils.setSoildButtonGlobalStyle(subOrderHolder.itemView.getContext(),subOrderHolder.tvRepayment);
                subOrderHolder.tvRepayment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onOrderViewItemClickListener!=null) {
                            onOrderViewItemClickListener.onItemClick(v, position,null);
                        }
                    }
                });
            }
            if (("").equals(orderBody.getOrderCs())) {
                subOrderHolder.orderCS.setVisibility(View.GONE);
            } else {
                subOrderHolder.orderCS.setVisibility(View.VISIBLE);
                subOrderHolder.orderCS.setText(orderBody.getOrderCs());
            }


            if(!TextUtils.isEmpty(orderBody.getAvailability())&&!"1".equals(orderBody.getAvailability())){
                subOrderHolder.tvUnavailable.setVisibility(View.VISIBLE);
                subOrderHolder.tvTrans.setVisibility(View.VISIBLE);
                subOrderHolder.cbReorderCheck.setEnabled(false);
            }else{
                subOrderHolder.tvUnavailable.setVisibility(View.GONE);
                subOrderHolder.tvTrans.setVisibility(View.GONE);
                subOrderHolder.cbReorderCheck.setEnabled(true);
            }

            subOrderHolder.orderPrice.setText(JDataUtils.formatDouble(orderBody.getOrderPrice()));
            subOrderHolder.orderNewStatus.setText(orderBody.getOrderTextStatus());

            JViewUtils.setStatus(subOrderHolder.orderStatus, orderBody.getOrderStatusCode());
            if (!TextUtils.isEmpty(orderBody.getMerchantName())) {
                String soldBy = subOrderHolder.orderMerName.getContext().getResources().getString(R.string.soldby);
                    subOrderHolder.orderMerName.setText(soldBy + " " + orderBody.getMerchantName());
                    subOrderHolder.orderMerName.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                subOrderHolder.orderMerName.setText("");
            }
            subOrderHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOrderViewItemClickListener.onItemClick(view, position,null);
                }
            });
        }
    }

    private void notifyBtnStatus(int position) {
        RecyclerView.ViewHolder viewHolder=recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null && viewHolder instanceof SubOrderHolder) {
            SubOrderHolder itemHolder = (SubOrderHolder) viewHolder;
            if (itemHolder.btnOrderListItemAddtocart.getTag()!=null){
                itemHolder.btnOrderListItemAddtocart.setText(context.getResources().getText(R.string.product_detail_addtocart));
            }else {
                itemHolder.btnOrderListItemAddtocart.setText(context.getResources().getText(R.string.product_detail_addorder_tocart));
            }
        }
    }

    private void setDiffBtnShow(CustomTextView btn){
        if (btn.getTag()!=null){
            btn.setText(context.getResources().getText(R.string.product_detail_addtocart));
        }else {
            btn.setText(context.getResources().getText(R.string.product_detail_addorder_tocart));
        }
    }

    /**
     *  base btn click position ,if no item checked ,then add all currency group's item ,else get all checked item
     * @param position btn click postion
     * @return
     */
    private ArrayList<OrderBody> getAllAddOrderToCart(int position) {
        ArrayList<OrderBody> checkedList = getCheckedList(position);
        markAllCheckedItems.clear();
        //current group order no check any item,then
        if (checkedList!=null && checkedList.isEmpty()){
            int[] preAndNextTitleIndex = getPreAndNextTitleIndex(position);
            //all item count
//            int totalItem=preAndNextTitleIndex[1]-preAndNextTitleIndex[0];

            //startItemPos = preTitlePos +1,
            for (int i= preAndNextTitleIndex[0]+1;i<preAndNextTitleIndex[1];i++){
                if (transformDataList.get(i) instanceof OrderBody){
                    OrderBody orderBody = (OrderBody) transformDataList.get(i);
                    markAllCheckedItems.add(orderBody);
                }
            }
        }else {
            markAllCheckedItems.addAll(checkedList);
        }
        return markAllCheckedItems;
    }

    /**
     * base (all checked lists) to get no sell prducts
     * @param checkedLists all checked lists
     */
    private ArrayList<OrderBody>[] getNoSellAndSellProductItem(ArrayList<OrderBody> checkedLists){
        ArrayList<OrderBody> noSellProducts=new ArrayList<>();
        ArrayList<OrderBody> sellProducts=new ArrayList<>();
        ArrayList<OrderBody>[] noSellAndSellItems =new ArrayList[2];
        if (checkedLists!=null &&  !checkedLists.isEmpty()){
            for (OrderBody orderBody:checkedLists){
                if (orderBody.getAvailability().equals("0")){
                    noSellProducts.add(orderBody);
                }else {
                    sellProducts.add(orderBody);
                }
            }
        }
        noSellAndSellItems[0]=noSellProducts;
        noSellAndSellItems[1]=sellProducts;
        return noSellAndSellItems;
    }

    /**
     * base click position btn ,get current grounp order -all child checked item
     * @param position click btn anchor
     * @return
     */
    private ArrayList<OrderBody> getCheckedList(int position){
        ArrayList<OrderBody> orderBodies=new ArrayList<>();
        if (itemCheckedMaps!=null && !itemCheckedMaps.isEmpty()){
            int[] preAndNextTitleIndex = getPreAndNextTitleIndex(position);
            for (Map.Entry<Integer,OrderBody> entry:itemCheckedMaps.entrySet()){
                //gather than last title and less-than next title :all item
                if (entry.getKey()>preAndNextTitleIndex[0] && entry.getKey()<preAndNextTitleIndex[1]){
                    orderBodies.add(entry.getValue());
                }
            }
        }
        return orderBodies;
    }

    //click 'add to cart' button if order's item show unavailable ,show this dialog
    public void showErrorProductDialog(ArrayList<OrderBody> beans, final IOnErrorDialogConfirm iOnErrorDialogConfirm) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle(context.getResources().getString(R.string.add_order_to_cart_error_title));
        mMaterialDialog.setTitleSize(16);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        ReRorderToCartAdapter adapter = new ReRorderToCartAdapter(context, beans, mImageLoader);
        listView.setAdapter(adapter);
        mMaterialDialog.setContentView(view);
        mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMaterialDialog!=null) {
                    mMaterialDialog.dismiss();
                }
                if (iOnErrorDialogConfirm!=null){
                    iOnErrorDialogConfirm.onComfirm();
                }
            }
        });
        mMaterialDialog.show();
    }

    private void initTitleAndSubItemPosition(){
        ArrayList<Integer> subBody=new ArrayList<>();
        //create titlePos targe subItem's Pos
        int titlePos=0;
        int SubBodyPos=0;
        if (transformDataList!=null && !transformDataList.isEmpty()) {
            titleAndSubBody.clear();
            for (int i = 0; i < transformDataList.size(); i++) {
                if (transformDataList.get(i) instanceof OrderTip) {
                    titlePoss.add(i);
                    titlePos = i;
                } else if (transformDataList.get(i) instanceof OrderBody) {
                    subBody.add(i);
                    SubBodyPos = i;
                }
                if (titlePos > SubBodyPos && SubBodyPos != 0) {
                    titleAndSubBody.put(titlePos - subBody.size() - 1, new ArrayList<Integer>(subBody));
                    subBody.clear();
                    //last one
                } else if (SubBodyPos == transformDataList.size() - 1) {
                    titleAndSubBody.put(titlePos, new ArrayList<Integer>(subBody));
                    subBody.clear();
                }
            }
        }
    }

    /**
     *
     * @param position add to cart position ,never equal title Position
     * @return
     */
    private int [] getPreAndNextTitleIndex(int position){
        int [] arr=new int[2];
        int markPos = 0;
        int markGTPos = 0;
        if (!titlePoss.isEmpty()) {
            for (int i=0;i<titlePoss.size();i++){
                if (position<titlePoss.get(i)){
                    if (i>0){
                        markPos=titlePoss.get(i-1);
                    }
                    markGTPos=titlePoss.get(i);
                    break;
                }
            }
        }
        arr[0]=markPos;
        arr[1]=markGTPos;
        return arr;
    }

    /**
     * get btn text to ga
     * @param btnText
     * @param orderId
     */
    private void gaBtnKind(String btnText,String orderId){
        if (!TextUtils.isEmpty(btnText)){
            GaTrackHelper.getInstance().googleAnalyticsEvent(Const.GA.ORDER_REORDER_CATEGORY,
                    Const.GA.ORDER_ADD_TO_CART_EVENT,
                    btnText,
                    Long.valueOf(orderId));
        }
    }


    private void gaChooseProduct(String productName,String orderId){
        GaTrackHelper.getInstance().googleAnalyticsEvent(Const.GA.ORDER_REORDER_CATEGORY,
                Const.GA.ORDER_CHOOSE_PRODUCT_EVENT,
                productName,
                Long.valueOf(orderId));
    }

    private void gaModifyQty(String productName,boolean isAddOrSub,String quantity,String orderId){
        String text="";
        if (isAddOrSub){
            text= Const.GA.ORDER_ADD_EVENT;
        }else {
            text= Const.GA.ORDER_SUBTRACT_EVENT;
        }
        GaTrackHelper.getInstance().googleAnalyticsReorderEvent(Const.GA.ORDER_REORDER_CATEGORY,
                Const.GA.ORDER_MODIFY_QTY_EVENT,
                productName,
                text,
                quantity,
                Long.valueOf(orderId));
    }


    @Override
    public int getItemCount() {
        return (getDataList(dataList) == null || getDataList(dataList).size() == 0) ? 1 : getDataList(dataList).size() + 1;//如果有header,若list不存在或大小为0就没有footView，反之则有
    }//这里要考虑到头尾部，多以要加2

    /**
     * @param position 根据位置判断这里该用哪个ViewHolder
     * @return
     */
    @Override
    public int getItemViewType(int position) {
//        if (position == 0){
//            return TYPE_HEADER;
//        } else
        if (isPositonFooter(position)) {
            return TYPE_FOOTER;
        } else {
            if (getDataList(dataList).get(position) instanceof OrderTip)
                return TYPE_ITEM;
            else
                return TYPE_BODY_ITEM;
        }
    }

    private boolean isPositonFooter(int position) {//这里的position从0算起
        //如果没有item
        return getDataList(dataList) == null && position == 1 || position == getDataList(dataList).size();
    }

    class VHFooter extends RecyclerView.ViewHolder {
        public final RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView
                footerView;

        public VHFooter(View itemView) {
            super(itemView);
            footerView = (RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView) itemView;
        }
    }


    public static class OrderListholder extends RecyclerView.ViewHolder {
        private final TextView orderNumber;
        private final TextView orderDate;
        private final TextView orderStatus;

        public OrderListholder(View view) {
            super(view);
            orderNumber = (TextView) view.findViewById(R.id.tv_myaccount_orderlist_ordernumber);
            orderDate = (TextView) view.findViewById(R.id.tv_myaccount_orderlist_date);
            orderStatus = (TextView) view.findViewById(R.id.tv_myaccount_orderlist_status);
        }
    }

    public static class SubOrderHolder extends RecyclerView.ViewHolder {
        final ImageView orderImage;
        final LinearLayout llOrderRePayment;
        final TextView orderName;
        final TextView orderCS;
        final TextView orderPrice;
        final TextView orderNum;
        final TextView orderStatus;
        final TextView orderNewStatus;
        final TextView orderMerName;
        final TextView tvRepayment;
        final TextView rmTop;
        final TextView tvUnavailable;
        final TextView tvTrans;
        final CustomTextView btnOrderListItemAddtocart;
        final CheckBox cbReorderCheck;
        final RelativeLayout rlShoppingcartCountSub;
        final CustomTextView tvShoppingcartCellCount;
        final RelativeLayout rlShoppingcartCountPlus;

        public SubOrderHolder(View view) {
            super(view);
            tvUnavailable= (TextView) view.findViewById(R.id.order_detail_unavailable);
            orderImage = (ImageView) view.findViewById(R.id.iv_orderlist_new);
            orderName = (TextView) view.findViewById(R.id.tv_orderlist_new_name);
            orderCS = (TextView) view.findViewById(R.id.tv_orderlist_new_det);
            orderPrice = (TextView) view.findViewById(R.id.tv_orderlist_new_pri);
            orderNum = (TextView) view.findViewById(R.id.tv_orderlist_new_ext);
            orderStatus = (TextView) view.findViewById(R.id.order_status);
            orderNewStatus = (TextView) view.findViewById(R.id.tv_orderlist_new_status);
            orderMerName = (TextView) view.findViewById(R.id.tv_orderlist_new_mername);
            tvRepayment = (TextView) view.findViewById(R.id.tv_order_repayment);
            llOrderRePayment= (LinearLayout) view.findViewById(R.id.ll_order_repayment);
            rmTop= (TextView) view.findViewById(R.id.rm_top);
            tvTrans= (TextView) view.findViewById(R.id.order_detail_trans);
            btnOrderListItemAddtocart= (CustomTextView) view.findViewById(R.id.btn_order_list_item_addtocart);
            cbReorderCheck= (CheckBox) view.findViewById(R.id.cb_reorder_check);
            rlShoppingcartCountSub= (RelativeLayout) view.findViewById(R.id.rl_shoppingcart_count_sub);
            tvShoppingcartCellCount = (CustomTextView) view.findViewById(R.id.tv_shoppingcart_cell_count);
            rlShoppingcartCountPlus = (RelativeLayout) view.findViewById(R.id.rl_shoppingcart_count_plus);
        }
    }

    private String setCS(MyAccountOrderInner orderInners) {
        StringBuilder str = new StringBuilder();
        ArrayList<HashMap<String, String>> colorAndSizes = orderInners.getOptions();
        if (colorAndSizes != null && colorAndSizes.size() > 0) {
            for (int i = 0; i < colorAndSizes.size(); i++) {
                str.append(colorAndSizes.get(i).get("value")).append(" | ");
            }
            str = new StringBuilder(str.substring(0, str.length() - 2));
        } else {
            //textView.setVisibility(View.GONE);
            str = new StringBuilder();
        }
        return str.toString();
    }

//    public void setStatus(TextView textView, String statusCode) {
//        if ("pending".equalsIgnoreCase(statusCode)) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_pending));
//            textView.setBackgroundResource(R.drawable.order_item_status_pending);
//        }
//        /*else  if ("pendingCD".equalsIgnoreCase(orderListCellNew.getStatusCode())) {
//            tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_pending_cd));
//        }*/
//        else if ("processing".equalsIgnoreCase(statusCode)) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_proccessing));
//            textView.setBackgroundResource(R.drawable.order_item_status_process);
//        } else if (statusCode.contains("verified")) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_verified));
//            textView.setBackgroundResource(R.drawable.order_item_status_verified);
//        } else if ("shipped".equalsIgnoreCase(statusCode)) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_shipped));
//            textView.setBackgroundResource(R.drawable.order_item_status_ship);
//        } else if (statusCode.contains("delivered")) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_delivered));
//            textView.setBackgroundResource(R.drawable.order_item_status_delivered);
//        } else if ("deliveryFailed".equalsIgnoreCase(statusCode)) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_delivery_failed));
//            textView.setBackgroundResource(R.drawable.order_item_status_failed);
//        } else if (statusCode.contains("canceled")) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_canceled));
//            textView.setBackgroundResource(R.drawable.order_item_status_cancel);
//        }
//    }
}
