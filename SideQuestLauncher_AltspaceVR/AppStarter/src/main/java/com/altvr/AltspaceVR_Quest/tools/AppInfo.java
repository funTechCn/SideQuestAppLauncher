package com.altvr.AltspaceVR_Quest.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Container to hold app informations
 */
public class AppInfo extends ApplicationInfo
{
    /** The current context */
    Context mContext;

    /**
     * @param app App to be hold
     */
    public AppInfo(Context context, ApplicationInfo app)
    {
        super(app);
        mContext = context;
    }

    /**
     * @return Name to be displayed
     */
    public String getDisplayName()
    {
        String retVal = this.loadLabel(mContext.getPackageManager()).toString();
        if(retVal == null || retVal.equals(""))
        {
            retVal = packageName;
        }
        return retVal;
    }

    /**
     * @return Icon to be displayed
     */
    public Drawable getDisplayIcon()
    {

        Drawable retVal = null;
        try
        {
            String icon =new File(mContext .getDataDir()+ File.separator+"icons").getAbsolutePath();
            File file=new File(icon+File.separator+this.packageName+File.separator+".png");
            if(file.isFile() && file.canRead()){
                retVal = new BitmapDrawable(mContext.getResources(),BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
        }
        catch (Exception ignore){ }
        if(retVal==null){
            retVal= this.loadIcon(mContext.getPackageManager());
        }
        return retVal;
    }
}
