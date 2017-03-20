package com.whitelabel.app.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.adapter.OrderListRecyclerViewAdapter;
import com.whitelabel.app.callback.ProductListFilterHideCallBack;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JScreenUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alex on 2016/1/27.
 */
public class RefreshLoadMoreRecyclerViewV2 extends RecyclerView {
    private int footerHeight = -1;
    GridLayoutManager layoutManager;
    // -- footer view
    private CustomDragRecyclerFooterView mFooterView;
    private boolean mEnablePullLoad;
    private boolean mPullLoading;
    private boolean isBottom;
    private boolean mIsFooterReady = false;
    private OnLoadMoreListener loadMoreListener;

    // -- header view
    private CustomDragHeaderView mHeaderView;
    private boolean mEnablePullRefresh = true;
    private boolean mIsRefreshing;
    private boolean isHeader;
    private boolean mIsHeaderReady = false;
    private Timer timer;
    private float oldY;
    Handler handler = new Handler();
    private OnRefreshListener refreshListener;
    private OrderListRecyclerViewAdapter adapter;
    private int maxPullHeight;//最多下拉高度的px值

    private static final int HEADER_HEIGHT = 68;//头部高度68dp
    private static final int MAX_PULL_LENGTH = 100;//最多下拉150dp
    private OnClickListener footerClickListener;
    private boolean firstTime = false;
    private boolean mIsShowSwitchFilterBar;


    public RefreshLoadMoreRecyclerViewV2(Context context) {
        super(context);
        initView(context);

    }


    public GridLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public RefreshLoadMoreRecyclerViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshLoadMoreRecyclerViewV2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void setAdapter(OrderListRecyclerViewAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    private FilterSortBottomView mFilterSortBottomView;

    public void setFillerSortBottomView(FilterSortBottomView fillerSortBottomView) {
        this.mFilterSortBottomView = fillerSortBottomView;
    }


    public void setSpanSizeLookUp(GridLayoutManager.SpanSizeLookup spanSizeLookUp) {
        this.layoutManager.setSpanSizeLookup(spanSizeLookUp);
    }

    public void initView(Context context) {
        layoutManager = new GridLayoutManager(context, 2);
        this.setLayoutManager(layoutManager);
        maxPullHeight = dp2px(getContext().getResources().getDisplayMetrics().density, MAX_PULL_LENGTH);//最多下拉150dp
        this.footerClickListener = new footerViewClickListener();
        this.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mFilterSortBottomView != null) {
                    if (!mIsShowSwitchFilterBar) {
                        return;
                    }
                    mFilterSortBottomView.setFilterShow(newState, new ProductListFilterHideCallBack() {
                        @Override
                        public void callBack() {

                        }
                    });
                }
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (isBottom) {
                            startLoadMore();
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = layoutManager.findFirstVisibleItemPosition();
                View firstVisibleChildView = layoutManager.findViewByPosition(position);
                View topBar = firstVisibleChildView.findViewById(R.id.rl_viewbar);
                int viewHeight = 0;
                int itemHeight = 0;
                int itemHeightDistance = 0;
                int ScrollDistance = 0;
                if (topBar != null) {
                    viewHeight = topBar.getHeight();
                    itemHeight = firstVisibleChildView.getHeight();
                    itemHeightDistance=itemHeight-viewHeight;
                }
                ScrollDistance = (position) * itemHeight - firstVisibleChildView.getTop()+30;
                JLogUtils.d("jay","position="+position+"--distance="+ScrollDistance+"--itemHeightDistance="+itemHeightDistance);

                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (mFilterSortBottomView != null) {
                    if (firstItemPosition == 0) {
                        mFilterSortBottomView.hideBottomSlideToTop(true);
                    } else {
                        mFilterSortBottomView.hideBottomSlideToTop(false);
                    }
                    if (mFilterSortBottomView.getBottomBar() != null) {
                        if (ScrollDistance >= itemHeightDistance) {
                            mIsShowSwitchFilterBar = true;
                            mFilterSortBottomView.getBottomBar().setVisibility(VISIBLE);
                        } else {
                            mIsShowSwitchFilterBar = false;
                            mFilterSortBottomView.getBottomBar().setVisibility(GONE);
                        }
                        mFilterSortBottomView.setIsShowSwitchFilterBar(mIsShowSwitchFilterBar);
                    }

                }
                JLogUtils.i("ray", "=================firstItemPosition=" + firstItemPosition + "----lastItemPosition=" + lastItemPosition + "===layoutManager.getItemCount()-1==" + (layoutManager.getItemCount() - 1));
                if (lastItemPosition == layoutManager.getItemCount() - 1 && mEnablePullLoad && lastItemPosition != 1) {//如果到了最后一个
                    isBottom = true;
                    mFooterView = (CustomDragRecyclerFooterView) layoutManager.findViewByPosition(layoutManager.findLastVisibleItemPosition());
                    if (footerHeight == -1 && mFooterView != null) {
                        mFooterView.setState(CustomDragRecyclerFooterView.STATE_LOADING);
                    }
//                    updateFooterHeight(dy);
                } else {
                    isBottom = false;
                }
            }
        });
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setLoadMoreEnable(boolean enable) {
        mPullLoading = false;
        mEnablePullLoad = enable;
        if (adapter != null) adapter.setPullLoadMoreEnable(enable);//adapter和recyclerView要同时设置
        if (mFooterView == null) return;
        mFooterView.setEnabled(enable);
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mFooterView != null) {
            mFooterView.setState(CustomDragRecyclerFooterView.STATE_ERROR);
        }
        if (mPullLoading == true) {
            mPullLoading = false;


        }
    }

    private void startLoadMore() {
        if (mPullLoading) return;
        mPullLoading = true;
        if (mFooterView != null) mFooterView.setState(CustomDragRecyclerFooterView.STATE_LOADING);
        mIsFooterReady = false;
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }
    }

    /**
     * 在刷新时要执行的方法
     */
    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    /**
     * 点击loadMore后要执行的事件
     */
    class footerViewClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            startLoadMore();
        }
    }


    private void updateHeaderHeight(float delta) {
        mHeaderView = (CustomDragHeaderView) layoutManager.findViewByPosition(0);
        if (delta > 0) {//如果是往下拉
            int topMargin = mHeaderView.getTopMargin();
            if (topMargin > maxPullHeight * 0.33333f) {
                delta = delta * 0.5f;
            } else if (topMargin > maxPullHeight * 0.16667F) {
                delta = delta * 0.55f;
            } else if (topMargin > 0) {
                delta = delta * 0.6f;
            } else if (topMargin < 0) {
                delta = delta * 0.6f;//如果没有被完全拖出来
            }
            JLogUtils.d("delta", delta + "");
            JLogUtils.d("topMargin", topMargin + "");
            mHeaderView.setTopMargin(mHeaderView.getTopMargin() + (int) delta);
        } else {//如果是推回去
            if (!mIsRefreshing || mHeaderView.getTopMargin() > 0) {//在刷新的时候不把margin设为负值以在惯性滑动的时候能滑回去
                this.scrollBy(0, (int) delta);//禁止既滚动，又同时减少触摸
//                JLogUtils.i("Alex2", "正在往回推" + delta);
                mHeaderView.setTopMargin(mHeaderView.getTopMargin() + (int) delta);
            }
        }
        if (mHeaderView.getTopMargin() > 0 && !mIsRefreshing) {
            mIsHeaderReady = true;
            mHeaderView.setState(CustomDragHeaderView.STATE_READY);
        }//设置为ready状态
        else if (!mIsRefreshing) {
            mIsHeaderReady = false;
            mHeaderView.setState(CustomDragHeaderView.STATE_NORMAL);
        }//设置为普通状态并且缩回去
    }

    @Override
    public void smoothScrollToPosition(final int position) {
        super.smoothScrollToPosition(position);
        final Timer scrollTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int bottomCardPosition = layoutManager.findLastVisibleItemPosition();
                if (bottomCardPosition < position + 1) {//如果要向下滚
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollBy(0, 50);
                        }
                    });
                } else if (bottomCardPosition > position) {//如果要向上滚
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollBy(0, -50);
                        }
                    });
                } else {
                    if (scrollTimer != null) scrollTimer.cancel();
                }
            }
        };
        scrollTimer.schedule(timerTask, 0, 20);

    }

    private void smoothShowHeader() {
        if (mHeaderView == null) return;
//        if(layoutManager.findFirstVisibleItemPosition()!=0){//如果刷新完毕的时候用户没有注视header
//            mHeaderView.setTopMargin(0);
//            return;
//        }
        if (timer != null) timer.cancel();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mHeaderView == null) {
                    if (timer != null) timer.cancel();
                    return;
                }
                if (mHeaderView.getTopMargin() < 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsRefreshing) {//如果目前是ready状态或者正在刷新状态
                                mHeaderView.setTopMargin(mHeaderView.getTopMargin() + 2);
                            }
                        }
                    });
                } else if (timer != null) {//如果已经完全缩回去了，但是动画还没有结束，就结束掉动画
                    timer.cancel();
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 16);
    }

    private void resetHeaderHeight() {
        if (mHeaderView == null)
            mHeaderView = (CustomDragHeaderView) layoutManager.findViewByPosition(0);
        if (layoutManager.findFirstVisibleItemPosition() != 0) {//如果刷新完毕的时候用户没有注视header
            mHeaderView.setTopMargin(-mHeaderView.getRealHeight());
            return;
        }
        if (timer != null) timer.cancel();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mHeaderView == null) return;
                if (mHeaderView.getTopMargin() > -mHeaderView.getRealHeight()) {//如果header没有完全缩回去
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsHeaderReady || mIsRefreshing) {//如果目前是ready状态或者正在刷新状态
                                int delta = mHeaderView.getTopMargin() / 9;
                                if (delta < 5) delta = 5;
                                if (mHeaderView.getTopMargin() > 0)
                                    mHeaderView.setTopMargin(mHeaderView.getTopMargin() - delta);
                            } else {
                                mHeaderView.setTopMargin(mHeaderView.getTopMargin() - 5);
                            }
                        }
                    });
                } else if (timer != null) {//如果已经完全缩回去了，但是动画还没有结束，就结束掉动画
                    timer.cancel();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHeaderView.setState(mHeaderView.STATE_FINISH);
                        }
                    });
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 10);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int delta = (int) (event.getY() - oldY);
                oldY = event.getY();
                if (layoutManager.findViewByPosition(0) instanceof CustomDragHeaderView) {
                    isHeader = true;
                    updateHeaderHeight(delta);//更新margin高度
                } else {
                    isHeader = false;
                    if (mHeaderView != null && !mIsRefreshing)
                        mHeaderView.setTopMargin(-mHeaderView.getRealHeight());
                }
                break;
//            case MotionEvent.ACTION_DOWN:
//                Log.i("Alex", "touch down");
//                oldY = event.getY();
//                if(timer!=null)timer.cancel();
//                break;
            case MotionEvent.ACTION_UP:

                if (mIsHeaderReady && !mIsRefreshing) startRefresh();
                if (isHeader) resetHeaderHeight();//抬手之后恢复高度
                break;
            case MotionEvent.ACTION_CANCEL:
                break;

        }
        return super.onTouchEvent(event);
    }

    /**
     * 因为设置了子元素的onclickListener之后，ontouch方法的down失效，所以要在分发前获取手指的位置
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.i("Alex", "touch down分发前");
                oldY = ev.getY();
                if (timer != null) timer.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.refreshListener = listener;
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mIsRefreshing = false;
        mEnablePullRefresh = enable;
        if (mHeaderView == null) return;
        if (!mEnablePullRefresh) {
            mHeaderView.setOnClickListener(null);
        } else {
            mHeaderView.setState(CustomDragHeaderView.STATE_NORMAL);
            mHeaderView.setVisibility(VISIBLE);
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopRefresh() {
        if (mIsRefreshing == true) {
            mIsRefreshing = false;
            mIsHeaderReady = false;
            if (mHeaderView == null) return;
            mHeaderView.setState(CustomDragHeaderView.STATE_NORMAL);
            resetHeaderHeight();
        }
    }

    public void forceRefresh() {
        if (mHeaderView == null)
            mHeaderView = (CustomDragHeaderView) layoutManager.findViewByPosition(0);
        if (mHeaderView != null) mHeaderView.setState(CustomDragHeaderView.STATE_REFRESHING);
        mIsRefreshing = true;
        ;
        mIsHeaderReady = false;
        smoothShowHeader();
        if (refreshListener != null) refreshListener.onRefresh();
    }


    private void startRefresh() {
        mIsRefreshing = true;
        mHeaderView.setState(CustomDragHeaderView.STATE_REFRESHING);
        mIsHeaderReady = false;
        if (refreshListener != null) refreshListener.onRefresh();

    }

    public interface OnRefreshListener {
        public void onRefresh();
    }


    /**
     * 适用于本recycler的头部下拉刷新view
     */
    public static class CustomDragHeaderView extends LinearLayout {
        public final static int STATE_NORMAL = 0;
        public final static int STATE_READY = 1;
        public final static int STATE_REFRESHING = 2;
        public final static int STATE_FINISH = 3;

        public float screenDensity;
        private final int ROTATE_ANIM_DURATION = 180;
        private Context mContext;

        private View mContentView;
        private View mProgressBar;
        private ImageView mArrowImageView;
        private TextView mHintTextView;
        private Animation mRotateUpAnim;
        private Animation mRotateDownAnim;

        public CustomDragHeaderView(Context context) {
            super(context);
            initView(context);
        }

        public CustomDragHeaderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView(context);
        }


        private int mState;

        public void setState(int state) {
            if (state == mState)
                return;

            if (state == STATE_REFRESHING) { // 显示进度
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
            } else { // 显示箭头图片
                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            switch (state) {
                case STATE_NORMAL:
                    if (mState == STATE_READY) {
                        mArrowImageView.startAnimation(mRotateDownAnim);
                        mHintTextView.setText("");
                    } else if (mState == STATE_REFRESHING) {//如果是从刷新状态过来
//                        mArrowImageView.clearAnimation();
                        mArrowImageView.setVisibility(INVISIBLE);
                        mHintTextView.setText("");
                    }
                    break;
                case STATE_READY:
                    if (mState != STATE_READY) {
                        mArrowImageView.clearAnimation();
                        mArrowImageView.startAnimation(mRotateUpAnim);
                    }
                    mHintTextView.setText("");
                    break;
                case STATE_REFRESHING:
                    mHintTextView.setText("");
                    break;
                case STATE_FINISH:
                    mArrowImageView.setVisibility(View.VISIBLE);
                    mHintTextView.setText("");
                    break;
                default:
            }

            mState = state;
        }

        public void setTopMargin(int height) {
            if (mContentView == null) return;
            LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
            lp.topMargin = height;
            mContentView.setLayoutParams(lp);
        }

        //
        public int getTopMargin() {
            LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
            return lp.topMargin;
        }

        public void setHeight(int height) {
            if (mContentView == null) return;
            LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
            lp.height = height;
            mContentView.setLayoutParams(lp);
        }

        private int realHeight;

        /**
         * 得到这个headerView真实的高度，而且这个高度是自己定的
         *
         * @return
         */
        public int getRealHeight() {
            return realHeight;
        }

        private void initView(Context context) {
            mContext = context;
            this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));//recyclerView里不加这句话的话宽度就会比较窄
//            this.setBackgroundColor(Color.BLACK);
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_header, null);
            addView(moreView);
            moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            mContentView = moreView.findViewById(R.id.xlistview_header_content);
            LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
            screenDensity = getContext().getResources().getDisplayMetrics().density;//设置屏幕密度，用来px向dp转化
            lp.height = dp2px(screenDensity, HEADER_HEIGHT);//头部高度75dp
            realHeight = lp.height;
            lp.topMargin = -lp.height;
            mContentView.setLayoutParams(lp);
            mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
            mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
            mHintTextView.setPadding(0, dp2px(screenDensity, 3), 0, 0);//不知道为什么这个文字总会向上偏一下，所以要补回来
            mProgressBar = findViewById(R.id.xlistview_header_progressbar);

            mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
            mRotateUpAnim.setFillAfter(true);
            mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
            mRotateDownAnim.setFillAfter(true);
        }
    }

    public static int dp2px(float density, int dp) {
        if (dp == 0) {
            return 0;
        }
        return (int) (dp * density + 0.5f);
    }

    public static class CustomDragRecyclerFooterView extends LinearLayout {

        public final static int STATE_LOADING = 2;
        public final static int STATE_ERROR = 3;
        private Context mContext;
        private View mProgressBar;
        View llContent;

        public CustomDragRecyclerFooterView(Context context) {
            super(context);
            initView(context);
        }

        public CustomDragRecyclerFooterView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView(context);
        }

        public void setState(int state) {
            if (state == STATE_LOADING) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else if (state == STATE_ERROR) {
                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void setEnabled(boolean enabled) {
            if (enabled) {
                llContent.setVisibility(View.VISIBLE);
            } else {
                llContent.setVisibility(View.GONE);
            }
        }

        private void initView(Context context) {
            mContext = context;
            this.setPadding(0, 0, 0, JScreenUtils.dip2px(context, 50));
            this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            this.setGravity(Gravity.CENTER_HORIZONTAL);
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.view_listview_footer, null);
            addView(moreView);
            llContent = moreView.findViewById(R.id.ll_content);
            mProgressBar = moreView.findViewById(R.id.progress_bar);
        }

    }
}
