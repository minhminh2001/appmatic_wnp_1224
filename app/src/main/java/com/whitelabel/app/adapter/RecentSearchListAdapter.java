package com.whitelabel.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.whitelabel.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aaron on 2018/4/8.
 */

public class RecentSearchListAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<String> recentSearchList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Context context;

    public RecentSearchListAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recent_search_list_item, parent, false);
        return new KeywordHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final String keyword = recentSearchList.get(position);

        KeywordHolder keywordHolder = (KeywordHolder)holder;
        if(!isFooter(position)){
            keywordHolder.tvKeyword.setVisibility(View.VISIBLE);
            keywordHolder.tvClearKeyword.setVisibility(View.GONE);

            keywordHolder.tvKeyword.setTag(position);
            keywordHolder.tvKeyword.setText(keyword);
            keywordHolder.tvKeyword.setOnClickListener(this);
        } else {
            keywordHolder.tvKeyword.setVisibility(View.GONE);
            keywordHolder.tvClearKeyword.setVisibility(View.VISIBLE);

            keywordHolder.tvClearKeyword.setTag(position);
            keywordHolder.tvClearKeyword.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return recentSearchList.size();
    }

    public void setRecentSearchList(List<String> list){

        recentSearchList.clear();

        if(list != null && list.size() > 0){
            recentSearchList.addAll(list);
            addFooter(recentSearchList);
        }

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    private void addFooter(List<String> list){
        if(list == null){
            return;
        }

        list.add(getStringById(R.string.recent_search_keyword_clear_data));
    }

    private boolean isFooter(int pos){
        return recentSearchList.size() - 1 == pos ? true : false;
    }

    private String getStringById(int id){
        if(context == null) {
            return null;
        }

        return context.getString(id);
    }

    @Override
    public void onClick(View view) {

        int position = (int)view.getTag();
        boolean isFooter = isFooter(position);
        String searchKeyword = !isFooter ? recentSearchList.get(position) : null;

        if(onItemClickListener != null){
            onItemClickListener.onItemClick(isFooter, searchKeyword);
        }
    }

    static class KeywordHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_keyword)
        public TextView tvKeyword;
        @BindView(R.id.tv_footer)
        public TextView tvClearKeyword;

        public KeywordHolder(View view){
            super(view);

            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(boolean isFooter, String keyword);
    }
}
