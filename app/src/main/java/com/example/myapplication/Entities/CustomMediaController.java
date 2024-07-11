package com.example.myapplication.Entities;

import android.content.Context;
import android.view.View;
import android.widget.MediaController;

public class CustomMediaController extends MediaController {
    public CustomMediaController(Context context, View anchor)     {
        super(context);
        super.setAnchorView(anchor);
    }
    @Override
    public void setAnchorView(View view)     {
        // Do nothing   
    }
} 

