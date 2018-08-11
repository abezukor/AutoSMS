package com.abezukor.abezukor.autosms;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.widget.Toast;

import java.util.Arrays;

import static android.content.Intent.ACTION_PICK_ACTIVITY;

public class DynamicShortcuts {
    public  DynamicShortcuts(){

    }

    public void createShortcut(Object info, Context context, AutoSMSObject autoSMS) {
        //If I can add a dynamic shortcut
        if(Build.VERSION.SDK_INT >= 25){
            //make a shortcut mamager
            ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
            shortcutManager.addDynamicShortcuts(Arrays.asList((ShortcutInfo) info));

            Toast.makeText(context, "Shortcut created under app icon. To add it to your home screen tap and hold on the Icon and drap shortcut to desired location.", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //Do it the old, depracated way
        Intent shortcutIntent = new Intent(context, ShortCutActivity.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcutIntent.putExtra("id", autoSMS.get_id());

        //makes intent to make the shortcut
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, autoSMS.get_homescreenname());
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher));


        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
        // Do it the pre-oreo way with The
    }

    public static void removeShortcut(int id, Context context){
        if (Build.VERSION.SDK_INT >= 25){
            ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
            shortcutManager.removeDynamicShortcuts(Arrays.asList(Integer.toString(id)));
        }
    }

    public static Object makeShortCutInfo(AutoSMSObject autoSMS, Context context) {
        Intent shortcutIntent = new Intent(context, ShortCutActivity.class);
        shortcutIntent.setAction("LOCATION_SHORTCUT");
        shortcutIntent.putExtra("id", autoSMS.get_id());

        if (Build.VERSION.SDK_INT >= 25) {

            ShortcutInfo shortcut = new ShortcutInfo.Builder(context, Integer.toString(autoSMS.get_id()))
                    .setShortLabel(autoSMS.get_homescreenname())
                    .setLongLabel(autoSMS.get_homescreenname())
                    .setIcon(Icon.createWithResource(context, R.drawable.ic_launcher))
                    .setIntent(shortcutIntent).build();
            return shortcut;
        }
        return  null;
    }
}
