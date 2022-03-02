package com.robotane.game.scrabbly.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.robotane.game.scrabbly.BaseActor;
import com.robotane.game.scrabbly.game.pieces.Piece;
import com.robotane.game.scrabbly.game.pieces.PieceType;
import com.robotane.game.scrabbly.game.pieces.Tile;
import com.robotane.game.scrabbly.util.Constants;

public class Board extends BaseActor {

    private static final String TAG = Board.class.getName();
    public Piece[][] pieces;
    public Tile[][] tiles;
    public Piece selectedPiece;

    public Board(float x, float y, Stage stage){
        super(x, y, stage);
        init();
    }

    

    private void init(){
        setSize(1024, 1024);
        pieces = new Piece[Constants.ROWS][Constants.COLS];
        tiles = new Tile[Constants.ROWS][Constants.COLS];
        Tile tile;
        Piece piece;
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                piece = null;
                if(col % 2 == ((row +  1) % 2)){
                    tile = new Tile(col, row, getStage(), PieceType.BLACK);
                    if(row<3){
                        piece = new Piece(col, row, getStage(), PieceType.WHITE, this);
//                        addActor(piece);
                    }
                    else if (row>4){
                        piece = new Piece(col, row, getStage(), PieceType.BLACK, this);
//                        addActor(piece);
                    }
                }
                else {
                    tile = new Tile(col, row, getStage(), PieceType.WHITE);
//                    addActor(tile);
                }

                tiles[row][col] = tile;
                pieces[row][col] = piece;
            }
        }

        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "touch started at (" + x + ", " + y + ")");
                System.out.println(selectedPiece.type);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "touch up at (" + x + ", " + y + ")");
            }
        });
    }

}
