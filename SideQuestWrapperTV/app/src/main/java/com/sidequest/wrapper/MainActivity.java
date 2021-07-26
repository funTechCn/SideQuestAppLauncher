package com.sidequest.wrapper;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Executable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("=================WrapperTVOnCreate start:first");
        System.out.println("am start -a android.intent.action.VIEW -d com.oculus.tv -e uri com.sidequest.launcher/com.sidequest.launcher.gui.MainActivity  com.oculus.vrshell/.MainActivity");
        super.onCreate(savedInstanceState);
        try {
            String targetPackage = "com.sidequest.launcher";

            Intent targetIntent;
            PackageManager pm = this.getPackageManager();
            targetIntent = pm.getLaunchIntentForPackage(targetPackage);
            if (targetIntent == null) {
                targetIntent = pm.getLeanbackLaunchIntentForPackage(targetPackage);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            System.out.println("=================WrapperTVOnCreate start:start-com.sidequest.launcher from TV");
            intent.setComponent(new ComponentName("com.oculus.vrshell", "com.oculus.vrshell.MainActivity"));
            intent.setData(Uri.parse("com.oculus.tv"));//com.oculus.vrshell.desktop
            intent.putExtra("uri", targetIntent.getComponent().flattenToString());
            this.startActivity(intent);
            System.out.println("=================WrapperTVOnCreate end:start-com.sidequest.launcher from TV");
        }catch (Exception e){
            System.out.println("=================WrapperTVOnStart start:start-com.sidequest.launcher from TV");
            e.printStackTrace();
            System.out.println("=================WrapperTVOnStart end:start-com.sidequest.launcher from TV");
        }
    }

    @Override
    protected void onStart() {
        System.out.println("=================WrapperTVOnStart start:start-com.sidequest.launcher from TV");
        super.onStart();
        this.finish();
        System.out.println("=================WrapperTVOnStart end:start-com.sidequest.launcher from TV");
    }
}
