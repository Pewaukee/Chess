package com.example.Pieces;

import java.util.ArrayList;

public class Bishop extends Piece2 {

    public Bishop(int x, int y, String color) {
        super(x, y, color);
        if (this.color.equals("white")) {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteBishop.png");
        } else {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackBishop.png");
        }
    }
    @Override
    public ArrayList<Location> getMoves() {return moves;}

    @Override
    public void setMoves(ArrayList<Piece2> pieces, Piece2 piece, King king, boolean lookForCheck) {
        // diaogonals are in 4 directions, so need to evaluate each way
        ArrayList<Location> res = new ArrayList<Location>();
        int x = this.x;
        int y = this.y;
        Location location;
        Piece2 occupiedPiece;

        boolean upRight, upLeft, downRight, downLeft;
        upRight = upLeft = downRight = downLeft = true;

        String otherColor = this.color.equals("white") ? "black": "white";

        for (int i = 1; i <= 7; i++) { // 7 = max amount of direction on one diagonol
            // up and right
            location = new Location(x+i, y-i);
            if (inBounds(location)) {
                if (upRight) {
                    occupiedPiece = isOccupied(pieces, location);
                    if (occupiedPiece == null) {res.add(location);} 
                    else if (occupiedPiece.color.equals(otherColor)) {
                        res.add(location); 
                        upRight = false;
                    } else {upRight = false;}
                }
            }
            
            // up and left
            location = new Location(x-i, y-i);
            if (inBounds(location)) {
                if (upLeft) {
                    occupiedPiece = isOccupied(pieces, location);
                    if (occupiedPiece == null) {res.add(location);} 
                    else if (occupiedPiece.color.equals(otherColor)) {
                        res.add(location); 
                        upLeft = false;
                    } else {upLeft = false;}
                }
            } 

            // down and right
            location = new Location(x+i, y+i);
            if (inBounds(location)) {
                if (downRight) {
                    occupiedPiece = isOccupied(pieces, location);
                    if (occupiedPiece == null) {res.add(location);} 
                    else if (occupiedPiece.color.equals(otherColor)) {
                        res.add(location); 
                        downRight = false;
                    } else {downRight = false;}
                }
            }

            // down and left
            location = new Location(x-i, y+i);
            if (inBounds(location)) {
                if (downLeft) {
                    occupiedPiece = isOccupied(pieces, location);
                    if (occupiedPiece == null) {res.add(location);} 
                    else if (occupiedPiece.color.equals(otherColor)) {
                        res.add(location); 
                        downLeft = false;
                    } else {downLeft = false;}
                }
            }
        }
        if (lookForCheck) {
            res = king.findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;
    }   
}
         
