package b.laixuantam.myaarlibrary.event;

import b.laixuantam.myaarlibrary.helper.BusHelper;

public class UserAcceptLocationPermissionEvent {

    public static void post() {
        BusHelper.post(new UserAcceptLocationPermissionEvent());
    }
}
