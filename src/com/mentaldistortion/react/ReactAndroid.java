package com.mentaldistortion.react;

import android.app.Activity;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.mentaldistortion.react.React;

public class ReactAndroid extends AndroidApplication
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        initialize(new React(), false);
    }
}
