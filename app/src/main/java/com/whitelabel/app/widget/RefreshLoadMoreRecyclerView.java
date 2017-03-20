package com.whitelabel.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.adapter.OrderListRecyclerViewAdapter;
import com.whitelabel.app.utils.JLogUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alex on 2016/1/27.
 */
public class RefreshLoadMoreRecyclerView extends RecyclerView {
    private int footerHeight = -1;
    LinearLayoutManager layoutManager;
    // -- footer view
    private CustomDragRecyclerFooterView mFooterView;
    private boolean mEnablePullLoad;
    private boolean mPullLoading;
    private boolean isBottom;
    private boolean mIsFooterReady = false;
    private LoadMoreListener loadMoreListener;

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
    private boolean firstTime=false;


    public RefreshLoadMoreRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public RefreshLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshLoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void setAdapter(OrderListRecyclerViewAdapter adapter){
        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    public boolean ismPullLoading() {
        return mPullLoading;
    }

    public boolean ismIsRefreshing() {
        return mIsRefreshing;
    }

    private void updateFooterHeight(float delta) {
        if(mFooterView==null)return;
        int bottomMargin = mFooterView.getBottomMargin();
        if(delta>50)delta = delta/6;
        if(delta>0) {//越往下滑越难滑
            if(bottomMargin>maxPullHeight)delta = delta*0.65f;
            else if(bottomMargin>maxPullHeight * 0.83333f)delta = delta*0.7f;
            else if(bottomMargin>maxPullHeight * 0.66667f)delta = delta*0.75f;
            else if(bottomMargin>maxPullHeight >> 1)delta = delta*0.8f;
            else if(bottomMargin>maxPullHeight * 0.33333f)delta = delta*0.85f;
            else if(bottomMargin>maxPullHeight * 0.16667F && delta > 20)delta = delta*0.2f;//如果是因为惯性向下迅速的俯冲
            else if(bottomMargin>maxPullHeight * 0.16667F)delta = delta*0.9f;
        }

        int height = mFooterView.getBottomMargin() + (int) (delta+0.5);

        if (mEnablePullLoad && !mPullLoading) {
            if (height > 1){//立即刷新
                mFooterView.setState(CustomDragRecyclerFooterView.STATE_READY);
                mIsFooterReady = true;
            } else {
                mFooterView.setState(CustomDragRecyclerFooterView.STATE_NORMAL);
                mIsFooterReady = false;
//
            }
        }
        mFooterView.setBottomMargin(height);

    }

    private void resetFooterHeight() {
        int bottomMargin=0;
        if (mFooterView!=null){
             bottomMargin = mFooterView.getBottomMargin();
        }
        if (bottomMargin > 20) {
            this.smoothScrollBy(0,-bottomMargin);
            if(mIsFooterReady){
                startLoadMore();
            }
        }
    }


    public void setLoadMoreListener(LoadMoreListener listener){
        this.loadMoreListener = listener;
    }

    public void initView(Context context){
        layoutManager = new LinearLayoutManager(context);//自带layoutManager，请勿设置
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        layoutManager.offsetChildrenVertical(height*2);//预加载2/3的卡片
        this.setLayoutManager(layoutManager);
        maxPullHeight = dp2px(getContext().getResources().getDisplayMetrics().density,MAX_PULL_LENGTH);//最多下拉150dp
        this.footerClickListener = new footerViewClickListener();
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if(isBottom){
                            startLoadMore();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if (mFooterView!=null){
                            mFooterView.setState(CustomDragRecyclerFooterView.STATE_READY);
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:;
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                if(lastItemPosition == layoutManager.getItemCount()-1 && mEnablePullLoad) {//如果到了最后一个
                    isBottom = true;
                    mFooterView = (CustomDragRecyclerFooterView)layoutManager.findViewByPosition(layoutManager.findLastVisibleItemPosition());
                    if(footerHeight==-1 && mFooterView!=null){
                        mFooterView.show();
                        mFooterView.setState(CustomDragRecyclerFooterView.STATE_NORMAL);
                        mFooterView.loading();
                    }
//                    updateFooterHeight(dy);
                }else {
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
    public void setPullLoadEnable(boolean enable) {
        mPullLoading = false;
        mEnablePullLoad = enable;
        if(adapter!=null)adapter.setPullLoadMoreEnable(enable);//adapter和recyclerView要同时设置
        if(mFooterView==null)return;
        if (!mEnablePullLoad) {
            mFooterView.hide();
            mFooterView.setBottomMargin(0);
            //make sure "pull up" don't show a line in bottom when listview with one page
        } else {
            mFooterView.show();
            mFooterView.setState(CustomDragRecyclerFooterView.STATE_NORMAL);
            mFooterView.setVisibility(VISIBLE);
//            mFooterView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startLoadMore();
//                }
//            });
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
            if(mFooterView==null)return;
//            mFooterView.show();
//            mFooterView.hide();
            mFooterView.setState(CustomDragRecyclerFooterView.STATE_ERROR);

        }
    }
    private void startLoadMore() {
        if(mPullLoading)return;
        mPullLoading = true;
        if(mFooterView!=null)mFooterView.setState(CustomDragRecyclerFooterView.STATE_LOADING);
        mIsFooterReady = false;
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }
    }

    /**
     * 在刷新时要执行的方法
     */
    public interface LoadMoreListener{
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
        if(delta>0){//如果是往下拉
            int topMargin = mHeaderView.getTopMargin();
            if(topMargin>maxPullHeight * 0.33333f){
                delta = delta*0.5f;
            }else if(topMargin>maxPullHeight * 0.16667F){
                delta = delta*0.55f;
            }else if(topMargin>0){
                delta = delta*0.6f;
            }else if(topMargin<0){
                delta = delta*0.6f;//如果没有被完全拖出来
            }
            JLogUtils.d("delta",delta+"");
            JLogUtils.d("topMargin",topMargin+"");
            mHeaderView.setTopMargin(mHeaderView.getTopMargin() + (int)delta);
        } else{//如果是推回去
            if(!mIsRefreshing || mHeaderView.getTopMargin()>0) {//在刷新的时候不把margin设为负值以在惯性滑动的时候能滑回去
                this.scrollBy(0, (int) delta);//禁止既滚动，又同时减少触摸
//                JLogUtils.i("Alex2", "正在往回推" + delta);
                mHeaderView.setTopMargin(mHeaderView.getTopMargin() + (int) delta);
            }
        }
        if(mHeaderView.getTopMargin()>0 && !mIsRefreshing){
            mIsHeaderReady = true;
            mHeaderView.setState(CustomDragHeaderView.STATE_READY);
        }//设置为ready状态
        else if(!mIsRefreshing){
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
                if(bottomCardPosition<position+1){//如果要向下滚
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollBy(0,50);
                        }
                    });
                }else if(bottomCardPosition>position){//如果要向上滚
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollBy(0,-50);
                        }
                    });
                }else {
                    if(scrollTimer!=null)scrollTimer.cancel();
                }
            }
        };
        scrollTimer.schedule(timerTask,0,20);

    }

    private void smoothShowHeader(){
        if(mHeaderView==null)return;
//        if(layoutManager.findFirstVisibleItemPosition()!=0){//如果刷新完毕的时候用户没有注视header
//            mHeaderView.setTopMargin(0);
//            return;
//        }
        if(timer!=null)timer.cancel();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(mHeaderView==null){
                    if(timer!=null)timer.cancel();
                    return;
                }
                if(mHeaderView.getTopMargin()<0){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsRefreshing) {//如果目前是ready状态或者正在刷新状态
                                mHeaderView.setTopMargin(mHeaderView.getTopMargin() +2);
                            }
                        }
                    });
                } else if(timer!=null){//如果已经完全缩回去了，但是动画还没有结束，就结束掉动画
                    timer.cancel();
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,0,16);
    }
    private void resetHeaderHeight() {
        if(mHeaderView==null)mHeaderView = (CustomDragHeaderView) layoutManager.findViewByPosition(0);
        if(layoutManager.findFirstVisibleItemPosition()!=0){//如果刷新完毕的时候用户没有注视header
            mHeaderView.setTopMargin(-mHeaderView.getRealHeight());
            return;
        }
        if(timer!=null)timer.cancel();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(mHeaderView==null)return;
                if(mHeaderView.getTopMargin()>-mHeaderView.getRealHeight()){//如果header没有完全缩回去
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
                } else if(timer!=null){//如果已经完全缩回去了，但是动画还没有结束，就结束掉动画
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
        timer.scheduleAtFixedRate(timerTask,0,10);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int delta = (int)(event.getY()-oldY);
                oldY = event.getY();
                if (layoutManager.findViewByPosition(0) instanceof CustomDragHeaderView) {
                    isHeader = true;
                    updateHeaderHeight(delta);//更新margin高度
                }else{
                    isHeader = false;
                    if(mHeaderView!=null && !mIsRefreshing)mHeaderView.setTopMargin(-mHeaderView.getRealHeight());
                }
                break;
//            case MotionEvent.ACTION_DOWN:
//                Log.i("Alex", "touch down");
//                oldY = event.getY();
//                if(timer!=null)timer.cancel();
//                break;
            case MotionEvent.ACTION_UP:

                if(mIsHeaderReady && !mIsRefreshing)startRefresh();
                if(isHeader)resetHeaderHeight();//抬手之后恢复高度
                break;
            case MotionEvent.ACTION_CANCEL:
                break;

        }
        return super.onTouchEvent(event);
    }

    /**
     * 因为设置了子元素的onclickListener之后，ontouch方法的down失效，所以要在分发前获取手指的位置
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

    public void setOnRefreshListener(OnRefreshListener listener){
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
        if(mHeaderView==null)return;
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
            if(mHeaderView==null)return;
            mHeaderView.setState(CustomDragHeaderView.STATE_NORMAL);
            resetHeaderHeight();
        }
    }

    public void forceRefresh(){
        if(mHeaderView==null)mHeaderView = (CustomDragHeaderView) layoutManager.findViewByPosition(0);
        if(mHeaderView!=null)mHeaderView.setState(CustomDragHeaderView.STATE_REFRESHING);
        mIsRefreshing = true;;
        mIsHeaderReady = false;
        smoothShowHeader();
        if (refreshListener != null)refreshListener.onRefresh();
    }


    private void startRefresh() {
        mIsRefreshing = true;
        mHeaderView.setState(CustomDragHeaderView.STATE_REFRESHING);
        mIsHeaderReady = false;
        if (refreshListener != null) refreshListener.onRefresh();

    }

    public interface OnRefreshListener{
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
                    }
                    else if (mState == STATE_REFRESHING) {//如果是从刷新状态过来
//                        mArrowImageView.clearAnimation();
                        mArrowImageView.setVisibility(INVISIBLE);
                        mHintTextView.setText("");
                    }
                    break;
                case STATE_READY:
                    if (mState != STATE_READY){
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
            if (mContentView==null) return ;
            LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
            lp.topMargin = height;
            mContentView.setLayoutParams(lp);
        }
        //
        public int getTopMargin() {
            LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
            return lp.topMargin;
        }

        public void setHeight(int height){
            if (mContentView==null) return ;
            LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
            lp.height = height;
            mContentView.setLayoutParams(lp);
        }
        private int realHeight;
        /**
         * 得到这个headerView真实的高度，而且这个高度是自己定的
         * @return
         */
        public int getRealHeight(){
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
            LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
            screenDensity = getContext().getResources().getDisplayMetrics().density;//设置屏幕密度，用来px向dp转化
            lp.height = dp2px(screenDensity,HEADER_HEIGHT);//头部高度75dp
            realHeight = lp.height;
            lp.topMargin = -lp.height;
            mContentView.setLayoutParams(lp);
            mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
            mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
            mHintTextView.setPadding(0,dp2px(screenDensity,3),0,0);//不知道为什么这个文字总会向上偏一下，所以要补回来
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
        public final static int STATE_NORMAL = 0;
        public final static int STATE_READY = 1;
        public final static int STATE_LOADING = 2;
        public final static int STATE_ERROR = 3;

        private Context mContext;
        private View mContentView;
        private View mProgressBar;
        private TextView mHintView;

        public CustomDragRecyclerFooterView(Context context) {
            super(context);
            initView(context);
        }

        public CustomDragRecyclerFooterView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView(context);
        }


        public void setState(int state) {
            mHintView.setVisibility(View.INVISIBLE);
            if (state == STATE_READY) {
                mContentView.setVisibility(VISIBLE);
                mHintView.setVisibility(View.VISIBLE);

            } else if (state == STATE_LOADING) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else if(state == STATE_ERROR){
                mContentView.setVisibility(GONE);
                mProgressBar.setVisibility(GONE);
            }
            else {
//                mHintView.setVisibility(View.VISIBLE);
//                mHintView.setText(R.string.xlistview_footer_hint_normal);
//                mHintView.setText("");
            }
        }

        public void setBottomMargin(int height) {
            if (height < 0) return ;
            LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
            lp.bottomMargin = height;
            mContentView.setLayoutParams(lp);
        }

        public int getBottomMargin() {
            LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
            return lp.bottomMargin;
        }


        /**
         * normal status
         */
        public void normal() {
            mHintView.setVisibility(View.VISIBLE);
//            mProgressBar.setVisibility(View.GONE);客户修改需求
        }


        /**
         * loading status
         */
        public void loading() {
            mHintView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * hide footer when disable pull load more
         */
        public void hide() {
            LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
            lp.height = 1;//这里如果设为0那么layoutManger就会抓不到
            mContentView.setLayoutParams(lp);
            mContentView.setBackgroundColor(Color.BLACK);//这里的颜色要和自己的背景色一致
        }

        /**
         * show footer
         */
        public void show() {
            LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
            lp.height = LayoutParams.WRAP_CONTENT;
            lp.width =  LayoutParams.MATCH_PARENT;
            mContentView.setLayoutParams(lp);
            mContentView.setBackgroundColor(Color.WHITE);//这里的颜色要和自己的背景色一致
        }

        private void initView(Context context) {
            mContext = context;
            this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_customdragfooterview, null);
            addView(moreView);
            moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            mContentView = moreView.findViewById(R.id.rlContentView);
            mProgressBar = moreView.findViewById(R.id.pbContentView);
            mProgressBar.setVisibility(GONE);
            mHintView = (TextView)moreView.findViewById(R.id.ctvContentView);
        }
    }
}
