package com.wingsoft.wifikey.Animation;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by wing on 15/7/28.
 */
public class ShakeAnimation extends Animation {
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        t.getMatrix().setTranslate((float)Math.sin(interpolatedTime*50)*20,0);

        super.applyTransformation(interpolatedTime, t);
    }
}
