package b.laixuantam.myaarlibrary.helper.map.direction.request;

import b.laixuantam.myaarlibrary.helper.map.direction.model.Direction;
import retrofit2.Call;

public class DirectionTask {
    private Call<Direction> directionCall;

    public DirectionTask(Call<Direction> directionCall) {
        this.directionCall = directionCall;
    }

    public void cancel() {
        if (directionCall != null && !directionCall.isCanceled()) {
            directionCall.cancel();
        }
    }

    public boolean isFinished() {
        return directionCall != null && directionCall.isExecuted();
    }

    public Call<Direction> toRetrofitCall() {
        return directionCall;
    }
}
