package com.whitelabel.app.adapter;

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

public class RecentSearchListAdapter extends RecyclerView.Adapter {

    private List<String> recentSearchList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

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
        keywordHolder.tvKeyword.setText(keyword);
        keywordHolder.tvKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(keyword);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentSearchList.size();
    }

    public void setRecentSearchList(List<String> list){
        if(list == null){
            return;
        }

        recentSearchList.clear();
        recentSearchList.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    static class KeywordHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_keyword)
        public TextView tvKeyword;

        public KeywordHolder(View view){
            super(view);

            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(String keyword);
    }
}
