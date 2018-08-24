package b.laixuantam.myaarlibrary.dependency;

import android.content.Context;

import java.security.Security;

import b.laixuantam.myaarlibrary.api.ApiManagement;
import b.laixuantam.myaarlibrary.helper.AppCleanerHelper;
import b.laixuantam.myaarlibrary.helper.ConnectivityHelper;
import b.laixuantam.myaarlibrary.helper.FileHelper;
import b.laixuantam.myaarlibrary.helper.ImageHelper;
import b.laixuantam.myaarlibrary.helper.InstallationHelper;
import b.laixuantam.myaarlibrary.helper.LanguageHelper;

public class AppObjectProvider implements ObjectProviderInterface {
    private final Context context;

    // singleton instances
    private InstallationHelper installationHelper;
    private Security security;
    private ImageHelper imageHelper;
    private AppCleanerHelper appCleanerHelper;
    private FileHelper fileHelper;
    private ConnectivityHelper connectivityHelper;
    private ApiManagement apiManagement;
    private LanguageHelper languageHelper;

    public AppObjectProvider(Context context) {
        this.context = context;
    }

    @Override
    public ImageHelper getImageHelper() {
        return (imageHelper == null) ? (imageHelper = new ImageHelper(context)) : imageHelper;
    }

    @Override
    public InstallationHelper getInstallationHelper() {
        return (installationHelper == null) ? (installationHelper = new InstallationHelper(context)) : installationHelper;
    }

    @Override
    public AppCleanerHelper getAppCleanerHelper() {
        return (appCleanerHelper == null) ? (appCleanerHelper = new AppCleanerHelper(context, getInstallationHelper())) : appCleanerHelper;
    }

    public FileHelper getFileHelper() {
        return (fileHelper == null) ? (fileHelper = new FileHelper(context)) : fileHelper;
    }

    @Override
    public ConnectivityHelper getConnectivityHelper() {
        return (connectivityHelper == null) ? (connectivityHelper = new ConnectivityHelper(context)) : connectivityHelper;
    }

    public ApiManagement getApiManagement() {
        return (apiManagement == null) ? (apiManagement = new ApiManagement()) : apiManagement;
    }

    @Override
    public LanguageHelper getLanguageHelper() {
        return (languageHelper == null) ? (languageHelper = new LanguageHelper()) : languageHelper;
    }

}