package org.example.asteroides.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import org.example.asteroides.R;

/**
 * Created by jamarfal on 24/10/16.
 */

public class DrawableController {

    private Context context;

    public DrawableController(Context context) {
        this.context = context;
    }

    public Drawable getAsteroid() {
        return ContextCompat.getDrawable(context, R.drawable.asteroide1);
    }

    public Drawable getShip() {
        return ContextCompat.getDrawable(context, R.drawable.nave);
    }

    public Drawable getAcceleratedShip() {
        return ContextCompat.getDrawable(context, R.drawable.nave_fuego);
    }

    public AnimationDrawable getMisil() {
        ImageView animatedMisil = new ImageView(context);
        animatedMisil.setBackgroundResource(R.drawable.animation_misil);
        return (AnimationDrawable) animatedMisil.getBackground();

    }

    public Drawable drawPathForShip() {
        Drawable drawableShip;
        Path pathShip = new Path();
        pathShip.moveTo(0.0f, 0.0f);
        pathShip.lineTo(0.0f, 1.0f);
        pathShip.lineTo(1.0f, .5f);
        pathShip.lineTo(0.0f, 0.0f);
        ShapeDrawable shapeDrawableShip = new ShapeDrawable(new PathShape(pathShip, 1, 1));
        shapeDrawableShip.getPaint().setColor(Color.WHITE);
        shapeDrawableShip.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawableShip.setIntrinsicWidth(20);
        shapeDrawableShip.setIntrinsicHeight(15);
        drawableShip = shapeDrawableShip;
        return drawableShip;
    }

    public Drawable drawPathForAsteroid() {
        Drawable drawableAsteroid;
        Path pathAsteroide = new Path();
        pathAsteroide.moveTo((float) 0.3, (float) 0.0);
        pathAsteroide.lineTo((float) 0.6, (float) 0.0);
        pathAsteroide.lineTo((float) 0.6, (float) 0.3);
        pathAsteroide.lineTo((float) 0.8, (float) 0.2);
        pathAsteroide.lineTo((float) 1.0, (float) 0.4);
        pathAsteroide.lineTo((float) 0.8, (float) 0.6);
        pathAsteroide.lineTo((float) 0.9, (float) 0.9);
        pathAsteroide.lineTo((float) 0.8, (float) 1.0);
        pathAsteroide.lineTo((float) 0.4, (float) 1.0);
        pathAsteroide.lineTo((float) 0.0, (float) 0.6);
        pathAsteroide.lineTo((float) 0.0, (float) 0.2);
        pathAsteroide.lineTo((float) 0.3, (float) 0.0);
        ShapeDrawable dAsteroide = new ShapeDrawable(
                new PathShape(pathAsteroide, 1, 1));
        dAsteroide.getPaint().setColor(Color.WHITE);
        dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
        dAsteroide.setIntrinsicWidth(50);
        dAsteroide.setIntrinsicHeight(50);
        drawableAsteroid = dAsteroide;
        return drawableAsteroid;
    }

    public ShapeDrawable drawPathForMisile() {
        ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
        dMisil.getPaint().setColor(Color.WHITE);
        dMisil.getPaint().setStyle(Paint.Style.STROKE);
        dMisil.setIntrinsicWidth(15);
        dMisil.setIntrinsicHeight(3);
        return dMisil;
    }


}
