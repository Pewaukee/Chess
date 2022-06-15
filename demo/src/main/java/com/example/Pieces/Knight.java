package com.example.Pieces;

import java.util.ArrayList;

public class Knight extends Piece{

    public Knight(int x, int y, String color) {
        super(x, y, color);
        if (this.color.equals("white")) {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteKnight.png");
        } else {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackKnight.png");
        }
    }
    @Override
    public ArrayList<Location> getMoves() {return moves;}

    @Override
    public void setMoves(ArrayList<Piece> pieces, Piece piece, King king, boolean lookForCheck) {
        // knight has 8 valid moves
        ArrayList<Location> res = new ArrayList<Location>();
        int x = this.x;
        int y = this.y;
        Location location;
        Piece occupiedPiece;
        String otherColor = this.color.equals("white") ? "black": "white";

        // up 1, left 2
        location = new Location(x-2, y-1);
        helper(pieces, location, res, otherColor);

        // up 2, left 1
        location = new Location(x-1, y-2);
        helper(pieces, location, res, otherColor);

        // up 1, right 2
        location = new Location(x+2, y-1);
        helper(pieces, location, res, otherColor);

        // up 2, right 1
        location = new Location(x+1, y-2);
        helper(pieces, location, res, otherColor);

        // down 1, left 2
        location = new Location(x-2, y+1);
        helper(pieces, location, res, otherColor);

        // down 2, left 1
        location = new Location(x-1, y+2);
        helper(pieces, location, res, otherColor);

        // down 1, right 2
        location = new Location(x+2, y+1);
        helper(pieces, location, res, otherColor);

        // down 2, right 1
        location = new Location(x+1, y+2);
        helper(pieces, location, res, otherColor);

        if (lookForCheck) {
            ArrayList<Location> toRemove = new ArrayList<Location>();
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
    private void helper(ArrayList<Piece> pieces, Location location, ArrayList<Location> res, String otherColor) {
        Piece occupiedPiece;
        if (inBounds(location)) {
            occupiedPiece = isOccupied(pieces, location);
            if (occupiedPiece == null) {res.add(location);}
            else if (occupiedPiece.color.equals(otherColor)) {
                res.add(location);
            }
        }
    }
}
