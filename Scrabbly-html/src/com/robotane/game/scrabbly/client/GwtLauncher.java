package com.robotane.game.scrabbly.client;

import com.robotane.game.scrabbly.ScrabblyGame;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(480, 320);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new ScrabblyGame();
	}

	@Override
	public ApplicationListener createApplicationListener() {
		return getApplicationListener ();
	}
}