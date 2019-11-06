# fcm-start-guide
Firebase Cloud Messaging(FCM)을 활용하여 푸시 알림을 구현하는 두가지 방법을 소개합니다.

##  Firebase Console
1. 메시지 수신 방식이 foreground(앱과 화면이 켜져 있는 경우)와 background(반대 경우)로 구분됩니다.
2. 먼저 foreground의 경우 프로젝트 내 구현된 [MyFirebaseMessagingService.java](https://github.com/rkddlsgur983/fcm-start-guide/blob/master/app/src/main/java/com/rkddlsgur983/fcm/services/MyFirebaseMessagingService.java)의 onMessageReceived() 메소드를 거쳐 푸시 메시지를 수신 받습니다.
따라서 진동, 알림음, 화면 켜짐과 같은 설정을 할 수 있습니다.
```
@Override
public void onMessageReceived(RemoteMessage remoteMessage) {

	if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            sendNotification(title, body);
        } else if (remoteMessage.getData().size() > 0) {
            if (!handleNow()) {
                scheduleJob();
            }
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("content");
            wakeUpApp();
            sendNotification(title, body);
	}
}
```

3. 이와는 다르게 background의 경우 [AndroidManifest.xml](https://github.com/rkddlsgur983/fcm-start-guide/blob/master/app/src/main/AndroidManifest.xml)에 명시된 default 옵션으로 푸시 메시지를 수신 받습니다. 이 경우 앱이 꺼져 있을 때 화면 켜짐이나 진동, 알림음과 같은 설정들은 변경할 수 없습니다. 단, 아이콘 변경은 가능합니다.
```
<!-- default setting -->
<meta-data
    android:name="com.google.firebase.messaging.default_notification_channel_id"
    android:value="@string/default_notification_channel_id" />
<meta-data
    android:name="com.google.firebase.messaging.default_notification_icon"
    android:resource="@drawable/ic_launcher_background" />
```

## 시작
1. [Firebase Console](https://console.firebase.google.com)에 접속하여 프로젝트를 생성합니다.
2. 생성된 프로젝트에 안드로이드 앱을 추가합니다.
