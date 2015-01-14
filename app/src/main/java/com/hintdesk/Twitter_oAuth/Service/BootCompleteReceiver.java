package com.hintdesk.Twitter_oAuth.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Christopher Stokes on 2014-07-18.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            context.startService(new Intent(context, MyService.class));
            System.out.println("Recived BOOT_COMPLEATED");
        }
    }
}
