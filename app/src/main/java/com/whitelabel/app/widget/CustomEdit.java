package com.whitelabel.app.widget;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.callback.CustomFocusChangeCallBack;
import com.whitelabel.app.utils.JDataUtils;


/**
 * Created by imaginato on 2015/7/24.
 */
public class CustomEdit extends LinearLayout implements View.OnFocusChangeListener, View.OnClickListener  {
    private CustomEditText myEditText;
    private TextView myText1,myText2;
    private  String hint="Enter";
    private boolean isPassword;
    private boolean isNumber;
    private  boolean notNull;
    private Context context;
    private int translateX;
    private ImageView imageViewClear;
    private View view;
    public CustomEdit(Context context) {
        super(context);
    }

    public CustomEdit(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context=context;
        //获得属性设置
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToolBar);
//        int textColor = a.getColor(R.styleable.MyView_textColor,0XFFFFFFFF);
//        float textSize = a.getDimension(R.styleable.MyView_textSize, 36);
        hint=a.getString(R.styleable.ToolBar_myHint);
        notNull=a.getBoolean(R.styleable.ToolBar_notNull, false);
        isPassword=a.getBoolean(R.styleable.ToolBar_isPassword, false);
        isNumber=a.getBoolean(R.styleable.ToolBar_isNumber, false);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.custom_edit, this);
        imageViewClear=(ImageView)view.findViewById(R.id.imageViewClear);
        imageViewClear.setOnClickListener(this);
        myEditText= (CustomEditText) view.findViewById(R.id.my_editText);
        if(isPassword){
            myEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        if(isNumber){
            myEditText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_CLASS_NUMBER);
        }
        myText1= (TextView) view.findViewById(R.id.my_text1);
        myText2=(TextView) view.findViewById(R.id.my_text2);
        myEditText.setOnFocusChangeListener(this);

        myEditText.setHint(hint);
        myText1.setText(hint);
        myText2.setText(hint);

        myEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (myEditText.getText().length() != 0&&isFocus&&myEditText.isEnabled()) {
                    imageViewClear.setVisibility(VISIBLE);
                    imageViewClear.setEnabled(true);
                } else {
                    imageViewClear.setVisibility(INVISIBLE);
                    imageViewClear.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        a.recycle();
    }
    private CustomFocusChangeCallBack customFocusChangeCallBack;
    public void setCustomFocusChangeCallBack(CustomFocusChangeCallBack customFocusChangeCallBack){
        this.customFocusChangeCallBack=customFocusChangeCallBack;
    }
    private boolean isFocus=false;
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(customFocusChangeCallBack!=null){
            customFocusChangeCallBack.focusChangeCalBack(v,hasFocus);
        }
        if(hasFocus){
            if(v.getId()==R.id.my_editText&&myEditText.isEnabled()){
                isFocus=true;
                if (myEditText.getText().length()!=0){
                    imageViewClear.setVisibility(VISIBLE);
                    imageViewClear.setEnabled(true);
                }else{
                    imageViewClear.setVisibility(INVISIBLE);
                    imageViewClear.setEnabled(false);
                }
                AnimationSet set=EditAnimation();
                myText2.setText(hint);
                if (myEditText.getText().toString().trim().equals("")) {
                    myText2.setVisibility(View.INVISIBLE);
                    myEditText.setHint("");
                    myText1.startAnimation(set);
                } else {
                    myText2.setVisibility(View.VISIBLE);
                    myText2.setTextColor(getResources().getColor(R.color.blue5097DA));
                }

            }
        }else {//添加失去焦点验证
            if(v.getId()==R.id.my_editText&&myEditText.isEnabled()){
                isFocus=false;
                myText1.clearAnimation();
                myText2.setVisibility(VISIBLE);
                myText2.setTextColor(getResources().getColor(R.color.label_saved));
                myEditText.setHint(hint);
                if (notNull) {
                    isEmpty();
                }else if (myEditText.getText().toString().trim().equals("")){
                    myText2.setVisibility(INVISIBLE);
                }
                imageViewClear.setVisibility(INVISIBLE);
                imageViewClear.setEnabled(false);
            }
        }
    }

    public void setEnabled(boolean enable){
        myEditText.setEnabled(enable);
    }

    public AnimationSet EditAnimation(){
        int parentHeight=view.getHeight();
        int text2Height=myText2.getHeight();
        translateX=parentHeight-text2Height-JDataUtils.dp2Px(5);

        AnimationSet set = new AnimationSet(true);
        set.setFillAfter(true);
        Animation tran = new TranslateAnimation(0, 0, 0, -translateX);
        //平移
        tran.setDuration(300);
        //渐变
        Animation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(300);
        set.addAnimation(tran);
        set.addAnimation(alpha);
        return set;
    }
    //非空验证
    public boolean isEmpty(){
        if (TextUtils.isEmpty(myEditText.getText().toString())) {
            myEditText.setHint(hint);
            myText2.clearAnimation();
            myText1.clearAnimation();
            myText2.setVisibility(View.VISIBLE);
            myText2.setTextColor(getResources().getColor(R.color.redC2060A));
            myText2.setText(getResources().getString(R.string.required_field));

            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        myEditText.setText("");
        imageViewClear.setVisibility(INVISIBLE);
        imageViewClear.setEnabled(false);
    }

    public void setErrorMsg(String text){
        if(TextUtils.isEmpty(text)){
            myText2.setText(hint);
            myText2.setVisibility(View.VISIBLE);
            myText2.setTextColor(getResources().getColor(R.color.blue5097DA));
        }else {
            myEditText.setHint(hint);
            myText2.setVisibility(View.VISIBLE);
            myText2.setText(text);
            myText2.setTextColor(getResources().getColor(R.color.redC2060A));
        }
    }

    public void setText(String text){
        if(!TextUtils.isEmpty(text)){
            myText2.setVisibility(VISIBLE);
            myText2.setTextColor(myText2.getCurrentTextColor());
            myEditText.setHint(hint);
        }
        myEditText.setText(text);

    }

    public EditText getEditText(){
        return myEditText;
    }

    public String getText(){

        return myEditText.getText().toString();
    }
}
