package com.jackpocket.colormorph.morphables;

import android.graphics.Canvas;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class Morphable {

    public static final int DEFAULT_ANIM_DURATION = 425;

    protected Interpolator interpolator = new AccelerateDecelerateInterpolator();

    private long animationDurationMs;
    protected long animationStartedAtMs;

    /**
     * An internal callback used by the ColorMorphDrawable to notify that it has started an animation
     * with this component. You shouldn't ever need to call this.
     */
    public Morphable notifyAnimationStart(){
        this.animationStartedAtMs = System.currentTimeMillis();
        return this;
    }

    public long getAnimationStartedAtMs() {
        return animationStartedAtMs;
    }

    public long getAnimationDurationMs() {
        return 0 < animationDurationMs
                ? animationDurationMs
                : DEFAULT_ANIM_DURATION;
    }

    public Morphable setAnimationDurationMs(long animationDurationMs) {
        this.animationDurationMs = animationDurationMs;
        return this;
    }

    public Morphable setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    protected float getInterpolation(){
        float percentCompleted = (System.currentTimeMillis() - animationStartedAtMs) / (float) getAnimationDurationMs();

        return percentCompleted < 1
                ? interpolator.getInterpolation(percentCompleted)
                : 1;
    }

    public abstract void draw(Canvas canvas);

    /**
     * @return the end state color to be used as the background by the ColorMorphDrawable
     */
    public abstract int getColor();

}
