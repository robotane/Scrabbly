package com.robotane.game.scrabbly.game;

import com.robotane.game.scrabbly.game.pieces.Piece;
import com.robotane.game.scrabbly.game.pieces.PieceType;

import java.util.ArrayList;

public class Player {
    public String name;
    public Board board;
    public PieceType color;
    public ArrayList<Piece> pieces;
    public ArrayList<Piece> takenPieces;

    public Player(String name, PieceType color, Board board) {
        this.name = name;
        this.color = color;
        this.board = board;
        takenPieces = new ArrayList<>();
        pieces = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PieceType getColor() {
        return color;
    }

    public void setColor(PieceType color) {
        this.color = color;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void removePiece(Piece piece){
        pieces.remove(piece);
    }

    public void addPiece(Piece piece){
        pieces.add(piece);
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    public ArrayList<Piece> getTakenPieces() {
        return takenPieces;
    }

    public void takePiece(Piece piece){
        takenPieces.add(piece);
        piece.hide();
        board.setPieceAt(piece.posOnBoard, null);
//        piece.setPosOnBoard(new Vector2(0,0));
    }

    public void setTakenPieces(ArrayList<Piece> takenPieces) {
        this.takenPieces = takenPieces;
    }
}
