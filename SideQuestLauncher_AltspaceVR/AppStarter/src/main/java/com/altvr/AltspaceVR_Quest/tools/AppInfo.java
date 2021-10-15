package com.altvr.AltspaceVR_Quest.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Container to hold app informations
 */
public class AppInfo extends ApplicationInfo
{
    /** The current context */
    Context mContext;
    private String iconPath=null;
    private String namePath=null;

    /**
     * @param app App to be hold
     */
    public AppInfo(Context context, ApplicationInfo app)
    {
        super(app);
        mContext = context;
        try {
            File names = mContext.getExternalFilesDir("names");
            if (names != null) {
                namePath = names.getAbsolutePath();
            } else {
                names.mkdir();
            }
            File icons = mContext.getExternalFilesDir("icons");
            if (icons != null) {
                iconPath = icons.getAbsolutePath();
            }else{
                icons.mkdir();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * @return Name to be displayed
     */
    public String getDisplayName()
    {
        String retVal = null;

        try
        {
            File file=new File(namePath+File.separator+this.packageName+".txt");
            if(file.isFile() && file.canRead()){
                InputStream instream = new FileInputStream(file);
                if (instream != null)
                {
                    ArrayList<String> newList=new ArrayList<String>();
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                            newList.add(line);
                    }
                    if(newList.size()>0) {
                        retVal = newList.get(0);
                    }
                    inputreader.close();
                    instream.close();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {

        }

        if(retVal == null || retVal.equals(""))
        {
            retVal = this.loadLabel(mContext.getPackageManager()).toString();
        }

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
            File file=new File(iconPath+File.separator+this.packageName+".jpg");
            if(!file.isFile() || !file.canRead()){
                file = new File(iconPath + File.separator + this.packageName + ".png");
            }
            if (file.isFile() && file.canRead()) {
                retVal = new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(retVal==null){
            retVal= this.loadIcon(mContext.getPackageManager());
        }
        return retVal;
    }
}
