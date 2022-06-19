package com.example.Pieces;

import java.util.ArrayList;

public class Piece extends Location {

    protected boolean alive;
    protected String color;
    protected ArrayList<Location> moves;
    protected String filename;
    protected String type;
    protected boolean check;

    public Piece(int x, int y, String color, String type, String filename) {
        super(x, y);
        this.color = color;
        this.alive = true;
        this.type = type;
        this.check = false;
        this.filename = filename;
    }

    public String getColor() {return color;}

    public String getFileString() {return this.filename;}
    public void setFileString(String filename) {this.filename = filename;}

    public boolean isAlive() {return alive;}
    public void setAlive(boolean alive) {this.alive = alive;}

    // these are just placement functions to be overwritten in subclasses
    public ArrayList<Location> getMoves() {return moves;}
    public void setMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck) {
        ArrayList<Location> res = new ArrayList<Location>();
        int x = this.x;
        int y = this.y;
        // if king, set the check variable
        switch (type) {
            case "pawn": // set pawn moves
                pawnMoves(pieces, piece, king, true, res);
                break;
            case "knight": // set knight moves
                knightMoves(pieces, piece, king, true, res);
                break;
            default:
                break;
        }
    }

    private void pawnMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck,  ArrayList<Location> res) {
        Piece occupiedPiece;
        String otherColor = piece.color.equals("white") ? "black": "white";
        switch (piece.color) {
            case "white":
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
                helper(pieces, upRight, res, otherColor);
                Location upLeft = new Location(x-1, y-1);
                helper(pieces, upLeft, res, otherColor);
                break;
            case "black":
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
                helper(pieces, downRight, res, otherColor);
                Location downLeft = new Location(x-1, y+1);
                helper(pieces, downLeft, res, otherColor);
                break;
            default:
                System.out.println("this shouldn't ever happen");
                break;
        
        }
        if (lookForCheck) {
            res = findChecks(res, piece, king, pieces, x, y);
        } // TODO  figure out how to find checks properly
        // this checks for pins?
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

    private void knightMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck,  ArrayList<Location> res) {
        Location location;
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
            res = findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;
    }

    public Piece isOccupied(ArrayList<Piece> pieces, Location location) {
        /* purpose
         * checks to see if a piece occupies a square,
         * if so return that piece
         */
        for (Piece piece: pieces) {
            if (piece.x == location.x && piece.y == location.y) {
                return piece;
            }
        }
        return null;
    }

    public ArrayList<Location> findChecks(ArrayList<Location> res, Piece piece, Piece king, ArrayList<Piece> pieces, int x, int y) {
        ArrayList<Location> toRemove = new ArrayList<Location>();
        for (Location location: res) {
            piece.x = location.x;
            piece.y = location.y;
            Piece occupiedPiece = isOccupied(pieces, location);
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
    public boolean inCheck(ArrayList<Piece> pieces, Piece king) { // see if a move causes check
        String otherColor = king.color.equals("white") ? "black": "white";
        for (Piece piece: pieces) {
            if (piece.color.equals(otherColor) && piece.isAlive()) {
                piece.setMoves(pieces, piece, king, false);
                for (Location location: piece.getMoves()) {
                    if (king.x == location.x && king.y == location.y) {
                        System.out.println(king.x + " k " + king.y);
                        king.check = true;
                        return true;
                    } // TODO  find out how to give checks properly
                }
            }
        }
        king.check = false;
        return false;
    }
}
