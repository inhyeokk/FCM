package com.rkddlsgur983.fcm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rkddlsgur983.fcm.services.FCMHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 파이어베이스 푸시 사용 방법 두가지
         * 1. token
         *  - 디바이스 고유의 토큰 값을 서버로 전송하여 보관
         *  - 필요 시 각 토큰과 매칭된 사용자의 디바이스에 특정 푸시를 전송
         * 2. topic 구독
         *  - topic을 구독하고 있는 모든 사용자에게 푸시 메시지 전송
         *  - 푸시 메시지 전송을 위해 서버에서 저장할 필요가 없음
         */
        FCMHelper.getToken();
        FCMHelper.setTopic("your-topic");
    }
}
