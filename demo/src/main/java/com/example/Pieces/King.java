package com.example.Pieces;

import java.util.ArrayList;

public class King extends Piece {

    protected boolean check;
    protected boolean stalemate;
    protected boolean checkmate;
    protected String fileStr; 


    public King(int x, int y, String color) {
        super(x, y, color);
        this.check = false;
        this.stalemate = false;
        this.checkmate = false;
        if (this.color.equals("white")) {
            fileStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteKing.png";
        } else {
            fileStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackKing.png";
        }
    }

    public boolean inCheck(ArrayList<Piece> pieces, String turn) { // see if a move causes check
        String color = this.getColor();
        if (color.equals("white")) {
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
        if (color.equals("black")) {
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
