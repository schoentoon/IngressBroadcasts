package com.schoentoon.ingressbroadcasts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, NotificationService.class));
    }
}
