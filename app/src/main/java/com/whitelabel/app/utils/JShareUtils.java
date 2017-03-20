package com.whitelabel.app.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.whitelabel.app.model.FacebookStoryEntity;

public class JShareUtils {
	public static final int HANDLER_WHAT_FACEBOOK_SUCCESS_OK = 71001;
	public static final int HANDLER_WHAT_FACEBOOK_ERROR_INIT = 71002;
	public static final int HANDLER_WHAT_FACEBOOK_ERROR_UNINSTALLED = 71003;
	public static final int HANDLER_WHAT_FACEBOOK_ERROR_NOINTERNET = 71004;

	private static void callBackByHandler(Handler handler, int what, Object obj) {
		if (handler != null) {
			Message msg = new Message();
			msg.what = what;
			msg.obj = obj;
			handler.sendMessage(msg);
		}
	}
	
	public static void publishFacebookStoryByNativeApp(Activity activity, FacebookStoryEntity entity,ShareDialog shareDialog,Handler shareHandler) {
		JLogUtils.i("share","publishFacebookStoryByNativeApp");

		if (activity == null || entity == null) {
			JLogUtils.i("share","activity == null || entity == null");
			return;
		}
		String link = entity.getLink();
		String applicationname = entity.getApplicationName();
		String description = entity.getDescription();
		String name = entity.getName();
		String caption = entity.getCaption();
		String picture = JImageUtils.getImageServerUrlByWidthHeight(activity,entity.getPicture(),-1,-1);
		JLogUtils.i("share","webURL ="+link+"  描述="+description+"  title="+name+"  imgURL="+picture);

		try {
			if (ShareDialog.canShow(ShareLinkContent.class)) {
				JLogUtils.i("share","ShareDialog.canShow(ShareLinkContent.class)");

				ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
				if(!TextUtils.isEmpty(name)) {
					builder.setContentTitle(name);
				}
				if(!TextUtils.isEmpty(description)) {
					builder.setContentDescription(description);
				}
				if(!TextUtils.isEmpty(link)) {
					builder.setContentUrl(Uri.parse(link));
				}
				if(!TextUtils.isEmpty(picture)) {
					builder.setImageUrl(Uri.parse(picture));
				}
				ShareLinkContent linkContent= builder.build();
				shareDialog.show(linkContent);
			} else {
				JLogUtils.i("share","HANDLER_WHAT_FACEBOOK_ERROR_UNINSTALLED");
				shareHandler.sendEmptyMessage(HANDLER_WHAT_FACEBOOK_ERROR_UNINSTALLED);
			}
		}catch (Exception ex){
			ex.getStackTrace();
			shareHandler.sendEmptyMessage(HANDLER_WHAT_FACEBOOK_ERROR_INIT);
		}
	}
	}
	
	

