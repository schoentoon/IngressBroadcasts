package com.schoentoon.ingressbroadcasts;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class NotificationService extends NotificationListenerService {
    private static final String INGRESS_PACKAGE = "com.nianticproject.ingress";
    private static final String ATTACK_INTENT = "com.schoentoon.ingressbroadcasts.ATTACK";

    private static final String UNDER_ATTACK = "under attack";
    private static final String USER = "user";
    private static final String PORTAL = "portal";
    private static final String WHEN = "when";

    /**
     * Let's prevent duplicate broadcasts, size is 8 as Ingress will never group more than
     * 8 attacks into a single notification.
     */
    private final Deque<Attack> queue = new ArrayDeque<Attack>(8);

    private static final class Attack {
        final String user;
        final String portal;
        public Attack(final String raw) throws Exception {
            int space = raw.indexOf(' ');
            if (space == -1) throw new Exception("Not an attack?");

            user = raw.substring(0, space);
            portal = raw.substring(space + 1);
        }

        public void broadcast(final Context context, final long when) {
            final Intent broadcast = new Intent(ATTACK_INTENT);
            broadcast.putExtra(USER, user);
            broadcast.putExtra(PORTAL, portal);
            broadcast.putExtra(WHEN, when);
            context.sendBroadcast(broadcast);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Attack attack = (Attack) o;

            if (portal != null ? !portal.equals(attack.portal) : attack.portal != null)
                return false;
            if (user != null ? !user.equals(attack.user) : attack.user != null) return false;

            return true;
        }

        @Override
        public String toString() {
            return "Attack{" +
                    "user='" + user + '\'' +
                    ", portal='" + portal + '\'' +
                    '}';
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!INGRESS_PACKAGE.equals(sbn.getPackageName())) return;

        final Notification notification = sbn.getNotification();

        final Bundle extras = notification.extras;

        final String title = extras.getString(Notification.EXTRA_TITLE);

        if (title.endsWith(UNDER_ATTACK)) {
            final CharSequence[] lines = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
            if (lines != null) {
                try {
                    Iterator<Attack> deleter = queue.iterator();
                    for (int i = 0; i < lines.length; ++i) {
                        deleter.next();
                    }
                    while (deleter.hasNext()) {
                        deleter.remove();
                        deleter.next();
                    }
                } catch (final Exception ignore) {
                }

                for (final CharSequence str : lines) {
                    try {
                        final Attack attack = new Attack(str.toString());

                        // if we actually have attack already we jump out
                        if (queue.contains(attack)) break;

                        queue.add(attack);
                        attack.broadcast(this, notification.when);
                        android.util.Log.d(ATTACK_INTENT, notification.when + " - " + attack.toString());
                    } catch (final Exception ignore) {
                    }
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}
