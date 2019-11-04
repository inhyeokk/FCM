package com.rkddlsgur983.fcm.services;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class FCMHelper {

    private static final String TOKEN = "TOKEN";
    private static MutableLiveData<String> token = new MutableLiveData<>();

    public static MutableLiveData<String> observeToken() {
        return token;
    }

    public static void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            token.setValue(instanceIdResult.getToken());
            Log.d(TOKEN, instanceIdResult.getToken());
        });
    }

    public static void setTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }
}
