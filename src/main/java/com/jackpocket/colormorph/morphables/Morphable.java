package com.jackpocket.colormorph.morphables;

import android.graphics.Canvas;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class Morphable {

    public static final int DEFAULT_ANIM_DURATION = 425;

    protected Interpolator interpolator = new AccelerateDecelerateInterpolator();

    private long animationDurationMs;
    protected long animationStartedAtMs;

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

    public abstract void draw(Canvas canvas);

    public abstract int getColor();

}
