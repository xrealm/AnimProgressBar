package com.mao.anim_pb;

import android.content.Context;

/**
 * Created by mpg on 2015/11/1 .
 */
public class Util {

    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
}
