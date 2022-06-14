package com.example.Pieces;

import java.util.ArrayList;

public class Pawn extends Piece {
    
    public Pawn(int x, int y, String color) {
        super(x, y, color);
        if (this.color.equals("white")) {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whitePawn.png");
        } else {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackPawn.png");
        }
    }

    @Override
    public ArrayList<Location> getMoves() {return moves;}

    @Override
    public void setMoves(ArrayList<Piece> pieces, Piece curPiece, King king) {
        /* pawn moves
         * a pawn has 4 available moves at a given time,
         * not dealing with en passant right now, but 
         * these moves are a 2 step forward, or a diagonal
         * take on either diaganol towards their opponent's
         * side
         */
        ArrayList<Location> res = new ArrayList<Location>();
        if (color.equals("white")) {
            //up 1 and not at the end of the board
            Location upOne = new Location(x, y-1);
            if (y > 0 && isOccupied(pieces, upOne) == null) {
                res.add(upOne);
                //up 2 and at starting position
                Location upTwo = new Location(x, y-2); // can only move 2 up if can move one up
                if (y == 6 && isOccupied(pieces, upTwo) == null) {res.add(upTwo);}
            }
            
            //check diagonals
            Location upRight = new Location(x+1, y-1);
            Location upLeft = new Location(x-1, y-1);
            if (x > 0 && x < 7 && y > 0) {
                Piece occupiedPiece;
                try {
                    occupiedPiece = isOccupied(pieces, upRight);
                    if (occupiedPiece.color.equals("black")) {res.add(upRight);}
                } catch (NullPointerException e) {}
                try {
                    occupiedPiece = isOccupied(pieces, upLeft);
                    if (occupiedPiece.color.equals("black")) {res.add(upLeft);}
                } catch (NullPointerException e) {}
            }
        }
        else if (color.equals("black")) {
            // up 1 and not at end of the board
            Location downOne = new Location(x, y+1);
            if (y < 7 && isOccupied(pieces, downOne) == null) {
                res.add(downOne);
                // up 2 and at starting position
                Location downTwo = new Location(x, y+2); // can only move down two if can move down one
                if (y == 1 && isOccupied(pieces, downTwo) == null) {res.add(downTwo);}
            }

            //check diagonals
            Location downRight = new Location(x+1, y+1);
            Location downLeft = new Location(x-1, y+1);
            if (x > 0 && x < 7 && y < 7) {
                Piece occupiedPiece;
                try {
                    occupiedPiece = isOccupied(pieces, downRight);
                    if (occupiedPiece.color.equals("white")) {res.add(downRight);}
                } catch (NullPointerException e) {}
                try {
                    occupiedPiece = isOccupied(pieces, downLeft);
                    if (occupiedPiece.color.equals("white")) {res.add(downLeft);}
                } catch (NullPointerException e) {}
            }
        }
        // TODO determine if any of the moves results in a check, stalls out the program sometimes
        //int oldX = curPiece.x;
        //int oldY = curPiece.y;
        ///ArrayList<Location> toRemove = new ArrayList<Location>();
        //for (Location location: res) {
            //curPiece.x = location.x;
            //curPiece.y = location.y;
            //if (king.inCheck(pieces)) {
                //toRemove.add(location);
                //System.out.println("we got em");
            //}
        //}
        //for (Location location: toRemove) {res.remove(location);}
        //curPiece.x = oldX;
        //curPiece.y = oldY;
        this.moves = res;
    }
}
