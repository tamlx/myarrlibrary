package b.laixuantam.myaarlibrary.helper;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import b.laixuantam.myaarlibrary.BuildConfig;
import b.laixuantam.myaarlibrary.dependency.AppProvider;


/**
 * Quản lý app id duy nhất của ứng dụng
 */
public class InstallationHelper {
    private final Context context;
    private static String installationID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public InstallationHelper(Context context) {
        this.context = context;
    }

    public synchronized String getId() {
        if (InstallationHelper.installationID == null) {
            File installation = new File(context.getFilesDir(), InstallationHelper.INSTALLATION);

            try {
                if (!installation.exists()) {
                    writeInstallationFile(installation);
                }

                InstallationHelper.installationID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        MyLog.d("DEVICE_ID", installationID);

        return InstallationHelper.installationID;
    }

    private String readInstallationFile(File file) throws IOException {
        return AppProvider.getFileHelper().readFile(file);
    }

    private void writeInstallationFile(File file) throws IOException {
        AppProvider.getFileHelper().writeFile(file, UUID.randomUUID().toString());
    }

    public synchronized void reset() {
        installationID = null;

        File installation = new File(context.getFilesDir(), InstallationHelper.INSTALLATION);

        if (installation.exists()) {
            installation.delete();
        }
    }

    public String getUserAgent() {
        String release = VERSION.RELEASE;
        int sdk = VERSION.SDK_INT;
        String version = BuildConfig.VERSION_NAME;
        int code = BuildConfig.VERSION_CODE;
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        return String.format("Android: (version: %s - sdk: %d) | App: (version: %s - code: %d) | Device: (brand: %s - model: %s)", release, sdk, version, code, manufacturer, model);
    }
}