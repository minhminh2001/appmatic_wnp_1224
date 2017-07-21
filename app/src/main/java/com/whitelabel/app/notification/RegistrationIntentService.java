/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whitelabel.app.notification;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.dao.NotificationDao;
import com.whitelabel.app.utils.JLogUtils;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public RegistrationIntentService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(GlobalData.gcmSendId,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            WhiteLabelApplication.getPhoneConfiguration().setRegistrationToken(token);
            JLogUtils.i(TAG, "GCM Registration Token: " + token);
            // TODO: Implement this method to send any registration to your app's servers.
            if (token != null)
                sendRegistrationToServer();
            // Subscribe to topic channels
            subscribeTopics(token);
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            JLogUtils.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
    private Handler dataHandler=new Handler();
    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *

     */
    private void sendRegistrationToServer() {
        String sessionKey= WhiteLabelApplication.getAppConfiguration().getUser() == null ? "" : WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
        new NotificationDao(TAG,dataHandler).sendRegistrationTokenToServer(sessionKey, WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());
//
//        SVRParameters parameter = new SVRParameters();
//        parameter.put("session_key", );
//        parameter.put("device_token", WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());
//        JLogUtils.i("RegistrationIntentService","device_token==============="+WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());
//        SVRNotificationAppOpen notificationAppOpenHandler = new SVRNotificationAppOpen(getBaseContext(), parameter);
//        notificationAppOpenHandler.loadDatasFromServer(new SVRCallback() {
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//                NotificationAppOpenReturnEntity notificationAppOpenReturnEntity = (NotificationAppOpenReturnEntity) result;
//                if (notificationAppOpenReturnEntity.getStatus() == 1) {
//                    JLogUtils.i("russell->RegistrationToken", "gcm's registration token has been post to server");
//                } else {
//                    JLogUtils.e("russell->RegistrationToken", "fail to post gcm's registration token to server");
//                }
//            }
//
//            @Override
//            public void onFailure(int resultCode, String errorMsg) {
//                JLogUtils.e("russell->RegistrationToken", "fail to post gcm's registration token to server");
//            }
//        });
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}
