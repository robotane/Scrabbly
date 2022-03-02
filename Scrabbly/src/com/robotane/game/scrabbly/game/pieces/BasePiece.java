package com.robotane.game.scrabbly.game.pieces;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.robotane.game.scrabbly.BaseActor;
import com.robotane.game.scrabbly.util.Constants;

public class BasePiece extends BaseActor {
    public TextureRegion region;
    public Vector2 posOnBoard;

    public BasePiece(float x, float y, Stage stage) {
        super(x, y, stage);
        posOnBoard = new Vector2(x, y);
    }
}
