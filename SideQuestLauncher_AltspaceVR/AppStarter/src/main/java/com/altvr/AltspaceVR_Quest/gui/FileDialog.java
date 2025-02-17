package com.altvr.AltspaceVR_Quest.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by luki on 27.06.15.
 */
public class FileDialog
{
    private static final String PARENT_DIR = "..";
    private final String TAG = getClass().getName();
    private String[] fileList;
    private File currentPath;

    public interface FileSelectedListener
    {
        void fileSelected(File file);
    }

    public interface DirectorySelectedListener
    {
        void directorySelected(File directory);
    }

    private ListenerList<FileSelectedListener> fileListenerList = new ListenerList<FileDialog.FileSelectedListener>();
    private ListenerList<DirectorySelectedListener> dirListenerList = new ListenerList<FileDialog.DirectorySelectedListener>();
    private final Activity activity;
    private boolean selectDirectoryOption;
    private String[] fileEndsWith;

    /**
     * @param activity
     * @param path
     */
    public FileDialog(Activity activity, File path)
    {
        this.activity = activity;
        if (!path.exists()) path = Environment.getExternalStorageDirectory();
        loadFileList(path);
    }

    /**
     * @return file dialog
     */
    public Dialog createFileDialog()
    {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(currentPath.getPath());
        if (selectDirectoryOption)
        {
            builder.setPositiveButton("Select directory", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    Log.d(TAG, currentPath.getPath());
                    fireDirectorySelectedEvent(currentPath);
                }
            });
        }

        builder.setItems(fileList, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                String fileChosen = fileList[which];
                File chosenFile = getChosenFile(fileChosen);
                if (chosenFile.isDirectory())
                {
                    loadFileList(chosenFile);
                    dialog.cancel();
                    dialog.dismiss();
                    showDialog();
                } else fireFileSelectedEvent(chosenFile);
            }
        });

        dialog = builder.show();
        return dialog;
    }


    public void addFileListener(FileSelectedListener listener)
    {
        fileListenerList.add(listener);
    }

    public void removeFileListener(FileSelectedListener listener)
    {
        fileListenerList.remove(listener);
    }

    public void setSelectDirectoryOption(boolean selectDirectoryOption)
    {
        this.selectDirectoryOption = selectDirectoryOption;
    }

    public void addDirectoryListener(DirectorySelectedListener listener)
    {
        dirListenerList.add(listener);
    }

    public void removeDirectoryListener(DirectorySelectedListener listener)
    {
        dirListenerList.remove(listener);
    }

    /**
     * Show file dialog
     */
    public void showDialog()
    {
        createFileDialog().show();
    }

    private void fireFileSelectedEvent(final File file)
    {
        fileListenerList.fireEvent(new ListenerList.FireHandler<FileSelectedListener>()
        {
            public void fireEvent(FileSelectedListener listener)
            {
                listener.fileSelected(file);
            }
        });
    }

    private void fireDirectorySelectedEvent(final File directory)
    {
        dirListenerList.fireEvent(new ListenerList.FireHandler<DirectorySelectedListener>()
        {
            public void fireEvent(DirectorySelectedListener listener)
            {
                listener.directorySelected(directory);
            }
        });
    }

    private void loadFileList(File path)
    {
        this.currentPath = path;
        List<String> r = new ArrayList<String>();
        if (path.exists())
        {
            if (path.getParentFile() != null) r.add(PARENT_DIR);
            FilenameFilter filter = new FilenameFilter()
            {
                public boolean accept(File dir, String filename)
                {
                    File sel = new File(dir, filename);
                    if (!sel.canRead()) return false;
                    if (selectDirectoryOption) return sel.isDirectory();
                    else
                    {
                        boolean endsWith = false;
                        if(fileEndsWith != null)
                        {
                            for (String ending : fileEndsWith)
                            {
                                if (filename.toLowerCase().endsWith(ending))
                                {
                                    endsWith = true;
                                    break;
                                }
                            }
                        }
                        return endsWith || sel.isDirectory();
                    }
                }
            };
            String[] fileList1 = path.list(filter);
            for (String file : fileList1)
            {
                r.add(file);
            }
        }
        Collections.sort(r);
        fileList = (String[]) r.toArray(new String[]{});
    }

    private File getChosenFile(String fileChosen)
    {
        if (fileChosen.equals(PARENT_DIR)) return currentPath.getParentFile();
        else return new File(currentPath, fileChosen);
    }

    public void setFileEndsWith(String[] fileEndsWith)
    {
        if(fileEndsWith != null)
        {
            for(Integer i = 0; i < fileEndsWith.length; i++)
            {
                fileEndsWith[i] = fileEndsWith[i].toLowerCase();
            }
        }
        this.fileEndsWith = fileEndsWith;

        // Again load file-list (because now file-list can change)
        loadFileList(currentPath);
    }
}

class ListenerList<L>
{
    private List<L> listenerList = new ArrayList<L>();

    public interface FireHandler<L>
    {
        void fireEvent(L listener);
    }

    public void add(L listener)
    {
        listenerList.add(listener);
    }

    public void fireEvent(FireHandler<L> fireHandler)
    {
        List<L> copy = new ArrayList<L>(listenerList);
        for (L l : copy)
        {
            fireHandler.fireEvent(l);
        }
    }

    public void remove(L listener)
    {
        listenerList.remove(listener);
    }

    public List<L> getListenerList()
    {
        return listenerList;
    }
}
