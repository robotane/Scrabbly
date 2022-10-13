package com.robotane.game.scrabbly.game.pieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.robotane.game.scrabbly.BaseActor;
import com.robotane.game.scrabbly.game.Assets;
import com.robotane.game.scrabbly.game.Board;
import com.robotane.game.scrabbly.util.Constants;

public class Tile extends BaseActor {
    public Vector2 posOnBoard;
    public PieceType  type;
    public TextureRegion tileRegion;
    public Board board;
    private static final String TAG = Tile.class.getName();
    private boolean isMarked;

    public Tile(float x, float y, Stage stage, PieceType type) {
        super(x * Constants.PIECES_ZOOM, y * Constants.PIECES_ZOOM , stage);
        posOnBoard = new Vector2(x, y);
        this.type = type;
        init();  // TODO
    }

    public Tile(float x, float y, Stage stage, PieceType type, Board board) { // TODO
        this(x, y, stage, type);
        this.board = board;  // TODO
    }

    @Override
    public void setX(float x) {
        super.setX(x * Constants.PIECES_ZOOM);
        posOnBoard.x = x;
    }

    @Override
    public void setY(float y) {
        super.setY(y* Constants.PIECES_ZOOM);
        posOnBoard.y = y;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean selected) {
        isMarked = selected;
        updateTexture();
    }

    public void updateTexture(){
        if (isMarked){
            tileRegion = type==PieceType.WHITE? Assets.instance.boardTile.gray_light : Assets.instance.boardTile.gray_dark;
        }
        else tileRegion = type==PieceType.WHITE? Assets.instance.boardTile.brown_light : Assets.instance.boardTile.brown_dark;
//        Gdx.app.log(TAG,tileRegion.toString());
        setAnimation(loadTextureRegion(tileRegion));
    }

    public void setPosOnBoard(Vector2 posOnBoard) {
        setX(posOnBoard.x);
        setY(posOnBoard.y);
    }

    private void init(){
        setMarked(false);

        addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "touch started at (" + x + ", " + y + ")");
                Gdx.app.log(TAG, "X x Y = "+ posOnBoard.x +" x "+ posOnBoard.y);
                if (board.canMovePieceTo(posOnBoard)) {
                    if (board.showMoves) board.unMarkValidMoves();
                    board.moveSelectedPieceTo(posOnBoard);
                }
                return false;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.log(TAG, "touch done at (" + x + ", " + y + ")");
                Gdx.app.log(TAG, "X x Y = "+ posOnBoard.x +" x "+ posOnBoard.y );
            }
        });
    }
}
