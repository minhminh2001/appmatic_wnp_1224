package com.whitelabel.app.ui.login;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.whitelabel.app.BuildConfig;
import com.whitelabel.app.R;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public class GoogleLogin {

    public int requestCode = 10;

    public GoogleSignInOptions gso;

    public GoogleApiClient mGoogleApiClient;

    public GoogleApiClient.OnConnectionFailedListener listener;

    private Fragment fragment;

    private GoogleSignListener googleSignListener;

    public GoogleLogin(Fragment fragment, GoogleApiClient.OnConnectionFailedListener listener) {
        this.fragment = fragment;
        this.listener = listener;

        //init google service
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestIdToken(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .requestProfile()
            .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(fragment.getActivity())
            .enableAutoManage(fragment.getActivity(), listener)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build();
    }

    //View root lifecycler,onPause or onStop,use
    public void stopManager() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(fragment.getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * login
     */
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        fragment.startActivityForResult(signInIntent, requestCode);
    }

    /**
     * logout
     */
    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
            new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        if (googleSignListener != null) {
                            googleSignListener.googleLogoutSuccess();
                        }
                    } else {
                        if (googleSignListener != null) {
                            googleSignListener.googleLogoutFail();
                        }
                    }
                }
            });
    }

    /**
     * Activity：onActivityResult use
     *
     * @param result response data
     */
    public String handleSignInResult(GoogleSignInResult result) {
        String res = "";

        if (result.isSuccess()) {
            //login sucess
            GoogleSignInAccount acct = result.getSignInAccount();
            // user name:acct.getDisplayName(),email:acct.getEmail(),token : acct.getIdToken(),
            // image icon url:acct.getPhotoUrl(),id:  acct.getId(),GrantedScopes: acct
            // .getGrantedScopes()
            if (googleSignListener != null) {
                googleSignListener.googleLoginSuccess(acct);
            }
        } else {
            // Signed out, show unauthenticated UI.
            res = "-1";  //-1 mean user logout ,can custom
            if (googleSignListener != null) {
                googleSignListener.googleLoginFail();
            }
        }
        return res;
    }

    public void setGoogleSignListener(GoogleSignListener googleSignListener) {
        this.googleSignListener = googleSignListener;
    }

    public interface GoogleSignListener {

        void googleLoginSuccess(GoogleSignInAccount account);

        void googleLoginFail();

        void googleLogoutSuccess();

        void googleLogoutFail();
    }

}