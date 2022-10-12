package com.robotane.game.scrabbly.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.robotane.game.scrabbly.BaseActor;
import com.robotane.game.scrabbly.game.pieces.Piece;
import com.robotane.game.scrabbly.game.pieces.PieceType;
import com.robotane.game.scrabbly.game.pieces.Tile;
import com.robotane.game.scrabbly.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board extends BaseActor {

    private static final String TAG = Board.class.getName();
    public Piece[][] pieces;
    public Tile[][] tiles;
    public Piece selectedPiece;
    public boolean selectTarget;
    public boolean selectPiece;
    public Player[] players; // TODO Use 'public Player[] players' instead and some constants like LIGHT_PLAYER=0,  DARK_PLAYER=1
    public final int LIGHT_PLAYER=0;
    public final int DARK_PLAYER=1;
    public Player currentPlayer;
    public boolean showMoves;

    public Board(float x, float y, Stage stage) {
        super(x, y, stage);
        init();
    }

    public PieceType getTileColor(float row, float col) {
        if ((int) col % 2 == (((int) row + 1) % 2)) return PieceType.BLACK;
        return PieceType.WHITE;
    }

    public Tile getTileAt(int x, int y) {
        return tiles[x][y];
    }

    public Tile getTileAt(Vector2 pos) {
        return getTileAt((int) pos.x, (int) pos.y);
    }

    public Piece getPieceAt(int x, int y) {
        return pieces[x][y];
    }

    public Piece getPieceAt(Vector2 pos) {
        return getPieceAt((int) pos.x, (int) pos.y);
    }

    public boolean hasPieceAt(int x, int y) {
        return pieces[x][y] != null;
    }

    public boolean hasPieceAt(Vector2 pos) {
        return hasPieceAt((int) pos.x, (int) pos.y);
    }

    public void setPieceAt(Vector2 pos, Piece piece) {
        setPieceAt((int) pos.x, (int) pos.y, piece);
    }

    private void setPieceAt(int x, int y, Piece piece) {
        pieces[x][y] = piece;
    }

    public boolean canMovePieceTo(int x, int y) {
        if (selectedPiece == null)
            return false;
        if (selectedPiece.type != currentPlayer.color)
            return false;
        Piece pieceAt = getPieceAt(x, y);
        boolean isEmptyTile = pieceAt == null;
        boolean isOpponent = pieceAt != null && pieceAt.type != selectedPiece.type;
        boolean isBlackTile = getTileColor(x, y) == PieceType.BLACK;
        boolean isValidMove = getValidMoves(false).containsKey(new Vector2(x, y));
        return selectTarget && (isEmptyTile || isOpponent) && isBlackTile && isValidMove;
    }

    public boolean canMovePieceTo(Vector2 pos) {
        return this.canMovePieceTo((int) pos.x, (int) pos.y);
    }

    private boolean isValidMove(Piece piece, int fromX, int fromY, int toX, int toY) {
        if (piece.type == PieceType.WHITE) {
            return (toX == fromX - 1 || toX == fromX + 1) && toY == fromY + 1;
        } else if (piece.type == PieceType.BLACK) {
            return (toX == fromX - 1 || toX == fromX + 1) && toY == fromY - 1;
        }
        return false;
    }

    private boolean isValidRow(float val) {
        return val >= 0 && val < Constants.ROWS;
    }

    private boolean isValidCol(float val) {
        return val >= 0 && val < Constants.COLS;
    }

    private boolean isValidRowCol(float row, float col) {
        return isValidRow(row) && isValidCol(col);
    }

    public Map<Vector2, Vector2> getValidMovesFrom(Vector2 from, ArrayList<Vector2> takenPos, boolean all) {
        int fromX = (int) from.x;
        int fromY = (int) from.y;
        Map<Vector2, Vector2> validMoves = new HashMap<>();

        int oneForward;
        int oneBackward;
        int oneLeft;
        int oneRight;
        int twoForward;
        int twoBackward;
        int twoLeft;
        int twoRight;

        PieceType opponentType;

        oneLeft = fromX - 1;
        oneRight = fromX + 1;
        twoLeft = fromX - 2;
        twoRight = fromX + 2;

        if (selectedPiece.type == PieceType.WHITE) {
            oneForward = fromY + 1;
            oneBackward = fromY - 1;
            twoForward = fromY + 2;
            twoBackward = fromY - 2;
            opponentType = PieceType.BLACK;
        }
        else {
            oneForward = fromY - 1;
            oneBackward = fromY + 1;
            twoForward = fromY - 2;
            twoBackward = fromY + 2;
            opponentType = PieceType.WHITE;
        }

        // FORWARD LEFT
        if (isValidRowCol(oneLeft, oneForward)) {
            if (!hasPieceAt(oneLeft, oneForward)) {
                if (selectedPiece.isKing){
                    Vector2 nextPosition = new Vector2(oneLeft, oneForward);
                    validMoves.put(nextPosition, null);
                    if ((selectedPiece.type == PieceType.WHITE && oneLeft != 0 && oneForward != Constants.COLS - 1)
                        ||(selectedPiece.type == PieceType.BLACK && oneLeft != 0 && oneForward != 0))
                        validMoves.putAll(getValidMovesFrom(nextPosition, takenPos, all));
                }
                else if (takenPos.isEmpty())
                    validMoves.put(new Vector2(oneLeft, oneForward), null);
            } else if (getPieceAt(oneLeft, oneForward).type == opponentType
                    && !takenPos.contains(getPieceAt(oneLeft, oneForward).posOnBoard)) {
                if (isValidRowCol(twoLeft, twoForward)) {
                    if (!hasPieceAt(twoLeft, twoForward)) {
                        Vector2 newTargetPos = new Vector2(twoLeft, twoForward);
                        Vector2 newTakenPos = new Vector2(oneLeft, oneForward);
                        validMoves.put(newTargetPos, newTakenPos);
                        takenPos.add(newTakenPos);
                        if (all) validMoves.putAll(getValidMovesFrom(newTargetPos, takenPos, all));
                    }
                }
            }
        }

        // FORWARD RIGHT
        if (isValidRowCol(oneRight, oneForward)) {
            if (!hasPieceAt(oneRight, oneForward)) {
                if (selectedPiece.isKing){
                    Vector2 nextPosition = new Vector2(oneRight, oneForward);
                    validMoves.put(nextPosition, null);
                    if ((selectedPiece.type == PieceType.WHITE && oneRight != Constants.ROWS - 1 && oneForward != Constants.COLS - 1)
                        ||(selectedPiece.type == PieceType.BLACK && oneRight != Constants.ROWS - 1 && oneForward != 0))
                        validMoves.putAll(getValidMovesFrom(nextPosition, takenPos, all));
                }
                if (takenPos.isEmpty())
                    validMoves.put(new Vector2(oneRight, oneForward), null);
            } else if (getPieceAt(oneRight, oneForward).type == opponentType
                    && !takenPos.contains(getPieceAt(oneRight, oneForward).posOnBoard)) {
                if (isValidRowCol(twoRight, twoForward)) {
                    if (!hasPieceAt(twoRight, twoForward)) {
                        Vector2 newTargetPos = new Vector2(twoRight, twoForward);
                        Vector2 newTakenPos = new Vector2(oneRight, oneForward);
                        validMoves.put(newTargetPos, newTakenPos);
                        takenPos.add(newTakenPos);
                        if (all) validMoves.putAll(getValidMovesFrom(newTargetPos, takenPos, all));
                    }
                }
            }
        }

        // BACKWARD RIGHT
        if (isValidRowCol(oneRight, oneBackward)) {
            if (!hasPieceAt(oneRight, oneBackward) && selectedPiece.isKing){
                Vector2 nextPosition = new Vector2(oneRight, oneBackward);
                validMoves.put(nextPosition, null);
                if ((selectedPiece.type == PieceType.WHITE && oneRight != Constants.ROWS - 1 && oneBackward != 0)
                    ||(selectedPiece.type == PieceType.BLACK && oneRight != Constants.ROWS - 1 && oneBackward != Constants.COLS - 1))
                    validMoves.putAll(getValidMovesFrom(nextPosition, takenPos, all));
            }
            else if (hasPieceAt(oneRight, oneBackward) && getPieceAt(oneRight, oneBackward).type == opponentType
                    && !takenPos.contains(getPieceAt(oneRight, oneBackward).posOnBoard)) {
                if (isValidRowCol(twoRight, twoBackward)) {
                    if (!hasPieceAt(twoRight, twoBackward)) {
                        Vector2 newTargetPos = new Vector2(twoRight, twoBackward);
                        Vector2 newTakenPos = new Vector2(oneRight, oneBackward);
                        validMoves.put(newTargetPos, newTakenPos);
                        takenPos.add(newTakenPos);
                        if (all) validMoves.putAll(getValidMovesFrom(newTargetPos, takenPos, all));
                    }
                }
            }
        }

        // BACKWARD LEFT
        if (isValidRowCol(oneLeft, oneBackward)) {
            if (!hasPieceAt(oneLeft, oneBackward) && selectedPiece.isKing){
                Vector2 nextPosition = new Vector2(oneLeft, oneBackward);
                validMoves.put(nextPosition, null);
                if ((selectedPiece.type == PieceType.WHITE && oneLeft != 0 && oneBackward != 0)
                    ||(selectedPiece.type == PieceType.BLACK && oneLeft != 0 && oneBackward != Constants.COLS - 1))
                    validMoves.putAll(getValidMovesFrom(nextPosition, takenPos, all));
            }
            else if (hasPieceAt(oneLeft, oneBackward) && getPieceAt(oneLeft, oneBackward).type == opponentType
                    && !takenPos.contains(getPieceAt(oneLeft, oneBackward).posOnBoard)) {
                if (isValidRowCol(twoLeft, twoBackward)) {
                    if (!hasPieceAt(twoLeft, twoBackward)) {
                        Vector2 newTargetPos = new Vector2(twoLeft, twoBackward);
                        Vector2 newTakenPos = new Vector2(oneLeft, oneBackward);
                        validMoves.put(newTargetPos, newTakenPos);
                        takenPos.add(newTakenPos);
                        if (all) validMoves.putAll(getValidMovesFrom(newTargetPos, takenPos, all));
                    }
                }
            }
        }

        Map<Vector2, Vector2> takingMoves = getTakingMoves(validMoves);
        return takingMoves.isEmpty() ? validMoves : takingMoves;
    }

    public Map<Vector2, Vector2> getTakingMoves(Map<Vector2, Vector2> validMoves) {
        Map<Vector2, Vector2> takingMoves = new HashMap<>();
        for (Vector2 k :
                validMoves.keySet()) {
            if (validMoves.get(k) != null)
                takingMoves.put(k, validMoves.get(k));
        }
        return takingMoves;
    }

    public Map<Vector2, Vector2> getValidMoves(boolean all) {
        return getValidMovesFrom(selectedPiece.posOnBoard, new ArrayList<>(), all);
    }

    public ArrayList<Vector2> getAllTakingPos(){
        ArrayList<Vector2> allTakingPos = new ArrayList<>();
        if (selectedPiece == null || currentPlayer.color != selectedPiece.type) return allTakingPos;
        for (Piece p :
                currentPlayer.getPieces()) {
            if (!getTakingMoves(getValidMovesFrom(p.posOnBoard, new ArrayList<>(), false)).isEmpty())
                allTakingPos.add(p.posOnBoard);
        }
        return allTakingPos;
    }

    public void markValidMoves() {
        Map<Vector2, Vector2> validMoves = getValidMoves(true);
        for (Vector2 pos :
                validMoves.keySet()) {
            getTileAt(pos).setMarked(true);
        }
    }

    public void unMarkValidMoves() {
        Map<Vector2, Vector2> validMoves = getValidMoves(true);
        for (Vector2 pos :
                validMoves.keySet()) {
            getTileAt(pos).setMarked(false);
        }
    }

    public void moveSelectedPieceTo(float x, float y) {
        Map<Vector2, Vector2> validMoves = getValidMoves(false);
        Vector2 pos = validMoves.get(new Vector2(x, y));
        Piece takenPiece = null;
        if (pos != null) {
            takenPiece = getPieceAt(pos);
            currentPlayer.takePiece(takenPiece);
            getOtherPlayer().removePiece(takenPiece);
        }
        selectedPiece.moveTo(x, y, 0.5f);
        selectedPiece.toFront();

        // TODO Check if a can take another piece, if so, select this piece and oblige it to take that piece
        if (takenPiece == null || getTakingMoves(getValidMoves(false)).isEmpty()){
            selectTarget = false;
            selectPiece = true;
            switchToNexPlayer();
        }
        else {
            if (showMoves) markValidMoves();
            selectPiece = false;
        }
    }

    private Player getOtherPlayer() {
        if (currentPlayer == players[LIGHT_PLAYER]) return players[DARK_PLAYER];
        return players[LIGHT_PLAYER];
//        return players.get((players.idOf(currentPlayer) + 1) % players.size());
    }

    private void switchToNexPlayer() {
        currentPlayer = getOtherPlayer();
    }

    public void moveSelectedPieceTo(Vector2 pos) {
        this.moveSelectedPieceTo(pos.x, pos.y);
    }

    private void init() {
        setSize(1024, 1024);
        selectTarget = false;
        selectPiece = true;
        showMoves = true;
        players = new Player[2];
        players[LIGHT_PLAYER] = new Player("John", PieceType.WHITE, this);
        players[DARK_PLAYER] = new Player("Smith", PieceType.BLACK, this);
        currentPlayer = players[LIGHT_PLAYER];
        pieces = new Piece[Constants.ROWS][Constants.COLS];
        tiles = new Tile[Constants.ROWS][Constants.COLS];
        Tile tile;
        Piece piece;
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                piece = null;
                if (col % 2 == ((row + 1) % 2)) {
                    tile = new Tile(col, row, getStage(), PieceType.BLACK, this);
                    if (row < 3) {
                        piece = new Piece(col, row, getStage(), PieceType.WHITE, this);
                        players[LIGHT_PLAYER].addPiece(piece);
                    }
                    else if (row>4){
                        piece = new Piece(col, row, getStage(), PieceType.BLACK, this);
                        players[DARK_PLAYER].addPiece(piece);
                    }
                } else {
                    tile = new Tile(col, row, getStage(), PieceType.WHITE, this);
                }

                tiles[col][row] = tile;
                pieces[col][row] = piece;
            }
        }

        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "touch started at (" + x + ", " + y + ")");
//                Gdx.app.log(TAG, selectedPiece.type.name());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "touch up at (" + x + ", " + y + ")");
            }
        });
    }

}
