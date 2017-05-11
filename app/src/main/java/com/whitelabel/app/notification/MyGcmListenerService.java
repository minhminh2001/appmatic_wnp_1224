package com.whitelabel.app.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.NotificationDetailActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.NotificationReceivedEntity;
import com.whitelabel.app.utils.FirebaseEventUtils;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;

import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate mHandler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");

        JLogUtils.d(TAG, "From: " + from);
        JLogUtils.d(TAG, "Message: " + data);

        createNotification(message);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.icon_v1 : R.mipmap.icon_v1;
    }

    private static int requestId = 1;

    public void createNotification(String message) {
        final NotificationReceivedEntity entity = new Gson().fromJson(message, NotificationReceivedEntity.class);
//        try {
//            FirebaseEventUtils.getInstance().customizedNotificationReceive(getApplicationContext(), entity.getTitle());
//        } catch (Exception ex) {
//            ex.getMessage();
//            JLogUtils.e(TAG, ex.getMessage());
//        }

        final NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setSmallIcon(getNotificationIcon())
                .setColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor())
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(entity.getTitle())
                .setContentText(entity.getMessage())
                .setContentIntent(createIntent(entity))
                .setAutoCancel(true)
                .setTicker(entity.getTitle())
                .setPriority(NotificationCompat.PRIORITY_LOW);

        if (!TextUtils.isEmpty(entity.getBanner())) {
            //Glide has to return bitmap to the main thread
//
            JImageUtils.downloadImageFromServerListener(this, entity.getBanner(), -1, -1, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                    NotificationCompat.BigPictureStyle bps = new NotificationCompat.BigPictureStyle().bigPicture(resource);
                    bps.setSummaryText(entity.getMessage());
                    nb.setStyle(bps);

                    sendNotification(entity, nb);
                }

                @Override
                public void onLoadFailed(Exception ex, Drawable errorDrawable) {
                    ex.getMessage();
                    JLogUtils.e(TAG, ex.getMessage());
                    sendNotification(entity, nb);
                }
            });
        } else {
            sendNotification(entity, nb);
        }
    }

    private PendingIntent createIntent(NotificationReceivedEntity entity) {
        Intent intent = new Intent(MyGcmListenerService.this, NotificationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("where", "system notification");
        bundle.putSerializable("data", entity);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyGcmListenerService.this, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        requestId++;
        return pendingIntent;
    }

    private void sendNotification(NotificationReceivedEntity entity, NotificationCompat.Builder nb) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            final int ringerMode = am.getRingerMode();
            /*
      Create and show a simple notification containing the received GCM message.

      */
            int VIBRATE = 1;
            int SOUND = 2;
            if (ringerMode == VIBRATE) {
                long v1[] = {0, 100, 200, 300};
                nb.setVibrate(v1);
            } else if (ringerMode == SOUND) {
                nb.setSound(defaultSoundUri);
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
            JLogUtils.e(TAG, ex.getMessage());
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        nb.setSound(alarmSound, AudioManager.STREAM_MUSIC);
        NotificationManager notificationManager =
                (NotificationManager) MyGcmListenerService.this.getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyId = 0;
        String title = "null";
        try {
            title = entity.getTitle();
            notifyId = Integer.parseInt(entity.getItems_id());
        } catch (Exception ex) {
            notifyId = (int) new Date().getTime();
            JLogUtils.e(TAG, ex.getMessage());
        }

        notificationManager.notify(notifyId, nb.build());
        try {
            GaTrackHelper.getInstance().googleAnalyticsEvent("Notification",
                    "Notification Received",
                    title,
                    (long) notifyId);
            FirebaseEventUtils.getInstance().customizedNotificationReceive(getApplicationContext(), entity.getTitle());
        } catch (Exception ex) {
            ex.getMessage();
            JLogUtils.e(TAG, ex.getMessage());
        }
    }
}
