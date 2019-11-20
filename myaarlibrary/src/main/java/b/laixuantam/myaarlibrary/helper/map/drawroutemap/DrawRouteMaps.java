package b.laixuantam.myaarlibrary.helper.map.drawroutemap;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class DrawRouteMaps {

    private static DrawRouteMaps instance;
    private Context context;

    public static DrawRouteMaps getInstance(Context context) {
        instance = new DrawRouteMaps();
        instance.context = context;
        return instance;
    }

    public DrawRouteMaps draw(LatLng origin, LatLng destination, GoogleMap googleMap, String api_key){
        String url_route = FetchUrl.getUrl(origin, destination,api_key);
        DrawRoute drawRoute = new DrawRoute(googleMap);
        drawRoute.execute(url_route);

        return instance;
    }

    public void clearPolyline(){
        if (RouteDrawerTask.polylineFinal != null){
            RouteDrawerTask.polylineFinal.remove();
            RouteDrawerTask.polylineFinal = null;
        }
    }

    public static Context getContext() {
        return instance.context;
    }
}
