package com.example.irhabi_ecsboard.retassms;
/**
 * Created by irhabi_ECSboArd on 4/5/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, TheService.class);
            context.startService(pushIntent);
        }
    }
}