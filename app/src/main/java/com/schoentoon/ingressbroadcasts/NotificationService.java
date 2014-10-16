package com.schoentoon.ingressbroadcasts;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationService extends NotificationListenerService {
    private static final String INGRESS_PACKAGE = "com.nianticproject.ingress";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!INGRESS_PACKAGE.equals(sbn.getPackageName())) return;

        final Bundle extras = sbn.getNotification().extras;

        final String title = extras.getString(Notification.EXTRA_TITLE);

        if (title.endsWith("under attack")) {
            CharSequence[] lines = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
            if (lines != null) {
                for (CharSequence str : lines) {
                    final String line = str.toString();
                    int space = line.indexOf(' ');
                    if (space == -1) continue;

                    final String user = line.substring(0, space);
                    final String portal = line.substring(space+1);

                    final Intent broadcast = new Intent("com.schoentoon.ingressbroadcasts.ATTACK");
                    broadcast.putExtra("user", user);
                    broadcast.putExtra("portal", portal);
                    sendBroadcast(broadcast);
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}
