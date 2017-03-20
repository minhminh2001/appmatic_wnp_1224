package com.whitelabel.app.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.utils.JScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 实现点击展开子目录，利用recycleView原生动画。
 */
public abstract class ExpandableRecyclerAdapter<T extends ExpandableRecyclerAdapter.ListItem> extends RecyclerView.Adapter<ExpandableRecyclerAdapter.ViewHolder> {
    protected Context mContext;
    private int maxChildItemCount=10;
    protected List<T> allItems = new ArrayList<>();
    protected List<T> visibleItems = new ArrayList<>();
    private List<Integer> indexList = new ArrayList<>();
    private SparseIntArray expandMap = new SparseIntArray();
    private int mode;

    private RecyclerView recyclerView;

    protected static final int TYPE_HEADER = 1000;

    private static final int ARROW_ROTATION_DURATION = 150;

    public static final int MODE_NORMAL = 0;
    //展开一个item，关闭其他所有item
    public static final int MODE_ACCORDION = 1;

    public ExpandableRecyclerAdapter(Context context, Activity activity) {
        mContext = context;
        getMaxChildItem(activity);
    }
    //适配出当前屏幕适应多少个child Item
    private void getMaxChildItem(Activity activity){
        int screenHeight=JScreenUtils.getScreenHeight(activity);
        if(screenHeight>=2350){
            maxChildItemCount=13;
        }else if(screenHeight>=1900){
            maxChildItemCount=12;
        }else if(screenHeight>=1170){
            maxChildItemCount=11;
        }else if(screenHeight>=800){
            maxChildItemCount=9;
        }else if(screenHeight>=200){
            maxChildItemCount=7;
        }
    }
    public void setRecycleView(RecyclerView recyclerView){
        this.recyclerView=recyclerView;
    }

    /**
     * 实体类
     */
    public static class ListItem {
        public String parentId;
        public int ItemType;
        public boolean expaned=false;

        public boolean isExpaned() {
            return expaned;
        }

        public void setExpaned(boolean expaned) {
            this.expaned = expaned;
        }

        public int getItemType() {
            return ItemType;
        }

        public void setItemType(int itemType) {
            ItemType = itemType;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public ListItem() {
        }
        public ListItem(int itemType) {
            ItemType = itemType;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return visibleItems == null ? 0 : visibleItems.size();
    }

    protected View inflate(int resourceID, ViewGroup viewGroup) {
        return LayoutInflater.from(mContext).inflate(resourceID, viewGroup, false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
    public  interface ArrowChangeListener{
        void arrowChange(View arrow, boolean openGroup, int position);
    }

    public class HeaderViewHolder extends ViewHolder {
        View arrow;
        ArrowChangeListener arrowChangeListener;
        public HeaderViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClick();
                }
            });
        }
        public void setArrowAndChangeListener(final View arrow, ArrowChangeListener arrowChangeListener){
            this.arrow = arrow;
            this.arrowChangeListener=arrowChangeListener;
        }
        public HeaderViewHolder(View view, final View arrow, ArrowChangeListener arrowChangeListener) {
            super(view);
            this.arrow = arrow;
            this.arrowChangeListener=arrowChangeListener;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClick();
                }
            });
        }
        //点击回调
        protected void handleClick() {
            int position=getLayoutPosition();
            boolean isExpland=toggleExpandedItems(position, false);
            if(arrowChangeListener!=null&&arrow!=null) {
                if (isExpland) {
                    arrowChangeListener.arrowChange(arrow, true,position);
                } else {
                    arrowChangeListener.arrowChange(arrow, false,position);
                }
            }
        }

    }

    //当前可在屏幕显示的item组
    public T getItem(int position){
        return visibleItems.get(position);
    }
    //展开收起互相切换
    public boolean toggleExpandedItems(int position, boolean notify) {
        if (isExpanded(position)) {
            collapseItems(position, notify);
            return false;
        } else {
            ListItem currentItem=visibleItems.get(position);
            expandItems(position, notify);
            if (mode == MODE_ACCORDION) {
                collapseAllExcept(position);
            }

            Message message=new Message();
            message.obj=currentItem;
            delayHandler.sendMessageDelayed(message,200);

            return true;
        }
    }

    DelayHandler delayHandler=new DelayHandler();
    private class DelayHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //如果显示不全，尝试滚动recycleView
            try {
                int position=visibleItems.indexOf(msg.obj);
                if(position==0){
                    //第一个不需要滚动
                    return;
                }
                int tagPosition = position;
                if (((SVRAppserviceCatalogSearchCategoryItemReturnEntity)getItem(position)).getChildren().size() < maxChildItemCount) {
                    tagPosition = position + ((SVRAppserviceCatalogSearchCategoryItemReturnEntity)getItem(position)).getChildren().size();
                } else {
                    tagPosition = position+maxChildItemCount;
                }
                recyclerView.smoothScrollToPosition(tagPosition);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //展开
    public void expandItems(int position, boolean notify) {
        int count = 0;
        int index = indexList.get(position);
        int insert = position;

        visibleItems.get(position).setExpaned(true);

        for (int i=index+1; i<allItems.size() && allItems.get(i).ItemType != TYPE_HEADER; i++) {
            insert++;
            count++;
            visibleItems.add(insert, allItems.get(i));
            indexList.add(insert, i);
        }

        notifyItemRangeInserted(position + 1, count);

        int allItemsPosition = indexList.get(position);
        expandMap.put(allItemsPosition, 1);

        if (notify) {
            notifyItemChanged(position);
        }
    }
    //关闭
    public void collapseItems(int position, boolean notify) {
        int count = 0;
        int index = indexList.get(position);
        visibleItems.get(position).setExpaned(false);
        for (int i=index+1; i<allItems.size() && allItems.get(i).ItemType != TYPE_HEADER; i++) {
            count++;
            visibleItems.remove(position + 1);
            indexList.remove(position + 1);
        }

        notifyItemRangeRemoved(position + 1, count);

        int allItemsPosition = indexList.get(position);
        expandMap.delete(allItemsPosition);

        if (notify) {
            notifyItemChanged(position);
        }
    }

    protected boolean isExpanded(int position) {
        int allItemsPosition = indexList.get(position);
        return expandMap.get(allItemsPosition, -1) >= 0;
    }

    @Override
    public int getItemViewType(int position) {
        return visibleItems.get(position).ItemType;
    }

    public void setItems(List<T> items) {
        allItems = items;
        List<T> visibleItems = new ArrayList<>();
        expandMap.clear();
        indexList.clear();

        for (int i=0; i<items.size(); i++) {
            if (items.get(i).ItemType == TYPE_HEADER) {
                indexList.add(i);
                visibleItems.add(items.get(i));
            }
        }

        this.visibleItems = visibleItems;
        notifyDataSetChanged();
    }

    protected void notifyItemInserted(int allItemsPosition, int visiblePosition) {
        incrementIndexList(allItemsPosition, visiblePosition, 1);
        incrementExpandMapAfter(allItemsPosition, 1);

        if (visiblePosition >= 0) {
            notifyItemInserted(visiblePosition);
        }
    }

    protected void removeItemAt(int visiblePosition) {
        int allItemsPosition = indexList.get(visiblePosition);

        allItems.remove(allItemsPosition);
        visibleItems.remove(visiblePosition);

        incrementIndexList(allItemsPosition, visiblePosition, -1);
        incrementExpandMapAfter(allItemsPosition, -1);

        notifyItemRemoved(visiblePosition);
    }

    private void incrementExpandMapAfter(int position, int direction) {
        SparseIntArray newExpandMap = new SparseIntArray();

        for (int i=0; i<expandMap.size(); i++) {
            int index = expandMap.keyAt(i);
            newExpandMap.put(index < position ? index : index + direction, 1);
        }

        expandMap = newExpandMap;
    }

    private void incrementIndexList(int allItemsPosition, int visiblePosition, int direction) {
        List<Integer> newIndexList = new ArrayList<>();

        for (int i=0; i<indexList.size(); i++) {
            if (i == visiblePosition) {
                if (direction > 0) {
                    newIndexList.add(allItemsPosition);
                }
            }

            int val = indexList.get(i);
            newIndexList.add(val < allItemsPosition ? val : val + direction);
        }

        indexList = newIndexList;
    }

    public void collapseAll() {
        collapseAllExcept(-1);
    }

    public void collapseAllExcept(int position) {
        for (int i=visibleItems.size()-1; i>=0; i--) {
            if (i != position && getItemViewType(i) == TYPE_HEADER) {
                if (isExpanded(i)) {
                    collapseItems(i, true);
                }
            }
        }
    }

    public void expandAll() {
        for (int i=visibleItems.size()-1; i>=0; i--) {
            if (getItemViewType(i) == TYPE_HEADER) {
                if (!isExpanded(i)) {
                    expandItems(i, true);
                }
            }
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
