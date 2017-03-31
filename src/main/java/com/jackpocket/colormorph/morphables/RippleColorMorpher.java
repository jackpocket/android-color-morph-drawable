package com.jackpocket.colormorph.morphables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.Interpolator;

public class RippleColorMorpher extends Morphable {

    protected Paint paint = new Paint();

    protected int[] centerPosition;
    protected boolean normalizedCenter = false;

    /**
     * @param targetColor @link{https://developer.android.com/reference/android/support/annotation/ColorInt.html}
     */
    public RippleColorMorpher(int targetColor){
        this.paint.setColor(targetColor);
        this.paint.setAntiAlias(true);
    }

    public RippleColorMorpher setCenterPosition(int[] centerPosition){
        this.centerPosition = centerPosition;
        this.normalizedCenter = false;

        return this;
    }

    @Override
    public Morphable notifyAnimationStart(){
        this.normalizedCenter = false;

        return super.notifyAnimationStart();
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
        int[] position = getNormalizedCenterPosition(canvas);

        float radius = (Math.max(canvas.getWidth(), canvas.getHeight()) * 1.20f)
                * getInterpolation();

        canvas.drawCircle(position[0],
                position[1],
                radius,
                paint);
    }

    private int[] getNormalizedCenterPosition(Canvas canvas){
        if(centerPosition == null)
            return new int[]{ canvas.getWidth() / 2, canvas.getHeight() / 2 };

        if(!normalizedCenter){
            centerPosition[0] = Math.max(0, centerPosition[0]);
            centerPosition[0] = Math.min(canvas.getWidth(), centerPosition[0]);

            centerPosition[1] = Math.max(0, centerPosition[1]);
            centerPosition[1] = Math.min(canvas.getHeight(), centerPosition[1]);

            normalizedCenter = true;
        }

        return centerPosition;
    }

}
