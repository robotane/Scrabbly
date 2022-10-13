package com.robotane.game.scrabbly.game.pieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.robotane.game.scrabbly.BaseActor;
import com.robotane.game.scrabbly.game.Assets;
import com.robotane.game.scrabbly.game.Board;
import com.robotane.game.scrabbly.util.Constants;

import java.util.ArrayList;

public class Piece extends BaseActor {
    public Vector2 posOnBoard;
    public PieceType  type;
    public boolean isKing;
    public Board board;
    private Animation<TextureRegion> normalAnimation;
    private Animation<TextureRegion> selectedAnimation;
    private static final String TAG = Piece.class.getName();
    private boolean isSelected;

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

    public void moveTo(float x, float y, float duration){
        this.addAction(Actions.moveTo(x * Constants.PIECES_ZOOM -2,
                            y * Constants.PIECES_ZOOM, duration));
        board.pieces[(int)posOnBoard.x][(int)posOnBoard.y] = null;
        posOnBoard.x = x;
        posOnBoard.y = y;
        board.pieces[(int)posOnBoard.x][(int)posOnBoard.y] = this;
        if ((type == PieceType.WHITE && y == Constants.COLS - 1) || (type == PieceType.BLACK && y == 0)) {
            setKing(true);
        }
    }

    public void hide(){
        addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.hide()));
    }

    public void moveTo(float x, float y){
        this.moveTo(x, y,0);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        updateTexture();
    }

    public void setKing(boolean king) {
        isKing = king;
        updateTexture();
    }

    public void updateTexture(){
        normalAnimation = loadTextureRegion(
                isKing ? (type == PieceType.WHITE? Assets.instance.piece.white_alt:Assets.instance.piece.orange_alt)
                        :(type == PieceType.WHITE? Assets.instance.piece.white:Assets.instance.piece.orange)
        );

        selectedAnimation = loadTextureRegion(
                isKing ? (type == PieceType.WHITE? Assets.instance.piece.purple_alt:Assets.instance.piece.yellow_alt)
                        :(type == PieceType.WHITE? Assets.instance.piece.purple:Assets.instance.piece.yellow)
        );
       setAnimation();
    }

    public void setAnimation(){
        setAnimation(isSelected? selectedAnimation : normalAnimation);
    }

    private void init(){
        setKing(false);
        setSelected(false);
        addCaptureListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "touch started at (" + x + ", " + y + ")");
                Gdx.app.log(TAG, "X x Y = "+ posOnBoard.x +" x "+ posOnBoard.y );
                Piece newPiece = (Piece) event.getListenerActor();

                if (board.selectPiece){
                    if (board.selectedPiece != null && board.selectedPiece != newPiece) {
                        board.selectedPiece.setSelected(false);
                        if (board.showMoves) board.unMarkValidMoves();
                    }
                    board.selectedPiece = newPiece;
                    ArrayList<Vector2> allTakingMoves = board.getAllTakingOriginPos();
                    boolean canBeSelected = (allTakingMoves.isEmpty() && board.currentPlayer.color == board.selectedPiece.type) || allTakingMoves.contains(board.selectedPiece.posOnBoard);
                    if (canBeSelected){
                        board.selectedPiece.setSelected(true);
                        board.selectTarget = true;
                        if (board.showMoves && board.currentPlayer.color == board.selectedPiece.type)
                            board.markValidMoves();
                    }
                Gdx.app.log(TAG, board.selectedPiece.type.name());
                Gdx.app.log(TAG, allTakingMoves.toString());
                Gdx.app.log(TAG, Boolean.toString(canBeSelected));
                }

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
