package b.laixuantam.myaarlibrary.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityHelper
{
    private final ConnectivityManager connectivityManager;

    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int TYPE_NOT_CONNECTED = 0;

    public ConnectivityHelper(Context context)
    {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Get connectivity status
     *
     * @return TYPE_WIFI, TYPE_MOBILE, TYPE_NOT_CONNECTED
     */
    private int getConnectivityStatus()
    {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting())
        {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
            {
                return TYPE_WIFI;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                return TYPE_MOBILE;
            }
        }

        return TYPE_NOT_CONNECTED;
    }

    public boolean isConnectivityWifi()
    {
        return getConnectivityStatus() == TYPE_WIFI;
    }

    /**
     * Returns true if there is Internet connection
     *
     * @return true if there is Internet connection
     */
    public boolean hasInternetConnection()
    {
        return (getConnectivityStatus() != TYPE_NOT_CONNECTED);
    }
}