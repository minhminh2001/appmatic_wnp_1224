package com.whitelabel.app.widget;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;

/**
 * 自定义倒计时控件
 * 布局里正常引用，
 * init（）传倒计时的时间（秒），和回调监听
 * start()开始计时
 */
public class CustomCountdown extends RelativeLayout {

	private View view;
	public Chronometer chronometer;
	private TextView tv_dd_desc,tv_dd,tv_dd2,tv_hh,tv_hh2,tv_ss,tv_ss2,tv_mm,tv_mm2,tv_dd_temp,tv_hh_temp,tv_mm_temp,tv_ss_temp;
	private OnCountDownListenter onCountDownListenter;

	public CustomCountdown(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view=inflater.inflate(R.layout.time_con, this);
		chronometer = (Chronometer) view.findViewById(R.id.curation_chronometer);

		tv_dd_desc=(TextView)view.findViewById(R.id.tv_dd_desc);
		tv_dd=(TextView)view.findViewById(R.id.tv_dd);
		tv_hh=(TextView)view.findViewById(R.id.tv_hh);
		tv_mm=(TextView)view.findViewById(R.id.tv_mm);
		tv_ss=(TextView)view.findViewById(R.id.tv_ss);
		tv_dd2=(TextView)view.findViewById(R.id.tv_dd2);
		tv_hh2=(TextView)view.findViewById(R.id.tv_hh2);
		tv_mm2=(TextView)view.findViewById(R.id.tv_mm2);
		tv_ss2=(TextView)view.findViewById(R.id.tv_ss2);

	}
	public interface OnCountDownListenter{
		void onListenter(boolean isOver, long timeLeft);
	}

	//最小单位秒
	public void init(long allTime,OnCountDownListenter onCountDownListenter){
		chronometer.setBase(allTime);
		chronometer.setOnChronometerTickListener(tickListener);
		this.onCountDownListenter=onCountDownListenter;
	}

	private long pastTime=0;
	public void start(){
		pastTime= SystemClock.elapsedRealtime();
		chronometer.start();
	}
	public void stop(){
		chronometer.stop();
	}
	Chronometer.OnChronometerTickListener tickListener = new Chronometer.OnChronometerTickListener() {
		@Override
		public void onChronometerTick(Chronometer chronometers) {
			long a=(SystemClock.elapsedRealtime())-pastTime;
			final long mainTime= chronometers.getBase()-a/1000;
			if(mainTime<1){
				// finsh the clock
				if(onCountDownListenter!=null){
					chronometer=null;
					onCountDownListenter.onListenter(true,mainTime);
				}
				return;
			}
			setDD(mainTime);
			setHH(mainTime);
			setMM(mainTime);
			setSS(mainTime);
			if(onCountDownListenter!=null){
				onCountDownListenter.onListenter(false,mainTime);
			}
		}

	};
	private void setDD(long time){
		String str = getDay(time);
		if(str.equals(tv_dd.getText())){
			return;
		}
		if("01".equals(str)){
			tv_dd_desc.setText(getResources().getString(R.string.day));
		}else{
			tv_dd_desc.setText(getResources().getString(R.string.days));
		}
		if(TextUtils.isEmpty(tv_dd.getText().toString())){
			// onStop clean All timeText后,打开此activity时 天时分会立即加载
			tv_dd.setText(str);
			return;
		}
		final Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.countdown_slide_to_top_in);
		final Animation in2 = AnimationUtils.loadAnimation(getContext(),R.anim.countdown_slide_to_top_out);
		tv_dd2.clearAnimation();
		tv_dd.clearAnimation();
		tv_dd2.setText(str);
		tv_dd2.startAnimation(in);
		tv_dd2.setVisibility(View.VISIBLE);
		in.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				tv_dd.startAnimation(in2);
				tv_dd.setVisibility(View.GONE);
				tv_dd_temp=tv_dd;
				tv_dd=tv_dd2;
				tv_dd2=tv_dd_temp;
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});
	}
	private void setHH(long time){
		String str = getHour(time);
		if(str.equals(tv_hh.getText())){
			return;
		}
		if(TextUtils.isEmpty(tv_hh.getText().toString())){
			// onStop clean All timeText后,打开此activity时 天时分会立即加载
			tv_hh.setText(str);
			return;
		}
		final Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.countdown_slide_to_top_in);
		final Animation in2 = AnimationUtils.loadAnimation(getContext(),R.anim.countdown_slide_to_top_out);
		tv_hh2.clearAnimation();
		tv_hh.clearAnimation();
		tv_hh2.setText(str);
		tv_hh2.startAnimation(in);
		tv_hh2.setVisibility(View.VISIBLE);
		in.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				tv_hh.startAnimation(in2);
				tv_hh.setVisibility(View.GONE);
				tv_hh_temp=tv_hh;
				tv_hh=tv_hh2;
				tv_hh2=tv_hh_temp;
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});
	}
	private void setMM(long time){
		String str=getMin(time);
		if(str.equals(tv_mm.getText())){
			return;
		}
		if(TextUtils.isEmpty(tv_mm.getText().toString())){
			// onStop clean All timeText后,打开此activity时 天时分会立即加载
			tv_mm.setText(str);
			return;
		}
		final Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.countdown_slide_to_top_in);
		final Animation in2 = AnimationUtils.loadAnimation(getContext(),R.anim.countdown_slide_to_top_out);
		tv_mm2.clearAnimation();
		tv_mm.clearAnimation();
		tv_mm2.setText(str);
		tv_mm2.startAnimation(in);
		tv_mm2.setVisibility(View.VISIBLE);
		in.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				tv_mm.startAnimation(in2);
				tv_mm.setVisibility(View.GONE);
				tv_mm_temp = tv_mm;
				tv_mm = tv_mm2;
				tv_mm2 = tv_mm_temp;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});
	}


	//activity onStop时需把旧的textVie清空,防止回来时秒数上下值不关联
	public void cleanAllTimeText(){
		if(tv_dd!=null) {
			tv_dd.setText("");
			tv_hh.setText("");
			tv_mm.setText("");
			tv_ss.setText("");
		}
	}
	/**
	 * 动画效果 in


	 <translate xmlns:android="http://schemas.android.com/apk/res/android"

	 android:fromYDelta="-90%p"

	 android:toYDelta="0"

	 android:duration="600">

	 </translate>
	 *
	 */
	/**
	 *  动画效果 out

	 <translate xmlns:android="http://schemas.android.com/apk/res/android"

	 android:fromYDelta="0"

	 android:toYDelta="90%p"

	 android:duration="600">

	 </translate>
	 *
	 */
	private void setSS(long time){
		String str=getSec(time);
		if(str.equals(tv_ss.getText())){
			return;
		}

		final Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.countdown_slide_to_top_in);
		final Animation in2 = AnimationUtils.loadAnimation(getContext(),R.anim.countdown_slide_to_top_out);
		tv_ss2.clearAnimation();
		tv_ss.clearAnimation();
		tv_ss2.setText(str);
		tv_ss2.startAnimation(in);
		tv_ss2.setVisibility(View.VISIBLE);
		in.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				tv_ss.startAnimation(in2);
				tv_ss.setVisibility(View.GONE);
				tv_ss_temp = tv_ss;
				tv_ss = tv_ss2;
				tv_ss2 = tv_ss_temp;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});
	}


	//  miss is sec
	public static String getDay(long miss){
		long dd=miss/3600/24;
		String ddStr=dd>9?dd+"":"0"+dd;
		return ddStr;
	}
	public static String getHour(long miss){
		long hh=(miss % (3600*24)   )/(3600);
		String hhStr=hh>9?hh+"":"0"+hh;
		return hhStr;
	}
	public static String getMin(long miss){
		long mm=(miss % (3600))/60;
		String  mmStr=mm>9?mm+"":"0"+mm;
		return mmStr;
	}
	public static String getSec(long miss){
		long ss=(miss % (60))    /1;
		String ssStr=ss>9?ss+"":"0"+ss;
		return ssStr;
	}

}
