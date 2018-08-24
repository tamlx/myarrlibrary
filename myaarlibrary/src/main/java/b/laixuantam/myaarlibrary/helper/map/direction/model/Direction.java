package b.laixuantam.myaarlibrary.helper.map.direction.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import b.laixuantam.myaarlibrary.helper.map.direction.constant.RequestResult;


@SuppressWarnings("WeakerAccess")
public class Direction implements Parcelable {
    @SerializedName("geocoded_waypoints")
    private List<GeocodedWaypoint> geocodedWaypointList;
    @SerializedName("routes")
    private List<Route> routeList;
    @SerializedName("status")
    private String status;
    @SerializedName("error_message")
    private String errorMessage;

    public Direction() {
    }

    protected Direction(Parcel in) {
        status = in.readString();
        errorMessage = in.readString();
    }

    public void setGeocodedWaypointList(List<GeocodedWaypoint> geocodedWaypointList) {
        this.geocodedWaypointList = geocodedWaypointList;
    }

    public List<GeocodedWaypoint> getGeocodedWaypointList() {
        return geocodedWaypointList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isOK() {
        return RequestResult.OK.equals(status);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(errorMessage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Direction> CREATOR = new Creator<Direction>() {
        @Override
        public Direction createFromParcel(Parcel in) {
            return new Direction(in);
        }

        @Override
        public Direction[] newArray(int size) {
            return new Direction[size];
        }
    };
}
