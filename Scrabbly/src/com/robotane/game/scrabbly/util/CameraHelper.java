package com.robotane.game.scrabbly.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.robotane.game.scrabbly.game.pieces.GameObject;

public class CameraHelper {

    private static final String TAG = CameraHelper.class.getName();

    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;

    private Vector2 position;
    private float zoom;
    private GameObject target;

    public CameraHelper () {
        position = new Vector2();
        zoom = 1.0f;
    }

    public void update (float deltaTime) {
        if (!hasTarget()) return;

        position.x = target.position.x + target.origin.x;
        position.y = target.position.y + target.origin.y;

        // Prevent camera from moving down too far
        position.y = Math.max(-1f, position.y);
    }

    public void setPosition (float x, float y) {
        this.position.set(x, y);
    }

    public Vector2 getPosition () {
        return position;
    }

    public void addZoom (float amount) {
        setZoom(zoom + amount);
    }

    public void setZoom (float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public float getZoom () {
        return zoom;
    }

    public void setTarget (GameObject target) {
        this.target = target;
    }

    public GameObject getTarget () {
        return target;
    }

    public boolean hasTarget () {
        return target != null;
    }

    public boolean hasTarget (GameObject target) {
        return hasTarget() && this.target.equals(target);
    }

    public void applyTo (OrthographicCamera camera) {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }

}
