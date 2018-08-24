package b.laixuantam.myaarlibrary.helper.map.direction.request;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.List;

import b.laixuantam.myaarlibrary.helper.map.direction.DirectionCallback;
import b.laixuantam.myaarlibrary.helper.map.direction.model.Direction;
import b.laixuantam.myaarlibrary.helper.map.direction.network.DirectionConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionRequest {
    private DirectionRequestParam param;

    public DirectionRequest(String apiKey, LatLng origin, LatLng destination, List<LatLng> waypointList) {
        param = new DirectionRequestParam().setApiKey(apiKey).setOrigin(origin).setDestination(destination).setWaypoints(waypointList);
    }

    public DirectionRequest transportMode(String transportMode) {
        param.setTransportMode(transportMode);
        return this;
    }

    public DirectionRequest language(String language) {
        param.setLanguage(language);
        return this;
    }

    public DirectionRequest unit(String unit) {
        param.setUnit(unit);
        return this;
    }

    public DirectionRequest avoid(String avoid) {
        String oldAvoid = param.getAvoid();
        if (oldAvoid != null && !oldAvoid.isEmpty()) {
            oldAvoid += "|";
        } else {
            oldAvoid = "";
        }
        oldAvoid += avoid;
        param.setAvoid(oldAvoid);
        return this;
    }

    public DirectionRequest transitMode(String transitMode) {
        String oldTransitMode = param.getTransitMode();
        if (oldTransitMode != null && !oldTransitMode.isEmpty()) {
            oldTransitMode += "|";
        } else {
            oldTransitMode = "";
        }
        oldTransitMode += transitMode;
        param.setTransitMode(oldTransitMode);
        return this;
    }

    public DirectionRequest alternativeRoute(boolean alternative) {
        param.setAlternatives(alternative);
        return this;
    }

    public DirectionRequest departureTime(String time) {
        param.setDepartureTime(time);
        return this;
    }

    public DirectionRequest optimizeWaypoints(boolean optimize) {
        param.setOptimizeWaypoints(optimize);
        return this;
    }

    public DirectionTask execute(final DirectionCallback callback) {
        Call<Direction> direction = DirectionConnection.getInstance()
                .createService()
                .getDirection(param.getOrigin().latitude + "," + param.getOrigin().longitude,
                        param.getDestination().latitude + "," + param.getDestination().longitude,
                        waypointsToString(param.getWaypoints()),
                        param.getTransportMode(),
                        param.getDepartureTime(),
                        param.getLanguage(),
                        param.getUnit(),
                        param.getAvoid(),
                        param.getTransitMode(),
                        param.isAlternatives(),
                        param.getApiKey());

        direction.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                if (callback != null) {
                    callback.onDirectionSuccess(response.body(), new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {
                callback.onDirectionFailure(t);
            }
        });
        return new DirectionTask(direction);
    }

    private String waypointsToString(List<LatLng> waypoints) {
        if (waypoints != null && !waypoints.isEmpty()) {
            StringBuilder string = new StringBuilder(param.isOptimizeWaypoints() ? "optimize:true|" : "");
            string.append(waypoints.get(0).latitude).append(",").append(waypoints.get(0).longitude);
            for (int i = 1; i < waypoints.size(); i++) {
                string.append("|").append(waypoints.get(i).latitude).append(",").append(waypoints.get(i).longitude);
            }
            return string.toString();
        }
        return null;
    }
}
