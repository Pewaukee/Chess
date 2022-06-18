package com.example.Pieces;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(int x, int y, String color) {
        super(x, y, color);
        if (this.color.equals("white")) {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteQueen.png");
        } else {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackQueen.png");
        }
    }
    @Override
    public ArrayList<Location> getMoves() {return moves;}

    @Override
    public void setMoves(ArrayList<Piece> pieces, Piece piece, King king, boolean lookForCheck) {
        // queen's moves are a combination of rook and bishop moves, diagonols and non-diagonols
        /*Bishop bishop = new Bishop(this.x, this.y, this.color);
        Rook rook = new Rook(this.x, this.y, this.color);
        ArrayList<Location> res = new ArrayList<Location>();
        bishop.setMoves(pieces, bishop, king, lookForCheck);
        rook.setMoves(pieces, rook, king, lookForCheck);
        res.addAll(bishop.getMoves());
        res.addAll(rook.getMoves());*/
        
        //TODO the below code works, above does not

        //System.out.println(res.size());
        ArrayList<Location> res = new ArrayList<Location>();
        int x = this.x;
        int y = this.y;
        Location location;
        Piece occupiedPiece;

        boolean upRight, upLeft, downRight, downLeft, up, down, left, right;
        upRight = upLeft = downRight = downLeft = up = down = left = right = true;
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
