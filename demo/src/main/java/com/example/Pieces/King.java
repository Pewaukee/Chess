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
        if (this.color.equals("white")) {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteKing.png");
        } else {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackKing.png");
        }
    }

    public boolean inCheck(ArrayList<Piece> pieces) { // see if a move causes check
        String color = this.color;
        String otherColor = color.equals("white") ? "black": "white";
        for (Piece piece: pieces) {
            if (piece.color.equals(otherColor) && piece.isAlive()) {
                try {
                    piece.setMoves(pieces, piece, this);
                    for (Location location: piece.getMoves()) {
                        if (this.x == location.getX() && this.y == location.getY()) {
                            this.check = true;
                            return true;
                        }
                    }
                } catch (NullPointerException e) {}
            }
        }
        this.check = false;
        return false;
    }

    @Override
    public ArrayList<Location> getMoves() {return moves;}
    
    @Override
    public void setMoves(ArrayList<Piece> pieces, Piece curPiece, King king) {

    }


}
