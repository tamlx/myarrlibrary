package b.laixuantam.myaarlibrary.event;

import b.laixuantam.myaarlibrary.helper.BusHelper;

public class GetCurrentPositionSuccess {
    public static void post() {
        BusHelper.post(new GetCurrentPositionSuccess());
    }
}
