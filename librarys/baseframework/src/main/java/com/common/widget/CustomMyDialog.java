package com.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.common.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by imaginato on 2015/11/9.
 */
public class CustomMyDialog extends Dialog {
    public CustomMyDialog(Context context) {
        super(context);
    }

    public CustomMyDialog(Context context, int theme) {
        super(context, theme);
    }
    public static class Builder {
        private List<String> mItems;
        private ArrayAdapter<String> itemAdapter;
        private AdapterView.OnItemClickListener mItemListener;
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setItems(List<String> items, final AdapterView.OnItemClickListener itemListener) {
            this.mItems = items;
            this.mItemListener = itemListener;
            return this;
        }
        //  如果有setItemAdapter,, setItems里的items可以为空
        public Builder setItemAdapter(ArrayAdapter itemAdapter) {
            this.itemAdapter = itemAdapter;
            return this;
        }
//        public CustomDialog create() {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            // instantiate the dialog with the custom Theme
//            final CustomDialog dialog = new CustomDialog(context,R.style.alertDialog,"");
//            View layout = inflater.inflate(R.layout.dialog_normal_layout, null);
//            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            //title
//            if(!TextUtils.isEmpty(title)) {
//                TextView titleView=(TextView) layout.findViewById(R.id.title);
//                titleView.setText(title);
//                titleView.setVisibility(View.VISIBLE);
//            }
//
//            //confirm button
//            if (positiveButtonText != null) {
//                ((TextView) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
//                if (positiveButtonClickListener != null) {
//                    ( layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View v) {
//                                    positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
//                                }
//                            });
//                }
//            } else {
//                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
//            }
//            //cancel button
//            if (negativeButtonText != null) {
//                ((TextView) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
//                if (negativeButtonClickListener != null) {
//                    ( layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View v) {
//                                    negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
//                                }
//                            });
//                }
//            } else {
//                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
//            }
//            //message
//            if (message != null) {
//                ((TextView) layout.findViewById(R.id.message)).setText(message);
//            } else if (contentView != null) {
//                ((LinearLayout) layout.findViewById(R.id.content))
//                        .removeAllViews();
//                ((LinearLayout) layout.findViewById(R.id.content))
//                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//            }
//            //list
//            if(  (mItems!=null&&mItems.size()>0)||itemAdapter!=null) {
//                layout.findViewById(R.id.ll_listview).setVisibility(View.VISIBLE);
//                layout.findViewById(R.id.content).setVisibility(View.GONE);
//                ListView listview=(ListView)layout.findViewById(R.id.listview);
//                if(this.itemAdapter!=null){
//                    listview.setAdapter(this.itemAdapter);
//                }else{
//                    listview.setAdapter(new ItemAdapter(dialog.getContext(),mItems));
//                }
//                listview.setOnItemClickListener(mItemListener);
//            }
//
//            dialog.setContentView(layout);
//            return dialog;
//        }
    }

    public static class ItemAdapter extends ArrayAdapter<String> {
        public List<String> strings=new ArrayList<>();
        private Context context;
        public ItemAdapter(Context context,List<String> strings) {
            super(context,0,strings);
            this.strings=strings;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=convertView;
            ItemHolder holder=null;
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.adapter_dialog_item,null);
                holder=new ItemHolder(view);
                view.setTag(holder);
            }else{
                holder= (ItemHolder) view.getTag();
            }
            holder.tv_item.setText(strings.get(position));
            return view;
        }

        private  class ItemHolder extends RecyclerView.ViewHolder{
            private TextView tv_item;
            public ItemHolder(View view) {
                super(view);
                tv_item=(TextView)view.findViewById(R.id.tv_item);
            }
        }

    }
}
