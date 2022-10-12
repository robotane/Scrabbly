package com.robotane.game.scrabbly;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.robotane.game.scrabbly.game.Assets;
import com.robotane.game.scrabbly.game.BoardController;
import com.robotane.game.scrabbly.game.BoardRender;
import com.robotane.game.scrabbly.game.GameScreen;

public class ScrabblyGame extends BaseGame implements ApplicationListener {

	private static final String TAG = ScrabblyGame.class.getName();

	private BoardController boardController;
	private BoardRender boardRenderer;

	private boolean paused;

	@Override
	public void create () {
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		super.create();
		setActiveScreen(new GameScreen());
	}

/*	@Override
	public void render () {
		// Do not update game world when paused.
		if (!paused) {
			// Update game world by the time that has passed
			// since last rendered frame.
			boardController.update(Gdx.graphics.getDeltaTime());
		}
		// Sets the clear screen color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Render game world to screen
		boardRenderer.render();
	}*/

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
//		boardRenderer.resize(width, height);
	}

	@Override
	public void pause () {
		paused = true;
	}

	@Override
	public void resume () {
		Assets.instance.init(new AssetManager());
		paused = false;
	}

	@Override
	public void dispose () {
//		boardRenderer.dispose();
		Assets.instance.dispose();
	}

}
