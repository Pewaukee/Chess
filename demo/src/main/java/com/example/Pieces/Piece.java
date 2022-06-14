package com.example.Pieces;

import java.util.ArrayList;

public class Piece extends Location {
    
    protected boolean alive;
    protected String color;
    protected ArrayList<Location> moves;
    protected String fileString;
    
    public Piece(int x, int y, String color) {
        super(x, y);
        this.color = color;
        this.alive = true;
    }

    public Piece() {
        super(-1, -1);
    }

    public String getColor() {return color;}

    public String getFileString() {return this.fileString;}
    public void setFileString(String filepath) {fileString = filepath;}

    public boolean isAlive() {return alive;}
    public void setAlive(boolean alive) {this.alive = alive;}

    // these are just placement functions to be overwritten in subclasses
    public ArrayList<Location> getMoves() {return moves;}
    public void setMoves(ArrayList<Piece> pieces, Piece curPiece, King king) {

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

}
