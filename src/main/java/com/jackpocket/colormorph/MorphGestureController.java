package com.jackpocket.colormorph;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

public class MorphGestureController extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {

    public interface MorphGestureEventListener {
        public void morphOnTouchDown(MotionEvent e);
        public void morphOnTouchUp(MotionEvent e);
    }

    private WeakReference<View> targetView = new WeakReference<View>(null);
    private MorphGestureEventListener eventListener;

    private GestureDetector gestureDetector;
    private boolean touched = false;

    private WeakReference<View.OnClickListener> onClickDelegate = new WeakReference<View.OnClickListener>(null);
    private WeakReference<View.OnLongClickListener> onLongClickDelegate = new WeakReference<View.OnLongClickListener>(null);
    private WeakReference<SimpleOnGestureListener> secondaryGestureDelegate = new WeakReference<SimpleOnGestureListener>(null);

    public MorphGestureController(Context context, MorphGestureEventListener eventListener){
        this.eventListener = eventListener;
        this.gestureDetector = new GestureDetector(context, this);
    }

    public MorphGestureController attach(View view){
        this.targetView = new WeakReference<View>(view);

        view.setOnTouchListener(this);

        return this;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean gestureEvent = gestureDetector.onTouchEvent(motionEvent);

        if(!(motionEvent.getAction() == MotionEvent.ACTION_DOWN
                || motionEvent.getAction() == MotionEvent.ACTION_MOVE)){
            touched = false;

            eventListener.morphOnTouchUp(motionEvent);
        }

        return gestureEvent;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        eventListener.morphOnTouchDown(e);

        touched = true;

        SimpleOnGestureListener delegate = secondaryGestureDelegate.get();
        if(delegate != null)
            delegate.onDown(e);

        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        View.OnClickListener listener = onClickDelegate.get();
        if(listener != null)
            listener.onClick(targetView.get());

        SimpleOnGestureListener delegate = secondaryGestureDelegate.get();
        if(delegate != null)
            delegate.onSingleTapConfirmed(event);

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        View.OnLongClickListener listener = onLongClickDelegate.get();
        if(listener != null)
            listener.onLongClick(targetView.get());

        SimpleOnGestureListener delegate = secondaryGestureDelegate.get();
        if(delegate != null)
            delegate.onLongPress(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        SimpleOnGestureListener delegate = secondaryGestureDelegate.get();
        if(delegate != null)
            delegate.onDoubleTap(e);

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        SimpleOnGestureListener delegate = secondaryGestureDelegate.get();
        if(delegate != null)
            delegate.onDoubleTapEvent(e);

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
        SimpleOnGestureListener delegate = secondaryGestureDelegate.get();
        if(delegate != null)
            delegate.onScroll(e1, e2, distanceX, distanceY);

        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
        SimpleOnGestureListener delegate = secondaryGestureDelegate.get();
        if(delegate != null)
            delegate.onFling(e1, e2, velocityX, velocityY);

        return true;
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        onClickDelegate = new WeakReference<View.OnClickListener>(onClickListener);
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
        onLongClickDelegate = new WeakReference<View.OnLongClickListener>(onLongClickListener);
    }

    public void setSecondaryGestureListener(GestureDetector.SimpleOnGestureListener gestureListener){
        this.secondaryGestureDelegate = new WeakReference<GestureDetector.SimpleOnGestureListener>(gestureListener);
    }

    public boolean isTouched(){
        return touched;
    }

}
