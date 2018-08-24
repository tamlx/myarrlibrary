package b.laixuantam.myaarlibrary.helper.map.direction;

import b.laixuantam.myaarlibrary.helper.map.direction.model.Direction;

public interface DirectionCallback {
    void onDirectionSuccess(Direction direction, String rawBody);
    void onDirectionFailure(Throwable t);
}
