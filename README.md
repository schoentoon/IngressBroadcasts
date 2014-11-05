IngresBroadcasts
================

Broadcasts for attacks are send as "com.schoentoon.ingressbroadcasts.ATTACK" with a "when", a "user" and a "portal" key.

Broadcasts for medals will be sent as "com.schoentoon.ingressbroadcasts.ACHIEVEMENT" with a "type", a "when" and an "icon" key. The icon will be a Bitmap parcelable containing the icon of the medal.

And finally broadcasts for neutralized portals will be send as "com.schoentoon.ingressbroadcasts.NEUTRALIZED_INTENT", this generally only happens when someone uses a virus on a portal you're on to flip it. This will contain the "when", the "user" and the "portal" key just like the attacks.
