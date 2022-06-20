package com.example.Pieces;

import java.util.ArrayList;

public class King extends Piece2 {

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

    public ArrayList<Location> findChecks(ArrayList<Location> res, Piece2 piece, King king, ArrayList<Piece2> pieces, int x, int y) {
        ArrayList<Location> toRemove = new ArrayList<Location>();
        for (Location location: res) {
            piece.x = location.x;
            piece.y = location.y;
            Piece2 occupiedPiece = isOccupied(pieces, location);
            if (occupiedPiece != null) {
                occupiedPiece.alive = false;
            }
            if (inCheck(pieces, king)) {
                toRemove.add(location);
            }
            try {
                occupiedPiece.alive = true; // occupied piece may be null
            } catch (NullPointerException e) {}
        }
        for (Location location: toRemove) {
            System.out.println(location.x + " x " + location.y);res.remove(location);}
        piece.x = x;
        piece.y = y;
        return res;
    }

    public boolean inCheck(ArrayList<Piece2> pieces, King king) { // see if a move causes check
        String otherColor = king.color.equals("white") ? "black": "white";
        for (Piece2 piece: pieces) {
            if (piece.color.equals(otherColor) && piece.isAlive()) {
                piece.setMoves(pieces, piece, king, false);
                for (Location location: piece.getMoves()) {
                    if (king.x == location.x && king.y == location.y) {
                        System.out.println(king.x + " k " + king.y);
                        king.check = true;
                        return true;
                    }
                }
            }
        }
        king.check = false;
        return false;
    }

    @Override
    public ArrayList<Location> getMoves() {return moves;}
    
    @Override
    public void setMoves(ArrayList<Piece2> pieces, Piece2 piece, King king, boolean lookForCheck) {
        // movement is the same as a queen, but only one square
        // implemented a distance formula in location class 
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
        for (Location loc: toRemove) {res.remove(loc);}
        if (lookForCheck) {
            res = findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;

        
    }


}
