package b.laixuantam.myaarlibrary.helper;

import android.content.Context;

import java.io.File;

import b.laixuantam.myaarlibrary.dependency.AppProvider;

public class AppCleanerHelper
{
    private final Context context;
    private final InstallationHelper installationHelper;

    public AppCleanerHelper(Context context, InstallationHelper installationHelper)
    {
        this.context = context;
        this.installationHelper = installationHelper;
    }

    public void clean(final OnCleanFinished onCleanFinished)
    {
        // TODO gọi api xoá token đăng ký cho device id

    }

    private void clearApplicationData(OnCleanFinished onCleanFinished)
    {

        // reset installation id
        InstallationHelper installationHelper = AppProvider.getInstallationHelper();
        installationHelper.reset();

        try
        {
            File appDir = new File(context.getCacheDir().getParent());

            if (appDir.exists())
            {
                File[] children = appDir.listFiles();

                for (File folder : children)
                {
                    if (!folder.getName().equals("lib"))
                    {
                        deleteFolder(folder);
                    }
                }
            }
        }
        catch (Exception e)
        {
            MyLog.e("AppCleaner", e.getMessage());
        }

        onCleanFinished.onCleanFinished();
    }

    private void deleteFolder(File folder)
    {
        if ((folder != null) && folder.isDirectory())
        {
            File[] list = folder.listFiles();

            for (File element : list)
            {
                deleteFolder(element);
            }
        }

        if (folder != null)
        {
            folder.delete();
        }
    }

    public interface OnCleanFinished
    {
        void onCleanFinished();
    }
}