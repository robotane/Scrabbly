package com.robotane.game.scrabbly.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.robotane.game.scrabbly.util.Constants;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    public AssetPiece piece;
    public BoardTile boardTile;
    public AssetFonts fonts;
    private AssetManager assetManager;

    private Assets() {
    }


    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        // create game resource objects
        piece = new AssetPiece(atlas);
        boardTile = new BoardTile(atlas);
        fonts = new AssetFonts();
    }


    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
    }

    @Override
    public void dispose() {

        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }

    public static class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts() {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            // set font sizes
//            defaultSmall.setScale(0.75f);
//            defaultNormal.setScale(1.0f);
//            defaultBig.setScale(2.0f);
            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }

    public static class AssetPiece {
        public final AtlasRegion black;
        public final AtlasRegion black_alt;
        public final AtlasRegion white;
        public final AtlasRegion white_alt;
        public final AtlasRegion yellow;
        public final AtlasRegion yellow_alt;
        public final AtlasRegion green;
        public final AtlasRegion green_alt;
        public final AtlasRegion purple;
        public final AtlasRegion purple_alt;
        public final AtlasRegion orange;
        public final AtlasRegion orange_alt;

        public AssetPiece(TextureAtlas atlas) {
            black = atlas.findRegion("black_piece");
            black_alt = atlas.findRegion("black_piece_alt");
            white = atlas.findRegion("white_piece");
            white_alt = atlas.findRegion("white_piece_alt");
            orange = atlas.findRegion("orange_piece");
            orange_alt = atlas.findRegion("orange_piece_alt");
            green = atlas.findRegion("green_piece");
            green_alt = atlas.findRegion("green_piece_alt");
            purple = atlas.findRegion("purple_piece");
            purple_alt = atlas.findRegion("purple_piece_alt");
            yellow = atlas.findRegion("yellow_piece");
            yellow_alt = atlas.findRegion("yellow_piece_alt");
        }
    }

    public static class BoardTile {
        public final AtlasRegion brown_dark;
        public final AtlasRegion brown_light;
        public final AtlasRegion gray_dark;
        public final AtlasRegion gray_light;

        public BoardTile(TextureAtlas atlas) {
            brown_dark = atlas.findRegion("square_brown_dark");
            brown_light = atlas.findRegion("square_brown_light");
            gray_dark = atlas.findRegion("square_gray_dark");
            gray_light = atlas.findRegion("square_gray_light");
        }
    }
}
