package com.jackpocket.colormorph.morphables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.Interpolator;

public class RippleColorMorpher extends Morphable {

    protected Paint paint = new Paint();
    protected int[] centerPosition;

    /**
     * @param targetColor @link{https://developer.android.com/reference/android/support/annotation/ColorInt.html}
     */
    public RippleColorMorpher(int targetColor){
        this.paint.setColor(targetColor);
        this.paint.setAntiAlias(true);
    }

    public RippleColorMorpher setCenterPosition(int[] centerPosition){
        this.centerPosition = centerPosition;
        return this;
    }

    public Paint getPaint() {
        return paint;
    }

    @Override
    public int getColor(){
        return paint.getColor();
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public int[] getCenterPosition() {
        return centerPosition;
    }

    @Override
    public void draw(Canvas canvas) {
        int[] position = centerPosition == null
                ? new int[]{ canvas.getWidth() / 2, canvas.getHeight() / 2 }
                : centerPosition;

        float radius = (Math.max(canvas.getWidth(), canvas.getHeight()) * 1.20f)
                * getInterpolation();

        canvas.drawCircle(position[0],
                position[1],
                radius,
                paint);
    }

}
