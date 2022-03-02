package com.robotane.game.scrabbly;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class Main {
		private static boolean rebuildAtlas = false;
		private static boolean drawDebugOutline = false;
		public static void main(String[] args) {
			if (rebuildAtlas) {
				Settings settings = new Settings();
				settings.maxWidth = 1024;
				settings.maxHeight = 1024;
				settings.debug = drawDebugOutline;
				TexturePacker.process(settings, "assets-raw/images", "images/", "scrabbly");
			}
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Scrabbly";
		cfg.useGL30 = false;
		cfg.width = 480;
		cfg.height = 480;
		new LwjglApplication(new ScrabblyGame(), cfg);
	}
}
