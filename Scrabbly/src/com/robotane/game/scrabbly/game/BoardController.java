package com.robotane.game.scrabbly.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.robotane.game.scrabbly.util.CameraHelper;

public class BoardController extends InputAdapter implements Disposable {

    public CameraHelper cameraHelper;
    Board board;

    public BoardController() {
        init();
    }

    private void init () {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
//		board = new Board();
	}

    @Override
    public void dispose() {
    }

    public void update(float deltaTime) {
    }
}
