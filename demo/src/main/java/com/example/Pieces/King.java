package com.example.Pieces;

import java.util.ArrayList;

public class King extends Piece {

    protected boolean check;
    protected boolean stalemate;
    protected boolean checkmate;


    public King(int x, int y, String color) {
        super(x, y, color);
        this.check = false;
        this.stalemate = false;
        this.checkmate = false;
    }

    public boolean inCheck(ArrayList<Piece> pieces, String turn) { // see if a move causes check
        if (turn.equals("white")) {
            for (Piece piece: pieces) {
                if (piece.getColor().equals("black")) {
                    piece.setMoves(pieces);
                    for (Location location: piece.getMoves()) {
                        if (this.x == location.getX() && this.y == location.getY()) {
                            this.check = true;
                            return true;
                        }
                    }
                }
            }
        }
        if (turn.equals("black")) {
            for (Piece piece: pieces) {
                if (piece.getColor().equals("white")) {
                    piece.setMoves(pieces);
                    for (Location location: piece.getMoves()) {
                        if (this.x == location.getX() && this.y == location.getY()) {
                            this.check = true;
                            return true;
                        }
                    }
                }
            }
        }
        this.check = false;
        return false;
    }

    @Override
    public ArrayList<Location> getMoves() {return moves;}
    
    @Override
    public void setMoves(ArrayList<Piece> pieces) {

    }


}
