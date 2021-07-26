package com.sidequest.wrapper2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("=================WrapperHomeOnCreate start:first");
        super.onCreate(savedInstanceState);
        try {
            String targetPackage = "com.sidequest.launcher";

            Intent targetIntent;
            PackageManager pm = this.getPackageManager();
            targetIntent = pm.getLaunchIntentForPackage(targetPackage);
            if (targetIntent == null) {
                targetIntent = pm.getLeanbackLaunchIntentForPackage(targetPackage);
            }
            System.out.println("=================WrapperHomeOnCreate start:start-com.sidequest.launcher from HOME");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setComponent(new ComponentName("com.oculus.vrshell", "com.oculus.vrshell.MainActivity"));
            intent.setData(Uri.parse("com.oculus.vrshell.desktop"));
            intent.putExtra("uri", targetIntent.getComponent().flattenToString());
            this.startActivity(intent);
            System.out.println("=================WrapperHomeOnCreate end:start-com.sidequest.launcher from HOME");
        }catch (Exception e){
            System.out.println("=================WrapperHomeError start:start-com.sidequest.launcher from HOME");
            e.printStackTrace();
            System.out.println("=================WrapperHomeError end:start-com.sidequest.launcher from HOME");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("=================WrapperHomeOnStart start:start-com.sidequest.launcher from HOME");
        this.finish();
        System.out.println("=================WrapperHomeOnStart end:start-com.sidequest.launcher from HOME");
    }
}
