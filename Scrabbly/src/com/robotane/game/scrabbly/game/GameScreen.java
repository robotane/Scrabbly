package com.robotane.game.scrabbly.game;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.robotane.game.scrabbly.BaseScreen;

public class GameScreen extends BaseScreen {

    private Board board;

    public void initialize() {
        board = new Board(0, 0, mainStage);
    }

    public void update(float deltaTime) {

    }
}
