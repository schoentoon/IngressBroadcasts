package com.schoentoon.ingressbroadcasts;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationService extends NotificationListenerService {
    private static final String INGRESS_PACKAGE = "com.nianticproject.ingress";
    private static final String ATTACK_INTENT = "com.schoentoon.ingressbroadcasts.ATTACK";

    private static final String UNDER_ATTACK = "under attack";
    private static final String USER = "user";
    private static final String PORTAL = "portal";
    private static final String WHEN = "when";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!INGRESS_PACKAGE.equals(sbn.getPackageName())) return;

        final Notification notification = sbn.getNotification();

        final Bundle extras = notification.extras;

        final String title = extras.getString(Notification.EXTRA_TITLE);

        if (title.endsWith(UNDER_ATTACK)) {
            final CharSequence[] lines = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
            if (lines != null) {
                for (final CharSequence str : lines) {
                    final String line = str.toString();
                    int space = line.indexOf(' ');
                    if (space == -1) continue;

                    final String user = line.substring(0, space);
                    final String portal = line.substring(space+1);

                    final Intent broadcast = new Intent(ATTACK_INTENT);
                    broadcast.putExtra(USER, user);
                    broadcast.putExtra(PORTAL, portal);
                    broadcast.putExtra(WHEN, notification.when);
                    sendBroadcast(broadcast);
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}
