package org.example.asteroides.view;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by jamarfal on 4/10/16.
 */

public class GraphicGame {

    private Drawable drawable;
    private int cenX, cenY;
    private int width, height;
    private double incX, incY;
    private double angle, rotacion;//Ángulo y velocidad rotación
    private int collisionCollider; //Para determinar colisión
    private int previousXposition, previosYposition; // Posición anterior
    private int radioInval; // Radio usado en invalidate()
    private View view; // Usada en view.invalidate()

    public GraphicGame(View view, Drawable drawable) {
        this.view = view;
        this.drawable = drawable;
        width = drawable.getIntrinsicWidth();
        height = drawable.getIntrinsicHeight();
        collisionCollider = (height + width) / 4;
        radioInval = (int) Math.hypot(width / 2, height / 2);
    }

    public void drawGraphic(Canvas canvas) {
        int x = cenX - width / 2;
        int y = cenY - height / 2;
        drawable.setBounds(x, y, x + width, y + height);
        canvas.save();
        canvas.rotate((float) angle, cenX, cenY);
        drawable.draw(canvas);
        canvas.restore();
        view.invalidate(cenX - radioInval, cenY - radioInval,
                cenX + radioInval, cenY + radioInval);
        view.invalidate(previousXposition - radioInval, previosYposition - radioInval,
                previousXposition + radioInval, previosYposition + radioInval);
        previousXposition = cenX;
        previosYposition = cenY;
    }

    public void increasePosition(double factor) {
        cenX += incX * factor;
        cenY += incY * factor;
        angle += rotacion * factor;
        // Si salimos de la pantalla, corregimos posición
        if (cenX < 0) cenX = view.getWidth();
        if (cenX > view.getWidth()) cenX = 0;
        if (cenY < 0) cenY = view.getHeight();
        if (cenY > view.getHeight()) cenY = 0;
    }

    public double distance(GraphicGame g) {
        return Math.hypot(cenX - g.cenX, cenY - g.cenY);
    }

    public boolean checkCollision(GraphicGame g) {
        return (distance(g) < (collisionCollider + g.collisionCollider));
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getCenX() {
        return cenX;
    }

    public void setCenX(int cenX) {
        this.cenX = cenX;
    }

    public int getCenY() {
        return cenY;
    }

    public void setCenY(int cenY) {
        this.cenY = cenY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getIncX() {
        return incX;
    }

    public void setIncX(double incX) {
        this.incX = incX;
    }

    public double getIncY() {
        return incY;
    }

    public void setIncY(double incY) {
        this.incY = incY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRotacion() {
        return rotacion;
    }

    public void setRotacion(double rotacion) {
        this.rotacion = rotacion;
    }

    public int getCollisionCollider() {
        return collisionCollider;
    }

    public void setCollisionCollider(int collisionCollider) {
        this.collisionCollider = collisionCollider;
    }

    public int getPreviousXposition() {
        return previousXposition;
    }

    public void setPreviousXposition(int previousXposition) {
        this.previousXposition = previousXposition;
    }

    public int getPreviosYposition() {
        return previosYposition;
    }

    public void setPreviosYposition(int previosYposition) {
        this.previosYposition = previosYposition;
    }

    public int getRadioInval() {
        return radioInval;
    }

    public void setRadioInval(int radioInval) {
        this.radioInval = radioInval;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
