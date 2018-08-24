package b.laixuantam.myaarlibrary.helper.map.direction.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Route implements Parcelable {
    @SerializedName("bounds")
    private Bound bound;
    @SerializedName("copyrights")
    private String copyrights;
    @SerializedName("legs")
    private List<Leg> legList;
    @SerializedName("overview_polyline")
    private RoutePolyline overviewPolyline;
    @SerializedName("summary")
    private String summary;
    @SerializedName("fare")
    private Fare fare;
    @SerializedName("warnings")
    private List<String> warningList;
    @SerializedName("waypoint_order")
    private List<Integer> waypointOrderList;

    public Route() {
    }

    protected Route(Parcel in) {
        bound = in.readParcelable(Bound.class.getClassLoader());
        copyrights = in.readString();
        overviewPolyline = in.readParcelable(RoutePolyline.class.getClassLoader());
        summary = in.readString();
        fare = in.readParcelable(Fare.class.getClassLoader());
        warningList = in.createStringArrayList();
    }

    public Bound getBound() {
        return bound;
    }

    public void setBound(Bound bound) {
        this.bound = bound;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public List<Leg> getLegList() {
        return legList;
    }

    public void setLegList(List<Leg> legList) {
        this.legList = legList;
    }

    public RoutePolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(RoutePolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public List<String> getWarningList() {
        return warningList;
    }

    public void setWarningList(List<String> warningList) {
        this.warningList = warningList;
    }

    public List<Integer> getWaypointOrderList() {
        return waypointOrderList;
    }

    public void setWaypointOrderList(List<Integer> waypointOrderList) {
        this.waypointOrderList = waypointOrderList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(bound, i);
        parcel.writeString(copyrights);
        parcel.writeParcelable(overviewPolyline, i);
        parcel.writeString(summary);
        parcel.writeParcelable(fare, i);
        parcel.writeStringList(warningList);
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };
}
