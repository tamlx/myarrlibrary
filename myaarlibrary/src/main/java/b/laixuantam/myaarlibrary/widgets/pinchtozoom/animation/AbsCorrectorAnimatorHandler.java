package b.laixuantam.myaarlibrary.widgets.pinchtozoom.animation;

import android.animation.ValueAnimator;

import b.laixuantam.myaarlibrary.widgets.pinchtozoom.ImageMatrixCorrector;

public abstract class AbsCorrectorAnimatorHandler implements ValueAnimator.AnimatorUpdateListener {

    private ImageMatrixCorrector corrector;
    private float[] values;

    public AbsCorrectorAnimatorHandler(ImageMatrixCorrector corrector) {
        this.corrector = corrector;
        this.values = new float[9];
    }

    public ImageMatrixCorrector getCorrector() {
        return corrector;
    }

    protected float[] getValues() {
        return values;
    }
}
