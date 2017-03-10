package com.jackpocket.colormorph;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import android.widget.FrameLayout;

public class ColorMorphLayout extends FrameLayout {

    private ColorMorphController morphController;

    public ColorMorphLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public ColorMorphLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ColorMorphLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle){
        morphController = new ColorMorphController(getContext(), attrs, defStyle)
                .attach(this);
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener){
        super.setOnClickListener(onClickListener);

        morphController.setOnClickListener(onClickListener);
    }

    @Override
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
        super.setOnLongClickListener(onLongClickListener);

        morphController.setOnLongClickListener(onLongClickListener);
    }

    public void setSecondaryGestureListener(GestureDetector.SimpleOnGestureListener gestureListener){
        morphController.setSecondaryGestureListener(gestureListener);
    }

    public ColorMorphController getMorphController(){
        return morphController;
    }

}
