package b.laixuantam.myaarlibrary.helper.map.drawroutemap;

import com.google.android.gms.maps.model.LatLng;

public class FetchUrl {
    public static String getUrl(LatLng origin, LatLng dest, String api_key) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false&units=metric&mode=driving";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key=" + api_key;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }
}
