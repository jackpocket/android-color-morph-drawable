package com.jackpocket.colormorph;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.jackpocket.colormorph.morphables.Morphable;
import com.jackpocket.colormorph.morphables.RippleColorMorpher;

import java.util.ArrayList;
import java.util.List;

public class ColorMorphDrawable extends Drawable {

    protected int color;
    protected Path clipPath;

    protected List<Morphable> morphers = new ArrayList<Morphable>();
    protected Interpolator defaultInterpolator = new AccelerateDecelerateInterpolator();
    protected long defaultDurationMs = Morphable.DEFAULT_ANIM_DURATION;

    protected int cornerRadius = 0;

    protected int[] lastCanvasInfo = new int[]{ 0, 0, 0 }; // x, y, cornerRadius

    public ColorMorphDrawable(int color){
        this.color = color;
    }

    /**
     * Override the current color and all animations that may be running with the supplied value
     */
    public ColorMorphDrawable overrideColor(int color) {
        synchronized(morphers){
            morphers.clear();
        }

        this.color = color;

        invalidateSelf();

        return this;
    }

    public ColorMorphDrawable setCornerRadiusPx(int cornerRadius) {
        this.cornerRadius = cornerRadius;

        invalidateSelf();

        return this;
    }

    public ColorMorphDrawable setDefaultDurationMs(long defaultDurationMs) {
        this.defaultDurationMs = defaultDurationMs;
        return this;
    }

    /**
     * Set the default Interpolator used for the default ripple animations of this drawable
     */
    public ColorMorphDrawable setDefaultInterpolator(Interpolator morphingInterpolator) {
        this.defaultInterpolator = morphingInterpolator;
        return this;
    }

    /**
     * Morph to the provided color from the center of the view
     */
    public ColorMorphDrawable morphRippledTo(int morphingTargetColor){
        return morphRippledTo(morphingTargetColor, null);
    }

    /**
     * Morph to the provided color from a specified center position
     */
    public ColorMorphDrawable morphRippledTo(int morphingTargetColor, int[] centerPosition){
        Morphable morpher = new RippleColorMorpher(morphingTargetColor)
                .setCenterPosition(centerPosition)
                .setAnimationDurationMs(defaultDurationMs)
                .setInterpolator(defaultInterpolator);

        return morphWith(morpher);
    }

    /**
     * Morph to the provided color using the supplied Morphable
     */
    public ColorMorphDrawable morphWith(Morphable morpher){
        synchronized(morphers){
            this.morphers.add(morpher);

            morpher.notifyAnimationStart();
        }

        invalidateSelf();

        return this;
    }

    @Override
    public void draw(Canvas canvas) {
        if(!(lastCanvasInfo[0] == canvas.getWidth()
                && lastCanvasInfo[1] == canvas.getHeight()
                && lastCanvasInfo[2] == cornerRadius)){

            clipPath = buildRoundedRectPath(canvas, cornerRadius);

            lastCanvasInfo[0] = canvas.getWidth();
            lastCanvasInfo[1] = canvas.getHeight();
            lastCanvasInfo[2] = Integer.valueOf(cornerRadius);
        }

        canvas.save();
        canvas.clipPath(clipPath);
        canvas.drawColor(color);

        if(0 < morphers.size()){
            synchronized(morphers){
                int deadIndex = -1;
                int currentIndex = 0;

                for(Morphable morphable : morphers){
                    morphable.draw(canvas);

                    if(morphable.getAnimationDurationMs() < System.currentTimeMillis() - morphable.getAnimationStartedAtMs())
                        deadIndex = currentIndex;

                    currentIndex ++;
                }

                if(-1 < deadIndex){
                    color = morphers.get(deadIndex)
                            .getColor();

                    for(int i = deadIndex; 0 <= i; i--)
                        morphers.remove(i);
                }

                invalidateSelf();
            }
        }

        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        this.color = (alpha << 24) | (color & 0x00FFFFFF);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
//        throw new RuntimeException("ColorFilter not applicable to RippleMorphColorDrawable");
        // Probably better to just do nothing than crash because of a ColorFilter?.....
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    protected Path buildRoundedRectPath(Canvas canvas, int radius){
        RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());

        Path path = new Path();
        path.moveTo(rect.left + radius, rect.top);
        path.lineTo(rect.right - radius,rect.top);
        path.quadTo(rect.right, rect.top, rect.right, rect.top + radius);
        path.lineTo(rect.right ,rect.bottom - radius);
        path.quadTo(rect.right ,rect.bottom, rect.right - radius, rect.bottom);
        path.lineTo(rect.left + radius, rect.bottom);
        path.quadTo(rect.left,rect.bottom, rect.left, rect.bottom - radius);
        path.lineTo(rect.left,rect.top + radius);
        path.quadTo(rect.left,rect.top, rect.left + radius, rect.top);
        path.close();

        return path;
    }

}
