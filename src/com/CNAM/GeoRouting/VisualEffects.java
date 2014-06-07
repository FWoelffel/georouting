package com.CNAM.GeoRouting;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
/**
 * Created by fwoelffel on 07/06/14.
 */
public class VisualEffects {

    public static void toggleContent(Context ctx, View v, boolean b)
    {
        if (!b )
        {
            //VisualEffects.slide_up(ctx, v);
            v.setVisibility(View.GONE);
        }
        else if (b ){
            v.setVisibility(View.VISIBLE);
            VisualEffects.slide_down(ctx, v);
        }
    }

    public static void slide_down(Context ctx, View v){
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context ctx, View v){
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

}
