package com.example.Pieces;

import java.util.ArrayList;

public class Knight extends Piece2{

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
    public void setMoves(ArrayList<Piece2> pieces, Piece2 piece, King king, boolean lookForCheck) {
        // knight has 8 valid moves
        ArrayList<Location> res = new ArrayList<Location>();
        int x = this.x;
        int y = this.y;
        Location location;
        Piece2 occupiedPiece;
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
            res = king.findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;


    }
    private void helper(ArrayList<Piece2> pieces, Location location, ArrayList<Location> res, String otherColor) {
        Piece2 occupiedPiece;
        if (inBounds(location)) {
            occupiedPiece = isOccupied(pieces, location);
            if (occupiedPiece == null) {res.add(location);}
            else if (occupiedPiece.color.equals(otherColor)) {
                res.add(location);
            }
        }
    }
}
