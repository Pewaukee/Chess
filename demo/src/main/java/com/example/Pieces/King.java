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
        String otherColor = this.color.equals("white") ? "black": "white";
        for (Piece piece: pieces) {
            if (piece.color.equals(otherColor) && piece.isAlive()) {
                try {
                    piece.setMoves(pieces, piece, this, false);
                    for (Location location: piece.getMoves()) {
                        if (this.x == location.x && this.y == location.y) {
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
    public void setMoves(ArrayList<Piece> pieces, Piece piece, King king, boolean lookForCheck) {
        // movement s the same as a queen, but only one square
        // could implement a distance formula in location class 
        int x = this.x;
        int y = this.y;
        ArrayList<Location> res = new ArrayList<Location>();
        Location location1 = new Location(this.x, this.y);
        Queen queen = new Queen(this.x, this.y, this.color);
        queen.setMoves(pieces, piece, king, lookForCheck);
        ArrayList<Location> toRemove = new ArrayList<Location>();
        res = queen.getMoves();
        for (Location move: res) {
            if (distance(location1, move) >= 1.5) { // more than one square away
                toRemove.add(move);
            }
        }
        if (lookForCheck) {
            for (Location loc: res) {
                this.x = loc.x;
                this.y = loc.y;
                if (king.inCheck(pieces)) {
                    toRemove.add(loc);
                }
            }
            for (Location loc: toRemove) {res.remove(loc);}
            this.x = x;
            this.y = y;
        }
        this.moves = res;

        
    }


}
