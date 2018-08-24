package b.laixuantam.myaarlibrary.helper.map.direction.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.helper.map.direction.model.Step;

public class DirectionConverter
{
    public static ArrayList<LatLng> getDirectionPoint(List<Step> stepList) {
        ArrayList<LatLng> directionPointList = new ArrayList<>();
        if (stepList != null && stepList.size() > 0) {
            for (Step step : stepList) {
                convertStepToPosition(step, directionPointList);
            }
        }
        return directionPointList;
    }

    private static void convertStepToPosition(Step step, ArrayList<LatLng> directionPointList) {
        // Get start location
        directionPointList.add(step.getStartLocation().getCoordination());

        // Get encoded points location
        if (step.getPolyline() != null) {
            List<LatLng> decodedPointList = step.getPolyline().getPointList();
            if (decodedPointList != null && decodedPointList.size() > 0) {
                for (LatLng position : decodedPointList) {
                    directionPointList.add(position);
                }
            }
        }

        // Get end location
        directionPointList.add(step.getEndLocation().getCoordination());
    }

    public static ArrayList<LatLng> getSectionPoint(List<Step> stepList) {
        ArrayList<LatLng> directionPointList = new ArrayList<>();
        if (stepList != null && stepList.size() > 0) {
            // Get start location only first position
            directionPointList.add(stepList.get(0).getStartLocation().getCoordination());
            for (Step step : stepList) {
                // Get end location
                directionPointList.add(step.getEndLocation().getCoordination());
            }
        }
        return directionPointList;
    }

    public static PolylineOptions createPolyline(Context context, ArrayList<LatLng> locationList, int width, int color) {
        PolylineOptions rectLine = new PolylineOptions().width(dpToPx(context, width)).color(color).geodesic(true);
        for (LatLng location : locationList) {
            rectLine.add(location);
        }
        return rectLine;
    }

    public static ArrayList<PolylineOptions> createTransitPolyline(Context context, List<Step> stepList, int transitWidth, int transitColor, int walkingWidth, int walkingColor) {
        ArrayList<PolylineOptions> polylineOptionsList = new ArrayList<>();
        if (stepList != null && stepList.size() > 0) {
            for (Step step : stepList) {
                ArrayList<LatLng> directionPointList = new ArrayList<>();
                convertStepToPosition(step, directionPointList);
                if (step.isContainStepList()) {
                    polylineOptionsList.add(createPolyline(context, directionPointList, walkingWidth, walkingColor));
                } else {
                    polylineOptionsList.add(createPolyline(context, directionPointList, transitWidth, transitColor));
                }
            }
        }
        return polylineOptionsList;
    }

    private static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
