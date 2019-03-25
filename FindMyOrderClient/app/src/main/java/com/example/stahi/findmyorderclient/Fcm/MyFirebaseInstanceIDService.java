package com.example.stahi.findmyorderclient.Fcm;

import android.util.Log;

import com.example.stahi.findmyorderclient.API.RestService;
import com.example.stahi.findmyorderclient.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInsIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(final String token) {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            RestService restService = new RestService();
            restService.getService().UpdateFirebaseToken(uid, token, "testString").enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        //Toast.makeText(getApplicationContext(), "FirebaseToken refreshed !", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    //Toast.makeText(getApplicationContext(), "Fail to refresh FirebaseToken !", Toast.LENGTH_LONG).show();
                }
            });



        }
    }

}
