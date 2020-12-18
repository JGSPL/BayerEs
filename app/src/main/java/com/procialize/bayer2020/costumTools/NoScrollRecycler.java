package com.procialize.bayer2020.costumTools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

class NoScrollRecycler extends RecyclerView {

    public NoScrollRecycler(Context context){
        super(context);
    }

    public NoScrollRecycler(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public NoScrollRecycler(Context context, AttributeSet attrs, int style){
        super(context, attrs, style);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){

        //Ignore scroll events.
        if(ev.getAction() == MotionEvent.ACTION_MOVE)
            return true;

        //Dispatch event for non-scroll actions, namely clicks!
        return super.dispatchTouchEvent(ev);
    }
}