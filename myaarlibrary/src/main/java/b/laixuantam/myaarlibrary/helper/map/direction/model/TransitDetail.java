package b.laixuantam.myaarlibrary.helper.map.direction.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
public class TransitDetail implements Parcelable {
    @SerializedName("arrival_stop")
    private StopPoint arrivalStopPoint;
    @SerializedName("arrival_time")
    private TimeInfo arrivalTime;
    @SerializedName("departure_stop")
    private StopPoint departureStopPoint;
    @SerializedName("departure_time")
    private TimeInfo departureTime;
    @SerializedName("line")
    private Line line;
    @SerializedName("headsign")
    private String headsign;
    @SerializedName("num_stops")
    private String stopNumber;

    public TransitDetail() {
    }

    protected TransitDetail(Parcel in) {
        arrivalStopPoint = in.readParcelable(StopPoint.class.getClassLoader());
        arrivalTime = in.readParcelable(TimeInfo.class.getClassLoader());
        departureStopPoint = in.readParcelable(StopPoint.class.getClassLoader());
        departureTime = in.readParcelable(TimeInfo.class.getClassLoader());
        line = in.readParcelable(Line.class.getClassLoader());
        headsign = in.readString();
        stopNumber = in.readString();
    }

    public StopPoint getArrivalStopPoint() {
        return arrivalStopPoint;
    }

    public void setArrivalStopPoint(StopPoint arrivalStopPoint) {
        this.arrivalStopPoint = arrivalStopPoint;
    }

    public TimeInfo getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(TimeInfo arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public StopPoint getDepartureStopPoint() {
        return departureStopPoint;
    }

    public void setDepartureStopPoint(StopPoint departureStopPoint) {
        this.departureStopPoint = departureStopPoint;
    }

    public TimeInfo getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(TimeInfo departureTime) {
        this.departureTime = departureTime;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getHeadsign() {
        return headsign;
    }

    public void setHeadsign(String headsign) {
        this.headsign = headsign;
    }

    public String getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(String stopNumber) {
        this.stopNumber = stopNumber;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(arrivalStopPoint, flags);
        dest.writeParcelable(arrivalTime, flags);
        dest.writeParcelable(departureStopPoint, flags);
        dest.writeParcelable(departureTime, flags);
        dest.writeParcelable(line, flags);
        dest.writeString(headsign);
        dest.writeString(stopNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransitDetail> CREATOR = new Creator<TransitDetail>() {
        @Override
        public TransitDetail createFromParcel(Parcel in) {
            return new TransitDetail(in);
        }

        @Override
        public TransitDetail[] newArray(int size) {
            return new TransitDetail[size];
        }
    };
}
