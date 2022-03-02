package com.robotane.game.scrabbly.game.pieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.robotane.game.scrabbly.BaseActor;
import com.robotane.game.scrabbly.game.Assets;
import com.robotane.game.scrabbly.game.Board;
import com.robotane.game.scrabbly.util.Constants;

public class Piece extends BaseActor {
    public Vector2 posOnBoard;
    public PieceType  type;
    public boolean king;
    public Board board;
    public TextureRegion pieceRegion;
    private static final String TAG = Piece.class.getName();

    public Piece(float x, float y, Stage stage, PieceType type) {
        super(x * Constants.PIECES_ZOOM -2 , y * Constants.PIECES_ZOOM , stage);
        posOnBoard = new Vector2(x, y);
        this.type = type;
        init();
    }

    public Piece(float x, float y, Stage stage, PieceType type, Board board){
        this(x, y, stage, type);
        this.board = board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void setX(float x) {
        super.setX(x * Constants.PIECES_ZOOM -2);
        posOnBoard.x = x;
    }

    @Override
    public void setY(float y) {
        super.setY(y* Constants.PIECES_ZOOM);
        posOnBoard.y = y;
    }

    public void setPosOnBoard(Vector2 posOnBoard) {
        setX(posOnBoard.x);
        setY(posOnBoard.y);
    }

    private void init(){
        king=false;
        pieceRegion = type==PieceType.WHITE? Assets.instance.piece.white : Assets.instance.piece.orange;
        loadTextureRegion(pieceRegion);

        addCaptureListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "touch started at (" + x + ", " + y + ")");
                Gdx.app.log(TAG, "X x Y = "+ posOnBoard.x +" x "+ posOnBoard.y );
                board.selectedPiece = (Piece) event.getListenerActor();
                Gdx.app.log(TAG, board.selectedPiece.type.name());

                return false;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "touch done at (" + x + ", " + y + ")");
                Gdx.app.log(TAG, "X x Y = "+ posOnBoard.x +" x "+ posOnBoard.y );
        }
        });
    }
}
