package com.procialize.bayer2020.costumTools;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import java.util.HashMap;

/**
 * Created by Rahul on 31-05-2019.
 */

public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
        //return hm.get("vicinity");

    }

    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }
    /**
     * After a selection, capture the new value and append to the existing
     * text
     */
    @Override
    protected void replaceText(final CharSequence text) {
        super.replaceText(text);
    }

}  /*extends AppCompatAutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    */ /** Returns the place description corresponding to the selected item *//*
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        *//** Each item in the autocompetetextview suggestion list is a hashmap object *//*
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }
}*/