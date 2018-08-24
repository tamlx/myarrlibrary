package b.laixuantam.myaarlibrary.dependency;


import b.laixuantam.myaarlibrary.api.ApiManagement;
import b.laixuantam.myaarlibrary.helper.AppCleanerHelper;
import b.laixuantam.myaarlibrary.helper.ConnectivityHelper;
import b.laixuantam.myaarlibrary.helper.FileHelper;
import b.laixuantam.myaarlibrary.helper.ImageHelper;
import b.laixuantam.myaarlibrary.helper.InstallationHelper;
import b.laixuantam.myaarlibrary.helper.LanguageHelper;

public class AppProvider
{
    private static ObjectProviderInterface instance;

    public static void init(ObjectProviderInterface objectProviderInterface)
    {
        instance = objectProviderInterface;
    }

    public static ImageHelper getImageHelper()
    {
        return instance.getImageHelper();
    }

    public static InstallationHelper getInstallationHelper()
    {
        return instance.getInstallationHelper();
    }

    public static AppCleanerHelper getAppCleanerHelper()
    {
        return instance.getAppCleanerHelper();
    }

    public static FileHelper getFileHelper()
    {
        return instance.getFileHelper();
    }

    public static ConnectivityHelper getConnectivityHelper()
    {
        return instance.getConnectivityHelper();
    }
    public static ApiManagement getApiManagement()
    {
        return instance.getApiManagement();
    }

    public static LanguageHelper getLanguageHelper()
    {
        return instance.getLanguageHelper();
    }

}