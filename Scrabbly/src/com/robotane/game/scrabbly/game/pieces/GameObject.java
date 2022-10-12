package com.robotane.game.scrabbly.game.pieces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameObject extends Actor {
    public Vector2 position;
    public Vector2 origin;
    public Vector2 dimension;
    public Animation animation;

    public GameObject() {
        super();
        origin = new Vector2();
        position = new Vector2();
        dimension = new Vector2(1,1);
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public void render(SpriteBatch batch){}

    public void act(float deltaTime) {
        super.act(deltaTime);
    }
}
