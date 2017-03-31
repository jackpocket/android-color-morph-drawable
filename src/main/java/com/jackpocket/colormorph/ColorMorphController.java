package com.jackpocket.colormorph;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.jackpcoket.colormorph.R;
import com.jackpocket.colormorph.morphables.Morphable;

public class ColorMorphController implements MorphGestureController.MorphGestureEventListener {

    private int colorNormal;
    private int colorTouched;

    private ColorMorphDrawable morphDrawable;

    private boolean touchEffectsEnabled;
    private MorphGestureController gestureDetector;

    public ColorMorphController(Context context){
        this(context, null, 0);
    }

    public ColorMorphController(Context context, AttributeSet attrs, int defStyle){
        this.gestureDetector = new MorphGestureController(context, this);

        this.colorNormal = context.getResources()
                .getColor(R.color.cmd__default_color_normal);

        this.colorTouched = context.getResources()
                .getColor(R.color.cmd__default_color_touched);

        this.touchEffectsEnabled = context.getResources()
                .getBoolean(R.bool.cmd__touch_effects_enabled_by_default);

        int cornerRadius = (int) context.getResources()
                .getDimension(R.dimen.cmd__default_corner_radius);

        this.morphDrawable = new ColorMorphDrawable(colorNormal);

        if(attrs != null){
            TypedArray a = context.getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.ColorMorphLayout, defStyle, 0);

            this.colorNormal = a.getInt(R.styleable.ColorMorphLayout_colorNormal, colorNormal);
            this.colorTouched = a.getInt(R.styleable.ColorMorphLayout_colorTouched, colorTouched);
            this.touchEffectsEnabled = a.getBoolean(R.styleable.ColorMorphLayout_touchEffectsEnabled, touchEffectsEnabled);

            this.morphDrawable.overrideColor(colorNormal);

            this.morphDrawable.setDefaultDurationMs(a.getInt(R.styleable.ColorMorphLayout_morphDurationMs,
                    Morphable.DEFAULT_ANIM_DURATION));

            cornerRadius = (int) a.getDimension(R.styleable.ColorMorphLayout_borderCornerRadius,
                    cornerRadius);

            a.recycle();
        }

        this.morphDrawable.setCornerRadiusPx(cornerRadius);
    }

    /**
     * Actually attach the morph Drawable and gesture detectors to the View. Make sure any
     */
    public ColorMorphController attach(View view){
        gestureDetector.attach(view);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(morphDrawable);
        else
            view.setBackgroundDrawable(morphDrawable);

        return this;
    }

    /**
     * Set the normal-state color, and override current drawable if not touched
     */
    public ColorMorphController setColorNormal(int colorNormal) {
        this.colorNormal = colorNormal;

        if(!gestureDetector.isTouched() && touchEffectsEnabled)
            this.morphDrawable.overrideColor(colorNormal);

        return this;
    }

    /**
     * Set the normal-state color, but will not directly override current drawable
     */
    public ColorMorphController setColorNormalSilently(int colorNormal) {
        this.colorNormal = colorNormal;

        return this;
    }

    /**
     * Set the touched-state color, and override current drawable if touched
     */
    public ColorMorphController setColorTouched(int colorTouched) {
        this.colorTouched = colorTouched;

        if(gestureDetector.isTouched() && touchEffectsEnabled)
            this.morphDrawable.overrideColor(colorTouched);

        return this;
    }

    /**
     * Set the touched-state color, but will not directly override current drawable
     */
    public ColorMorphController setColorTouchedSilently(int colorTouched) {
        this.colorTouched = colorTouched;

        return this;
    }

    /**
     * Morph to the provided color from the center of the view
     */
    public ColorMorphController morphRippledTo(int color){
        return morphRippledTo(color, null);
    }

    /**
     * Morph to the provided color from a specified center position
     */
    public ColorMorphController morphRippledTo(int color, int[] fromPosition){
        this.morphDrawable.morphRippledTo(color, fromPosition);

        return this;
    }

    /**
     * Morph to the provided color using the supplied Morphable
     */
    public ColorMorphController morphWith(Morphable morphable){
        this.morphDrawable.morphWith(morphable);

        return this;
    }

    public ColorMorphDrawable getMorphDrawable(){
        return morphDrawable;
    }

    @Override
    public void morphOnTouchDown(MotionEvent event){
        morphDrawable.morphRippledTo(colorTouched,
                new int[]{(int) event.getX(), (int) event.getY()});
    }

    @Override
    public void morphOnTouchUp(MotionEvent event){
        morphDrawable.morphRippledTo(colorNormal,
                new int[]{(int) event.getX(), (int) event.getY()});
    }

    /**
     * Set a View.OnClickListener to be triggered by the GestureDetector's onSingleTapConfirmed() callback.
     * Note: You MUST also set the listener on the View if not using the ColorMorphLayout pattern
     */
    public ColorMorphController setOnClickListener(View.OnClickListener onClickListener){
        gestureDetector.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * Set a View.OnLongClickListener to be triggered by the GestureDetector's onLongPress() callback.
     * Note: You MUST also set the listener on the View if not using the ColorMorphLayout pattern
     */
    public ColorMorphController setOnLongClickListener(View.OnLongClickListener onClickListener){
        gestureDetector.setOnLongClickListener(onClickListener);
        return this;
    }

    /**
     * Set a secondary SimpleOnGestureListener to receive passive events from the attached View.
     * All gestures are handled internally and pass off the events as they occur.
     */
    public ColorMorphController setSecondaryGestureListener(SimpleOnGestureListener gestureListener){
        gestureDetector.setSecondaryGestureListener(gestureListener);
        return this;
    }

    public ColorMorphController setTouchEffectsEnabled(boolean touchEffectsEnabled){
        this.touchEffectsEnabled = touchEffectsEnabled;
        return this;
    }

    public boolean isTouched(){
        return gestureDetector.isTouched();
    }

}
