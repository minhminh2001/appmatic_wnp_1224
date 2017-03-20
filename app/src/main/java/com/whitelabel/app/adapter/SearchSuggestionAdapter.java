package com.whitelabel.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.SuggestsEntity;

import java.util.ArrayList;

public class SearchSuggestionAdapter extends BaseAdapter implements Filterable {

    private ArrayList<SuggestsEntity> mSuggestsEntityArrayList;
    private ArrayList<SuggestsEntity> mFilterSuggestsEntityArrayList;
    private LayoutInflater inflater;
    private Context mContext;
    private String mSubTitle;

    public SearchSuggestionAdapter(Context context, ArrayList<SuggestsEntity> suggestsEntities) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mFilterSuggestsEntityArrayList = new ArrayList<>();
        this.mSuggestsEntityArrayList = suggestsEntities;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
//                if (!TextUtils.isEmpty(constraint.toString()) && mSuggestsEntityArrayList!=null&&mSuggestsEntityArrayList.size() > 0) {
//                   mSubTitle=constraint.toString();
//
//                    // Retrieve the autocomplete results.
//                    ArrayList<SuggestsEntity> searchData = new ArrayList<>();
//                    for (int i = 0; i < mSuggestsEntityArrayList.size(); i++) {
//                        mSuggestsEntityArrayList.get(i).setSubText(constraint.toString());
//                    }
//                    for (SuggestsEntity item : mSuggestsEntityArrayList) {
//                        JLogUtils.d("jay", "constraint=" + constraint);
//                        JLogUtils.d("jay", "--title=" + item.getTitle());
//                        SuggestsEntity bean = new SuggestsEntity();
//                        bean.setRow_type(item.getRow_type());
//                        bean.setId(item.getId());
//                        bean.setTitle(item.getTitle());
//                        bean.setSubText(constraint.toString());
//                        searchData.add(bean);
//                    }
//                    // Assign the data to the FilterResults
//                    mFilterSuggestsEntityArrayList = searchData;
//                    filterResults.count = searchData.size();
//                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                if (results.values != null) {
//                    mFilterSuggestsEntityArrayList = (ArrayList<SuggestsEntity>) results.values;
//                    notifyDataSetChanged();
//                }
            }
        };
        return filter;
    }

    @Override
    public int getCount() {
        return mSuggestsEntityArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSuggestsEntityArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        SuggestionsViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.suggest_item, parent, false);
            viewHolder = new SuggestionsViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SuggestionsViewHolder) convertView.getTag();
        }

        SuggestsEntity currentListData = (SuggestsEntity) getItem(position);
//        String subTitle = currentListData.getSubText();
        String title = currentListData.getTitle();
//        if (!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(subTitle)) {
//                SpannableString spannableString = JDataUtils.keyWordHighLighting(Color.BLACK, title, subTitle);
//                viewHolder.textView.setText(spannableString);
//        }
        viewHolder.textView.setText(title);

//            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mOnSuggestionsImageClickListener != null) {
//                        mOnSuggestionsImageClickListener.onSuggestionsImageClick(SearchSuggestionAdapter.this, v, position, position);
//                    }
//                }
//            });



        return convertView;
    }

    public void setOnSuggestionsImageClickListener(OnSuggestionsImageClickListener listener) {
        mOnSuggestionsImageClickListener = listener;
    }

    private class SuggestionsViewHolder {

        TextView textView;
        ImageView imageView;

        public SuggestionsViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.suggestion_text);
//            imageView = (ImageView) convertView.findViewById(R.id.suggestion_icon);
        }
    }

    private OnSuggestionsImageClickListener mOnSuggestionsImageClickListener;

    public interface OnSuggestionsImageClickListener {
        void onSuggestionsImageClick(SearchSuggestionAdapter parent, View view, int position, long id);
    }
}