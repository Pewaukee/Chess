package com.example.Pieces;

import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(int x, int y, String color) {
        super(x, y, color);
        if (this.color.equals("white")) {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteRook.png");
        } else {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackRook.png");
        }
    }

    @Override
    public ArrayList<Location> getMoves() {return moves;}

    @Override
    public void setMoves(ArrayList<Piece> pieces, Piece piece, King king, boolean lookForCheck) {
        ArrayList<Location> res = new ArrayList<Location>();
        int x = this.x;
        int y = this.y;
        Location location;
        Piece occupiedPiece;

        boolean up, down, left, right;
        up = down = left = right = true;

        String otherColor = this.color.equals("white") ? "black": "white";

        for (int i = 1; i <= 7; i++) { // 7 = max amount of direction on one diagonol
            // up
            location = new Location(x, y-i);
            if (y-i >= 0) {
                if (up) {
                    occupiedPiece = isOccupied(pieces, location);
                    if (occupiedPiece == null) {res.add(location);}
                    else if (occupiedPiece.color.equals(otherColor)) {
                        res.add(location); 
                        up = false;
                    }
                    else {up = false;}
                }
            }
            // down
            location = new Location(x, y+i);
            if (y+i <= 7) {
                if (down) {
                    occupiedPiece = isOccupied(pieces, location);
                    if (occupiedPiece == null) {res.add(location);}
                    else if (occupiedPiece.color.equals(otherColor)) {
                        res.add(location);
                        down = false;
                    } else {down = false;}
                }
            }
            // left
            location = new Location(x-i, y);
            if (x-i >= 0) {
                if (left) {
                    occupiedPiece = isOccupied(pieces, location);
                    if (occupiedPiece == null) {res.add(location);}
                    else if (occupiedPiece.color.equals(otherColor)) {
                        res.add(location);
                        left = false;
                    } else {left = false;}
                }
            }
            // right
            location = new Location(x+i, y);
            if (x+i <= 7) {
                if (right) {
                    occupiedPiece = isOccupied(pieces, location);
                    if (occupiedPiece == null) {res.add(location);}
                    else if (occupiedPiece.color.equals(otherColor)) {
                        res.add(location);
                        right = false;
                    } else {right = false;}
                }
            }
        }
        if (lookForCheck) {
            res = king.findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;
    }
}
