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
    public Player[] players;
    public final int LIGHT_PLAYER=0;
    public final int DARK_PLAYER=1;
    public Player currentPlayer;
    public boolean showMoves;
    // TODO Comment every method in this so big class
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

    private int getNextForward(PieceType type, int currentPos){
        return type == PieceType.WHITE? currentPos + 1: currentPos - 1;
    }

    private int getNextBackward(PieceType type, int currentPos){
        return type == PieceType.WHITE? currentPos - 1: currentPos + 1;
    }

    private int getNextLeft(PieceType type, int currentPos){
        return currentPos - 1;
    }

    private int getNextRight(PieceType type, int currentPos){
        return currentPos + 1;
    }

    private int getNextForward(int currentPos){
        return getNextForward(selectedPiece.type, currentPos);
    }

    private int getNextBackward(int currentPos){
        return getNextBackward(selectedPiece.type, currentPos);
    }

    private int getNextLeft(int currentPos){
        return getNextLeft(selectedPiece.type, currentPos);
    }

    private int getNextRight(int currentPos){
        return getNextRight(selectedPiece.type, currentPos);
    }

    public Map<Vector2, ArrayList<Vector2>> getValidMovesFromOff(Vector2 from, PieceType type, boolean isKing, ArrayList<Vector2> takenPos, boolean all) {
        int fromX = (int) from.x;
        int fromY = (int) from.y;
        Map<Vector2, ArrayList<Vector2>> validMoves = new HashMap<>();

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

        if (type == PieceType.WHITE) {
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
            if (isKing || !hasPieceAt(oneLeft, oneForward)) {
                if (isKing){
                    int currentX = oneLeft, nextX;
                    int currentY = oneForward, nextY;
                    ArrayList<Vector2> takenPieces = new ArrayList<>();
                    while (isValidRowCol(currentX, currentY)) {
                        if (!hasPieceAt(currentX, currentY)){
                            validMoves.put(new Vector2(currentX, currentY), new ArrayList<>(takenPieces));
                        }
                        else {
                            if (getPieceAt(currentX, currentY).type == opponentType){
                                nextX = getNextLeft(currentX);
                                nextY = getNextForward(currentY);
                                if (kingTaking(takenPos, validMoves, currentX, nextX, currentY, nextY, takenPieces))
                                    break;
                            }
                            else break;
                        }
                        currentX = getNextLeft(currentX);
                        currentY = getNextForward(currentY);
                    }
                }
                else if (takenPos.isEmpty())
                    validMoves.putIfAbsent(new Vector2(oneLeft, oneForward), new ArrayList<>());
            } else {
                takingForward(type, isKing, takenPos, all, validMoves, oneForward, oneLeft, twoForward, twoLeft, opponentType);
            }
        }

        // FORWARD RIGHT
        if (isValidRowCol(oneRight, oneForward)) {
            if (isKing || !hasPieceAt(oneRight, oneForward)) {
                if (isKing){
                    int currentX = oneRight, nextX;
                    int currentY = oneForward, nextY;
                    ArrayList<Vector2> takenPieces = new ArrayList<>();
                    while (isValidRowCol(currentX, currentY)) {
                        if (!hasPieceAt(currentX, currentY)){
                            validMoves.put(new Vector2(currentX, currentY), new ArrayList<>(takenPieces));
                        }
                        else {
                            if (getPieceAt(currentX, currentY).type == opponentType){
                                nextX = getNextRight(currentX);
                                nextY = getNextForward(currentY);
                                if (kingTaking(takenPos, validMoves, currentX, nextX, currentY, nextY, takenPieces))
                                    break;
                            }
                            else break;
                        }
                        currentX = getNextRight(currentX);
                        currentY = getNextForward(currentY);
                    }
                }
                if (takenPos.isEmpty())
                    validMoves.putIfAbsent(new Vector2(oneRight, oneForward), new ArrayList<>());
            } else
                takingForward(type, isKing, takenPos, all, validMoves, oneForward, oneRight, twoForward, twoRight, opponentType);
        }

        // BACKWARD RIGHT
        if (isValidRowCol(oneRight, oneBackward)) {
            if (isKing){
                int currentX = oneRight, nextX;
                int currentY = oneBackward, nextY;
                ArrayList<Vector2> takenPieces = new ArrayList<>();
                while (isValidRowCol(currentX, currentY)) {
                    if (!hasPieceAt(currentX, currentY)){
                        validMoves.put(new Vector2(currentX, currentY), new ArrayList<>(takenPieces));
                    }
                    else {
                        if (getPieceAt(currentX, currentY).type == opponentType) {
                            nextX = getNextRight(currentX);
                            nextY = getNextBackward(currentY);
                            if (kingTaking(takenPos, validMoves, currentX, nextX, currentY, nextY, takenPieces))
                                break;
                        }
                        else break;
                    }
                    currentX = getNextRight(currentX);
                    currentY = getNextBackward(currentY);
                }
            }
            else {
                takingBackward(type, isKing, takenPos, all, validMoves, oneBackward, oneRight, twoBackward, twoRight, opponentType);
            }
        }

        // BACKWARD LEFT
        if (isValidRowCol(oneLeft, oneBackward)) {
            if (isKing){
                int currentX = oneLeft, nextX;
                int currentY = oneBackward, nextY;
                ArrayList<Vector2> takenPieces = new ArrayList<>();
                while (isValidRowCol(currentX, currentY)) {
                    if (!hasPieceAt(currentX, currentY)){
                        validMoves.put(new Vector2(currentX, currentY), new ArrayList<>(takenPieces));
                    }
                    else {
                        if (getPieceAt(currentX, currentY).type == opponentType) {
                            nextX = getNextLeft(currentX);
                            nextY = getNextBackward(currentY);
                            if (kingTaking(takenPos, validMoves, currentX, nextX, currentY, nextY, takenPieces))
                                break;
                        }
                        else break;
                    }
                    currentX = getNextLeft(currentX);
                    currentY = getNextBackward(currentY);
                }
            }
            else
                takingBackward(type, isKing, takenPos, all, validMoves, oneBackward, oneLeft, twoBackward, twoLeft, opponentType);
        }

        Map<Vector2, ArrayList<Vector2>> takingMoves = getTakingTargetPos(validMoves);
        return takingMoves.isEmpty() ? validMoves : takingMoves;
    }

    private boolean kingTaking(ArrayList<Vector2> takenPos, Map<Vector2, ArrayList<Vector2>> validMoves, int currentX, int nextX, int currentY, int nextY, ArrayList<Vector2> takenPieces) {
        if (isValidRowCol(nextX, nextY)){
            if (!hasPieceAt(nextX, nextY)) {
                Vector2 newTargetPos = new Vector2(nextX, nextY);
                Vector2 newTakenPos = new Vector2(currentX, currentY);
                takenPos.add(newTakenPos);
                takenPieces.add(newTakenPos);
                validMoves.put(newTargetPos, new ArrayList<>(takenPieces));
            }
            else return true;
        }
        return false;
    }

    private void takingBackward(PieceType type, boolean isKing, ArrayList<Vector2> takenPos, boolean all, Map<Vector2, ArrayList<Vector2>> validMoves, int oneBackward, int oneRight, int twoBackward, int twoRight, PieceType opponentType) {
        if (hasPieceAt(oneRight, oneBackward) && getPieceAt(oneRight, oneBackward).type == opponentType
                && !takenPos.contains(getPieceAt(oneRight, oneBackward).posOnBoard)) {
            taking(type, isKing, takenPos, all, validMoves, oneBackward, oneRight, twoBackward, twoRight);
        }
    }

    private void taking(PieceType type, boolean isKing, ArrayList<Vector2> takenPos, boolean all, Map<Vector2, ArrayList<Vector2>> validMoves, int currentY, int currentX, int nextY, int nextX) {
        if (isValidRowCol(nextX, nextY)) {
            if (!hasPieceAt(nextX, nextY)) {
                Vector2 newTargetPos = new Vector2(nextX, nextY);
                Vector2 newTakenPos = new Vector2(currentX, currentY);
                takenPos.add(newTakenPos);
//                validMoves.put(newTargetPos, takenPos);
                validMoves.compute(newTargetPos, (k, v) -> {
                    if (v == null){
                        v = new ArrayList<>();
                    }
                    v.add(newTakenPos);
                    return v;
                });
                if ((type == PieceType.WHITE && newTargetPos.y == Constants.COLS - 1) || (type == PieceType.BLACK && newTargetPos.y == 0)) {
                    isKing = true;
                }
                if (all) validMoves.putAll(getValidMovesFromOff(newTargetPos, type, isKing, takenPos, all));
            }
        }
    }

    private void takingForward(PieceType type, boolean isKing, ArrayList<Vector2> takenPos, boolean all, Map<Vector2, ArrayList<Vector2>> validMoves, int oneForward, int oneLeft, int twoForward, int twoLeft, PieceType opponentType) {
        if (getPieceAt(oneLeft, oneForward).type == opponentType
                && !takenPos.contains(getPieceAt(oneLeft, oneForward).posOnBoard)) {
            taking(type, isKing, takenPos, all, validMoves, oneForward, oneLeft, twoForward, twoLeft);
        }
    }

    public Map<Vector2, ArrayList<Vector2>> getTakingTargetPos(Map<Vector2, ArrayList<Vector2>> validMoves) {
        Map<Vector2, ArrayList<Vector2>> takingMoves = new HashMap<>();
        ArrayList<Vector2> v;
        for (Vector2 k :
                validMoves.keySet()) {
            v = validMoves.get(k);
            if (v != null && !v.isEmpty())
                takingMoves.put(k, v);
        }
        return takingMoves;
    }

    public Map<Vector2, ArrayList<Vector2>> getValidMoves(boolean all) {
        return getValidMovesFrom(selectedPiece.posOnBoard, new ArrayList<>(), all);
    }

    public Map<Vector2, ArrayList<Vector2>> getValidMovesOff(Piece off, boolean all) {
        return getValidMovesFromOff(off.posOnBoard, off.type, off.isKing, new ArrayList<>(), all);
    }

    private Map<Vector2, ArrayList<Vector2>> getValidMovesFrom(Vector2 from, ArrayList<Vector2> takenPos, boolean all) {
        return getValidMovesFromOff(from, selectedPiece.type, selectedPiece.isKing, takenPos, all);
    }

    /**
     * Get all the positions from which a piece can be taken
     * @return a list of position
     */
    public ArrayList<Vector2> getAllTakingOriginPos(){
        ArrayList<Vector2> allTakingPos = new ArrayList<>();
        if (selectedPiece == null || currentPlayer.color != selectedPiece.type) return allTakingPos;
        for (Piece p :
                currentPlayer.getPieces()) {
            if (!getTakingTargetPos(getValidMovesOff(p, false)).isEmpty())
                allTakingPos.add(p.posOnBoard);
        }
        return allTakingPos;
    }

    public void markValidMoves() {
//        Map<Vector2, ArrayList<Vector2>> validMoves = getValidMoves(true);
        for (Vector2 pos :
                getValidMoves(true).keySet()) {
            getTileAt(pos).setMarked(true);
        }
    }

    public void unMarkValidMoves() {
//        Map<Vector2, ArrayList<Vector2>> validMoves = getValidMoves(true);
        for (Vector2 pos :
                getValidMoves(true).keySet()) {
            getTileAt(pos).setMarked(false);
        }
    }

    public void moveSelectedPieceTo(float x, float y) {
        Map<Vector2, ArrayList<Vector2>> validMoves = getValidMoves(false);
        ArrayList<Vector2> takenPos = validMoves.get(new Vector2(x, y));
        Piece takenPiece = null;
        if (takenPos != null) {
            for (Vector2 pos :
                    takenPos) {
                takenPiece = getPieceAt(pos);
                currentPlayer.takePiece(takenPiece);
                getOtherPlayer().removePiece(takenPiece);
            }
        }
        selectedPiece.moveTo(x, y, 0.5f);
        selectedPiece.toFront();

        if (takenPiece == null || getTakingTargetPos(getValidMoves(false)).isEmpty()){
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
