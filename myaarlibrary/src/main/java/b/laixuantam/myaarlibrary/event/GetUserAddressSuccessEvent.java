package b.laixuantam.myaarlibrary.event;


import b.laixuantam.myaarlibrary.helper.BusHelper;

public class GetUserAddressSuccessEvent {

    public static void post() {
        BusHelper.post(new GetUserAddressSuccessEvent());
    }
}
