package com.robotane.game.scrabbly.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.robotane.game.scrabbly.util.Constants;

public class BoardRender implements Disposable {

    private static final String TAG = BoardRender.class.getName();

    private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private BoardController boardController;

    public BoardRender(BoardController boardController) {
        this.boardController = boardController;
        init();
    }

    private void init () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true); // flip y-axis
		cameraGUI.update();
	}

	private void renderBoard(SpriteBatch batch) {
		boardController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
//		boardController.board.render(batch);
		batch.end();
	}

	public void render(){
    	renderBoard(batch);
	}

    @Override
    public void dispose() {
    	batch.dispose();
    	boardController.dispose();
    }

	public void resize(int width, int height) {
	}
}
