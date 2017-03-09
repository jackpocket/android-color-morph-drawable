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

    public ColorMorphController attach(View view){
        gestureDetector.attach(view);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(morphDrawable);
        else
            view.setBackgroundDrawable(morphDrawable);

        return this;
    }

    public ColorMorphController setColorNormal(int colorNormal) {
        this.colorNormal = colorNormal;

        if(!gestureDetector.isTouched() && touchEffectsEnabled)
            this.morphDrawable.overrideColor(colorNormal);

        return this;
    }

    public ColorMorphController setColorTouched(int colorTouched) {
        this.colorTouched = colorTouched;

        if(gestureDetector.isTouched() && touchEffectsEnabled)
            this.morphDrawable.overrideColor(colorTouched);

        return this;
    }

    public ColorMorphController morphRippledTo(int color){
        return morphRippledTo(color, null);
    }

    public ColorMorphController morphRippledTo(int color, int[] fromPosition){
        this.morphDrawable.morphRippledTo(color, fromPosition);

        return this;
    }

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

    public void setOnClickListener(View.OnClickListener onClickListener){
        gestureDetector.setOnClickListener(onClickListener);
    }

    public void setSecondaryGestureListener(SimpleOnGestureListener gestureListener){
        gestureDetector.setSecondaryGestureListener(gestureListener);
    }
}
